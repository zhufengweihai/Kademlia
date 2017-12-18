package com.zf.kademlia.routing;

import static com.zf.kademlia.Commons.K;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeoutException;

import com.zf.kademlia.KadDataManager;
import com.zf.kademlia.ScheduleManager;
import com.zf.kademlia.client.KadResponseListener;
import com.zf.kademlia.client.KademliaClient;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.protocol.KadMessage;
import com.zf.kademlia.protocol.Ping;

import lombok.Getter;

/**
 * @author zhufeng7
 * @date 2017-11-28.
 */
@Getter
public class Bucket {
	private int bucketId;
	private List<Node> nodes = new ArrayList<Node>();
	private List<Node> replaceNodes = new ArrayList<Node>();
	private ScheduledFuture sf = null;

	public Bucket(int bucketId) {
		this.bucketId = bucketId;
	}

	public void addNode(Node node) {
		int k = KadDataManager.instance().getConfig().getK();
		if (nodes.size() < k) {
			nodes.add(node);
			return;
		}

		Node last = nodes.get(nodes.size() - 1);
		Ping ping = new Ping(KadDataManager.instance().getLocalNode());
		KademliaClient.sendMessage(last, ping, new KadResponseListener() {
			@Override
			public void onResponse(KadMessage message) {
				nodes.remove(last);
				last.setLastSeen(System.currentTimeMillis());
				nodes.add(last);
				replaceNodes.add(node);
				if (replaceNodes.size() > k) {
					replaceNodes.remove(replaceNodes.size() - 1);
				}
			}

			@Override
			public void onFailed(Node ignore) {
				nodes.remove(last);
				nodes.add(node);
			}
		});
	}

	public void refreshBucket() {
		if (nodes.isEmpty()) {
			return;
		}
		if (sf != null) {
			sf.cancel(true);
		}

		Node node = nodes.get(new Random().nextInt(nodes.size() - 1));
		Ping ping = new Ping(KadDataManager.instance().getLocalNode());
		KademliaClient.sendMessage(node, ping, new KadResponseListener() {
			@Override
			public void onResponse(KadMessage message) {
				nodes.remove(node);
				node.setLastSeen(System.currentTimeMillis());
				nodes.add(node);
			}

			@Override
			public void onFailed(Node ignore) {
				nodes.remove(node);
			}
		});

		while (nodes.size() < k && !replaceNodes.isEmpty()) {
			Node node = replaceNodes.get(0);
			Ping ping = new Ping(KadDataManager.instance().getLocalNode());
			KademliaClient.sendMessage(node, ping, new KadResponseListener() {
				@Override
				public void onResponse(KadMessage message) {
					replaceNodes.remove(node);
					node.setLastSeen(System.currentTimeMillis());
					nodes.add(node);
				}

				@Override
				public void onFailed(Node ignore) {
					replaceNodes.remove(node);
				}
			});
		}
	}

	private boolean shouldRefresh() {
		long current = System.currentTimeMillis();
		int refreshInterval = KadDataManager.instance().getConfig().getRefreshInterval();
		for (Node node : nodes) {
			if (current - node.getLastSeen() >= refreshInterval) {
				return true;
			}
		}
		return false;
	}

	void retireNode(Node nodeToRetire) {
		nodes.remove(nodeToRetire);
		// 从备用列表中选取一个在线节点填充到列表中
		while (nodes.size() < K && !replacementNodes.isEmpty()) {
			Node node = replacementNodes.first();
			try {
				KademliaClient.instance().sendPing(node, pong -> {
					replacementNodes.remove(node);
					node.setLastSeen(System.currentTimeMillis());
					nodes.add(node);
				});
			} catch (TimeoutException exp) {
				replacementNodes.remove(node);
			}
		}
	}
}

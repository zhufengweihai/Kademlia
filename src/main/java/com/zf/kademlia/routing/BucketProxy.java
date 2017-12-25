package com.zf.kademlia.routing;

import com.zf.kademlia.ExecutorManager;
import com.zf.kademlia.Kademlia;
import com.zf.kademlia.net.KadResponseListener;
import com.zf.kademlia.net.KademliaClient;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.protocol.KadMessage;
import com.zf.kademlia.protocol.Ping;

public class BucketProxy extends Bucket {
	private Bucket bucket = null;

	public BucketProxy(int bucketId) {
		bucket = new Bucket(bucketId);
	}

	private KadResponseListener createAddNodeListener(Node node, Node headNode) {
		return new KadResponseListener() {
			@Override
			public void onResponse(KadMessage message) {
				bucket.updateNode(headNode);
				bucket.updateReplaceNodes(node);
			}

			@Override
			public void onFailed(Node ignore) {
				bucket.addNodeAndRemoveHead(node);
			}
		};
	}

	private KadResponseListener createRefreshListener(Node node) {
		return new KadResponseListener() {
			@Override
			public void onResponse(KadMessage message) {
				bucket.updateNode(node);
			}

			@Override
			public void onFailed(Node ignore) {
				bucket.removeNode(node);
			}
		};
	}

	private void ChooseAliveOneFromReplaceNodes() {
		while (bucket.isFull()) {
			Node replaceNode;
			while ((replaceNode = bucket.getAndRemoveHeadReplaceNode()) != null) {
				Ping ping = new Ping(Kademlia.localNode);
				KademliaClient.sendMessage(replaceNode, ping, createChooseReplaceListener());
			}
		}
	}

	private KadResponseListener createChooseReplaceListener() {
		return new KadResponseListener() {
			@Override
			public void onResponse(KadMessage message) {
				bucket.updateNode(message.getOrigin());
			}
		};
	}

	void refreshBucket() {
		Runnable task = () -> {
			if (bucket.isEmpty()) {
				return;
			}

			Node node = bucket.getRandomNode();
			Ping ping = new Ping(Kademlia.localNode);
			KademliaClient.sendMessage(node, ping, createRefreshListener(node));
			ChooseAliveOneFromReplaceNodes();
		};
		ExecutorManager.scheduleAndCancelLast(this, task, Kademlia.config.getRefreshInterval());
	}

	public void addNode(Node node) {
		if (isFull()) {
			Node headNode = bucket.getHeadNode();
			Ping ping = new Ping(Kademlia.localNode);
			KademliaClient.sendMessage(headNode, ping, createAddNodeListener(node, headNode));
			return;
		}

		bucket.addNode(node);
	}

	public void retireNode(Node retireNode) {
		bucket.retireNode(retireNode);
		ChooseAliveOneFromReplaceNodes();
	}
}

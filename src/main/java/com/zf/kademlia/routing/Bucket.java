package com.zf.kademlia.routing;

import java.util.LinkedList;
import java.util.Random;

import com.zf.kademlia.KadDataManager;
import com.zf.kademlia.node.Node;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author zhufeng7
 * @date 2017-11-28.
 */
@Getter
@NoArgsConstructor
class Bucket {
	private int bucketId;
	private LinkedList<Node> nodes = new LinkedList<Node>();
	private LinkedList<Node> replaceNodes = new LinkedList<Node>();

	public Bucket(int bucketId) {
		this.bucketId = bucketId;
	}

	public void addNode(Node node) {
		int k = KadDataManager.instance().getConfig().getK();
		if (nodes.size() < k) {
			nodes.add(node);
		}
	}

	Node getHeadNode() {
		return nodes.getFirst();
	}

	void updateNode(Node node) {
		nodes.remove(node);
		node.setLastSeen(System.currentTimeMillis());
		nodes.addLast(node);
	}

	void removeNode(Node node) {
		nodes.remove(node);
	}

	void updateNodesWhenHeadAbsent(Node node) {
		nodes.removeFirst();
		nodes.addLast(node);
	}

	boolean isEmpty() {
		return nodes.isEmpty();
	}

	boolean isFull() {
		return nodes.size() < KadDataManager.instance().getConfig().getK();
	}

	Node getRandomNode() {
		return nodes.get(new Random().nextInt(nodes.size() - 1));
	}

	void updateReplaceNodes(Node node) {
		int k = KadDataManager.instance().getConfig().getK();
		replaceNodes.add(node);
		if (replaceNodes.size() > k) {
			replaceNodes.remove(replaceNodes.size() - 1);
		}
	}

	Node getAndRemoveHeadReplaceNode() {
		if (replaceNodes.size() > 0) {
			return replaceNodes.removeFirst();
		}
		return null;
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
}

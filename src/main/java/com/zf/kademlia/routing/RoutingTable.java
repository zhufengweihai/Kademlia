package com.zf.kademlia.routing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.zf.kademlia.common.Commons;
import com.zf.kademlia.node.Node;

public class RoutingTable implements Serializable {
	private static final long serialVersionUID = 5880872114220832106L;

	private Node localNode = null;
	private List<KBucket> buckets = new ArrayList<>(Commons.ID_LENGTH);

	public RoutingTable() {
		for (int i = 0; i < Commons.ID_LENGTH; i++) {
			buckets.add(new KBucket());
		}
	}

	public RoutingTable(Node localNode) {
		this();
		this.localNode = localNode;
	}

	public List<KBucket> getBuckets() {
		return buckets;
	}

	public void addBucket(KBucket bucket) {
		buckets.add(bucket);
	}

	public boolean isEmpty() {
		return buckets.isEmpty();
	}

	public void addNodes(List<Node> nodes) {
		for (Node node : nodes) {
			byte[] distance = localNode.distance(node);
			int index = KBucket.getBucketIndex(distance);
			buckets.get(index).addNode(node);
		}
	}

	public Node getLocalNode() {
		return localNode;
	}

	public void setLocalNode(Node localNode) {
		this.localNode = localNode;
	}

	@Override
	public String toString() {
		return "RoutingTable [buckets=" + buckets + "]";
	}
}

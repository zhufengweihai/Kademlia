package com.zf.kademlia.routing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.zf.kademlia.common.Commons;

public class RoutingTable implements Serializable {
	private static final long serialVersionUID = 5880872114220832106L;

	private Contact localNode = null;
	private List<KBucket> buckets = new ArrayList<>(Commons.ID_LENGTH);

	public RoutingTable() {
		for (int i = 0; i < Commons.ID_LENGTH; i++) {
			buckets.add(new KBucket());
		}
	}

	public RoutingTable(Contact localNode) {
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

	public void addNodes(List<Contact> nodes) {
		for (Contact node : nodes) {
			byte[] distance = localNode.distance(node);
			int index = KBucket.getBucketIndex(distance);
			buckets.get(index).addNode(node);
		}
	}

	public Contact getLocalNode() {
		return localNode;
	}

	public void setLocalNode(Contact localNode) {
		this.localNode = localNode;
	}

	public List<Contact> getNearbyContacts(Contact target) {
		TopKDistanceQueue queue = new TopKDistanceQueue(Commons.K);
		for (KBucket bucket : buckets) {
			List<Contact> contacts = bucket.getContacts();
			for (Contact contact : contacts) {
				queue.add(new TopKDistanceQueue.DistanceOrder(target, contact));
			}
		}

		return queue.getResults();

	}

	@Override
	public String toString() {
		return "RoutingTable [buckets=" + buckets + "]";
	}
}

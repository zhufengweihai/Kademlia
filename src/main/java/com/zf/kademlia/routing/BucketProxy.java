package com.zf.kademlia.routing;

import com.zf.kademlia.Kademlia;
import com.zf.kademlia.client.KadResponseListener;
import com.zf.kademlia.client.KademliaClient;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.protocol.KadMessage;
import com.zf.kademlia.protocol.Ping;

public class BucketProxy extends Bucket {
	private Bucket bucket = null;

	public BucketProxy(int bucketId) {
		bucket = new Bucket(bucketId);
	}

	public void addNode(Node node) {
		bucket.addNode(node);

		Node headNode = bucket.getHeadNode();
		Ping ping = new Ping(Kademlia.localNode);
		KademliaClient.sendMessage(headNode, ping, new KadResponseListener() {
			@Override
			public void onResponse(KadMessage message) {
				bucket.updateNode(headNode);
				bucket.updateReplaceNodes(node);
			}

			@Override
			public void onFailed(Node ignore) {
				bucket.updateNodesWhenHeadAbsent(node);
			}
		});
	}

	public void refreshBucket() {
		if (bucket.isEmpty()) {
			return;
		}

		Node node = bucket.getRandomNode();
		Ping ping = new Ping(Kademlia.localNode);
		KademliaClient.sendMessage(node, ping, new KadResponseListener() {
			@Override
			public void onResponse(KadMessage message) {
				bucket.updateNode(node);
			}

			@Override
			public void onFailed(Node ignore) {
				bucket.removeNode(node);
			}
		});

		ChooseAliveOneFromReplaceNodes();
	}

	private void ChooseAliveOneFromReplaceNodes() {
		while (bucket.isFull()) {
			Node replaceNode;
			while ((replaceNode = bucket.getAndRemoveHeadReplaceNode()) != null) {
				Ping ping = new Ping(Kademlia.localNode);
				KademliaClient.sendMessage(replaceNode, ping, new KadResponseListener() {
					@Override
					public void onResponse(KadMessage message) {
						bucket.updateNode(message.getOrigin());
					}
				});
			}
		}
	}

	public void retireNode(Node retireNode) {
		bucket.removeNode(retireNode);
		ChooseAliveOneFromReplaceNodes();
	}
}

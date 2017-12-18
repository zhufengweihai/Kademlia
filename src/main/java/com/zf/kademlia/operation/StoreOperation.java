package com.zf.kademlia.operation;

import java.util.List;

import com.zf.kademlia.KadDataManager;
import com.zf.kademlia.client.KademliaClient;
import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.protocol.KadMessage;
import com.zf.kademlia.protocol.Store;

/**
 * @author zhufeng
 * @date 2017/12/4
 */
public class StoreOperation extends BaseOperation {
	private Key key = null;
	private String value = null;

	public StoreOperation(Node node, Key key, String value) {
		super(node);
		this.key = key;
		this.value = value;
	}

	@Override
	public void execute() {
		List<Node> nodes = KadDataManager.instance().getRoutingTable().findClosest(key);
		for (Node node : nodes) {
			KademliaClient.sendMessage(node, createMessage(), this);
		}
	}

	@Override
	public KadMessage createMessage() {
		return new Store(KadDataManager.instance().getLocalNode(), key, value);
	}
}

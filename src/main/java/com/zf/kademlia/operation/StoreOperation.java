package com.zf.kademlia.operation;

import java.util.List;

import com.zf.kademlia.Kademlia;
import com.zf.kademlia.net.KademliaClient;
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

	public StoreOperation(Key key, String value) {
		super(null);
		this.key = key;
		this.value = value;
	}

	@Override
	KadMessage createMessage() {
		return new Store(Kademlia.localNode, key, value);
	}

	@Override
	public void execute() {
		List<Node> nodes = Kademlia.routingTable.findClosest(key);
		for (Node node : nodes) {
			KademliaClient.sendMessage(node, createMessage(), this);
		}
	}
}

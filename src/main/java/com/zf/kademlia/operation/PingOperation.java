package com.zf.kademlia.operation;

import com.zf.kademlia.Kademlia;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.protocol.KadMessage;
import com.zf.kademlia.protocol.Ping;

/**
 * @author zhufeng
 * @date 2017/12/3
 */
public class PingOperation extends BaseOperation {
	public PingOperation(Node node) {
		super(node);
	}

	@Override
	public KadMessage createMessage() {
		return new Ping(Kademlia.localNode);
	}

	@Override
	public void onResponse(KadMessage message) {
		Kademlia.routingTable.addNode(message.getOrigin());
	}
}

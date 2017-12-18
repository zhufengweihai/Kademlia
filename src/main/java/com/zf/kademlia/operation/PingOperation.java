package com.zf.kademlia.operation;

import com.zf.kademlia.KadDataManager;
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
		return new Ping(KadDataManager.instance().getLocalNode());
	}

	@Override
	public void onResponse(KadMessage message) {
		KadDataManager.instance().getRoutingTable().addNode(message.getOrigin());
	}
}

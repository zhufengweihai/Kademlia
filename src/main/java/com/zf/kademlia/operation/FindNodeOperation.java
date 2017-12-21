package com.zf.kademlia.operation;

import java.util.List;

import com.zf.kademlia.Kademlia;
import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.protocol.FindNode;
import com.zf.kademlia.protocol.KadMessage;
import com.zf.kademlia.protocol.NodeReply;

/**
 * @author zhufeng
 * @date 2017/12/3 0003
 */

public class FindNodeOperation extends BaseOperation {
	private Key key = null;

	public FindNodeOperation(Node node, Key key) {
		super(node);
		this.key = key;
	}

	@Override
	public KadMessage createMessage() {
		return new FindNode(Kademlia.localNode, key);
	}

	@Override
	public void onResponse(KadMessage message) {
		NodeReply nodeReply = (NodeReply) message;
		List<Node> nodes = nodeReply.getNodes();
		Kademlia.routingTable.addNodes(nodes);
	}
}

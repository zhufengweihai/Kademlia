package com.zf.kademlia.operation;

import com.zf.kademlia.Kademlia;
import com.zf.kademlia.net.KadResponseListener;
import com.zf.kademlia.net.KademliaClient;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.protocol.KadMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhufeng
 * @date 2017/12/3
 */
@Getter
@Setter
@AllArgsConstructor
public abstract class BaseOperation implements KadResponseListener {
	Node node;

	public abstract KadMessage createMessage();

	public void execute() {
		KademliaClient.sendMessage(node, createMessage(), this);
	}

	@Override
	public void onFailed(Node node) {
		Kademlia.routingTable.retireNode(node);
	}
}

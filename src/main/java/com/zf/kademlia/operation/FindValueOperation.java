package com.zf.kademlia.operation;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.mina.util.ConcurrentHashSet;

import com.zf.kademlia.Kademlia;
import com.zf.kademlia.net.KademliaClient;
import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.protocol.FindValue;
import com.zf.kademlia.protocol.KadMessage;
import com.zf.kademlia.protocol.MessageType;
import com.zf.kademlia.protocol.NodeReply;
import com.zf.kademlia.protocol.ValueReply;

/**
 * @author zhufeng
 * @date 2017/12/3 0003
 */

public class FindValueOperation extends BaseOperation {
	private Key key = null;
	private Set<Node> checkedNodes = new ConcurrentHashSet<>();
	private AtomicReference<String> value = null;
	private CountDownLatch latch = new CountDownLatch(1);

	public FindValueOperation(Key key) {
		super(null);
		this.key = key;
	}

	@Override
	KadMessage createMessage() {
		return new FindValue(Kademlia.localNode, key);
	}

	@Override
	public void execute() {
		List<Node> nodes = Kademlia.routingTable.findClosest(key);
		execute(nodes);
	}

	private void execute(List<Node> nodes) {
		for (Node node : nodes) {
			if (!checkedNodes.contains(node) && value.get() == null) {
				KademliaClient.sendMessage(node, createMessage(), this);
				checkedNodes.add(node);
			}
		}
	}

	@Override
	public void onResponse(KadMessage message) {
		if (message.getType() == MessageType.NODE_REPLY) {
			List<Node> nodes = ((NodeReply) message).getNodes();
			Kademlia.routingTable.addNodes(nodes);
			execute(nodes);
		} else if (message.getType() == MessageType.VALUE_REPLY) {
			value.getAndSet(((ValueReply) message).getValue());
			latch.countDown();
		}
	}

	public String getValue() {
		try {
			latch.await(Kademlia.config.getTimeout(), TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return value.get();
	}
}

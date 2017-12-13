package com.zf.kademlia.operation;

import com.zf.common.CommonManager;
import com.zf.kademlia.KadDataManager;
import com.zf.kademlia.client.KademliaClient;
import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.protocol.FindValue;
import com.zf.kademlia.protocol.KadMessage;
import com.zf.kademlia.protocol.MessageType;
import com.zf.kademlia.protocol.NodeReply;
import com.zf.kademlia.protocol.ValueReply;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import io.netty.util.internal.ConcurrentSet;

/**
 * @author zhufeng
 * @date 2017/12/3 0003
 */

public class FindValueOperation extends BaseOperation {
    private Key key = null;
    private Set<Node> checkedNodes = new ConcurrentSet<>();
    private AtomicReference<String> value = null;

    public FindValueOperation(Key key) {
        super(null);
        this.key = key;
    }

    @Override
    public KadMessage createMessage() {
        return new FindValue(CommonManager.instance().randomLong(), KadDataManager.instance().getLocalNode(), key);
    }

    @Override
    public void execute() {
        List<Node> nodes = KadDataManager.instance().getRoutingTable().getSortedNodes(key);
        execute(nodes);
    }

    private void execute(List<Node> nodes) {
        for (Node node : nodes) {
            if (!checkedNodes.contains(node) && value.get() == null) {
                KademliaClient.instance().send(node, createMessage(), this);
                checkedNodes.add(node);
            }
        }
    }

    @Override
    public void onResponse(KadMessage message) {
        if (message.getType() == MessageType.NODE_REPLY) {
            List<Node> nodes = ((NodeReply) message).getNodes();
            KadDataManager.instance().getRoutingTable().addNodes(nodes);
            execute(nodes);
        } else if (message.getType() == MessageType.VALUE_REPLY) {
            value.getAndSet(((ValueReply) message).getValue());
        }
    }

    public String getValue() {
        return value.get();
    }
}

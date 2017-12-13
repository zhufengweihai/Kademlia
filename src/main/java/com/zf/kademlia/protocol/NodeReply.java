package com.zf.kademlia.protocol;

import com.zf.kademlia.node.Node;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author zhufeng7
 * @date 2017-11-29.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class NodeReply extends KadMessage {
    private List<Node> nodes;

    public NodeReply(long seqId, Node origin, List<Node> nodes) {
        super(MessageType.NODE_REPLY, seqId, origin);
        this.nodes = nodes;
    }
}

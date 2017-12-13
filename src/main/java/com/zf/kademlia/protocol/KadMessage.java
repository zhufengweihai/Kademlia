package com.zf.kademlia.protocol;

import com.zf.kademlia.node.Node;

import lombok.Data;

/**
 * @author zhufeng7
 * @date 2017-11-29.
 */
@Data
public abstract class KadMessage {
    private final MessageType type;
    private final long seqId;
    private final Node origin;
}

package com.zf.kademlia.protocol;

import com.zf.kademlia.node.Node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 对Ping的回应
 *
 * @author zhufeng7
 * @date 2017-11-29.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Pong extends KadMessage {

    public Pong(long seqId, Node origin) {
        super(MessageType.PONG, seqId, origin);
    }
}

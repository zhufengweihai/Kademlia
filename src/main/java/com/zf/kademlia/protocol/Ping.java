package com.zf.kademlia.protocol;

import com.zf.kademlia.node.Node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 用于验证节点是否还活着。
 *
 * @author zhufeng7
 * @date 2017-11-29.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Ping extends KadMessage {

    public Ping(long seqId, Node origin) {
        super(MessageType.PING, seqId, origin);
    }
}

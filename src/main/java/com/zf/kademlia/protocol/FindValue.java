package com.zf.kademlia.protocol;

import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 与FIND_NODE相同，但是如果请求的接收者在其存储中有请求的密钥，它将返回相应的值。
 *
 * @author zhufeng7
 * @date 2017-11-29.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FindValue extends KadMessage {
    private final Key key;

    public FindValue(long seqId, Node origin, Key key) {
        super(MessageType.FIND_VALUE, seqId, origin);
        this.key = key;
    }
}

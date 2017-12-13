package com.zf.kademlia.protocol;

import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * FIND_VALUE的回应
 *
 * @author zhufeng7
 * @date 2017-11-29.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ValueReply extends KadMessage {
    private final String value;
    private final Key key;

    public ValueReply(long seqId, Node origin, Key key, String value) {
        super(MessageType.VALUE_REPLY, seqId, origin);
        this.key = key;
        this.value = value;
    }
}

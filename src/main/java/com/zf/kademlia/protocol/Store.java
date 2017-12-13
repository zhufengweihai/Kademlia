package com.zf.kademlia.protocol;

import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 将一个（键，值）对存储在一个节点中。
 *
 * @author zhufeng7
 * @date 2017-11-29.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Store extends KadMessage {
    private final String value;
    private final Key key;

    public Store(long seqId, Node origin, Key key, String value) {
        super(MessageType.STORE, seqId, origin);
        this.key = key;
        this.value = value;
    }
}

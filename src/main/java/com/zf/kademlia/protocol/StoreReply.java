package com.zf.kademlia.protocol;

import com.zf.kademlia.node.Node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 对存储操作的回应
 *
 * @author zhufeng7
 * @date 2017-11-29.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class StoreReply extends KadMessage {
    public StoreReply(long seqId, Node origin) {
        super(MessageType.STORE_REPLY, seqId, origin);
    }
}

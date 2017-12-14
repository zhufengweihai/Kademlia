package com.zf.kademlia.protocol;

import com.zf.kademlia.node.Node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhufeng7
 * @date 2017-11-29.
 */
@Getter
@Setter
@AllArgsConstructor
public abstract class KadMessage {
	private MessageType type;
	private long seqId;
	private Node origin;
}

package com.zf.kademlia.protocol;

import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 请求的接收者将返回与请求的key最接近的k个节点.
 *
 * @author zhufeng7
 * @date 2017-11-29.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FindNode extends KadMessage {
	private Key findKey = null;

	public FindNode(Node origin, Key findKey) {
		super(MessageType.FIND_NODE, origin);
		this.findKey = findKey;
	}
}

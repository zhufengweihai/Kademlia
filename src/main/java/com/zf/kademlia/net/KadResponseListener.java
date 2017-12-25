package com.zf.kademlia.net;

import com.zf.kademlia.node.Node;
import com.zf.kademlia.protocol.KadMessage;

/**
 * @author zhufeng
 * @date 2017-12-16.
 *
 */
public interface KadResponseListener {
	default void onResponse(KadMessage message) {
	}

	default void onFailed(Node node) {
	}
}

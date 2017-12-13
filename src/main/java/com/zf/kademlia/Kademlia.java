package com.zf.kademlia;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.operation.FindNodeOperation;
import com.zf.kademlia.operation.PingOperation;

/**
 * @author zhufeng7
 * @date 2017-11-28.
 */
public class Kademlia {
	private static final Logger LOGGER = LoggerFactory.getLogger(Kademlia.class);

	private void init() {

	}

	public void bootstrap(Node bootstrapNode) {
		new PingOperation(bootstrapNode).execute();
		new FindNodeOperation(bootstrapNode, KadDataManager.instance().getLocalNode().getId()).execute();
		refreshBuckets();
	}

	public void store(Key key, String value) {

	}

	private void refreshBuckets() {
		for (int i = 1; i < Key.ID_LENGTH; i++) {
			final Key current = KadDataManager.instance().getLocalNode().getId().generateNodeIdByDistance(i);
			List<Node> nodes = KadDataManager.instance().getRoutingTable().getNodes();
			for (Node node : nodes) {
				new FindNodeOperation(node, current).execute();
			}
		}
	}

	public void close() {

	}
}

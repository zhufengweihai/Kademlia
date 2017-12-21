package com.zf.kademlia;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.operation.FindNodeOperation;
import com.zf.kademlia.operation.PingOperation;
import com.zf.kademlia.routing.RoutingTable;
import com.zf.kademlia.routing.ValueTable;

/**
 * @author zhufeng7
 * @date 2017-11-28.
 */
public class Kademlia {
	private static Logger logger = LoggerFactory.getLogger(Kademlia.class);
	public static KademliaConfig config = null;
	public static Node localNode = null;
	public static RoutingTable routingTable = null;
	public static ValueTable valueTable = null;

	private void init() {

	}

	public void bootstrap(Node bootstrapNode) {
		new PingOperation(bootstrapNode).execute();
		new FindNodeOperation(bootstrapNode, localNode.getId()).execute();
		refreshBuckets();
	}

	public void store(Key key, String value) {

	}

	private void refreshBuckets() {
		for (int i = 1; i < Key.ID_LENGTH; i++) {
			final Key current = localNode.getId().generateNodeIdByDistance(i);
			List<Node> nodes = routingTable.getNodes();
			for (Node node : nodes) {
				new FindNodeOperation(node, current).execute();
			}
		}
	}

	public void close() {

	}
}

package com.zf.kademlia;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zf.kademlia.node.Key;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.operation.FindNodeOperation;
import com.zf.kademlia.operation.FindValueOperation;
import com.zf.kademlia.operation.PingOperation;
import com.zf.kademlia.operation.StoreOperation;
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

	public Kademlia() {
		
		valueTable = ValueTable.build("");
	}

	public void bootstrap(Node bootstrapNode) {
		new PingOperation(bootstrapNode).execute();
		new FindNodeOperation(bootstrapNode, localNode.getId()).execute();
	}

	public void store(Key key, String value) {
		new StoreOperation(key, value).execute();
	}

	public String get(Key key) {
		if (valueTable.contains(key)) {
			return valueTable.get(key).getContent();
		}
		FindValueOperation operation = new FindValueOperation(key);
		operation.execute();
		return operation.getValue();
	}

	public void close() {

	}
}

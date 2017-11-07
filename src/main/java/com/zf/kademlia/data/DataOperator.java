package com.zf.kademlia.data;

import java.io.IOException;
import java.util.List;

import com.zf.kademlia.node.Node;
import com.zf.kademlia.routing.RoutingTable;

public interface DataOperator {
	void saveRoutingTable(RoutingTable routingTable) throws IOException;

	RoutingTable readRoutingTable();

	List<Node> readBootstraps();
}

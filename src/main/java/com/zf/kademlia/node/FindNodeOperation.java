package com.zf.kademlia.node;

import com.zf.kademlia.routing.RoutingTable;

public class FindNodeOperation {
	private RoutingTable routingTable = null;

	public FindNodeOperation(RoutingTable routingTable) {
		super();
		this.routingTable = routingTable;
	}

}

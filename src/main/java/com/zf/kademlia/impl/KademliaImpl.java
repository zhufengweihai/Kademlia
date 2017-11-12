package com.zf.kademlia.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zf.kademlia.Kademlia;
import com.zf.kademlia.common.Commons;
import com.zf.kademlia.data.DataOperator;
import com.zf.kademlia.data.impl.DataOperatorImpl;
import com.zf.kademlia.net.KadServer;
import com.zf.kademlia.routing.Contact;
import com.zf.kademlia.routing.RoutingTable;

public class KademliaImpl implements Kademlia {
	private final static Logger LOGGER = LoggerFactory.getLogger(KademliaImpl.class);

	@Override
	public boolean start() {
		KadServer kadServer = KadServer.instance();
		if (!kadServer.init()) {
			return false;
		}

		DataOperator operator = new DataOperatorImpl();
		RoutingTable routingTable = operator.readRoutingTable();
		if (routingTable.isEmpty()) {
			routingTable.setLocalNode(Contact.createNode(kadServer.getIP(), Commons.PORT));
			List<Contact> bootstraps = operator.readBootstraps();
			if (bootstraps.isEmpty()) {
				return false;
			}
			
			routingTable.addNodes(bootstraps);
		}

		return true;
	}

	@Override
	public void start(Commons kadConfig) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void proccess() {
		// TODO Auto-generated method stub

	}

}

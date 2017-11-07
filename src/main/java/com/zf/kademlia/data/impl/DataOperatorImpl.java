package com.zf.kademlia.data.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zf.kademlia.common.Commons;
import com.zf.kademlia.data.DataOperator;
import com.zf.kademlia.encryption.CryptoProvider;
import com.zf.kademlia.encryption.impl.CryptoProviderImpl;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.routing.RoutingTable;

public class DataOperatorImpl implements DataOperator {
	private final static Logger LOGGER = LoggerFactory.getLogger(DataOperatorImpl.class);

	@Override
	public void saveRoutingTable(RoutingTable routingTable) throws IOException {
		byte[] data = SerializationUtils.serialize(routingTable);
		CryptoProvider cryptoProvider = new CryptoProviderImpl();
		FileUtils.writeByteArrayToFile(new File(Commons.PATH_ROUTING_TABLE), cryptoProvider.encrypt(data));
	}

	@Override
	public RoutingTable readRoutingTable() {
		try {
			byte[] data = FileUtils.readFileToByteArray(new File(Commons.PATH_ROUTING_TABLE));
			CryptoProvider cryptoProvider = new CryptoProviderImpl();
			return (RoutingTable) SerializationUtils.deserialize(cryptoProvider.decrypt(data));
		} catch (IOException e) {
			LOGGER.error("Failed to read routingTable", e);
		}
		return new RoutingTable();
	}

	@Override
	public List<Node> readBootstraps() {
		try {
			byte[] data = FileUtils.readFileToByteArray(new File(Commons.PATH_BOOTSTRAP));
			CryptoProvider cryptoProvider = new CryptoProviderImpl();
			return (List<Node>) SerializationUtils.deserialize(cryptoProvider.decrypt(data));
		} catch (IOException e) {
			LOGGER.error("Failed to read Bootstraps", e);
		}
		return new ArrayList<Node>();

	}

}

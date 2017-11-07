package com.zf.kademlia.data;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import com.zf.kademlia.common.Commons;
import com.zf.kademlia.data.impl.DataOperatorImpl;
import com.zf.kademlia.encryption.CryptoProvider;
import com.zf.kademlia.encryption.impl.CryptoProviderImpl;
import com.zf.kademlia.node.Node;
import com.zf.kademlia.routing.KBucket;
import com.zf.kademlia.routing.RoutingTable;

public class DataOperatorTest {
	@Test
	public void testSave() {
		try {
			DataOperator operator = new DataOperatorImpl();
			RoutingTable routingTable = new RoutingTable();
			for (int i = 0; i < Commons.ID_LENGTH; i++) {
				KBucket bucket = new KBucket();
				for (int j = 0; j <= RandomUtils.nextInt(5); j++) {
					Node node = Node.createNode("192.168.0.1", 10000);
					bucket.addNode(node);
				}
				routingTable.addBucket(bucket);
			}

			operator.saveRoutingTable(routingTable);
			assertTrue(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testRead() {
		DataOperator operator = new DataOperatorImpl();
		RoutingTable routingTable = operator.readRoutingTable();
		System.out.println(routingTable);
		assertTrue(true);
	}

	@Test
	public void testReadBootstrap() {
		try {
			DataOperator operator = new DataOperatorImpl();
			List<Node> nodes = new ArrayList<>();
			for (int j = 0; j <= RandomUtils.nextInt(5); j++) {
				Node node = Node.createNode("192.168.0.1", 10000);
				;
				nodes.add(node);
			}

			byte[] data = SerializationUtils.serialize((Serializable) nodes);
			CryptoProvider cryptoProvider = new CryptoProviderImpl();
			FileUtils.writeByteArrayToFile(new File(Commons.PATH_BOOTSTRAP), cryptoProvider.encrypt(data));

			List<Node> bootstraps = operator.readBootstraps();
			System.out.println(bootstraps);
			assertTrue(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

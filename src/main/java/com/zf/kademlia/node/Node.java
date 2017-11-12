package com.zf.kademlia.node;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.zf.kademlia.common.Commons;
import com.zf.kademlia.routing.Contact;
import com.zf.kademlia.routing.KBucket;
import com.zf.kademlia.routing.RoutingTable;

public class Node {
	private RoutingTable routingTable = null;

	public Node(RoutingTable routingTable) {
		this.routingTable = routingTable;
		startSelfRefresh();
	}

	public void findNode(Contact contact) {

	}

	public void findValue(Contact contact) {
		try {
			MessageDigest md5=MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void startSelfRefresh() {
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(createRreshTask(), Commons.TIME_INTERVAL, Commons.TIME_INTERVAL, TimeUnit.MINUTES);
	}

	private Runnable createRreshTask() {
		return new Runnable() {
			public void run() {
				List<KBucket> buckets = routingTable.getBuckets();
				for (KBucket bucket : buckets) {
					if (bucket.isOld()) {

					}
				}

			}
		};
	}
}

package com.zf.kademlia.routing;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.zf.kademlia.common.Commons;

public class RoutingManageService {
	private ScheduledExecutorService service = null;
	private RoutingTable routingTable = null;

	public RoutingManageService(RoutingTable routingTable) {
		this.routingTable = routingTable;
	}

	public void start() {
		Runnable runnable = new Runnable() {
			public void run() {
				List<KBucket> buckets = routingTable.getBuckets();
				for (KBucket bucket : buckets) {
					if (bucket.isOld()) {

					}
				}

			}
		};
		service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(runnable, Commons.TIME_INTERVAL, Commons.TIME_INTERVAL, TimeUnit.MINUTES);
	}

	public void stop() {
		if (service != null) {
			service.shutdown();
		}
	}
}

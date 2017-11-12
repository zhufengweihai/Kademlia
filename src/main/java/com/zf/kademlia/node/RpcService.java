package com.zf.kademlia.node;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.zf.kademlia.common.Commons;
import com.zf.kademlia.routing.KBucket;
import com.zf.kademlia.routing.RoutingTable;

public class RpcService {
	private static final int COUNT_TASK_MIN = 1;
	private static final int COUNT_TASK_MAX = 10;

	private ExecutorService executorService = null;

	public RpcService() {

	}

	public void start() {
		executorService = new ThreadPoolExecutor(COUNT_TASK_MIN, COUNT_TASK_MAX, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>());
	}

	public void addTask() {

	}

	public void stop() {
		if (executorService != null) {
			executorService.shutdown();
		}
	}
}

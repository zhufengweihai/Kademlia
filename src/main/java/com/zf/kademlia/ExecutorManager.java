package com.zf.kademlia;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhufeng
 * @date 2017-12-17
 */
public class ExecutorManager {
	private static ExecutorManager manager = new ExecutorManager();
	private ExecutorService executorService = null;
	private ScheduledExecutorService scheduledService = null;

	private ExecutorManager() {
		executorService = new ThreadPoolExecutor(0, 3, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		scheduledService = new ScheduledThreadPoolExecutor(0);
		new HashMap<>();
	}

	public static void schedule(Runnable callable, long delay) {
		manager.scheduledService.schedule(callable, delay, TimeUnit.MILLISECONDS);
	}
}

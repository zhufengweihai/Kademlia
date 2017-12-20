package com.zf.kademlia;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ListenableScheduledFuture;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * @author zhufeng
 * @date 2017-12-17
 */
public class ExecutorManager {
	private static ExecutorManager manager = new ExecutorManager();
	private ExecutorService executorService = null;
	private ListeningScheduledExecutorService scheduledService = null;
	private Map<Object, Future<?>> scheduledMap = null;

	private ExecutorManager() {
		executorService = new ThreadPoolExecutor(0, 3, 1L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		scheduledService = MoreExecutors.listeningDecorator(new ScheduledThreadPoolExecutor(1));
		scheduledMap = new HashMap<>();
	}

	public static void scheduleAndCancelLast(Object requester, Runnable callable, long delay) {
		ListenableScheduledFuture<?> future = manager.scheduledService.schedule(callable, delay, TimeUnit.MILLISECONDS);
		future.addListener(createCompleteListener(future), manager.executorService);
	}

	private static Runnable createCompleteListener(Future<?> future) {
		return () -> {
			manager.scheduledMap.remove(future);
		};
	}
}

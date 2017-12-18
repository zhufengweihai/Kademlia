package com.zf.kademlia;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhufeng
 * @date 2017-12-17
 */
public class ScheduleManager {
	private static ScheduleManager scheduleManager = new ScheduleManager();
	private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

	private ScheduleManager() {
	}

	public static ScheduleManager instance() {
		return scheduleManager;
	}

	public static ScheduledFuture<?> schedule(Runnable callable) {
		return scheduleManager.executorService.schedule(callable, 0, TimeUnit.MILLISECONDS);
	}

	public static void schedule(Runnable callable, long delay) {
		scheduleManager.executorService.schedule(callable, delay, TimeUnit.MILLISECONDS);
	}
}

package com.epam.jarst.jmh.benchmark.profiler;

import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.IterationParams;
import org.openjdk.jmh.profile.InternalProfiler;
import org.openjdk.jmh.profile.ProfilerResult;
import org.openjdk.jmh.results.AggregationPolicy;
import org.openjdk.jmh.results.IterationResult;
import org.openjdk.jmh.results.Result;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class HotSpotAllocationProfiler implements InternalProfiler {
	private static final ThreadMXBean threadMXBean;
	private static final Method getThreadAllocatedBytes;
	private static final Collection<String> FAIL_MSGS;

	private static final long[] EMPTY_IDS = new long[0];
	public static final String PREFIX = "\u00b7";

	private long[] beforeIds, beforeAllocated;
	private long[] tmpIds, tmpAllocated; // To avoid allocation while returning
	// two values from
	// getTotalAllocatedMemory
	private long beforeTime;

	static {
		threadMXBean = ManagementFactory.getThreadMXBean();
		Collection<String> failMessages;
		Method getBytes;
		try {
			getBytes = threadMXBean.getClass().getMethod("getThreadAllocatedBytes", long[].class);
			getBytes.setAccessible(true);
			failMessages = Collections.emptyList();
		} catch (NoSuchMethodException e) {
			getBytes = null;
			failMessages = Collections.singletonList("Method getThreadAllocatedBytes is not found in ThreadMXBean: "
					+ e.getMessage() + ", are you running HotSpot VM 1.6u26+?");
		}
		getThreadAllocatedBytes = getBytes;
		FAIL_MSGS = failMessages;
	}

	public boolean checkSupport(List<String> msgs) {
		msgs.addAll(FAIL_MSGS);
		return FAIL_MSGS.isEmpty();
	}

	public String label() {
		return "hs_alloc";
	}

	@Override
	public String getDescription() {
		return "Memory allocation profiler";
	}

	@Override
	public void beforeIteration(BenchmarkParams benchmarkParams, IterationParams iterationParams) {
		snapshotAllocatedMemory();
		beforeAllocated = tmpAllocated;
		beforeIds = tmpIds;
		beforeTime = System.nanoTime();
	}

	private void snapshotAllocatedMemory() {
		if (getThreadAllocatedBytes == null) {
			tmpIds = tmpAllocated = EMPTY_IDS;
			return;
		}
		try {
			tmpIds = threadMXBean.getAllThreadIds();
			tmpAllocated = (long[]) getThreadAllocatedBytes.invoke(threadMXBean, (Object) tmpIds);
			return;
		} catch (IllegalAccessException | InvocationTargetException e) {
		    /* intentionally left blank */
		}
		// In exceptional cases, assume information is not available
		tmpIds = tmpAllocated = EMPTY_IDS;
	}

	@Override
	public Collection<? extends Result> afterIteration(BenchmarkParams benchmarkParams, IterationParams iterationParams, IterationResult result) {
		long afterTime = System.nanoTime();
		snapshotAllocatedMemory();
		if (beforeAllocated.length == 0 || tmpAllocated.length == 0) {
			// When allocation profiling fails, make sure it is distinguishable
			// in report
			return Collections.singleton(new ProfilerResult(PREFIX + "allocated", -1, "error", AggregationPolicy.MAX));
		}
		long allocated = estimateAllocatedBytes();
		// When no allocations measured, we just ignore zero result lines to
		// improve readability
		if (allocated == 0) {
			return Collections.emptyList();
		}
		ArrayList<Result> results = new ArrayList<Result>(2);
		if (afterTime - beforeTime != 0) {
			results.add(new ProfilerResult(PREFIX + "allocation.rate", 1.0 * allocated / 1024 / 1024
					* TimeUnit.SECONDS.toNanos(1) / (afterTime - beforeTime), "MB/sec", AggregationPolicy.AVG));
		}
		if (result.getMetadata().getAllOps() != 0) {
			results.add(new ProfilerResult(PREFIX + "allocated", 1.0 * allocated / result.getMetadata().getAllOps(), "B/op", AggregationPolicy.AVG));
		}
		return results;
	}

	/**
	 * Estimates allocated bytes based on two snapshots. The problem is threads
	 * can come and go while performing the benchmark, thus we would miss
	 * allocations made in a thread that was created and died between the
	 * snapshots.
	 *
	 * @return estimated number of allocated bytes between profiler calls
	 */
	private long estimateAllocatedBytes() {
		HashMap<Long, Integer> prevIndex = new HashMap<Long, Integer>();
		for (int i = 0; i < beforeIds.length; i++) {
			long id = beforeIds[i];
			prevIndex.put(id, i);
		}
		long currentThreadId = Thread.currentThread().getId();
		long allocated = 0;
		for (int i = 0; i < tmpIds.length; i++) {
			long id = tmpIds[i];
			if (id == currentThreadId) {
				continue;
			}
			allocated += tmpAllocated[i];
			Integer prev = prevIndex.get(id);
			if (prev != null) {
				allocated -= beforeAllocated[prev];
			}
		}
		return allocated;
	}
}
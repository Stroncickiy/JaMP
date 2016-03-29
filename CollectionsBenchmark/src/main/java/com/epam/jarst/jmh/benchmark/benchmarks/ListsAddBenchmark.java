package com.epam.jarst.jmh.benchmark.benchmarks;

import com.epam.jarst.jmh.benchmark.annotations.BenchmarkTest;
import com.epam.jarst.jmh.benchmark.profiler.HotSpotAllocationProfiler;
import com.epam.jarst.jmh.benchmark.results.BenchmarkResultSet;
import com.google.common.collect.Lists;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@BenchmarkTest(name = "Add data to Lists (to end) ",description = "add data to end of different Array lists")
public class ListsAddBenchmark implements  BenchmarkTask {
	public List<Integer> jdkList;
	public List<Integer> guavaList;
	public IntList fastUtilList;
	public TIntList troveList;
	public com.carrotsearch.hppc.IntArrayList hppcList = new com.carrotsearch.hppc.IntArrayList();

	public int i;

	@Setup(Level.Trial)
	public void init() {
		i = 0;
		jdkList = new ArrayList<>();
		fastUtilList = new IntArrayList();
		troveList = new TIntArrayList();
		guavaList = Lists.newArrayList();
	}

	@TearDown(Level.Trial)
	public void tearDown() {

	}

	@Benchmark
	public void jdk() {
		jdkList.add(i++);
	}

	@Benchmark
	public void guava() {
		guavaList.add(i++);
	}

	@Benchmark
	public void fastUtil() {
		fastUtilList.add(i++);
	}

	@Benchmark
	public void trove() {
		troveList.add(i++);
	}

	@Benchmark
	public void hppc() {
		hppcList.add(i++);
	}



    @Override
	public BenchmarkResultSet runBenchmark() throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(".*" + ListsAddBenchmark.class.getSimpleName() + ".*")
				.jvmArgs("-Xms3G", "-Xmx3G", "-XX:MaxDirectMemorySize=1G", "-XX:+PrintGCDetails", "-XX:+PrintGCTimeStamps", "-Xloggc:D:\\gc.log")
				.timeUnit(TimeUnit.MICROSECONDS)
				.forks(10)
				.warmupIterations(50)
				.warmupTime(TimeValue.nanoseconds(1000000))
				.measurementIterations(50)
				.measurementTime(TimeValue.nanoseconds(1000000))
				.build();
        Collection<RunResult> results = new Runner(opt).run();
        ArrayList<RunResult> runResults = new ArrayList<>(results);
        Collections.sort(runResults,(o1, o2) -> (int)(o2.getPrimaryResult().getScore()-o1.getPrimaryResult().getScore()));
        BenchmarkTest annotation = AnnotationUtils.findAnnotation(this.getClass(), BenchmarkTest.class);
        String description = annotation.description();
        String name = annotation.name();
        BenchmarkResultSet benchmarkResultSet = new BenchmarkResultSet(name,description);
        benchmarkResultSet.setRunResults(runResults);
        return benchmarkResultSet;
	}

}

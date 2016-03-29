package com.epam.jarst.jmh.benchmark.benchmarks;

import com.epam.jarst.jmh.benchmark.annotations.BenchmarkTest;
import com.epam.jarst.jmh.benchmark.profiler.HotSpotAllocationProfiler;
import com.epam.jarst.jmh.benchmark.results.BenchmarkResultSet;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@BenchmarkTest(name = "Linked Vs Array",description = "Comparison of LinkedList and ArrayList")
@State(Scope.Thread)
public class LinkedVsArrayList implements  BenchmarkTask {
	public List<Integer> linkedList;
	public List<Integer> arrayList;


	public int i;

	@Setup(Level.Trial)
	public void init() {
		i = 0;
		linkedList = new LinkedList<>();
		arrayList = new ArrayList<>();
	}

	@TearDown(Level.Trial)
	public void tearDown() {

	}

	@Benchmark
	public void linked() {
		linkedList.add(i++);
	}

	@Benchmark
	public void array() {
		arrayList.add(i++);
	}


	@Override
	public BenchmarkResultSet runBenchmark() throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(".*" + LinkedVsArrayList.class.getSimpleName() + ".*")
				.jvmArgs("-Xms3G", "-Xmx3G", "-XX:MaxDirectMemorySize=1G", "-XX:+PrintGCDetails", "-XX:+PrintGCTimeStamps", "-Xloggc:D:\\gc.log")
				.timeUnit(TimeUnit.MICROSECONDS)
				.forks(1)
				.warmupIterations(10)
				.warmupTime(TimeValue.nanoseconds(1000000))
				.measurementIterations(10)
				.measurementTime(TimeValue.nanoseconds(1000000))
				.build();
        Collection<RunResult> results = new Runner(opt).run();
        List<RunResult> runResults = new ArrayList<>(results);
        Collections.sort(runResults,(o1, o2) -> (int)(o2.getPrimaryResult().getScore()-o1.getPrimaryResult().getScore()));
        BenchmarkTest annotation = AnnotationUtils.findAnnotation(this.getClass(), BenchmarkTest.class);
        String description = annotation.description();
        String name = annotation.name();
        BenchmarkResultSet benchmarkResultSet = new BenchmarkResultSet(name,description);
        benchmarkResultSet.setRunResults(runResults);
        return benchmarkResultSet;
	}

}

package com.epam.jarst.jmh.benchmark.benchmarks;

import com.epam.jarst.jmh.benchmark.annotations.BenchmarkTest;
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

import java.util.*;
import java.util.concurrent.TimeUnit;

@BenchmarkTest(name = "Add data to  Lists (to Middle)  ",description = "Comparison of add operation to middle of different Array lists")
@State(Scope.Thread)
public class ListsAddIntoMiddleBenchmark implements  BenchmarkTask {
	public List<Integer> jdkList;
	public List<Integer> guavaList;
	public IntList fastUtilList;
	public TIntList troveList;
	public com.carrotsearch.hppc.IntArrayList hppcList = new com.carrotsearch.hppc.IntArrayList();
    public List<Integer> jdkLinkedList;

	public int i;

	@Setup(Level.Trial)
	public void init() {
		i = 0;
		jdkList = new ArrayList<>();
		fastUtilList = new IntArrayList();
		troveList = new TIntArrayList();
		guavaList = Lists.newArrayList();
        jdkLinkedList = new LinkedList<>();
		for (int j = 0; j < 1000; j++) {
			jdkList.add(j);
			fastUtilList.add(j);
			guavaList.add(j);
			troveList.add(j);
			hppcList.add(j);
            jdkLinkedList.add(j);
		}
	}

	@TearDown(Level.Trial)
	public void tearDown() {

	}

    @Benchmark
    public void jdkLinked() {
        jdkLinkedList.add(jdkLinkedList.size()/2,i++);
    }



	@Benchmark
	public void jdkArrayList() {
		jdkList.add(jdkList.size()/2,i++);
	}

	@Benchmark
	public void guava() {
		guavaList.add(guavaList.size()/2,i++);
	}

	@Benchmark
	public void fastUtil() {
		fastUtilList.add(guavaList.size()/2,i++);
	}

	@Benchmark
	public void trove() {
		troveList.insert(troveList.size()/2,i++);
	}

	@Benchmark
	public void hppc() {
		hppcList.add(hppcList.size(),i++);
	}



    @Override
	public BenchmarkResultSet runBenchmark() throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(".*" + ListsAddIntoMiddleBenchmark.class.getSimpleName() + ".*")
				.jvmArgs("-Xms3G", "-Xmx3G", "-XX:MaxDirectMemorySize=1G", "-XX:+PrintGCDetails", "-XX:+PrintGCTimeStamps", "-Xloggc:D:\\gc.log")
				.timeUnit(TimeUnit.MICROSECONDS)
				.forks(1)
				.warmupIterations(10)
				.warmupTime(TimeValue.nanoseconds(1000000))
				.mode(Mode.AverageTime)
				.measurementIterations(10)
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

package com.epam.jarst.jmh.benchmark.benchmarks;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.epam.jarst.jmh.benchmark.annotations.BenchmarkTest;
import com.epam.jarst.jmh.benchmark.profiler.HotSpotAllocationProfiler;
import com.epam.jarst.jmh.benchmark.results.BenchmarkResultSet;
import com.google.common.collect.Maps;
import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
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

@BenchmarkTest(name = "Maps Put 100 000 elements Comparison", description = "Comparison of time taken for put operation on Different maps")
@State(Scope.Thread)
public class MapsPutBenchmark implements BenchmarkTask {
    public Map<Integer, Integer> hashMap;
    public Dictionary<Integer, Integer> hashTable;
    public Map<Integer, Integer> guavaMap;
    public Int2ObjectOpenHashMap<Integer> fastutilMap;
    public TMap<Integer, Integer> tMap;
    public com.carrotsearch.hppc.IntObjectHashMap hppcMap;


    public int i;

    @Setup(Level.Trial)
    public void init() {
        i = 0;
        hashMap = new HashMap<>();
        hashTable = new Hashtable<>();
        guavaMap = Maps.newHashMap();
        fastutilMap = new Int2ObjectOpenHashMap<>();
        tMap = new THashMap<>();
        hppcMap = new IntObjectHashMap();

    }

    @TearDown(Level.Trial)
    public void tearDown() {

    }

    @Benchmark
    public void jdkMap() {
        for (int i = 0; i <100_000 ; i++) {
            hashMap.put(i, i);
        }

    }

    @Benchmark
    public void jdkTable() {
        for (int i = 0; i <100_000 ; i++) {
            hashTable.put(i, i);
        }
    }

    @Benchmark
    public void fastUtilMap() {
        for (int i = 0; i <100_000 ; i++) {
            fastutilMap.put(i, Integer.valueOf(i));
        }
    }

    @Benchmark
    public void guavaMap() {
        for (int i = 0; i <100_000 ; i++) {
            guavaMap.put(Integer.valueOf(i), Integer.valueOf(i));
        }
    }

    @Benchmark
    public void tMap() {
        for (int i = 0; i <100_000 ; i++) {
            tMap.put(Integer.valueOf(i), Integer.valueOf(i));
        }
    }

    @Benchmark
    public void hppcMap() {
        for (int i = 0; i <100_000 ; i++) {
            hppcMap.put(i, Integer.valueOf(i));
        }
    }


    @Override
    public BenchmarkResultSet runBenchmark() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + MapsPutBenchmark.class.getSimpleName() + ".*")
                .jvmArgs("-Xms3G", "-Xmx3G", "-XX:MaxDirectMemorySize=1G", "-XX:+PrintGCDetails", "-XX:+PrintGCTimeStamps", "-Xloggc:D:\\gc.log")
                .timeUnit(TimeUnit.MILLISECONDS)
                .forks(1)
                .mode(Mode.AverageTime)
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
        BenchmarkResultSet benchmarkResultSet = new BenchmarkResultSet(name, description);
        benchmarkResultSet.setRunResults(runResults);
        return benchmarkResultSet;
    }

}

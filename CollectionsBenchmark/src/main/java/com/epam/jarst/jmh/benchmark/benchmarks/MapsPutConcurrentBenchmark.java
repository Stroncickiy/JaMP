package com.epam.jarst.jmh.benchmark.benchmarks;

import com.carrotsearch.hppc.IntObjectHashMap;
import com.epam.jarst.jmh.benchmark.annotations.BenchmarkTest;
import com.epam.jarst.jmh.benchmark.results.BenchmarkResultSet;
import com.google.common.collect.Maps;
import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.omg.PortableInterceptor.INACTIVE;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@BenchmarkTest(name = "Maps Put 100 000 elements Comparison Concurrently", description = "Comparison of time taken for put operation on Different maps in Concurrent Environment")
@State(Scope.Benchmark)
public class MapsPutConcurrentBenchmark implements BenchmarkTask {
    public Map<Integer, Integer> hashMap;
    public Dictionary<Integer, Integer> hashTable;
    public Map<Integer, Integer> guavaMap;
    public Int2ObjectOpenHashMap<Integer> fastutilMap;
    public TMap<Integer, Integer> tMap;
    public IntObjectHashMap hppcMap;
    public Map<Integer, Integer> synchronizedMap;
    public Map<Integer, Integer> concurentHashMap;


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
        synchronizedMap = Collections.synchronizedMap(new HashMap<>());
        concurentHashMap = new ConcurrentHashMap<>();

    }

    @Setup(Level.Iteration)
    public void beforeIteration() {
      i++;
    }

    @TearDown
    public  void tearDown(){

    }


    @Group(value = "jdkSynchronizedMapGroup")
    @Benchmark
    public void jdkSyncronizedMap() {
        synchronizedMap.put(i, i);

    }

    @Group(value = "jdkSynchronizedMapGroup")
    @Benchmark
    public void jdkSyncronizedMap2() {
        synchronizedMap.put(i, i);

    }


    @Group(value = "jdkConcurentMapGroup")
    @Benchmark
    public void jdkConcurrentMap() {
        concurentHashMap.put(i, i);

    }

    @Group(value = "jdkConcurentMapGroup")
    @Benchmark
    public void jdkConcurrentMap2() {
        concurentHashMap.put(i, i);

    }


    @Group(value = "jdkMapGroup")
    @Benchmark
    public void jdkMap() {
        hashMap.put(i, i);

    }

    @Group(value = "jdkMapGroup")
    @Benchmark
    public void jdkMap2() {
        hashMap.put(i, i);

    }


    @Group(value = "jdkTableGroup")
    @Benchmark
    public void jdkTable() {
        hashTable.put(i, i);

    }

    @Group(value = "jdkTableGroup")
    @Benchmark
    public void jdkTable2() {
        hashTable.put(i, i);

    }

    @Group(value = "fastUtilMapGroup")
    @Benchmark
    public void fastUtilMap() {
        fastutilMap.put(i, Integer.valueOf(i));

    }

    @Group(value = "fastUtilMapGroup")
    @Benchmark
    public void fastUtilMap2() {
        fastutilMap.put(i, Integer.valueOf(i));

    }


    @Group(value = "guavaMapGroup")
    @Benchmark
    public void guavaMap() {
        guavaMap.put(Integer.valueOf(i), Integer.valueOf(i));

    }


    @Group(value = "guavaMapGroup")
    @Benchmark
    public void guavaMap2() {
        guavaMap.put(Integer.valueOf(i), Integer.valueOf(i));

    }


    @Group(value = "troveMapGroup")
    @Benchmark
    public void tMap() {
        tMap.put(Integer.valueOf(i), Integer.valueOf(i));

    }

    @Group(value = "troveMapGroup")
    @Benchmark
    public void tMap2() {
        tMap.put(Integer.valueOf(i), Integer.valueOf(i));

    }

    @Group(value = "hppcMapGroup")
    @Benchmark
    public void hppcMap() {
        hppcMap.put(i, Integer.valueOf(i));

    }


    @Group(value = "hppcMapGroup")
    @Benchmark
    public void hppcMap2() {
        hppcMap.put(i, Integer.valueOf(i));

    }


    @Override
    public BenchmarkResultSet runBenchmark() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + MapsPutConcurrentBenchmark.class.getSimpleName() + ".*")
                .jvmArgs("-Xms3G", "-Xmx3G", "-XX:MaxDirectMemorySize=1G", "-XX:+PrintGCDetails", "-XX:+PrintGCTimeStamps", "-Xloggc:D:\\gc.log")
                .timeUnit(TimeUnit.MILLISECONDS)
                .forks(1)
                .threads(20)
                .mode(Mode.AverageTime)
                .warmupIterations(10)
                .warmupTime(TimeValue.nanoseconds(1000000))
                .measurementIterations(10)
                .measurementTime(TimeValue.nanoseconds(1000000))
                .build();
        Collection<RunResult> results = new Runner(opt).run();
        List<RunResult> runResults = new ArrayList<>(results);
        Collections.sort(runResults, (o1, o2) -> (int) (o2.getPrimaryResult().getScore() - o1.getPrimaryResult().getScore()));
        BenchmarkTest annotation = AnnotationUtils.findAnnotation(this.getClass(), BenchmarkTest.class);
        String description = annotation.description();
        String name = annotation.name();
        BenchmarkResultSet benchmarkResultSet = new BenchmarkResultSet(name, description);
        benchmarkResultSet.setRunResults(runResults);
        return benchmarkResultSet;
    }

}

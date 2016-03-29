package com.epam.jarst.jmh.benchmark.benchmarks;

import com.epam.jarst.jmh.benchmark.annotations.BenchmarkTest;
import com.epam.jarst.jmh.benchmark.results.BenchmarkResultSet;
import com.epam.jarst.jmh.benchmark.results.FileSystemScaningResult;
import com.epam.jarst.jmh.benchmark.scanners.FileSystemScanner;
import com.epam.jarst.jmh.benchmark.scanners.impl.ForkJoinFileSystemScanner;
import com.epam.jarst.jmh.benchmark.scanners.impl.MultyThreadFileSystemScanner;
import com.epam.jarst.jmh.benchmark.scanners.impl.SingleThreadFileSystemScanner;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

@BenchmarkTest(name = "FileSystem Scanning", description = "Comparison of different FileSystem scanners which uses different count of threads")
@State(Scope.Benchmark)
public class FileSystemScanBenchmark implements BenchmarkTask {

    private FileSystemScanner forkJoinScanner = new ForkJoinFileSystemScanner();
    private FileSystemScanner singleThreadScanner = new SingleThreadFileSystemScanner();
    private FileSystemScanner multyThreadScanner = new MultyThreadFileSystemScanner();


    @Setup(Level.Trial)
    public void init() {


    }

    @TearDown(Level.Trial)
    public void tearDown() {

    }

    @Benchmark
    public void scanFileSystemInOneThread() {
        singleThreadScanner.scanFileSystem(new File("D:\\"));
        singleThreadScanner.printResult();


    }


    @Benchmark
    public void scanFileSystemInMultipleThreads() {
        multyThreadScanner.scanFileSystem(new File("D:\\"));
        multyThreadScanner.printResult();
    }

    @Benchmark
    public void scanFileSystemForkJoin() {
        forkJoinScanner.scanFileSystem(new File("D:\\"));
        forkJoinScanner.printResult();
    }


    @Override
    public BenchmarkResultSet runBenchmark() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(".*" + FileSystemScanBenchmark.class.getSimpleName() + ".*")
                .jvmArgs("-Xms3G", "-Xmx3G", "-XX:MaxDirectMemorySize=1G", "-XX:+PrintGCDetails", "-XX:+PrintGCTimeStamps", "-Xloggc:D:\\gc.log")
                .timeUnit(TimeUnit.SECONDS)
                .forks(1)
                .mode(Mode.SingleShotTime)
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

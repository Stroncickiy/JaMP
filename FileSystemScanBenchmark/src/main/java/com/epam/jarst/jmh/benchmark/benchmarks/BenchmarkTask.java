package com.epam.jarst.jmh.benchmark.benchmarks;


import com.epam.jarst.jmh.benchmark.results.BenchmarkResultSet;
import org.openjdk.jmh.runner.RunnerException;


public interface BenchmarkTask {
     BenchmarkResultSet runBenchmark() throws RunnerException;
}

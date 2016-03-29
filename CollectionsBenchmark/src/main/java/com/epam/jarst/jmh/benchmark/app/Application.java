package com.epam.jarst.jmh.benchmark.app;


import com.epam.jarst.jmh.benchmark.benchmarks.*;
import com.epam.jarst.jmh.benchmark.results.BenchmarkResultSet;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.results.format.ResultFormatFactory;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.RunnerException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Application {

    public static void main(String... args) throws IllegalAccessException, InstantiationException, RunnerException, FileNotFoundException {
        Set<Class<? extends BenchmarkTask>> classes = new HashSet<>();
        classes.add(ListsAddBenchmark.class);
        classes.add(ListsAddIntoMiddleBenchmark.class);
        classes.add(LinkedVsArrayList.class);
        classes.add(MapsPutBenchmark.class);
        classes.add(MapsPutConcurrentBenchmark.class);



        List<BenchmarkResultSet> benchmarkResultSets = new ArrayList<>();
        for (Class<? extends BenchmarkTask> clazz : classes) {
            BenchmarkTask benchmarkTask = clazz.newInstance();
            BenchmarkResultSet benchmarkResultSet = benchmarkTask.runBenchmark();
            benchmarkResultSets.add(benchmarkResultSet);
        }

        for (BenchmarkResultSet benchmarkResultSet : benchmarkResultSets) {
            System.out.println();
            System.out.println("-------------------------------------------------------------------------");
            System.out.println(" Benchmark:  " + benchmarkResultSet.getBenchmarkName());
            System.out.println(" Description:  " + benchmarkResultSet.getBenchmarkDesription());
            List<RunResult> runResults = benchmarkResultSet.getRunResults();
            FileOutputStream fileOutputStream = new FileOutputStream(new File(benchmarkResultSet.getBenchmarkName() + ".csv"));
            ResultFormatFactory.getInstance(ResultFormatType.CSV, new PrintStream(fileOutputStream)).writeOut(runResults);
            ResultFormatFactory.getInstance(ResultFormatType.TEXT, System.out).writeOut(runResults);
            System.out.println("-------------------------------------------------------------------------");
            System.out.println();


        }


    }
}

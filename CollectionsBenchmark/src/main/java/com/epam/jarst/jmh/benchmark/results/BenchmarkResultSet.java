package com.epam.jarst.jmh.benchmark.results;

import org.openjdk.jmh.results.RunResult;

import java.util.Collections;
import java.util.List;


public class BenchmarkResultSet {

    private String benchmarkName;
    private String benchmarkDesription;
    private List<RunResult> runResults;


    public BenchmarkResultSet(String benchmarkName, String benchmarkDesription) {
        this.benchmarkName = benchmarkName;
        this.benchmarkDesription = benchmarkDesription;
    }

    public String getBenchmarkName() {
        return benchmarkName;
    }

    public void setBenchmarkName(String benchmarkName) {
        this.benchmarkName = benchmarkName;
    }

    public String getBenchmarkDesription() {
        return benchmarkDesription;
    }

    public void setBenchmarkDesription(String benchmarkDesription) {
        this.benchmarkDesription = benchmarkDesription;
    }

    public List<RunResult> getRunResults() {
        return runResults;
    }

    public void setRunResults(List<RunResult> runResults) {
        this.runResults = runResults;
    }


    @Override
    public String toString() {
        return "BenchmarkResultSet{" +
                "benchmarkName='" + benchmarkName + '\'' +
                ", benchmarkDesription='" + benchmarkDesription + '\'' +
                ", runResults=" + runResults +
                '}';
    }
}

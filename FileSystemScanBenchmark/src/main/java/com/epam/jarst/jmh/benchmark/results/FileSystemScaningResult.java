package com.epam.jarst.jmh.benchmark.results;


import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FileSystemScaningResult {
    private AtomicInteger countAllFilesAndFolders;
    private AtomicInteger countFiles;
    private AtomicInteger countFolders;
    private AtomicInteger countDistinctFiles;
    private Map<String, AtomicInteger> fileOccurrences;

    public FileSystemScaningResult() {
        countDistinctFiles = new AtomicInteger();
        countAllFilesAndFolders = new AtomicInteger();
        countFiles = new AtomicInteger();
        countFolders = new AtomicInteger();
        countDistinctFiles = new AtomicInteger();
        fileOccurrences = Collections.synchronizedMap(new HashMap<>());
    }

    public AtomicInteger getCountAllFilesAndFolders() {
        return countAllFilesAndFolders;
    }

    public void registerFile(File file) {
        if (file.isFile()) {
            countFiles.incrementAndGet();
        } else {
            countFolders.incrementAndGet();
        }
        countAllFilesAndFolders.incrementAndGet();
        registerOccurence(file);
    }


    private void registerOccurence(File file) {
        if (file.getName() != null) {
            if (fileOccurrences.containsKey(file.getName())) {
                fileOccurrences.get(file.getName()).incrementAndGet();
            } else {
                countDistinctFiles.incrementAndGet();
                fileOccurrences.put(file.getName(), new AtomicInteger(1));
            }
        }

    }


    public AtomicInteger getCountFiles() {
        return countFiles;
    }


    public AtomicInteger getCountFolders() {
        return countFolders;
    }


    public AtomicInteger getCountDistinctFiles() {
        return countDistinctFiles;
    }


    public Map<String, AtomicInteger> getFileOccurrences() {
        return fileOccurrences;
    }


}

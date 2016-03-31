package com.epam.jarst.jmh.benchmark.scanners;


import com.epam.jarst.jmh.benchmark.results.FileSystemScaningResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class FileSystemScanner {

    protected String name;

    protected FileSystemScanner(String name) {
        this.name = name;
    }

    protected FileSystemScaningResult fileSystemScaningResult;

    public void scanFileSystem(File root) {
        fileSystemScaningResult = new FileSystemScaningResult();
        walk(root);
        printResult();
    }

    protected synchronized void printResult() {
        System.out.println("---------------------------------------");
        System.out.println("File Scanning result of " + name + " Thread Scanner");
        System.out.println("count of all " +
                fileSystemScaningResult.getCountAllFilesAndFolders());
        System.out.println("count of files " +
                fileSystemScaningResult.getCountFiles());
        System.out.println("count of Folders " +
                fileSystemScaningResult.getCountFolders());
        System.out.println("count of distinct files and folders " +
                fileSystemScaningResult.getCountDistinctFiles());

        Map<String, AtomicInteger> fileOccurences = fileSystemScaningResult.getFileOccurrences();

        System.out.println("Most Popular File And Folder Names");
        Set<Map.Entry<String, AtomicInteger>> entries = fileOccurences.entrySet();
        ArrayList<Map.Entry<String, AtomicInteger>> listEntries = new ArrayList<>(entries);

        List<Map.Entry<String, AtomicInteger>> top10Entries;
        if (listEntries.size() > 10) {
            top10Entries = listEntries.subList(0, 10);
        } else {
            top10Entries = (List<Map.Entry<String, AtomicInteger>>) listEntries.clone();
        }
        for (Map.Entry<String, AtomicInteger> entry : top10Entries) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }


    }

    protected abstract void walk(File root);
}

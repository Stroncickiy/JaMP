package com.epam.jarst.jmh.benchmark.results;


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class FileSystemScaningResult {
    private int countAllFilesAndFolders;
    private int countFiles;
    private int countFolders;
    private int countDistinctFiles;
    private Map<String, Integer> fileOccurrences;

    public FileSystemScaningResult() {
        fileOccurrences = new HashMap<>();
    }

    public int getCountAllFilesAndFolders() {
        return countAllFilesAndFolders;
    }

    public void registerFile(File file) {
        if (file.isFile()) {
            countFiles++;
        } else {
            countFolders++;
        }
        countAllFilesAndFolders++;
        registerOccurence(file);
    }


    private void registerOccurence(File file) {
        if (file.getName() != null) {
            if (fileOccurrences.containsKey(file)) {
                fileOccurrences.computeIfPresent(file.getName(), (file1, integer) -> integer++);
            } else {
                countDistinctFiles++;
                fileOccurrences.put(file.getName(), 1);
            }
        }

    }


    public int getCountFiles() {
        return countFiles;
    }


    public int getCountFolders() {
        return countFolders;
    }


    public int getCountDistinctFiles() {
        return countDistinctFiles;
    }


    public Map<String, Integer> getFileOccurrences() {
        return fileOccurrences;
    }


}

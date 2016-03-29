package com.epam.jarst.jmh.benchmark.scanners.impl;


import com.epam.jarst.jmh.benchmark.results.FileSystemScaningResult;
import com.epam.jarst.jmh.benchmark.scanners.FileSystemScanner;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultyThreadFileSystemScanner extends FileSystemScanner {

    private ExecutorService executorService;

    public MultyThreadFileSystemScanner() {
        executorService = Executors.newFixedThreadPool(20);
    }

    protected void walk(File root) {
        File[] list = root.listFiles();
        if (list == null) return;
        for (File currentFileOrFolder : list) {
            if (currentFileOrFolder.isDirectory()) {
                executorService.submit((Runnable) () -> walk(currentFileOrFolder));
            }
            fileSystemScaningResult.registerFile(currentFileOrFolder);
        }
    }

}

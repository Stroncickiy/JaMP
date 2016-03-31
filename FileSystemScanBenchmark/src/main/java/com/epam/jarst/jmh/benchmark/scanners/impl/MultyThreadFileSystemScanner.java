package com.epam.jarst.jmh.benchmark.scanners.impl;


import com.epam.jarst.jmh.benchmark.results.FileSystemScaningResult;
import com.epam.jarst.jmh.benchmark.scanners.FileSystemScanner;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class MultyThreadFileSystemScanner extends FileSystemScanner {

    private ExecutorService executorService;

    private Phaser phaser = new Phaser();

    public MultyThreadFileSystemScanner(String name) {
        super(name);
        executorService = Executors.newFixedThreadPool(10);
    }

    @Override
    public void scanFileSystem(File root) {
        fileSystemScaningResult = new FileSystemScaningResult();
        phaser.register();

        scanFileSystemPhase(root);
        executorService.shutdown();
        printResult();
    }


    private void scanFileSystemPhase(File root) {
        walk(root);
        phaser.arriveAndAwaitAdvance();
    }

    protected void walk(File root) {
        File[] list = root.listFiles();
        if (list == null) {
            phaser.arriveAndDeregister();
            return;
        }
        for (File currentFileOrFolder : list) {
            fileSystemScaningResult.registerFile(currentFileOrFolder);
            if (currentFileOrFolder.isDirectory()) {
                phaser.register();
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        walk(currentFileOrFolder);
                    }
                });
            }

        }
        phaser.arriveAndDeregister();

    }

}

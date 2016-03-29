package com.epam.jarst.jmh.benchmark.scanners.impl;

import com.epam.jarst.jmh.benchmark.scanners.FileSystemScanner;

import java.io.File;


public class SingleThreadFileSystemScanner extends FileSystemScanner {


    protected void walk(File root) {
        File[] list = root.listFiles();
        if (list == null) return;
        for (File currentFileOrFolder : list) {
            if (currentFileOrFolder.isDirectory()) {
                walk(currentFileOrFolder);
            }
            fileSystemScaningResult.registerFile(currentFileOrFolder);
        }
    }

}

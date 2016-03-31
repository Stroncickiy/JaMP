package com.epam.jarst.jmh.benchmark.scanners.impl;


import com.epam.jarst.jmh.benchmark.scanners.FileSystemScanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ForkJoinFileSystemScanner extends FileSystemScanner {

    public ForkJoinFileSystemScanner(String name) {
        super(name);
    }

    @Override
    protected void walk(File root) {
        ForkJoinPool pool = new ForkJoinPool(30);
        pool.submit(new ScanFolderAction(root));
    }


    private class ScanFolderAction extends RecursiveAction {

        private File[] workingFolderFiles;

        public ScanFolderAction(File folderToWalk) {
            this.workingFolderFiles = folderToWalk.listFiles();
        }

        @Override
        protected void compute() {

            if (workingFolderFiles == null) return;
            if (this.workingFolderFiles.length > 0) {
                List<ScanFolderAction> subTasks =
                        new ArrayList<>();
                subTasks.addAll(createSubtasks(workingFolderFiles));
                for (File file : workingFolderFiles) {
                    fileSystemScaningResult.registerFile(file);
                }
                subTasks.forEach(RecursiveAction::fork);
            }
        }

        private List<ScanFolderAction> createSubtasks(File[] workingFolderFiles) {
            List<ScanFolderAction> subtasks =
                    new ArrayList<>();
            for (File file : workingFolderFiles) {
                if (file.isDirectory()) {
                    subtasks.add(new ScanFolderAction(file));
                }
            }
            return subtasks;
        }

    }

}

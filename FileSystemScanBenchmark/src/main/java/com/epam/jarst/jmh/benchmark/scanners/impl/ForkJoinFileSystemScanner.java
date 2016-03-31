package com.epam.jarst.jmh.benchmark.scanners.impl;


import com.epam.jarst.jmh.benchmark.scanners.FileSystemScanner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Phaser;
import java.util.concurrent.RecursiveAction;

public class ForkJoinFileSystemScanner extends FileSystemScanner {
    private Phaser phaser = new Phaser();

    public ForkJoinFileSystemScanner(String name) {
        super(name);
    }

    @Override
    protected void walk(File root) {
        ForkJoinPool pool = new ForkJoinPool(20);
        phaser.register();
        phaser.register();
        pool.invoke(new ScanFolderAction(root));
        phaser.arriveAndAwaitAdvance();
        pool.shutdown();
    }


    private class ScanFolderAction extends RecursiveAction {

        private File[] workingFolderFiles;

        public ScanFolderAction(File folderToWalk) {
            this.workingFolderFiles = folderToWalk.listFiles();
        }

        @Override
        protected void compute() {
            if (workingFolderFiles == null) {
                phaser.arriveAndDeregister();
                return;
            }
            if (this.workingFolderFiles.length > 0) {
                List<ScanFolderAction> subTasks = createSubtasks(workingFolderFiles);
                for (File file : workingFolderFiles) {
                    fileSystemScaningResult.registerFile(file);
                }
                for (RecursiveAction action : subTasks) {
                    phaser.register();
                    action.fork();
                }
            }
            phaser.arriveAndDeregister();

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

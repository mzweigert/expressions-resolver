package com.mzweigert.expressions_resolver.service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ExpressionsResolverService {

    private static final int DEFAULT_WORKERS = 4;
    private static final int DEFAULT_FIlES_PER_THREAD = 5;

    private Properties properties = new Properties();
    private int filesPerThread;
    private ExecutorService executorService;

    public ExpressionsResolverService() {
        try {
            properties.load(getClass()
                    .getClassLoader()
                    .getResourceAsStream("config.properties")
            );
            this.filesPerThread = initFilesPerThread();
            int workers = initWorkers();
            this.executorService = Executors.newWorkStealingPool(workers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int initWorkers() {
        Object workers = properties.get("workers");
        if (workers != null) {
            return Integer.valueOf(workers.toString());
        } else {
            return DEFAULT_WORKERS;
        }
    }

    private int initFilesPerThread() {
        Object filesPerThread = properties.get("files_per_thread");
        if (filesPerThread != null) {
            return Integer.valueOf(filesPerThread.toString());
        } else {
            return DEFAULT_FIlES_PER_THREAD;
        }
    }

    public void resolve(File inputDir, File outputDir) {
        String[] fileNames = inputDir.list();
        if(fileNames == null || fileNames.length <= 0) {
            System.out.println("No files in input dir");
            return;
        }

        Collection<List<File>> filesPerThread = splitFilesPerThread(fileNames);
        List<FilesProcessingTask> tasks = filesPerThread.stream()
                .map(files -> new FilesProcessingTask(files, outputDir))
                .collect(Collectors.toList());

        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Collection<List<File>> splitFilesPerThread(String[] fileNames) {
        final AtomicInteger counter = new AtomicInteger();

        return Arrays.stream(fileNames)
                .filter(fileName -> fileName.endsWith(".xml"))
                .map(File::new)
                .collect(Collectors.groupingBy(
                        file -> counter.getAndIncrement() / filesPerThread
                ))
                .values();
    }
}

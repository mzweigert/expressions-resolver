package com.mzweigert.expressions_resolver.service;

import com.mzweigert.expressions_resolver.configuration.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ExpressionsResolverService {

    private ExecutorService executorService;

    public ExpressionsResolverService() {
        int workers = Integer.parseInt(Configuration.getProperty("workers"));
        this.executorService = Executors.newWorkStealingPool(workers);
    }

    public void resolve(File inputDir, File outputDir) {
        String[] fileNames = inputDir.list();
        if (fileNames == null || fileNames.length <= 0) {
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
        int filesPerThread = Integer.parseInt(Configuration.getProperty("filesPerThread"));

        return Arrays.stream(fileNames)
                .filter(fileName -> fileName.endsWith(".xml"))
                .map(File::new)
                .collect(Collectors.groupingBy(
                        file -> counter.getAndIncrement() / filesPerThread
                ))
                .values();
    }
}

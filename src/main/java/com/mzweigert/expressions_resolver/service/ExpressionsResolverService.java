package com.mzweigert.expressions_resolver.service;

import com.mzweigert.expressions_resolver.configuration.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
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
        File[] files = inputDir.listFiles();
        if (files == null || files.length <= 0) {
            System.out.println("No files in input dir");
            return;
        }

        Collection<FilesProcessingTask> tasks = createFilesProcessingTasks(files, outputDir);

        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Collection<FilesProcessingTask> createFilesProcessingTasks(File[] files, File outputDir) {
        final AtomicInteger counter = new AtomicInteger();
        int filesPerThread = Integer.parseInt(Configuration.getProperty("filesPerThread"));

        return Arrays.stream(files)
                .filter(file -> file.getName().endsWith(".xml"))
                .collect(Collectors.groupingBy(
                        file -> counter.getAndIncrement() / filesPerThread
                ))
                .values()
                .stream()
                .map(portionFiles -> new FilesProcessingTask(portionFiles, outputDir))
                .collect(Collectors.toList());
    }
}

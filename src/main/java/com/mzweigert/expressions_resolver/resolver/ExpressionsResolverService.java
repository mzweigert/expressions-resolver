package com.mzweigert.expressions_resolver.resolver;

import com.mzweigert.expressions_resolver.configuration.Configuration;
import com.mzweigert.expressions_resolver.serialization.ExpressionsSerializationService;
import com.mzweigert.expressions_resolver.serialization.ExpressionsSerializationServiceFactory;
import com.mzweigert.expressions_resolver.serialization.SerializationType;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ExpressionsResolverService {

    private static final Logger logger = Logger.getAnonymousLogger();

    private ExecutorService executorService;

    public ExpressionsResolverService() {
        int workers = Integer.parseInt(Configuration.getProperty("workers"));
        this.executorService = Executors.newWorkStealingPool(workers);
    }

    public void resolve(File inputDir, File outputDir, SerializationType type) {
        File[] files = inputDir.listFiles();
        if (files == null || files.length <= 0) {
            logger.log(Level.SEVERE, "No files in input directory");
            return;
        }
        ExpressionsSerializationService serializationService =
                ExpressionsSerializationServiceFactory.getInstance(type);
        createFilesProcessingTasks(files, outputDir, serializationService)
                .stream()
                .map(task -> executorService.submit(task))
                .forEach(this::safeFutureGet);
        executorService.shutdown();
    }

    private void safeFutureGet(Future<?> future) {
        if (future == null) {
            return;
        }
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private Collection<FilesProcessingTask> createFilesProcessingTasks(File[] files, File outputDir,
                                                                       ExpressionsSerializationService serializationService) {

        final AtomicInteger counter = new AtomicInteger();
        int filesPerThread = Integer.parseInt(Configuration.getProperty("filesPerThread"));

        return Arrays.stream(files)
                .filter(file -> file.getName().endsWith(".xml"))
                .collect(Collectors.groupingBy(
                        file -> counter.getAndIncrement() / filesPerThread
                ))
                .values()
                .stream()
                .map(portionFiles -> new FilesProcessingTask(portionFiles, outputDir, serializationService))
                .collect(Collectors.toList());
    }
}

package com.mzweigert.expressions_resolver.resolver;

import com.mzweigert.expressions_resolver.configuration.Configuration;
import com.mzweigert.expressions_resolver.serialization.ExpressionsSerializationService;
import com.mzweigert.expressions_resolver.serialization.ExpressionsSerializationServiceFactory;
import com.mzweigert.expressions_resolver.serialization.SerializationType;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    public Optional<FilesProcessingTaskResult> resolve(File inputDir, File outputDir, SerializationType type) {
        File[] files = inputDir.listFiles();
        if (files == null || files.length <= 0) {
            logger.log(Level.SEVERE, "No files in input directory");
            return Optional.empty();
        }
        ExpressionsSerializationService serializationService = ExpressionsSerializationServiceFactory.getInstance(type);
        Collection<FilesProcessingTask> filesProcessingTasks = createFilesProcessingTasks(files, outputDir, serializationService);

        FilesProcessingTaskResult single = null;
        try {
            List<FilesProcessingTaskResult> results = extractResults(filesProcessingTasks);
            single = createSingle(results);

        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Something gone wrong with processing files... \n" + e.toString());
            e.printStackTrace();
        }
        executorService.shutdown();
        return Optional.ofNullable(single);
    }

    private List<FilesProcessingTaskResult> extractResults(Collection<FilesProcessingTask> filesProcessingTasks) throws InterruptedException {
        return executorService.invokeAll(filesProcessingTasks)
            .stream()
            .map(this::safeFutureGet)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private FilesProcessingTaskResult createSingle(List<FilesProcessingTaskResult> results) {
        Collection<File> allSuccessProcessedFiles = new HashSet<>();
        Collection<File> allFailedProcessedFiles = new HashSet<>();

        for (FilesProcessingTaskResult result : results) {
            allFailedProcessedFiles.addAll(result.getFailProcessedFiles());
            allSuccessProcessedFiles.addAll(result.getSuccessProcessedFiles());
        }

        return new FilesProcessingTaskResult(allSuccessProcessedFiles, allFailedProcessedFiles);
    }

    private FilesProcessingTaskResult safeFutureGet(Future<FilesProcessingTaskResult> future) {
        if (future == null) {
            return null;
        }
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
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

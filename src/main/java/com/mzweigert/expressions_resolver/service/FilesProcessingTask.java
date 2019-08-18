package com.mzweigert.expressions_resolver.service;

import com.mzweigert.expressions_resolver.serialization.ExpressionsSerialization;
import com.mzweigert.expressions_resolver.serialization.model.Expression;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class FilesProcessingTask implements Callable<Void> {

    private final Collection<File> filesToProcess;
    private final File outputDir;
    private final ExpressionsSerialization expressionsSerialization;

    public FilesProcessingTask(Collection<File> filesToProcess, File outputDir) {
        this.filesToProcess = filesToProcess;
        this.outputDir = outputDir;
        this.expressionsSerialization = new ExpressionsSerialization();
    }

    @Override
    public Void call() {
        filesToProcess.forEach(toProcess -> {
            Map<Long, String> results = getResults(toProcess);
            Optional<File> outputFile = createOutputFile(toProcess.getName());
            outputFile.ifPresent(file -> expressionsSerialization.marshall(results, file));
        });
        return null;
    }

    private Map<Long, String> getResults(File toProcess) {
        return expressionsSerialization.unmarshall(toProcess)
                .stream()
                .collect(Collectors.toMap(
                        Expression::getId,
                        Expression::calculateAsString,
                        (x, y) -> y,
                        LinkedHashMap::new
                ));
    }

    private Optional<File> createOutputFile(String inputFileName) {
        int dotIndex = inputFileName.lastIndexOf('.');
        String outputFileName = inputFileName.substring(0, dotIndex) + "_result" + inputFileName.substring(dotIndex);
        File outputFile = new File(outputDir, outputFileName);
        try {
            if (outputFile.createNewFile()) {
                return Optional.of(outputFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}

package com.mzweigert.expressions_resolver.service;

import com.mzweigert.expressions_resolver.serialization.ExpressionUnmarshallException;
import com.mzweigert.expressions_resolver.serialization.ExpressionsSerializationService;
import com.mzweigert.expressions_resolver.serialization.model.Expression;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FilesProcessingTask implements Runnable {

    private final Collection<File> filesToProcess;
    private final File outputDir;
    private final ExpressionsSerializationService serializationService;

    public FilesProcessingTask(Collection<File> filesToProcess, File outputDir,
                               ExpressionsSerializationService serializationService) {
        this.filesToProcess = filesToProcess;
        this.outputDir = outputDir;
        this.serializationService = serializationService;
    }

    @Override
    public void run() {
        filesToProcess.forEach(this::processFile);
    }

    private void processFile(File toProcess) {
        try {
            Map<Long, String> results = getResults(toProcess);
            Optional<File> outputFile = createOutputFile(toProcess.getName());
            if (outputFile.isPresent()) {
                serializationService.marshall(results, outputFile.get());
            }
        } catch (JAXBException e) {
            //todo: Handle failed processing file
            e.printStackTrace();
        }
    }

    private Map<Long, String> getResults(File toProcess) throws ExpressionUnmarshallException {
        return serializationService.unmarshall(toProcess)
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

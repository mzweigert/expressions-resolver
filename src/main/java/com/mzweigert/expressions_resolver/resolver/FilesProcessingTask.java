package com.mzweigert.expressions_resolver.resolver;

import com.mzweigert.expressions_resolver.configuration.FileManager;
import com.mzweigert.expressions_resolver.serialization.ExpressionUnmarshallException;
import com.mzweigert.expressions_resolver.serialization.ExpressionsSerializationService;
import com.mzweigert.expressions_resolver.serialization.model.Expression;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FilesProcessingTask implements Callable<FilesProcessingTaskResult> {

    private static final Logger logger = Logger.getAnonymousLogger();

    private final Collection<File> filesToProcess;
    private final File outputDir;
    private final ExpressionsSerializationService serializationService;
    private final FileManager fileManager;

    FilesProcessingTask(Collection<File> filesToProcess, File outputDir,
                        ExpressionsSerializationService serializationService) {
        this.filesToProcess = filesToProcess;
        this.outputDir = outputDir;
        this.serializationService = serializationService;
        this.fileManager = new FileManager();
    }

    private enum ProcessResult {
        SUCCESS,
        FAIL
    }

    @Override
    public FilesProcessingTaskResult call() {
        Collection<File> successProcessedFiles = new HashSet<>();
        Collection<File> failedProcessedFiles = new HashSet<>();
        filesToProcess.forEach(file -> {
            Optional<File> outputFile = fileManager.createOutputFile(outputDir, file.getName());
            if(!outputFile.isPresent()) {
                return;
            }
            ProcessResult processResult = processInput(file, outputFile.get());
            if(processResult == ProcessResult.SUCCESS) {
                successProcessedFiles.add(file);
            } else {
                failedProcessedFiles.add(file);
            }
        });
        return new FilesProcessingTaskResult(successProcessedFiles, failedProcessedFiles);
    }

    private ProcessResult processInput(File file, File outputFile) {
        Map<Long, String> results;
        try {
            results = calculateResults(file);
            serializationService.marshall(results, outputFile);
            return ProcessResult.SUCCESS;
        } catch (JAXBException e) {
            logger.warning(e.toString());
            fileManager.saveErrorToFile(e, outputFile);
            return ProcessResult.FAIL;
        }
    }

    private Map<Long, String> calculateResults(File toProcess) throws ExpressionUnmarshallException {
        return serializationService.unmarshall(toProcess)
                .stream()
                .collect(Collectors.toMap(
                        Expression::getId,
                        Expression::calculateAsString,
                        (x, y) -> y,
                        LinkedHashMap::new
                ));
    }

}

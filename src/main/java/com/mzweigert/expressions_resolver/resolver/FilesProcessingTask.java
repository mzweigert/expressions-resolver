package com.mzweigert.expressions_resolver.resolver;

import com.mzweigert.expressions_resolver.serialization.ExpressionUnmarshallException;
import com.mzweigert.expressions_resolver.serialization.ExpressionsSerializationService;
import com.mzweigert.expressions_resolver.serialization.model.Expression;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FilesProcessingTask implements Runnable {

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

    @Override
    public void run() {
        filesToProcess.forEach(this::processInput);
    }

    private void processInput(File toProcess) {
        Optional<File> outputFile = fileManager.createOutputFile(outputDir, toProcess.getName());
        if(!outputFile.isPresent()) {
            return;
        }
        Map<Long, String> results;
        try {
            results = calculateResults(toProcess);
            serializationService.marshall(results, outputFile.get());

        } catch (JAXBException e) {
            logger.warning(e.toString());
            fileManager.saveErrorToFile(e, outputFile.get());
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

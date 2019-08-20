package com.mzweigert.expressions_resolver;

import com.mzweigert.expressions_resolver.configuration.FileManager;
import com.mzweigert.expressions_resolver.resolver.FilesProcessingTaskResult;
import com.mzweigert.expressions_resolver.serialization.SerializationType;
import com.mzweigert.expressions_resolver.resolver.ExpressionsResolverService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Runner {

    private static final Logger logger = Logger.getAnonymousLogger();

    private ExpressionsResolverService expressionsResolverService;
    private FileManager fileManager;

    public Runner(ExpressionsResolverService expressionsResolverService) {
        this.expressionsResolverService = expressionsResolverService;
        this.fileManager = new FileManager();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            logger.log(Level.SEVERE, "No input and output folders given in args!");
        } else {
            ExpressionsResolverService resolverService = new ExpressionsResolverService();
            new Runner(resolverService).run(args[0], args[1]);
        }
    }

    void run(String inputFolder, String outputFolder) {
        File inputDir = new File(inputFolder);
        boolean inputDirInvalid = !fileManager.checkDirectory(inputDir, false);

        File outputDir = new File(outputFolder);
        boolean outputDirInvalid = !fileManager.checkDirectory(outputDir, true);

        SerializationType type = getSerializationType();

        if (inputDirInvalid || outputDirInvalid) {
            return;
        }
        expressionsResolverService
                .resolve(inputDir, outputDir, type)
                .ifPresent(result -> logger.log(Level.INFO, createInfoFromResult(result)));
    }

    private String createInfoFromResult(FilesProcessingTaskResult result) {
        Optional<File> failedFiles = tryCreateInfoWithFailProcessedFiles(result);

		return "Resolver processed " + result.getSuccessProcessedFilesSize()
				+ " files with success and "
            	+ result.getFailProcessedFilesSize() + " files with fail. \n"
                + failedFiles.map(file -> "Fail processing documents saved in " + file.toString()).orElse("");
    }

    private Optional<File> tryCreateInfoWithFailProcessedFiles(FilesProcessingTaskResult result) {
        Optional<File> failedFiles = Optional.empty();
        if(result.getFailProcessedFilesSize() > 0){
            String fileName = "fail_processed_files_from_"
                    + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date())
                    + ".txt";
            String content = result.getFailProcessedFilesNames()
                    .stream()
                    .collect(Collectors.joining("\n"));
            failedFiles = fileManager.createFileWithContent(fileName, content);
        }
        return failedFiles;
    }

    private SerializationType getSerializationType() {
        return SerializationType.XML;
    }

}
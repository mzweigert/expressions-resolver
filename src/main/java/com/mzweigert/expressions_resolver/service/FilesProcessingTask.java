package com.mzweigert.expressions_resolver.service;

import org.w3c.dom.Element;

import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.Callable;

public class FilesProcessingTask implements Callable<Void> {

    private Collection<File> filesToProcess;
    private final File outputDir;

    public FilesProcessingTask(Collection<File> filesToProcess, File outputDir) {
        this.filesToProcess = filesToProcess;
        this.outputDir = outputDir;
    }

    @Override
    public Void call() {
        filesToProcess.forEach(this::createOutputFile);
        return null;
    }

    private Optional<File> createOutputFile(File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        String outputFileName = name.substring(0, dotIndex)  + "_result" + name.substring(dotIndex);
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

package com.mzweigert.expressions_resolver.resolver;

import javax.xml.bind.JAXBException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

class FileManager {

    private static final Logger logger = Logger.getAnonymousLogger();

    void saveErrorToFile(JAXBException error, File file) {
        try(FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.write(error.toString());
            writer.flush();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot write error to file : " + error.getMessage());
        }
    }

    Optional<File> createOutputFile(File outputDir, String inputFileName) {
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

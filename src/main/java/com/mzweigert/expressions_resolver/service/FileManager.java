package com.mzweigert.expressions_resolver.service;

import javax.xml.bind.JAXBException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

class FileManager {

    void saveErrorToFile(JAXBException error, File file) {
        try(FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.write(error.toString());
        } catch (IOException e) {
            System.out.println("Cannot write error to file : " + error.getMessage());
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

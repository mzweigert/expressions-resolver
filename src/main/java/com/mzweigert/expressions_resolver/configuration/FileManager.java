package com.mzweigert.expressions_resolver.configuration;

import javax.xml.bind.JAXBException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileManager {

    private static final Logger logger = Logger.getAnonymousLogger();

    public void saveErrorToFile(JAXBException error, File file) {
        try(FileWriter fileWriter = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fileWriter)) {
            writer.write(error.toString());
            writer.flush();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot write error to file : " + error.getMessage());
        }
    }

    public Optional<File> createOutputFile(File outputDir, String inputFileName) {
        int dotIndex = inputFileName.lastIndexOf('.');
        String outputFileName = inputFileName.substring(0, dotIndex) + "_result" + inputFileName.substring(dotIndex);
        File outputFile = new File(outputDir, outputFileName);
        try {
            if(outputFile.exists() || outputFile.createNewFile()){
                return Optional.of(outputFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<File> createFileWithContent(String name, Object content) {
        File outputFile = new File(name);
        try (FileOutputStream inputStream = new FileOutputStream(outputFile);
             OutputStreamWriter writer =  new OutputStreamWriter(inputStream, StandardCharsets.UTF_8)) {
            writer.write(content.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.of(outputFile);
    }

    public boolean checkDirectory(File dir, boolean createIfNotExists) {
        if (!dir.exists()) {
            if(createIfNotExists){
                dir.mkdir();
            } else {
                logger.log(Level.SEVERE, dir.getName() + " not exists!");
                return false;
            }
        } else if (!dir.isDirectory()) {
            logger.log(Level.SEVERE, dir.getName() + " is not a folder!");
            return false;
        }
        return true;
    }
}

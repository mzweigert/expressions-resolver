package com.mzweigert.expressions_resolver.service;

import com.mzweigert.expressions_resolver.TestUtilsIT;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class FileManagerIT {

    private FileManager manager = new FileManager();

    private File outputDir;

    @Before
    public void setUp() {
        outputDir = new File("output_test");
        outputDir.mkdir();
    }

    @After
    public void clean() {
        TestUtilsIT.deleteFiles(outputDir);
    }

    @Test
    public void saveErrorToFile() throws IOException {
        //GIVEN
        JAXBException exception = new JAXBException("Error");
        File file = new File(outputDir, "test.xml");
        file.createNewFile();

        //WHEN
        manager.saveErrorToFile(exception, file);

        //THEN
        File[] files = outputDir.listFiles();
        assertThat(files).isNotEmpty();
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            assertThat(reader.readLine()).isNotNull();
        }
    }

    @Test
    public void createOutputFile() {
        //GIVEN
        String inputFileName = "test_create_file.xml";

        //WHEN
        Optional<File> outputFile = manager.createOutputFile(outputDir, inputFileName);

        //THEN
        assertThat(outputFile.isPresent()).isTrue();
        assertThat(outputFile.get().exists()).isTrue();
    }
}
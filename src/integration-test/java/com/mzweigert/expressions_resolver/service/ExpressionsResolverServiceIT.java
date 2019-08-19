package com.mzweigert.expressions_resolver.service;

import com.mzweigert.expressions_resolver.TestUtilsIT;
import com.mzweigert.expressions_resolver.serialization.SerializationType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ExpressionsResolverServiceIT {

    private ExpressionsResolverService service = new ExpressionsResolverService();

    private File inputDir, outputDir;

    @Before
    public void setUp() {
        inputDir = new File("input_test");
        inputDir.mkdir();
        outputDir = new File("output_test");
        outputDir.mkdir();
    }

    @After
    public void clean() {
        TestUtilsIT.deleteFiles(inputDir);
        TestUtilsIT.deleteFiles(outputDir);
    }

    @Test
    public void resolveSingleXMLFiles() {
        String filePath = getClass().getClassLoader().getResource("").getFile();
        File[] files = Objects.requireNonNull(new File(filePath).listFiles());
        List<File> xmlFiles = Arrays.stream(files)
                .filter(file -> file.getName().endsWith(".xml"))
                .collect(Collectors.toList());
        xmlFiles.forEach(file -> TestUtilsIT.copyFile(inputDir, file, 1));


        service.resolve(inputDir, outputDir, SerializationType.XML);

        File[] outputFiles = outputDir.listFiles();
        assertThat(outputFiles).isNotEmpty();
        assertThat(outputFiles).hasSize(xmlFiles.size());
    }

    @Test
    public void resolveManyXMLFiles() {
        String file = getClass().getClassLoader().getResource("very_complex_expressions.xml").getFile();
        File toCopy = new File(file);
        int filesSize = 2000;
        TestUtilsIT.copyFile(inputDir, toCopy, filesSize);

        service.resolve(inputDir, outputDir, SerializationType.XML);

        File[] outputFiles = outputDir.listFiles();
        assertThat(outputFiles).isNotEmpty();
        assertThat(outputFiles).hasSize(filesSize);
    }
}
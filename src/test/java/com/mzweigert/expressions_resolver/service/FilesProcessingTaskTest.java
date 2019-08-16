package com.mzweigert.expressions_resolver.service;

import com.mzweigert.expressions_resolver.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Collection;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class FilesProcessingTaskTest {

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
        TestUtils.deleteFile(inputDir);
        TestUtils.deleteFile(outputDir);
    }

    @Test
    public void givenFilesInInput_whenCall_thenSuccessCreateOutputFiles() {
        //GIVEN
        int filesSize = 5;
        Collection<File> files = TestUtils.createFiles(inputDir, filesSize);

        //WHEN
        new FilesProcessingTask(files, outputDir).call();

        //THEN
        assertThat(outputDir.listFiles()).isNotNull();
        assertThat(Objects.requireNonNull(outputDir.listFiles()).length).isEqualTo(filesSize);
    }
}
package com.mzweigert.expressions_resolver.resolver;

import com.mzweigert.expressions_resolver.TestUtilsIT;
import com.mzweigert.expressions_resolver.serialization.SerializationType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ExpressionsResolverServiceIT {

    private ExpressionsResolverService service = new ExpressionsResolverService();

    private File inputDir, outputDir;

    @Before
    public void setUp() {
        inputDir = new File("input_test_1");
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


        Optional<FilesProcessingTaskResult> result = service.resolve(inputDir, outputDir, SerializationType.XML);

        File[] outputFiles = outputDir.listFiles();
        assertThat(outputFiles).isNotEmpty();
        assertThat(outputFiles).hasSize(xmlFiles.size());
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getSuccessProcessedFilesSize()).isEqualTo(outputFiles.length - 1);
        assertThat(result.get().getFailProcessedFilesSize()).isEqualTo(1);
    }

    @Test
    public void resolveManyXMLFiles() {
        String file = getClass().getClassLoader().getResource("very_complex_expressions.xml").getFile();
        File toCopy = new File(file);
        int filesSize = 2000;
        TestUtilsIT.copyFile(inputDir, toCopy, filesSize);

        Optional<FilesProcessingTaskResult> result = service.resolve(inputDir, outputDir, SerializationType.XML);

        File[] outputFiles = outputDir.listFiles();
        assertThat(outputFiles).isNotEmpty();
        assertThat(outputFiles).hasSize(filesSize);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getSuccessProcessedFilesSize()).isEqualTo(outputFiles.length);
    }
}
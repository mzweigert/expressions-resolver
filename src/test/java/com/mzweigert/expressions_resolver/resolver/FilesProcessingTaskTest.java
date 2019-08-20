package com.mzweigert.expressions_resolver.resolver;

import com.mzweigert.expressions_resolver.TestUtils;
import com.mzweigert.expressions_resolver.serialization.ExpressionsSerializationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.Collection;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FilesProcessingTaskTest {

    @Mock
    private ExpressionsSerializationService serializationService;

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
    public void givenFilesInInput_whenRun_thenSuccessCreateOutputFiles() throws Exception {
        //GIVEN
        int filesSize = 5;
        Collection<File> files = TestUtils.createFiles(inputDir, filesSize);

        //WHEN
        new FilesProcessingTask(files, outputDir, serializationService).call();

        //THEN
        assertThat(outputDir.listFiles()).isNotNull();
        assertThat(Objects.requireNonNull(outputDir.listFiles()).length).isEqualTo(filesSize);
        verify(serializationService, times(5)).unmarshall(any());
        verify(serializationService, times(5)).marshall(any(), any());
    }
}
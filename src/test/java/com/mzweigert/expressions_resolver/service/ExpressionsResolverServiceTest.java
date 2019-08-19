package com.mzweigert.expressions_resolver.service;

import com.mzweigert.expressions_resolver.TestUtils;
import com.mzweigert.expressions_resolver.configuration.Configuration;
import com.mzweigert.expressions_resolver.serialization.SerializationType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.concurrent.ExecutorService;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExpressionsResolverServiceTest {

    @Mock
    private ExecutorService executorService;

    @InjectMocks
    private ExpressionsResolverService service = new ExpressionsResolverService();

    private File inputDir, outputDir;

    @Before
    public void setUp() {
        inputDir = new File("input_test");
        inputDir.mkdir();
        outputDir = new File("output_test");
        outputDir.mkdir();
        Whitebox.setInternalState(service, "executorService", executorService);
    }

    @After
    public void clean() {
        TestUtils.deleteFile(inputDir);
        TestUtils.deleteFile(outputDir);
    }

    @Test
    public void givenEmptyFolder_whenProcess_thenNoAction() {
        //WHEN
        service.resolve(inputDir, outputDir, SerializationType.XML);

        //THEN
        verifyZeroInteractions(executorService);
    }

    @Test
    public void givenFolderWithFiles_whenProcess_thenSuccessInvokeActions() {
        //GIVEN
        int nFiles = 100;
        int filesPerThread = Integer.valueOf(Configuration.getProperty("filesPerThread"));
        TestUtils.createFiles(inputDir, nFiles);

        //WHEN

        service.resolve(inputDir, outputDir, SerializationType.XML);

        //THEN
        verify(executorService, times(nFiles / filesPerThread)).submit(any(Runnable.class));
        verify(executorService).shutdown();
    }
}
package com.mzweigert.expressions_resolver.service;

import com.mzweigert.expressions_resolver.TestUtils;
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
        service.resolve(inputDir, outputDir);

        //THEN
        verifyZeroInteractions(executorService);
    }

    @Test
    public void givenFolderWithFiles_whenProcess_thenSuccessInvokeActions() throws InterruptedException {
        //GIVEN
        TestUtils.createFiles(inputDir, 100);

        //WHEN
        service.resolve(inputDir, outputDir);

        //THEN
        verify(executorService).invokeAll(anyList());
    }
}
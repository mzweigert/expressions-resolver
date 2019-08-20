package com.mzweigert.expressions_resolver;

import com.mzweigert.expressions_resolver.resolver.ExpressionsResolverService;
import com.mzweigert.expressions_resolver.resolver.FilesProcessingTaskResult;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RunnerTest {

    @Mock
    private ExpressionsResolverService expressionsResolverService;

    @InjectMocks
    private Runner runner;

    private String inputDir, outputDir;

    @Before
    public void setUp() {
        inputDir = "input_test";
        outputDir = "output_test";
    }

    @After
    public void clean() {
        TestUtils.deleteFile(new File(inputDir));
        TestUtils.deleteFile(new File(outputDir));
    }

    @Test
    public void givenNotCreatedFolders_whenRun_thenNotResolving() {
        FilesProcessingTaskResult result = new FilesProcessingTaskResult(Lists.newArrayList(), Lists.newArrayList());

        //WHEN
        when(expressionsResolverService.resolve(any(), any(), any())).thenReturn(Optional.of(result));
        runner.run(inputDir, outputDir);

        //THEN
        verifyZeroInteractions(expressionsResolverService);
    }

    @Test
    public void givenCreatedOutputFolder_whenRun_thenNotResolving() {
        FilesProcessingTaskResult result = new FilesProcessingTaskResult(Lists.newArrayList(), Lists.newArrayList());

        //WHEN
        when(expressionsResolverService.resolve(any(), any(), any())).thenReturn(Optional.of(result));
        runner.run(inputDir, outputDir);

        //THEN
        verifyZeroInteractions(expressionsResolverService);
    }


    @Test
    public void givenCreatedInputFolder_whenRun_thenSuccessCallResolving() {
        new File(inputDir).mkdir();
        FilesProcessingTaskResult result = new FilesProcessingTaskResult(Lists.newArrayList(), Lists.newArrayList());

        //WHEN
        when(expressionsResolverService.resolve(any(), any(), any())).thenReturn(Optional.of(result));
        runner.run(inputDir, outputDir);

        //THEN
        verify(expressionsResolverService).resolve(any(), any(), any());
    }

    @Test
    public void givenCreatedFolders_whenRun_thenSuccessCallResolving() {
        new File(inputDir).mkdir();
        new File(outputDir).mkdir();
        FilesProcessingTaskResult result = new FilesProcessingTaskResult(Lists.newArrayList(), Lists.newArrayList());

        //WHEN
        when(expressionsResolverService.resolve(any(), any(), any())).thenReturn(Optional.of(result));
        runner.run(inputDir, outputDir);

        //THEN
        verify(expressionsResolverService).resolve(any(), any(), any());
    }
}
package com.mzweigert.expressions_resolver.serialization;

import com.mzweigert.expressions_resolver.TestUtilsIT;
import com.mzweigert.expressions_resolver.serialization.xml.XMLExpressionsSerializationService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ExpressionsXMLMarshallingSerializationIT {

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
    public void givenSimpleResults_whenMarshall_thenSuccessSerializeToXml() throws Exception {
        //GIVEN
        Map<Long, String> results = new HashMap<>();
        results.put(1L, "2");
        File outputFile = new File(outputDir, "result.xml");
        outputFile.createNewFile();

        //WHEN
        new XMLExpressionsSerializationService().marshall(results, outputFile);

        //THEN
        File[] files = outputDir.listFiles();
        assertThat(files).isNotEmpty();
        assertThat(files).hasSize(1);
    }

    @Test(expected = ExpressionMarshallException.class)
    public void givenEmptyResults_whenMarshall_thenThrowException() throws Exception {
        //GIVEN
        Map<Long, String> results = new HashMap<>();
        File outputFile = new File(outputDir, "result.xml");
        outputFile.createNewFile();

        //WHEN
        new XMLExpressionsSerializationService().marshall(results, outputFile);
    }
}
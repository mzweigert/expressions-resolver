package com.mzweigert.expressions_resolver;

import java.io.File;

public class TestUtilsIT {

    public static File loadFileFromResource(String fileName) {
        ClassLoader classLoader = TestUtilsIT.class.getClassLoader();
        return new File(classLoader.getResource(fileName).getFile());
    }
}

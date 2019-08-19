package com.mzweigert.expressions_resolver;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;


public class TestUtilsIT {


    public static void deleteFiles(File dir) {
        Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .parallel()
                .forEach(File::delete);
        dir.delete();
    }

    public static File loadFileFromResource(String fileName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(fileName);
        return new File(resource.getFile());
    }
}

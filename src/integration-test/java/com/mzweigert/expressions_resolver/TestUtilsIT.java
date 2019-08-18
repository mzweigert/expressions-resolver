package com.mzweigert.expressions_resolver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.IntStream;

import static com.mzweigert.expressions_resolver.TestUtils.deleteFile;

public class TestUtilsIT {

    public static void copyFile(File dir, File toCopy, int nFiles) {
        IntStream.range(0, nFiles)
                .parallel()
                .forEach(i -> createFile(dir, toCopy,  i));
    }

    private static void createFile(File dir, File toCopy, int i) {
        try {
            File file = new File(dir, toCopy.getName() + "_" + i + ".xml");
            file.createNewFile();
            OutputStream os = new FileOutputStream(file);
            Files.copy(Paths.get(toCopy.getPath()), os);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFiles(File dir) {
        Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .parallel()
                .forEach(File::delete);
        dir.delete();
    }

    public static File loadFileFromResource(String fileName) {
        ClassLoader classLoader = TestUtilsIT.class.getClassLoader();
        return new File(classLoader.getResource(fileName).getFile());
    }
}

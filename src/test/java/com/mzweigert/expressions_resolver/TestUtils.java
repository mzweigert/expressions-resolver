package com.mzweigert.expressions_resolver;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class TestUtils {

    public static Collection<File> createFiles(File inputDir, int nFiles) {
        Collection<File> files = new HashSet<>();
        for (int i = 0; i < nFiles; i++) {
            try {
                File file = new File(inputDir, "file_" + i + ".xml");
                file.createNewFile();
                files.add(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return files;
    }

    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            for (File c : Objects.requireNonNull(file.listFiles()))
                deleteFile(c);
        }
        if (!file.delete()) {
            System.out.println("Failed to deleteFile file: " + file);
        }
    }
}

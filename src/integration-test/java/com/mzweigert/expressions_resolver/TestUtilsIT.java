package com.mzweigert.expressions_resolver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;


public class TestUtilsIT {

    public static void copyFile(File dir, File toCopy, int nFiles) {
        CountDownLatch latch = new CountDownLatch(nFiles);
        IntStream.range(0, nFiles)
                .parallel()
                .forEach(i -> {
                    createFile(dir, toCopy, i);
                    latch.countDown();

                });
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static File createFile(File dir, File toCopy, int i) {
        File file = null;
        try {
            String name = toCopy.getName();
            int dotIndex = name.lastIndexOf('.');
            String outputFileName = name.substring(0, dotIndex) + "_" + i + name.substring(dotIndex);
            file = new File(dir, outputFileName);
            OutputStream os = new FileOutputStream(file);
            Files.copy(Paths.get(toCopy.getPath()), os);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    public static void deleteFiles(File dir) {
        File[] array = Objects.requireNonNull(dir.listFiles());
        CountDownLatch latch = new CountDownLatch(array.length);
        Arrays.stream(array)
                .parallel()
                .forEach(file -> {
                    while (!file.delete()){
                        System.out.println("cannot delete file " + file.getName());
                    }
                    latch.countDown();
                });
        try {
            latch.await();
            dir.delete();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static File loadFileFromResource(String fileName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(fileName);
        return new File(resource.getFile());
    }
}

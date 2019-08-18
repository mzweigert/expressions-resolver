package com.mzweigert.expressions_resolver;

import com.mzweigert.expressions_resolver.serialization.model.input.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    public static Expression generateSimple(OperationType type, BigDecimal... values) throws IllegalAccessException {
        NestedExpression[] nestedExpressions = Arrays.stream(values)
                .map(NestedExpression::new)
                .toArray(NestedExpression[]::new);
        return generateComplex(type, nestedExpressions);
    }

    public static Expression generateComplex(OperationType type, Expression... nested) throws IllegalAccessException {
        switch (type) {
            case ADDITION:
                Addition addition = new Addition();
                List<NestedExpression> items = Arrays.stream(nested)
                        .map(NestedExpression::new)
                        .collect(Collectors.toList());
                addition.setItems(items);
                return addition;
            case SUBTRACTION:
                if(nested.length < 2) {
                    throw new IllegalAccessException("Need 2 values to create subtraction!");
                }
                Subtraction subtraction = new Subtraction();
                subtraction.setMinuend(new NestedExpression(nested[0]));
                subtraction.setSubtrahend(new NestedExpression(nested[1]));
                return subtraction;
            case DIVISION:
                if(nested.length < 2) {
                    throw new IllegalAccessException("Need 2 values to create division!");
                }
                Division division = new Division();
                division.setDividend(new NestedExpression(nested[0]));
                division.setDivisor(new NestedExpression(nested[1]));
                return division;
            case MULTIPLICATION:
                Multiplication multiplication = new Multiplication();
                List<NestedExpression> factors = Arrays.stream(nested)
                        .map(NestedExpression::new)
                        .collect(Collectors.toList());
                multiplication.setFactors(factors);
                return multiplication;
        }

        throw new NotImplementedException();
    }

}

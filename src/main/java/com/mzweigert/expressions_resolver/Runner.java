package com.mzweigert.expressions_resolver;

import com.mzweigert.expressions_resolver.serialization.SerializationType;
import com.mzweigert.expressions_resolver.resolver.ExpressionsResolverService;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Runner {

    private static final Logger logger = Logger.getAnonymousLogger();

    private ExpressionsResolverService expressionsResolverService;

    public Runner(ExpressionsResolverService expressionsResolverService) {
        this.expressionsResolverService = expressionsResolverService;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            logger.log(Level.SEVERE, "No input and output folders given in args!");
        } else {
            ExpressionsResolverService fileIOService = new ExpressionsResolverService();
            new Runner(fileIOService).run(args[0], args[1]);
        }
    }

    void run(String inputFolder, String outputFolder) {
        File inputDir = new File(inputFolder);
        boolean inputDirValid = checkDirectory(inputDir, false);

        File outputDir = new File(outputFolder);
        boolean outputDirValid = checkDirectory(outputDir, true);

        SerializationType type = getSerializationType();

        if (inputDirValid && outputDirValid) {
            expressionsResolverService.resolve(inputDir, outputDir, type);
        }
    }

    private SerializationType getSerializationType() {
        return SerializationType.XML;
    }

    private boolean checkDirectory(File dir, boolean createIfNotExists) {
        if (!dir.exists() && createIfNotExists) {
            dir.mkdir();
        } else if (!dir.isDirectory()) {
            logger.warning(dir.getName() + " is not a folder!");
            return false;
        }
        return true;
    }
}
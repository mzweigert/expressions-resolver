package com.mzweigert.expressions_resolver;

import com.mzweigert.expressions_resolver.service.ExpressionsResolverService;

import java.io.File;

public class Runner {

    private ExpressionsResolverService expressionsResolverService;

    public Runner(ExpressionsResolverService expressionsResolverService) {
        this.expressionsResolverService = expressionsResolverService;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("No input and output folders given in args!");
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

        if (inputDirValid && outputDirValid) {
            expressionsResolverService.resolve(inputDir, outputDir);
        }
    }

    private boolean checkDirectory(File dir, boolean createIfNotExists) {
        if (!dir.exists() && createIfNotExists) {
            dir.mkdir();
        } else if (!dir.isDirectory()) {
            System.out.println(dir.getName() + " is not a folder!");
            return false;
        }
        return true;
    }
}
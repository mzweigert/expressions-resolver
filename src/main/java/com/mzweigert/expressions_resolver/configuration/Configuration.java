package com.mzweigert.expressions_resolver.configuration;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class Configuration {

    private static Configuration instance;

    private static final int DEFAULT_WORKERS = 4;
    private static final int DEFAULT_FIlES_PER_THREAD = 5;


    private static Configuration getInstance() {
        if (instance == null) {
            synchronized (Configuration.class) {
                if (instance == null) {
                    instance = new Configuration();
                }
            }
        }
        return instance;
    }

    /**
     * Finds value configuration for property name
     */
    public static String getProperty(String name) {
        return getInstance().properties
                .getOrDefault(name, getDefault(name));
    }

    private static String getDefault(String name) {
        switch (name) {
            case "workers":
                return String.valueOf(DEFAULT_WORKERS);
            case "filesPerThread":
                return String.valueOf(DEFAULT_FIlES_PER_THREAD);

        }
        throw new ConfigurationNotFoundException("Cannot found property with name : " + name);
    }

    private Map<String, String> properties;

    private Configuration() {
        try {
            Properties properties = new Properties();
            properties.load(getClass()
                    .getClassLoader()
                    .getResourceAsStream("config.properties")
            );
            this.properties = properties
                    .stringPropertyNames()
                    .stream()
                    .collect(Collectors.toMap(
                            name -> name,
                            properties::getProperty
                    ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

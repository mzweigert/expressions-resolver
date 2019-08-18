package com.mzweigert.expressions_resolver.configuration;

public class ConfigurationNotFoundException extends RuntimeException {
    public ConfigurationNotFoundException(String reason) {
        super(reason);
    }
}

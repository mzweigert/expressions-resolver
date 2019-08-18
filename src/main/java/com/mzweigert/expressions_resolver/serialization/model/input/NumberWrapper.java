package com.mzweigert.expressions_resolver.serialization.model.input;

/**
 * Wrapper for simple number value.
 */
public class NumberWrapper extends Expression {

    private Double number;

    public Double getNumber() {
        return number;
    }

    public NumberWrapper(Double number) {
        this.number = number;
    }

}

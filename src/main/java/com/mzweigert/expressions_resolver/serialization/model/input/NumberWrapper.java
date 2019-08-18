package com.mzweigert.expressions_resolver.serialization.model.input;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Wrapper for simple number value.
 */
public class NumberWrapper extends Expression {

    private BigDecimal number;

    public BigDecimal getNumber() {
        return number;
    }

    public NumberWrapper(BigDecimal number) {
        this.number = number;
    }

    @Override
    public Optional<BigDecimal> calculate() {
        return Optional.ofNullable(number);
    }
}

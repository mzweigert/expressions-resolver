package com.mzweigert.expressions_resolver.serialization.model.input;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@XmlRootElement(name = "multiplication")
public class Multiplication extends Expression {

    private List<NestedExpression> factors;

    @XmlElement(name = "factor", type = NestedExpression.class)
    public List<NestedExpression> getFactors() {
        return factors;
    }

    public void setFactors(List<NestedExpression> factors) {
        this.factors = factors;
    }

    @Override
    public Optional<BigDecimal> calculate() {
        if (factors == null) {
            return Optional.ofNullable(BigDecimal.ONE);
        }
        return Optional.ofNullable(factors.stream()
                .map(NestedExpression::calculate)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(BigDecimal.ONE, BigDecimal::multiply));
    }
}

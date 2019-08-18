package com.mzweigert.expressions_resolver.serialization.model.input;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@XmlRootElement(name = "division")
public class Division extends Expression {

    private NestedExpression dividend;

    private NestedExpression divisor;

    @XmlElement(name = "dividend", type = NestedExpression.class)
    public NestedExpression getDividend() {
        return dividend;
    }

    public void setDividend(NestedExpression dividend) {
        this.dividend = dividend;
    }

    @XmlElement(name = "divisor", type = NestedExpression.class)
    public NestedExpression getDivisor() {
        return divisor;
    }

    public void setDivisor(NestedExpression divisor) {
        this.divisor = divisor;
    }

    @Override
    public Optional<BigDecimal> calculate() {
        if (dividend == null || divisor == null) {
            return Optional.empty();
        }
        Optional<BigDecimal> dividendCalculated = dividend.calculate();
        Optional<BigDecimal> divisorCalculated = divisor.calculate();
        if (!dividendCalculated.isPresent() || !divisorCalculated.isPresent()) {
            return Optional.empty();
        }

        BigDecimal result = dividendCalculated.get().divide(divisorCalculated.get(), 2, RoundingMode.HALF_DOWN);
        return Optional.ofNullable(result);
    }

}

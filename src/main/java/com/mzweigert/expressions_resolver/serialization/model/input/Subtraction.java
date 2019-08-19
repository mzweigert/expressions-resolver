package com.mzweigert.expressions_resolver.serialization.model.input;

import com.mzweigert.expressions_resolver.serialization.model.Expression;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Optional;

@XmlRootElement(name = "subtraction")
public class Subtraction extends Expression {

    private NestedExpression minuend;

    private NestedExpression subtrahend;

    @XmlElement(name = "minuend", type = NestedExpression.class)
    public NestedExpression getMinuend() {
        return minuend;
    }

    public void setMinuend(NestedExpression minuend) {
        this.minuend = minuend;
    }

    @XmlElement(name = "subtrahend", type = NestedExpression.class)
    public NestedExpression getSubtrahend() {
        return subtrahend;
    }

    public void setSubtrahend(NestedExpression subtrahend) {
        this.subtrahend = subtrahend;
    }

    @Override
    public Optional<BigDecimal> calculate() {
        if (minuend == null || subtrahend == null) {
            return Optional.empty();
        }
        Optional<BigDecimal> minuendCalculated = minuend.calculate();
        Optional<BigDecimal> subtrahendCalculated = subtrahend.calculate();
        if (!minuendCalculated.isPresent() || !subtrahendCalculated.isPresent()) {
            return Optional.empty();
        }

        BigDecimal result = minuendCalculated.get().subtract(subtrahendCalculated.get());
        return Optional.ofNullable(result);
    }
}
package com.mzweigert.expressions_resolver.serialization.model.input;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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

}

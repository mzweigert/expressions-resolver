package com.mzweigert.expressions_resolver.serialization.model.input;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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

}
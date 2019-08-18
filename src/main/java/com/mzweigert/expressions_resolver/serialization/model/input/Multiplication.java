package com.mzweigert.expressions_resolver.serialization.model.input;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

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


}

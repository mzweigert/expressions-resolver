package com.mzweigert.expressions_resolver.serialization.model.input;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "addition")
public class Addition extends Expression {

    private List<NestedExpression> items;

    @XmlElement(name = "item", type = NestedExpression.class)
    public List<NestedExpression> getItems() {
        return items;
    }

    public void setItems(List<NestedExpression> nestedExpressions) {
        this.items = nestedExpressions;
    }

}

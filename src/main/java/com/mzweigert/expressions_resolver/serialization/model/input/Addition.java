package com.mzweigert.expressions_resolver.serialization.model.input;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<BigDecimal> calculate() {
        if (items == null) {
            return Optional.ofNullable(BigDecimal.ZERO);
        }
        BigDecimal result = items.stream()
                .map(NestedExpression::calculate)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return Optional.of(result);
    }
}

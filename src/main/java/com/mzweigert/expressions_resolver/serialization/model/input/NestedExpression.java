package com.mzweigert.expressions_resolver.serialization.model.input;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@XmlRootElement
public class NestedExpression extends Expression {

    @XmlMixed
    @XmlElementRefs({
            @XmlElementRef(name = "addition", type = Addition.class),
            @XmlElementRef(name = "subtraction", type = Subtraction.class),
            @XmlElementRef(name = "multiplication", type = Multiplication.class),
            @XmlElementRef(name = "division ", type = Division.class)
    })
    private List<Object> value = new ArrayList<>();

    public Optional<Expression> extract() {
        if (value.size() <= 0) {
            return Optional.empty();
        }
        Object o = value.get(0);
        if (o instanceof String) {
            NumberWrapper value = new NumberWrapper(Double.valueOf(o.toString()));
            return Optional.of(value);
        } else if (Expression.class.isAssignableFrom(o.getClass())) {
            return Optional.of((Expression) o);
        }
        return Optional.empty();
    }

}
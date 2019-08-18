package com.mzweigert.expressions_resolver.serialization.model.input;

import com.mzweigert.expressions_resolver.serialization.model.Expression;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@XmlRootElement
@XmlAccessorType
public class NestedExpression extends Expression {

    @XmlMixed
    @XmlElementRefs({
            @XmlElementRef(name = "addition", type = Addition.class),
            @XmlElementRef(name = "subtraction", type = Subtraction.class),
            @XmlElementRef(name = "multiplication", type = Multiplication.class),
            @XmlElementRef(name = "division ", type = Division.class)
    })
    private List<Object> value = new ArrayList<>();

    public NestedExpression() {
    }

    public NestedExpression(Number simple) {
        this.value = new ArrayList<>();
        this.value.add(simple);
    }

    public NestedExpression(Expression nested) {
        this.value = new ArrayList<>();
        this.value.add(nested);
    }

    public Optional<Expression> extract() {
        if (value.size() <= 0) {
            return Optional.empty();
        }
        Object o = value.get(0);
        if (Expression.class.isAssignableFrom(o.getClass())) {
            return Optional.of((Expression) o);
        }
        BigDecimal number = null;
        if (o instanceof String) {
            number = new BigDecimal(o.toString());
        } else if (o instanceof BigDecimal) {
            number = (BigDecimal) o;
        }

        return number != null ?
                Optional.of(new NumberWrapper(number)) :
                Optional.empty();
    }

    @Override
    public Optional<BigDecimal> calculate() {
        return extract().flatMap(Expression::calculate);
    }
}
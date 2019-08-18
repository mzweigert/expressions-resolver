package com.mzweigert.expressions_resolver.serialization.model.output;

import com.mzweigert.expressions_resolver.serialization.model.Expression;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.math.BigDecimal;
import java.util.Optional;

@XmlRootElement(name = "result")
public class Result extends Expression {

    private String value;

    public Result() { }

    public Result(Long id, String value) {
        this.setId(id);
        this.value = value;
    }

    @XmlValue
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Optional<BigDecimal> calculate() {
        try {
            BigDecimal value = new BigDecimal(this.value).stripTrailingZeros();
            return Optional.of(value);
        } catch (Exception e) {
            System.out.println("Result is not a number!");
            return Optional.empty();
        }
    }
}

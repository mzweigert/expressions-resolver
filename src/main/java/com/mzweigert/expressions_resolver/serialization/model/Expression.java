package com.mzweigert.expressions_resolver.serialization.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;

@XmlTransient
public abstract class Expression implements Serializable {

    private Long id;

    @XmlAttribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String calculateAsString() {
        try {
            Optional<BigDecimal> result = this.calculate();
            return result.isPresent() ? result.get().toString() : "Result is empty";
        } catch (Exception e) {
            e.printStackTrace();
            return "Cannot calculate result. An error has occurred : " + e.getMessage();
        }
    }

    public abstract Optional<BigDecimal> calculate();
}

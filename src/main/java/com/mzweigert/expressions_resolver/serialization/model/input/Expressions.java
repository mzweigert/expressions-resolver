package com.mzweigert.expressions_resolver.serialization.model.input;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Expressions {

    private List<Expression> expressions;

    @XmlAnyElement(lax = true)
    public List<Expression> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<Expression> expressions) {
        this.expressions = expressions;
    }

}

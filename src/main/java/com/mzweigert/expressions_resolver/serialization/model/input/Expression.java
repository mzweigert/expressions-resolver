package com.mzweigert.expressions_resolver.serialization.model.input;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

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

}

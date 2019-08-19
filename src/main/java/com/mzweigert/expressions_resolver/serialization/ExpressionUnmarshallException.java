package com.mzweigert.expressions_resolver.serialization;

import javax.xml.bind.JAXBException;

public class ExpressionUnmarshallException extends JAXBException {

    public ExpressionUnmarshallException(Exception e) {
        super(e);
    }
}

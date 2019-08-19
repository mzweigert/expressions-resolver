package com.mzweigert.expressions_resolver.serialization;

import javax.xml.bind.JAXBException;

public class ExpressionMarshallException extends JAXBException {
    public ExpressionMarshallException(Exception e) {
        super(e);
    }
}

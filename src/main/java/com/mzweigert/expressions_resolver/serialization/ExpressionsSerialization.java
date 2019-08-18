package com.mzweigert.expressions_resolver.serialization;

import com.mzweigert.expressions_resolver.serialization.model.input.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExpressionsSerialization {

    private JAXBContext jaxbContext;

    public ExpressionsSerialization() {
        try {
            jaxbContext = JAXBContext.newInstance(
                    Expressions.class, Expression.class, NestedExpression.class,
                    Addition.class, Multiplication.class, Subtraction.class, Division.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public List<Expression> unmarshall(File file) {
        try {
            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(file));
            xsr = xif.createFilteredReader(xsr, reader -> {
                if(reader.getEventType() == XMLStreamReader.CHARACTERS) {
                    return reader.getText().trim().length() > 0;
                }
                return true;
            });
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            Expressions result = (Expressions) unmarshaller.unmarshal(xsr);
            return result.getExpressions();
        } catch (JAXBException | XMLStreamException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

}

package com.mzweigert.expressions_resolver.serialization;

import com.mzweigert.expressions_resolver.serialization.model.input.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpressionsSerialization {

    private Unmarshaller unmarshaller;

    public ExpressionsSerialization() {
        try {
            unmarshaller = JAXBContext.newInstance(
                    Expressions.class, Expression.class, NestedExpression.class,
                    Addition.class, Multiplication.class, Subtraction.class, Division.class)
                    .createUnmarshaller();
            tryFindSchema().ifPresent(schema -> unmarshaller.setSchema(schema));
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
        }
    }

    private Optional<Schema> tryFindSchema() throws SAXException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL resource = getClass().getClassLoader().getResource("expressions.xsd");
        if(resource == null){
            return Optional.empty();
        }
        return Optional.ofNullable(sf.newSchema(new File(resource.getFile())));
    }

    public List<Expression> unmarshall(File file) {
        try {
            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(file));
            xsr = xif.createFilteredReader(xsr, this::filterStream);
            Expressions result = (Expressions) unmarshaller.unmarshal(xsr);
            return result.getExpressions();
        } catch (JAXBException | XMLStreamException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private boolean filterStream(XMLStreamReader reader) {
        if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
            return reader.getText().trim().length() > 0;
        }
        return true;
    }

}

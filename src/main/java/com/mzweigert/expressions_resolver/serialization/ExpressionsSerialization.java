package com.mzweigert.expressions_resolver.serialization;

import com.mzweigert.expressions_resolver.serialization.model.Expression;
import com.mzweigert.expressions_resolver.serialization.model.Expressions;
import com.mzweigert.expressions_resolver.serialization.model.input.*;
import com.mzweigert.expressions_resolver.serialization.model.output.Result;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExpressionsSerialization {

    private Unmarshaller unmarshaller;
    private Marshaller marshaller;

    public ExpressionsSerialization() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(
                    Expressions.class, Expression.class, NestedExpression.class, Result.class,
                    Addition.class, Multiplication.class, Subtraction.class, Division.class);
            unmarshaller = jaxbContext.createUnmarshaller();
            tryFindSchema("expressions.xsd").ifPresent(schema -> unmarshaller.setSchema(schema));

            marshaller = jaxbContext.createMarshaller();
            tryFindSchema("expressions_results.xsd").ifPresent(schema -> marshaller.setSchema(schema));
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
        }
    }

    private Optional<Schema> tryFindSchema(String schemaFileName) throws SAXException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL resource = getClass().getClassLoader().getResource(schemaFileName);
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

    public void marshall(Map<Long, String> mapResults, File outputFile) {
        List<Expression> results = mapResults.entrySet()
                .stream()
                .map(entry -> new Result(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        Expressions expressions = new Expressions();
        expressions.setExpressions(results);
        try {
            marshaller.marshal(expressions, outputFile);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}

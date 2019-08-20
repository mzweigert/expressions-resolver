package com.mzweigert.expressions_resolver.serialization.xml;

import com.mzweigert.expressions_resolver.serialization.ExpressionMarshallException;
import com.mzweigert.expressions_resolver.serialization.ExpressionUnmarshallException;
import com.mzweigert.expressions_resolver.serialization.ExpressionsSerializationService;
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
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class XMLExpressionsSerializationService implements ExpressionsSerializationService {

    private static final Logger logger = Logger.getAnonymousLogger();

    private static final Class[] CONTEXT_CLASSES = {
            Expressions.class, Expression.class, NestedExpression.class, Result.class,
            Addition.class, Multiplication.class, Subtraction.class, Division.class};

    private ThreadLocal<Unmarshaller> unmarshaller;
    private ThreadLocal<Marshaller> marshaller;

    public XMLExpressionsSerializationService() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CONTEXT_CLASSES);
            this.unmarshaller = initUnmarshaller(jaxbContext);
            this.marshaller = initMarshaller(jaxbContext);
        } catch (SAXException | JAXBException e) {
            logger.log(Level.SEVERE, e.toString());
        }
    }

    private ThreadLocal<Marshaller> initMarshaller(JAXBContext jaxbContext) throws SAXException {
        Optional<Schema> outputSchema = tryFindSchema("expressions_results.xsd");
        return ThreadLocal.withInitial(() -> safe(() -> {
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            outputSchema.ifPresent(marshaller::setSchema);
            return marshaller;
        }));
    }

    private ThreadLocal<Unmarshaller> initUnmarshaller(JAXBContext jaxbContext) throws SAXException {
        Optional<Schema> inputSchema = tryFindSchema("expressions.xsd");
        return ThreadLocal.withInitial(() -> safe(() -> {
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            inputSchema.ifPresent(unmarshaller::setSchema);
            return unmarshaller;
        }));
    }

    private static <T> T safe(Callable<T> fn) {
        try {
            return fn.call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<Schema> tryFindSchema(String schemaFileName) throws SAXException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        URL resource = getClass().getClassLoader().getResource(schemaFileName);
        if (resource == null) {
            logger.warning("Schema " + schemaFileName + " not found!");
            return Optional.empty();
        }
        File schema = new File(resource.getFile());
        if (!schema.exists()) {
            sf.newSchema(schema);
            logger.warning("Schema " + schema.getName() + " not found!");
            return Optional.empty();
        }
        return Optional.of(sf.newSchema(schema));
    }

    @Override
    public List<Expression> unmarshall(File file) throws ExpressionUnmarshallException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            XMLInputFactory xif = XMLInputFactory.newFactory();
            XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(inputStream));
            xsr = xif.createFilteredReader(xsr, this::filterStream);
            Expressions result = (Expressions) unmarshaller.get().unmarshal(xsr);
            return result.getExpressions();
        } catch (JAXBException | XMLStreamException | IOException e) {
            throw new ExpressionUnmarshallException(e);
        }
    }

    private boolean filterStream(XMLStreamReader reader) {
        if (reader.getEventType() == XMLStreamReader.CHARACTERS) {
            return reader.getText().trim().length() > 0;
        }
        return true;
    }

    @Override
    public void marshall(Map<Long, String> mapResults, File outputFile) throws ExpressionMarshallException {
        List<Expression> results = mapResults.entrySet()
                .stream()
                .map(entry -> new Result(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        Expressions expressions = new Expressions();
        expressions.setExpressions(results);
        try (FileOutputStream inputStream = new FileOutputStream(outputFile)) {
            marshaller.get().marshal(expressions, inputStream);
        } catch (JAXBException | IOException e) {
            throw new ExpressionMarshallException(e);
        }
    }

}

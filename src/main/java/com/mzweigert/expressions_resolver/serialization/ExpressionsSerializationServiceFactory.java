package com.mzweigert.expressions_resolver.serialization;

import com.mzweigert.expressions_resolver.serialization.xml.XMLExpressionsSerializationService;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ExpressionsSerializationServiceFactory {

    public static ExpressionsSerializationService getInstance(SerializationType type) {
        switch (type) {
            case XML:
                return new XMLExpressionsSerializationService();
        }
        throw new NotImplementedException();
    }
}

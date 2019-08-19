package com.mzweigert.expressions_resolver.serialization;

import com.mzweigert.expressions_resolver.serialization.model.Expression;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ExpressionsSerializationService {

    List<Expression> unmarshall(File file) throws ExpressionUnmarshallException;

    void marshall(Map<Long, String> mapResults, File outputFile) throws ExpressionMarshallException;
}

package com.mzweigert.expressions_resolver.serialization;

import com.mzweigert.expressions_resolver.TestUtils;
import com.mzweigert.expressions_resolver.serialization.model.input.*;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class SimpleUnmarshallingTest {

    @Test
    public void givenSimpleAddition_whenUnmarshall_thenSuccessUnmarshalling() {
        //GIVEN
        File file = TestUtils.loadFileFromResource("simple_addition.xml");

        //WHEN
        List<Expression> result = new ExpressionsSerialization().unmarshall(file);

        //THEN
        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isInstanceOf(Addition.class);

        List<NestedExpression> items = ((Addition) result.get(0)).getItems();
        assertThat(items).isNotEmpty();
        assertThat(items).hasSize(2);
    }

    @Test
    public void givenSimpleSubtraction_whenUnmarshall_thenSuccessUnmarshalling() {
        //GIVEN
        File file = TestUtils.loadFileFromResource("simple_subtraction.xml");

        //WHEN
        List<Expression> result = new ExpressionsSerialization().unmarshall(file);

        //THEN
        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isInstanceOf(Subtraction.class);

        NestedExpression minuend = ((Subtraction) result.get(0)).getMinuend();
        NestedExpression subtrahend = ((Subtraction) result.get(0)).getSubtrahend();

        assertThat(minuend).isNotNull();
        assertThat(minuend.extract().get()).isInstanceOf(NumberWrapper.class);

        assertThat(subtrahend).isNotNull();
        assertThat(subtrahend.extract().get()).isInstanceOf(NumberWrapper.class);
    }

    @Test
    public void givenSimpleMultiplication_whenUnmarshall_thenSuccessUnmarshalling() {
        //GIVEN
        File file = TestUtils.loadFileFromResource("simple_multiplication.xml");

        //WHEN
        List<Expression> result = new ExpressionsSerialization().unmarshall(file);

        //THEN
        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isInstanceOf(Multiplication.class);

        List<NestedExpression> items = ((Multiplication) result.get(0)).getFactors();
        assertThat(items).isNotEmpty();
        assertThat(items).hasSize(3);
    }


    @Test
    public void givenSimpleDivision_whenUnmarshall_thenSuccessUnmarshalling() {
        //GIVEN
        File file = TestUtils.loadFileFromResource("simple_division.xml");

        //WHEN
        List<Expression> result = new ExpressionsSerialization().unmarshall(file);

        //THEN
        assertThat(result).isNotEmpty();
        assertThat(result.get(0)).isInstanceOf(Division.class);

        NestedExpression dividend = ((Division) result.get(0)).getDividend();
        NestedExpression divisor = ((Division) result.get(0)).getDivisor();

        assertThat(dividend).isNotNull();
        assertThat(dividend.extract().get()).isInstanceOf(NumberWrapper.class);

        assertThat(divisor).isNotNull();
        assertThat(divisor.extract().get()).isInstanceOf(NumberWrapper.class);
    }


    @Test
    public void givenSimpleMixed_whenUnmarshall_thenSuccessUnmarshalling() {
        //GIVEN
        File file = TestUtils.loadFileFromResource("simple_mixed.xml");

        //WHEN
        List<Expression> result = new ExpressionsSerialization().unmarshall(file);

        //THEN
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(4);
        assertThat(result.get(0)).isInstanceOf(Addition.class);
        assertThat(result.get(1)).isInstanceOf(Subtraction.class);
        assertThat(result.get(2)).isInstanceOf(Multiplication.class);
        assertThat(result.get(3)).isInstanceOf(Division.class);

    }
}
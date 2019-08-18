package com.mzweigert.expressions_resolver.serialization;

import com.mzweigert.expressions_resolver.TestUtilsIT;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class CalculateExpressionIT {

    @Test
    public void givenSimpleAddition_whenCalculate_thenSuccessReturnResult() {
        //GIVEN
        File file = TestUtilsIT.loadFileFromResource("simple_addition.xml");
        BigDecimal expected = new BigDecimal("5.32");

        //WHEN
        Optional<BigDecimal> result = new ExpressionsSerialization()
                .unmarshall(file)
                .get(0)
                .calculate();

        //THEN
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().setScale(2, RoundingMode.HALF_DOWN)).isEqualTo(expected);
    }

    @Test
    public void givenSimpleSubtraction_whenCalculate_thenSuccessReturnResult() {
        //GIVEN
        File file = TestUtilsIT.loadFileFromResource("simple_subtraction.xml");
        BigDecimal expected = new BigDecimal("1.11");

        //WHEN
        Optional<BigDecimal> result = new ExpressionsSerialization()
                .unmarshall(file)
                .get(0)
                .calculate();

        //THEN
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().setScale(2, RoundingMode.HALF_DOWN)).isEqualTo(expected);
    }

    @Test
    public void givenSimpleMultiplication_whenCalculate_thenSuccessReturnResult() {
        //GIVEN
        File file = TestUtilsIT.loadFileFromResource("simple_multiplication.xml");
        BigDecimal expected = new BigDecimal("155.57");

        //WHEN
        Optional<BigDecimal> result = new ExpressionsSerialization()
                .unmarshall(file)
                .get(0)
                .calculate();

        //THEN
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().setScale(2, RoundingMode.HALF_DOWN)).isEqualTo(expected);
    }

    @Test
    public void givenSimpleDivision_whenCalculate_thenSuccessReturnResult() {
        //GIVEN
        File file = TestUtilsIT.loadFileFromResource("simple_division.xml");
        BigDecimal expected = new BigDecimal("1.78");

        //WHEN
        Optional<BigDecimal> result = new ExpressionsSerialization()
                .unmarshall(file)
                .get(0)
                .calculate();

        //THEN
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().setScale(2, RoundingMode.HALF_DOWN)).isEqualTo(expected);
    }

    @Test
    public void givenComplexMixedExpressions_whenCalculate_thenSuccessReturnResult() {
        //GIVEN
        File file = TestUtilsIT.loadFileFromResource("complex_mixed_expressions.xml");
        BigDecimal expected = new BigDecimal("276.03");

        //WHEN
        Optional<BigDecimal> result = new ExpressionsSerialization()
                .unmarshall(file)
                .get(0)
                .calculate();

        //THEN
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().setScale(2, RoundingMode.HALF_DOWN)).isEqualTo(expected);
    }
}
package com.mzweigert.expressions_resolver.serialization.model.input;

import com.mzweigert.expressions_resolver.OperationType;
import com.mzweigert.expressions_resolver.serialization.model.Expression;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.mzweigert.expressions_resolver.TestUtils.generateComplex;
import static com.mzweigert.expressions_resolver.TestUtils.generateSimple;
import static org.assertj.core.api.Java6Assertions.assertThat;


public class AdditionTest {

    @Test
    public void calculateSimple() throws IllegalAccessException {

        // 1.123 + 2.232 + 3.355 ~ 6.71
        Expression addition = generateSimple(
                OperationType.ADDITION,
                new BigDecimal("1.123"), new BigDecimal("2.232"), new BigDecimal("3.355"));

        BigDecimal expected = new BigDecimal("6.71");

        Optional<BigDecimal> calculate = addition.calculate();

        assertThat(calculate.isPresent()).isTrue();
        assertThat(calculate.get().setScale(2, RoundingMode.HALF_DOWN)).isEqualTo(expected);
    }

    @Test
    public void calculateComplex() throws IllegalAccessException {
        // 1.2222 + 1.4111 ~ 2.63
        Expression simpleAddition = generateSimple(OperationType.ADDITION,
                new BigDecimal("1.2222"), new BigDecimal("1.4111"));
        // 9.5 - 2.3 = 7.2
        Expression simpleSubtraction = generateSimple(OperationType.SUBTRACTION,
                new BigDecimal("9.5"), new BigDecimal("2.3"));
        // 2.5 * 1.7 = 4.25
        Expression simpleMultiplication = generateSimple(OperationType.MULTIPLICATION,
                new BigDecimal("2.5"),  new BigDecimal("1.7"));
        // 10 / 3 = 3.3(3)
        Expression simpleDivision = generateSimple(OperationType.DIVISION,
                BigDecimal.TEN, new BigDecimal("3"));

        BigDecimal expected = new BigDecimal("17.41");

        Expression complexAddition = generateComplex(
                OperationType.ADDITION,
                simpleAddition, simpleSubtraction,
                simpleMultiplication, simpleDivision
        );

        Optional<BigDecimal> calculate = complexAddition.calculate();

        assertThat(calculate.isPresent()).isTrue();
        assertThat(calculate.get().setScale(2, RoundingMode.HALF_DOWN)).isEqualTo(expected);

    }

}
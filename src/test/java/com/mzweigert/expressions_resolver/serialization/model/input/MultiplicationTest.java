package com.mzweigert.expressions_resolver.serialization.model.input;

import com.mzweigert.expressions_resolver.OperationType;
import com.mzweigert.expressions_resolver.serialization.model.Expression;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static com.mzweigert.expressions_resolver.TestUtils.generateComplex;
import static com.mzweigert.expressions_resolver.TestUtils.generateSimple;
import static org.assertj.core.api.Java6Assertions.assertThat;

public class MultiplicationTest {

    @Test
    public void calculateSimple() throws IllegalAccessException {

        // 1.123 * 2.232 * 3.355 = 8.409428280
        Expression addition = generateSimple(
                OperationType.MULTIPLICATION,
                new BigDecimal("1.123"), new BigDecimal("2.232"), new BigDecimal("3.355"));

        BigDecimal expected = new BigDecimal("8.41");

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
                new BigDecimal("2.5"), new BigDecimal("1.7"));
        // 10 / 3 = 3.3(3)
        Expression simpleDivision = generateSimple(OperationType.DIVISION,
                BigDecimal.TEN, new BigDecimal("3"));

        Expression simpleNumber = new NumberWrapper(new BigDecimal("3.3333"));

        BigDecimal expected = new BigDecimal("894.42");

        Expression complexAddition = generateComplex(
                OperationType.MULTIPLICATION,
                simpleAddition, simpleSubtraction,
                simpleMultiplication, simpleDivision,
                simpleNumber
        );

        Optional<BigDecimal> calculate = complexAddition.calculate();

        assertThat(calculate.isPresent()).isTrue();
        assertThat(calculate.get().setScale(2, RoundingMode.HALF_DOWN)).isEqualTo(expected);
    }
}
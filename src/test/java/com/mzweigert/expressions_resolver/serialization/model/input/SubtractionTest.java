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

public class SubtractionTest {

    @Test
    public void calculateSimple() throws IllegalAccessException {

        // 5.255 - 1.234 = 4.021
        Expression subtraction = generateSimple(
                OperationType.SUBTRACTION, new BigDecimal("5.255"), new BigDecimal("1.234"));

        BigDecimal expected = new BigDecimal("4.02");

        Optional<BigDecimal> calculate = subtraction.calculate();

        assertThat(calculate.isPresent()).isTrue();
        assertThat(calculate.get().setScale(2, RoundingMode.HALF_DOWN)).isEqualTo(expected);
    }

    @Test
    public void calculateComplex() throws IllegalAccessException {
        // 10 / 3 = 3.3(3)
        Expression simpleDivision = generateSimple(OperationType.DIVISION,
                BigDecimal.TEN, new BigDecimal("3"));

        // 2.51 * 1.23 = 3.0873
        Expression simpleMultiplication = generateSimple(OperationType.MULTIPLICATION,
                new BigDecimal("2.51"),  new BigDecimal("1.23"));

        // 3.3(3) - 3.0873 = 0.24603(3) ~ 0.24
        BigDecimal expected = new BigDecimal("0.24");

        Expression complexSubtraction = generateComplex(
                OperationType.SUBTRACTION,
                simpleDivision, simpleMultiplication
        );

        Optional<BigDecimal> calculate = complexSubtraction.calculate();

        assertThat(calculate.isPresent()).isTrue();
        assertThat(calculate.get().setScale(2, RoundingMode.HALF_DOWN)).isEqualTo(expected);
    }
}
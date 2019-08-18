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

public class DivisionTest {

    @Test
    public void calculateSimple() throws IllegalAccessException {

        // 10 / 3 = 3,03(03)
        Expression division = generateSimple(
                OperationType.DIVISION, new BigDecimal("10"), new BigDecimal("3.3"));

        BigDecimal expected = new BigDecimal("3.03");

        Optional<BigDecimal> calculate = division.calculate();

        assertThat(calculate.isPresent()).isTrue();
        assertThat(calculate.get().setScale(2, RoundingMode.HALF_DOWN)).isEqualTo(expected);
    }

    @Test(expected = ArithmeticException.class)
    public void calculateSimpleDivisionByZero_throwException() throws Exception {

        Expression division = generateSimple(
                OperationType.DIVISION, new BigDecimal("10"), BigDecimal.ZERO);

        division.calculate();
    }

    @Test
    public void calculateComplex() throws IllegalAccessException {
        // 2.5 * 1.72 = 4.3
        Expression simpleMultiplication = generateSimple(OperationType.MULTIPLICATION,
                new BigDecimal("2.5"),  new BigDecimal("1.72"));
        // 10 / 3 = 3.3(3)
        Expression simpleDivision = generateSimple(OperationType.DIVISION,
                BigDecimal.TEN, new BigDecimal("3"));

        // 4.3 / 3.3(3) = 1.275 ~ 1.28
        BigDecimal expected = new BigDecimal("1.29");

        Expression complexDivision = generateComplex(
                OperationType.DIVISION,
                simpleMultiplication, simpleDivision
        );

        Optional<BigDecimal> calculate = complexDivision.calculate();

        assertThat(calculate.isPresent()).isTrue();
        assertThat(calculate.get().setScale(2, RoundingMode.HALF_DOWN)).isEqualTo(expected);
    }
}
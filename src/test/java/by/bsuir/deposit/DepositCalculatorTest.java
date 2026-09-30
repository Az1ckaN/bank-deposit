package by.bsuir.deposit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DepositCalculatorTest {

    // ---------- Позитивные сценарии ----------

    @Test
    @DisplayName("1 месяц под 12% годовых: +1%")
    void oneMonth() {
        BigDecimal result = DepositCalculator.calculateFinalAmount(
                new BigDecimal("1000"), new BigDecimal("12"), 1);
        assertEquals(new BigDecimal("1110.00"), result);
    }

    @Test
    @DisplayName("12 месяцев под 12% с капитализацией")
    void twelveMonths() {
        BigDecimal result = DepositCalculator.calculateFinalAmount(
                new BigDecimal("1000"), new BigDecimal("12"), 12);
        assertEquals(new BigDecimal("1126.84"), result);
    }

    @ParameterizedTest(name = "{0} при {1}% за {2} мес. = {3}")
    @CsvSource({
            "1000, 12, 1, 1010.00",
            "1000, 12, 2, 1020.10",
            "500,  0,  6, 500.00",
            "2000, 6,  1, 2010.00"
    })
    void parameterized(String principal, String rate, int months, String expected) {
        BigDecimal result = DepositCalculator.calculateFinalAmount(
                new BigDecimal(principal), new BigDecimal(rate), months);
        assertEquals(new BigDecimal(expected), result);
    }

    @Test
    void interestOnly() {
        BigDecimal interest = DepositCalculator.calculateInterest(
                new BigDecimal("1000"), new BigDecimal("12"), 1);
        assertEquals(new BigDecimal("10.00"), interest);
    }

    // ---------- Граничные сценарии ----------

    @Test
    @DisplayName("Срок 0 месяцев: сумма не меняется")
    void zeroMonths() {
        BigDecimal result = DepositCalculator.calculateFinalAmount(
                new BigDecimal("1000"), new BigDecimal("12"), 0);
        assertEquals(new BigDecimal("1000.00"), result);
    }

    @Test
    @DisplayName("Ставка 0%: сумма не меняется")
    void zeroRate() {
        BigDecimal result = DepositCalculator.calculateFinalAmount(
                new BigDecimal("1000"), BigDecimal.ZERO, 12);
        assertEquals(new BigDecimal("1000.00"), result);
    }

    @Test
    @DisplayName("Минимальная сумма 0.01 без роста")
    void minimalPrincipal() {
        BigDecimal result = DepositCalculator.calculateFinalAmount(
                new BigDecimal("0.01"), new BigDecimal("12"), 1);
        assertEquals(new BigDecimal("0.01"), result);
    }

    @Test
    @DisplayName("Максимальная ставка 100%: за месяц +1/12")
    void maxRate() {
        BigDecimal result = DepositCalculator.calculateFinalAmount(
                new BigDecimal("1200"), new BigDecimal("100"), 1);
        assertEquals(new BigDecimal("1300.00"), result);
    }

    // ---------- Ошибочные сценарии ----------

    @Test
    void negativePrincipal() {
        assertThrows(IllegalArgumentException.class, () ->
                DepositCalculator.calculateFinalAmount(new BigDecimal("-1"), new BigDecimal("5"), 1));
    }

    @Test
    void zeroPrincipal() {
        assertThrows(IllegalArgumentException.class, () ->
                DepositCalculator.calculateFinalAmount(BigDecimal.ZERO, new BigDecimal("5"), 1));
    }

    @Test
    void negativeRate() {
        assertThrows(IllegalArgumentException.class, () ->
                DepositCalculator.calculateFinalAmount(new BigDecimal("100"), new BigDecimal("-0.1"), 1));
    }

    @Test
    void rateAbove100() {
        assertThrows(IllegalArgumentException.class, () ->
                DepositCalculator.calculateFinalAmount(new BigDecimal("100"), new BigDecimal("100.01"), 1));
    }

    @Test
    void negativeMonths() {
        assertThrows(IllegalArgumentException.class, () ->
                DepositCalculator.calculateFinalAmount(new BigDecimal("100"), new BigDecimal("5"), -1));
    }

    @Test
    void nullArguments() {
        assertThrows(IllegalArgumentException.class, () ->
                DepositCalculator.calculateFinalAmount(null, new BigDecimal("5"), 1));
        assertThrows(IllegalArgumentException.class, () ->
                DepositCalculator.calculateFinalAmount(new BigDecimal("100"), null, 1));
    }
}

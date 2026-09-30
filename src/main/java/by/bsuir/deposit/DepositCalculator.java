package by.bsuir.deposit;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Расчёт банковского вклада с ежемесячной капитализацией процентов.
 */
public final class DepositCalculator {

    private static final BigDecimal MAX_RATE = new BigDecimal("100");
    private static final BigDecimal MONTHS_TIMES_PERCENT = new BigDecimal("1200");

    private DepositCalculator() {
    }

    /**
     * @param principal         сумма вклада (> 0)
     * @param annualRatePercent годовая ставка в процентах (0..100)
     * @param months            срок в месяцах (>= 0)
     * @return итоговая сумма, округлённая до копеек
     */
    public static BigDecimal calculateFinalAmount(BigDecimal principal,
                                                  BigDecimal annualRatePercent,
                                                  int months) {
        validate(principal, annualRatePercent, months);

        BigDecimal monthlyRate = annualRatePercent.divide(MONTHS_TIMES_PERCENT, 10, RoundingMode.HALF_UP);
        BigDecimal amount = principal.setScale(2, RoundingMode.HALF_UP);

        for (int i = 0; i < months; i++) {
            BigDecimal interest = amount.multiply(monthlyRate);
            amount = amount.add(interest).setScale(2, RoundingMode.HALF_UP);
        }
        return amount;
    }

    /** Только начисленные проценты. */
    public static BigDecimal calculateInterest(BigDecimal principal,
                                               BigDecimal annualRatePercent,
                                               int months) {
        BigDecimal finalAmount = calculateFinalAmount(principal, annualRatePercent, months);
        return finalAmount.subtract(principal.setScale(2, RoundingMode.HALF_UP));
    }

    private static void validate(BigDecimal principal, BigDecimal rate, int months) {
        if (principal == null || rate == null) {
            throw new IllegalArgumentException("Сумма и ставка не должны быть null");
        }
        if (principal.signum() <= 0) {
            throw new IllegalArgumentException("Сумма вклада должна быть положительной");
        }
        if (rate.signum() < 0 || rate.compareTo(MAX_RATE) > 0) {
            throw new IllegalArgumentException("Ставка должна быть в диапазоне 0..100");
        }
        if (months < 0) {
            throw new IllegalArgumentException("Срок не может быть отрицательным");
        }
    }
}

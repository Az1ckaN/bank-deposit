package by.bsuir.deposit;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        BigDecimal principal = new BigDecimal("1000");
        BigDecimal rate = new BigDecimal("12");
        int months = 12;

        BigDecimal total = DepositCalculator.calculateFinalAmount(principal, rate, months);
        BigDecimal interest = DepositCalculator.calculateInterest(principal, rate, months);

        System.out.println("Вклад: " + principal + ", ставка: " + rate + "%, срок: " + months + " мес.");
        System.out.println("Итоговая сумма: " + total);
        System.out.println("Проценты: " + interest);
    }
}

package br.com.core.ohmybills.utils;

import java.time.LocalDate;
import java.time.YearMonth;

public class RecurrenceAndInstallmentsUtils {

    public static boolean verifyIfRecursInTheMonth(YearMonth yearMonth, LocalDate start) {
        YearMonth startYearMonth = YearMonth.from(start);
        return (startYearMonth.equals(yearMonth) || !startYearMonth.isAfter(yearMonth));
    }

    public static boolean isAppliesByInstallments(YearMonth yearMonth, int installments, LocalDate start) {
        for (int i = 0; i < installments; i++) {
            LocalDate installmentDate = start.plusMonths(i);
            if (YearMonth.from(installmentDate).equals(yearMonth)) {
                return true;
            }
        }
        return false;
    }
}

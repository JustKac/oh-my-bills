package br.com.core.ohmybills.utils;

import java.time.LocalDate;
import java.time.YearMonth;

public class RecurrenceAndInstallmentsUtils {

    private RecurrenceAndInstallmentsUtils(){}

    /**
     * Verifica se a data está no mês especificado ou em um mês anterior.
     *
     * @param yearMonth Mês de referência
     * @param date Data a ser verificada
     * @return true se a data estiver no mês ou antes dele
     */
    public static boolean isDateInOrBeforeMonth(YearMonth yearMonth, LocalDate date) {
        YearMonth dateYearMonth = YearMonth.from(date);
        return !dateYearMonth.isAfter(yearMonth);
    }

    /**
     * Verifica se alguma parcela ocorre no mês especificado.
     *
     * @param yearMonth Mês de referência
     * @param installments Número de parcelas
     * @param startDate Data da primeira parcela
     * @return true se alguma parcela ocorrer no mês especificado
     */
    public static boolean isAnyInstallmentInMonth(YearMonth yearMonth, int installments, LocalDate startDate) {
        for (int i = 0; i < installments; i++) {
            if (YearMonth.from(startDate.plusMonths(i)).equals(yearMonth)) {
                return true;
            }
        }
        return false;
    }
}
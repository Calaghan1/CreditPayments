package com.gridnine.testing;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Helpers {
    static String table_name = " Дата платежа |  Сумма платежа  |  Сумма в погашение тела кредита |  Сумма платежа в погашение процентов |  Остаток долга";
    static String stringForm = "%12s  |  %13.2f  |  %29.2f  |  %34.2f  |  %10.2f";
    Set<LocalDate> dates = new HashSet<>();

    public void addHoliday() {
        //Тут требуется дописать праздники из производственного календаря. Есть варианты с API типо holidayapi.com но там платнгый доступ
        dates.add(LocalDate.of(2025, 1, 1));
        dates.add(LocalDate.of(2025, 2, 1));
        dates.add(LocalDate.of(2025, 3, 1));
        dates.add(LocalDate.of(2025, 4, 1));
        dates.add(LocalDate.of(2025, 5, 1));
        dates.add(LocalDate.of(2025, 6, 1));
        dates.add(LocalDate.of(2025, 5, 9));
        dates.add(LocalDate.of(2025, 6, 12));
    }
    LocalDate checkHolidays(LocalDate date) {
        addHoliday();
        while (date.getDayOfWeek() == DayOfWeek.SUNDAY || dates.contains(date)) {
            date = date.plusDays(1);
        }
        return date;
    }
    private boolean validateInput(double creditSum, double interestRate, int payments, LocalDate date) {
        if (creditSum <= 0) {
            return false;
        }
        if (interestRate <= 0) {
            return false;
        }
        if (payments <= 0) {
            return false;
        }
        if (date == null) {
            return false;
        }
        return true;
    }
    List<String> anualPayment(double creditSum, double interestRate, int payments, LocalDate date) {
        List<String> table = new ArrayList<>();
        if (!validateInput(creditSum, interestRate, payments, date)) {
            table.add("Wrong input");
            return table;
        }
        double percent;

        double body;
        table.add(table_name);
        double interestRateMonthly = interestRate / 12;
        double annual = (interestRateMonthly * Math.pow(1 + interestRateMonthly, payments))
                /(Math.pow(1 + interestRateMonthly, payments) - 1);
        double payment = creditSum * annual;
        for (int i = 0; i < payments - 1; i++) {
            percent = percent(creditSum, interestRate, date);
            body = payment - percent;
            date = date.plusMonths(1);
            creditSum -= body;
            date = checkHolidays(date);
            String month = stringForm.formatted(date, payment, body, percent, creditSum);
            table.add(month);
        }
        percent = percent(creditSum, interestRate, date);
        String month = stringForm.formatted(checkHolidays(date), creditSum + percent, creditSum, percent, 0.0);
        table.add(month);
        return table;
    }



    double percent(double debtLeft, double interestRate, LocalDate date) {
        return debtLeft * interestRate * date.lengthOfMonth() / date.lengthOfYear();
    }



    List<String> defPayment(double creditSum, double interestRate, int payments, LocalDate date) {
        double partOfDebt = creditSum / payments;
        double percent;
        List<String> table = new ArrayList<>();
        if (!validateInput(creditSum, interestRate, payments, date)) {
            table.add("Wrong input");
            return table;
        }
        table.add(table_name);
        for (int i = 0; i < payments - 1; i++) {
            percent = percent(creditSum, interestRate, date);
            date = checkHolidays(date);
            String str = stringForm.formatted(date, partOfDebt + percent, partOfDebt, percent, creditSum - partOfDebt - percent);
            table.add(str);
            creditSum -= partOfDebt;
            date = date.plusMonths(1);
        }
        percent = percent(creditSum, interestRate, date);
        String month = stringForm.formatted(checkHolidays(date), creditSum + percent, creditSum, percent, 0.0);
        table.add(month);
        return table;
    }
}

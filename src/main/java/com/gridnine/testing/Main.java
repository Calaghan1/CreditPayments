package com.gridnine.testing;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {



    public static void main(String[] args) {

        double creditSum = 2000000;
        int paymentMonths = 60;
        LocalDate date = LocalDate.now();
        double interestRate = 15;
        Helpers helpers = new Helpers();
        List<String> strs = helpers.defPayment(creditSum, interestRate / 100, paymentMonths, date);
        for (String str : strs) {
            System.out.println(str);
        }
        System.out.println("=============================================================================================================");
        strs = helpers.anualPayment(creditSum, interestRate / 100, paymentMonths, date);
        for (String str : strs) {
            System.out.println(str);
        }
    }
}
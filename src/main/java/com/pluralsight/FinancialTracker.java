package com.pluralsight;

import javax.swing.text.DateFormatter;
import java.io.*;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;

public class FinancialTracker {

    private static ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args) {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    public static void loadTransactions(String fileName) {
        // This method should load transactions from a file with the given file name.
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>|<time>|<description>|<vendor>|<amount>
        // For example: 2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String input;
            while ((input = reader.readLine()) != null) {
                String[] parts = input.split("\\|");

                LocalDate date = LocalDate.parse(parts[0]);
                LocalTime time = LocalTime.parse(parts[1]);
                String description = parts[2];
                String vendor = parts[3];
                double price = Double.parseDouble(parts[4]);

                transactions.add(new Transaction(date, time, description, vendor, price));
            }


        } catch (Exception e) {
            System.err.println("Related file can not exist.");
            e.printStackTrace();

        }

    }


    private static void addDeposit(Scanner scanner) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a deposit.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount should be a positive number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new deposit should be added to the `transactions` ArrayList.

        System.out.println("Enter the date of deposit(yyyy-MM-dd):");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

        System.out.println("Enter the time of deposit(HH:mm:ss):");
        LocalTime time = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);

        System.out.println("Enter the description of deposit");
        String description = scanner.nextLine();

        System.out.println("Enter the vendor of the deposit:");
        String vendor = scanner.nextLine();

        System.out.println("Enter the amount of deposit");
        Double amount = scanner.nextDouble();

        try {

            if (vendor.trim().isEmpty()) {
                System.out.println("Vendor can not be emty");
                return;
            }

            if (description.trim().isEmpty()) {
                System.out.println("Description can not be empty");
                return;
            }
            if (amount <= 0) {
                System.out.println("Amount should be bigger than 0 ");
                return;
            }
            Transaction newTransaction = new Transaction(date, time, description, vendor, amount);
            transactions.add(newTransaction);
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true))) {

                bufferedWriter.write(newTransaction.toString());
                bufferedWriter.newLine();

                System.out.println("Deposit added successfully ");

            }

        } catch (Exception e) {
            System.out.println("Invalid date or time format. Try again");

        }

    }


    private static void addPayment(Scanner scanner) {
        // This method should prompt the user to enter the date, time, description, vendor, and amount of a payment.
        // The user should enter the date and time in the following format: yyyy-MM-dd HH:mm:ss
        // The amount received should be a positive number then transformed to a negative number.
        // After validating the input, a new `Transaction` object should be created with the entered values.
        // The new payment should be added to the `transactions` ArrayList.

        System.out.println("Enter the date of payment(yyy-MM-dd):");
        LocalDate date = LocalDate.parse(scanner.nextLine(), DATE_FORMATTER);

        System.out.println("Enter the time of payment(HH:mm:ss):");
        LocalTime time = LocalTime.parse(scanner.nextLine(), TIME_FORMATTER);

        System.out.println("Enter the description of payment");
        String description = scanner.nextLine();

        System.out.println("Enter the vendor of the payment:");
        String vendor = scanner.nextLine();

        System.out.println("Enter the amount of payment(positive number): ");
        Double amount = scanner.nextDouble();

        try {
            if (vendor.trim().isEmpty()) {
                System.out.println("Vendor can not be emty");
                return;
            }

            if (description.trim().isEmpty()) {
                System.out.println("Description can not be emty");
                return;
            }
            if (amount <= 0) {
                System.out.println("Amount must be bigger than 0 ");
                return;
            }

            double negativeAmount = -amount;

            Transaction newTransaction = new Transaction(date, time, description, vendor, negativeAmount);
            transactions.add(newTransaction);

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
                bufferedWriter.write(newTransaction.toString());
                bufferedWriter.newLine();
            }

            System.out.println("Payment added successfully:");

        } catch (Exception e) {
            System.out.println("Invalid date or time format. Please try again.");
        }

    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) A`ll");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void displayLedger() {
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
        System.out.println("Date|Time|Description|Vendor|Amount");
        for (Transaction table : transactions) {
            System.out.println(table);
        }

    }


    private static void displayDeposits() {
        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
        System.out.println("Date|Time|Description|Vendor|Amount");
        for (Transaction table : transactions) {
            if (table.getAmount() > 0) {
                System.out.println(table.toString());
            }
        }
    }

    private static void displayPayments() {
        // This method should display a table of all payments in the `transactions` ArrayList.
        // The table should have columns for date, time, description, vendor, and amount.
        System.out.println("Date|Time|Description|Vendor|Amount");
        for (Transaction table : transactions) {
            if (table.getAmount() < 0) {
                System.out.println(table.toString());
            }
        }
    }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            LocalDate now = LocalDate.now();
            switch (input) {
                case "1":

                    // Generate a report for all transactions within the current month,
                    // including the date, time, description, vendor, and amount for each transaction.

                    LocalDate startDay = now.with(TemporalAdjusters.firstDayOfMonth());
                    LocalDate endDay = now;

                    filterTransactionsByDate(startDay, endDay);
                    break;
                case "2":
                    // Generate a report for all transactions within the previous month,
                    // including the date, time, description, vendor, and amount for each transaction.

                    LocalDate firstDayOfMinusMonth = now.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
                    LocalDate lastDayOfMinusMonth = now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
                    filterTransactionsByDate(firstDayOfMinusMonth,lastDayOfMinusMonth);
                    break;
                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, time, description, vendor, and amount for each transaction.

                    LocalDate firstDayOfCurrentYear = now.with(TemporalAdjusters.firstDayOfYear());
                    LocalDate lastDayOfCurrrentYear = now.with(TemporalAdjusters.lastDayOfYear());
                    filterTransactionsByDate(firstDayOfCurrentYear, lastDayOfCurrrentYear);
                    break;


                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, time, description, vendor, and amount for each transaction.
                    LocalDate firstDayOfPreviousYear = now.minusYears(1).with(TemporalAdjusters.firstDayOfYear());
                    LocalDate lastDayOfPreviousYear = now.minusYears(1).with(TemporalAdjusters.lastDayOfYear());
                    break;

                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, time, description, vendor, and amount for each transaction.
                    System.out.println("Please Enter Name Of Vendor: ");
                    String vendor = scanner.nextLine();
                    filterTransactionsByVendor(vendor);
                    break;

                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }


    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        // This method filters the transactions by date and prints a report to the console.
        // It takes two parameters: startDate and endDate, which represent the range of dates to filter by.
        // The method loops through the transactions list and checks each transaction's date against the date range.
        // Transactions that fall within the date range are printed to the console.
        // If no transactions fall within the date range, the method prints a message indicating that there are no results.

        boolean results = false;
        System.out.println("Filtered Transactions:");
        System.out.println("Date|Time|Description|Vendor|Amound");

        for (Transaction table : transactions) {
            if (!table.getDate().isBefore(startDate) && !table.getDate().isAfter(endDate)) {
                System.out.println(table);
                results = true;
            }
        }
        if (!results) {
            System.out.println("No transactions found in the given date range");
        }
    }

    private static void filterTransactionsByVendor(String vendor) {
        // This method filters the transactions by vendor and prints a report to the console.
        // It takes one parameter: vendor, which represents the name of the vendor to filter by.
        // The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
        // Transactions with a matching vendor name are printed to the console.
        // If no transactions match the specified vendor name, the method prints a message indicating that there are no results.
        System.out.println("Date|Time|Description|Vendor|Amount");
        boolean found = false;

        for (Transaction table : transactions) {
            if (table.getVendor().equalsIgnoreCase(vendor)) {
                System.out.println(table);
                found = true;
            }
        }
        if (!found) {
            System.out.println("A matching operation error occurred");
        }


    }
}
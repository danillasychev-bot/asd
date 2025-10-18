package org.example.Task_4;

import java.util.Scanner;

public class Check {
    private static final Scanner scanner = new Scanner(System.in);

    public static String readValidNumber() {
        while (true) {
            String number = scanner.nextLine();
            if (number.matches("[1-9][0-9]*")) {
                return number;
            } else {
                System.out.print("Ошибка ввода! Недопустимые символы.\nПовторите ввод: ");
            }
        }
    }

    public static String readValidInput() {
        while (true) {
            String number = scanner.nextLine();
            if (number.matches("[0-5]")) {
                return number;
            } else {
                System.out.print("Ошибка ввода! Недопустимые символы.\nВведите номер: ");
            }
        }
    }

    public static String readValidTablename() {
        while (true) {
            String name = scanner.nextLine().trim().toLowerCase();
            if (name.matches("[a-zа-я][a-zа-я0-9]*")) {
                return name;
            } else {
                System.out.print("Ошибка! Некорректные символы!\nВведите снова: ");
            }
        }
    }

    public static String readValidText() {
        while (true) {
            String name = scanner.nextLine().trim().toLowerCase();
            if (name.matches("[a-zа-я]+")) {
                return name;
            } else {
                System.out.print("Ошибка! Некорректные символы!\nВведите снова: ");
            }
        }
    }

    public static String readValidRun() {
        while (true) {
            String name = scanner.nextLine();
            if (name.matches("[1-4]")) {
                return name;
            } else {
                System.out.print("Ошибка ввода! Недопустимые символы.\nВведите номер: ");
            }
        }
    }

    public static void main(String[] args) {

        readValidNumber();
        readValidText();
        readValidInput();
        readValidTablename();
        readValidRun();
    }
}

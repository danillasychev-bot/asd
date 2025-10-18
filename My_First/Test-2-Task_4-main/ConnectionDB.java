package org.example.Task_4;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDB {

    protected static String postgreSQL = "jdbc:postgresql://localhost:5432/postgres";
    protected static Connection con;

    public static void connect() {
        try {
            con = DriverManager.getConnection(postgreSQL, "postgres", "pam311007");
            System.out.println("Подключение выполненно успешно!");
        } catch (Exception e) {
            System.out.println("Ошибка подключения: " + e);
        }
    }

    public static void main(String[] args) {
        connect();
    }
}
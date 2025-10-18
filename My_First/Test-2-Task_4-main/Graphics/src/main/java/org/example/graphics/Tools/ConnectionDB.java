package org.example.graphics.Tools;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDB {

    protected static String postgreSQL = "jdbc:postgresql://localhost:5432/postgres";
    protected static Connection con;

    public static void connect() {
        try {
            con = DriverManager.getConnection(postgreSQL, "postgres", "1234");
            System.out.println("Подключение выполненно успешно!");
        } catch (Exception e) {
            System.out.println("Ошибка подключения: " + e);
        }
    }
    public Connection getConnection() {
        return con;
    }


    public static void main(String[] args) {
        connect();
    }
}
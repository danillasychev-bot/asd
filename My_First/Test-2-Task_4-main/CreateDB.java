package org.example.Task_4;

import java.sql.SQLException;
import java.sql.Statement;

import static org.example.Task_4.ConnectionDB.con;

public class CreateDB {
    private static String tablename;

    public static void create() throws SQLException {
        System.out.println("Введите название таблицы: ");
        tablename = Check.readValidTablename();
        String query = "CREATE TABLE IF NOT EXISTS " + tablename + "(ID SERIAL PRIMARY KEY, NAME VARCHAR(1000), AGE VARCHAR(1000), SALARY VARCHAR(1000))";
        Statement stmt = con.createStatement();
        stmt.executeUpdate(query);
    }

    public static String getTablename() {
        return tablename;
    }

    public static void main(String[] args) throws SQLException {
        create();
    }
}

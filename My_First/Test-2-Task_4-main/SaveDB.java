package org.example.Task_4;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.Task_4.ConnectionDB.con;

public class SaveDB {
    public static void save() throws SQLException {
        String tablename = CreateDB.getTablename();

        String query = "SELECT * FROM " + tablename;
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
        org.apache.poi.xssf.usermodel.XSSFSheet sheet = workbook.createSheet(tablename);

        org.apache.poi.xssf.usermodel.XSSFRow headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("NAME");
        headerRow.createCell(2).setCellValue("AGE");
        headerRow.createCell(3).setCellValue("SALARY");

        int rowNum = 1;
        while (rs.next()) {
            org.apache.poi.xssf.usermodel.XSSFRow row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rs.getInt("id"));
            row.createCell(1).setCellValue(rs.getString("name"));
            row.createCell(2).setCellValue(rs.getInt("age"));
            row.createCell(3).setCellValue(rs.getInt("salary"));

            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            try (java.io.FileOutputStream fileOut = new java.io.FileOutputStream(tablename + ".xlsx")) {
                workbook.write(fileOut);
                System.out.println("Таблица сохранена в файл " + tablename + ".xlsx");
            } catch (Exception e) {
                System.out.println("Ошибка при сохранении файла: " + e.getMessage());
            }
        }
    }
    public static void main (String[]args) throws SQLException {
        save();
    }
}


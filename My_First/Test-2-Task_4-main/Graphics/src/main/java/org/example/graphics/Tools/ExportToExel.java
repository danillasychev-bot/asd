package org.example.graphics.Tools;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicReference;

public class ExportToExel {
    private static Connection connection;
    private static String tablename;
    private static ConnectionDB dbManager;
    private static CreateDB tbmanager;
    private static void showAlert(Alert.AlertType alertType, String tittle, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(tittle);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private static boolean readValidTableName(String name) {
        return name.matches("[A-Za-zА-Яа-я_][A-Za-zА-Яа-я0-9_]*");

    }
    public static void exportToExcel() throws IOException {
        dbManager = new ConnectionDB();
        tbmanager = new CreateDB();
        tablename = tbmanager.getTablename();
        connection = dbManager.getConnection();
        AtomicReference<XSSFWorkbook> workbook = new AtomicReference<>();
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(CreateSrt.class.getResource("ExportToExel.fxml"));
        BorderPane root = loader.load();
        URL cssUrl = CreateDB.class.getResource("Style.css");
        if (cssUrl == null) {
            cssUrl = CreateDB.class.getResource("/org/example/graphics/Style.css");
        }
        if (cssUrl != null) {
            root.getStylesheets().add(cssUrl.toExternalForm());

        }
        TextField tableExel = (TextField) root.lookup("#text");
        Button save = (Button) root.lookup("#save");
        Button retur = (Button) root.lookup("#return");
        retur.setOnAction(actionEvent -> window.close());
        System.out.println(tableExel.getText());
        save.setOnAction(actionEvent -> {
            String TableExel = tableExel.getText();
            if (!readValidTableName(TableExel) ||  TableExel.trim().isEmpty() || TableExel == "CON" || TableExel == "PRN" || TableExel == "AUX" || TableExel == "NUL"|| TableExel == "COM1"|| TableExel == "COM2"|| TableExel == "COM3"|| TableExel == "COM4"|| TableExel == "COM5"|| TableExel == "COM6"||  TableExel == "COM7"|| TableExel == "COM8"|| TableExel == "COM9"|| TableExel == "LPT1"|| TableExel == "LPT2"|| TableExel == "LPT3"|| TableExel == "LPT4"|| TableExel == "LPT5"|| TableExel == "LPT6"|| TableExel == "LPT7"|| TableExel == "LPT8"|| TableExel == "LPT9"){
                showAlert(Alert.AlertType.ERROR, "Ошибка!", "Некорректное название файла!");
                return;
            }
            try {
                if (connection == null || tablename == null || tablename.isEmpty()) {
                    throw new IllegalArgumentException("Не указаны обязательные параметры");
                }

                String query = "SELECT * FROM " + tablename;
                PreparedStatement ps = connection.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                workbook.set(new XSSFWorkbook());
                XSSFSheet sheet = workbook.get().createSheet(tablename);
                XSSFRow headerRow = sheet.createRow(0);


                headerRow.createCell(0).setCellValue("ID");
                headerRow.createCell(1).setCellValue("1 строка");
                headerRow.createCell(2).setCellValue("2 строка");
                headerRow.createCell(3).setCellValue("Перевёрнутая 1 строка");
                headerRow.createCell(4).setCellValue("Перевёрнутая 2 строка");
                headerRow.createCell(5).setCellValue("Слитые строки");

                int rowNum = 1;

                while (rs.next()) {
                    XSSFRow row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(rs.getInt("id"));
                    row.createCell(1).setCellValue(rs.getString("STR1"));
                    row.createCell(2).setCellValue(rs.getString("STR2"));
                    row.createCell(3).setCellValue(rs.getString("STR1TURN"));
                    row.createCell(4).setCellValue(rs.getString("STR2TURN"));
                    row.createCell(5).setCellValue(rs.getString("CONSTR"));
                }


                for (int i = 0; i < 6; i++) {
                    sheet.autoSizeColumn(i);
                }


                try (FileOutputStream fileOut = new FileOutputStream(tableExel.getText()+ ".xlsx")) {
                    workbook.get().write(fileOut);
                    showAlert(Alert.AlertType.INFORMATION,"Успешно!","Таблица сохранена в файл " + tableExel.getText() + ".xlsx");
                    System.out.println();
                }

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR,"Ошибка!","Ошибка при экспорте в Excel: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    if (workbook.get() != null) {
                        workbook.get().close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR,"Ошибка!","Ошибка при закрытии ресурсов: " + e.getMessage());

                }
            }

        });
        Scene scene = new Scene(root);
        window.setScene(scene);
        window.showAndWait();

    }


}





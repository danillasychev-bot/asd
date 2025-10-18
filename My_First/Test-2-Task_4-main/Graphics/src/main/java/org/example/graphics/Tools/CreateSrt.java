package org.example.graphics.Tools;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateSrt {

    private static Connection connection;
    private static String tablename;
    private static ConnectionDB dbManager;
    private static CreateDB tbmanager;
    private static int id = 0;
    public static boolean isSTRCreated = false;

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static boolean isValidString(String name3) {
        return name3 != null && !name3.isEmpty() && !name3.contains(" ") && name3.matches("^[^\\s]+$");
    }

    public static void createRows() throws SQLException, IOException {
        dbManager = new ConnectionDB();
        tbmanager = new CreateDB();
        tablename = tbmanager.getTablename();
        connection = dbManager.getConnection();

        id += 1;

        String query = "INSERT INTO " + tablename + " (STR1, STR2) VALUES (?, ?)";

        PreparedStatement ps = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Введите строки");



        FXMLLoader loader = new FXMLLoader(CreateSrt.class.getResource("Createstr.fxml"));
        BorderPane root = loader.load();

        URL cssUrl = CreateDB.class.getResource("Style.css");
        if (cssUrl == null) {
            cssUrl = CreateDB.class.getResource("/org/example/graphics/Style.css");
        }
        if (cssUrl != null) {
            root.getStylesheets().add(cssUrl.toExternalForm());

        }

        // Находим элементы из FXML
        TextField str1 = (TextField) root.lookup("#str1");
        TextField str2 = (TextField) root.lookup("#str2");
        Button save = (Button) root.lookup("#save");
        Button cancel = (Button) root.lookup("#return");

        // Обработчик кнопки "Отмена"
        cancel.setOnAction(e -> window.close());

        // Обработчик кнопки "Сохранить"
        save.setOnAction(actionEvent -> {
            String text1 = str1.getText().trim();
            String text2 = str2.getText().trim();


            // Проверка на пустые строки
            if (text1.isEmpty() || text2.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Ошибка!", "Обе строки должны быть заполнены");
                return;
            }

            // Проверка длины строк
            if (text1.length() < 50 || text2.length() < 50) {
                showAlert(Alert.AlertType.ERROR, "Ошибка!", "Длина каждой строки должна быть не менее 50 символов");
                return;
            }

            // Проверка на валидность строк (без пробелов)
            if (!isValidString(text1) || !isValidString(text2)) {
                showAlert(Alert.AlertType.ERROR, "Ошибка!", "Строки не должны содержать пробелов");
                return;
            }

            try {
                ps.setString(1, text1);
                ps.setString(2, text2);
                ps.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Успешно", "Строки добавлены в БД");
                window.close();
                isSTRCreated = true;

            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка базы данных!");
                alert.setContentText("Не удалось добавить строки в базу данных: " + e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
                isSTRCreated = false;
            }
        });

        // Создаем сцену и показываем окно
        Scene scene = new Scene(root);
        window.setScene(scene);
        window.showAndWait();
    }
}
package org.example.graphics.Tools;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.example.graphics.Tools.ConnectionDB.con;

public class CreateDB {
    private static String tablename;
    public static boolean isDBCreated = false;

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

    public static void showTables() throws SQLException, IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Созданные таблицы");
        FXMLLoader loader = new FXMLLoader(CreateSrt.class.getResource("ShowTables.fxml"));
        BorderPane root = loader.load();
        URL cssUrl = CreateDB.class.getResource("Style.css");
        if (cssUrl == null) {
            cssUrl = CreateDB.class.getResource("/org/example/graphics/Style.css");
        }
        if (cssUrl != null) {
            root.getStylesheets().add(cssUrl.toExternalForm());

        }


        TextArea tableList = (TextArea) root.lookup("#text");
        tableList.setEditable(false);
        Button closeButton = (Button) root.lookup("#close");




        closeButton.setOnAction(e -> window.close());


        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'")) {

            StringBuilder tables = new StringBuilder();
            while (rs.next()) {
                tables.append(rs.getString("table_name")).append("\n");
            }

            tableList.setText(tables.toString());

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Произошла ошибка при получении списка таблиц");
            alert.setContentText("Ошибка: " + e.getMessage());
            alert.showAndWait();
        }

        // Создаем сцену и показываем окно
        Scene scene = new Scene(root);
        window.setScene(scene);
        window.showAndWait();
    }

    public static void create() throws SQLException, IOException {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle("Создание таблицы");


        FXMLLoader loader = new FXMLLoader(CreateDB.class.getResource("Createdb.fxml"));
        BorderPane root = loader.load();


        URL cssUrl = CreateDB.class.getResource("Style.css");
        if (cssUrl == null) {
            cssUrl = CreateDB.class.getResource("/org/example/graphics/Style.css");
        }
        if (cssUrl != null) {
            root.getStylesheets().add(cssUrl.toExternalForm());

        }

        TextField text = (TextField) root.lookup("#text");
        Button save = (Button) root.lookup("#save");
        Button retur = (Button) root.lookup("#return");




        retur.setOnAction(actionEvent -> window.close());

        save.setOnAction(actionEvent -> {

            String tablename1 = text.getText();
            if (!readValidTableName(tablename1)) {
                showAlert(Alert.AlertType.ERROR, "Ошибка!", "Некорректное название таблицы!");
                return;
            }
            tablename = tablename1;

            String query = "CREATE TABLE IF NOT EXISTS " + tablename1 +
                    "(ID SERIAL PRIMARY KEY, " +
                    "STR1 VARCHAR(1000), " +
                    "STR2 VARCHAR(1000), " +
                    "STR1TURN VARCHAR(1000), " +
                    "STR2TURN VARCHAR(1000), " +
                    "CONSTR VARCHAR(1000))";

            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate(query);
                isDBCreated = true;
                showAlert(Alert.AlertType.INFORMATION, "Успех", "Таблица успешно создана!");
                window.close();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Ошибка", "Некорректные символы!");
            }
        });
        Scene scene = new Scene(root);
        window.setScene(scene);
        window.showAndWait();
    }

    public static String getTablename() {
        return tablename;
    }
}

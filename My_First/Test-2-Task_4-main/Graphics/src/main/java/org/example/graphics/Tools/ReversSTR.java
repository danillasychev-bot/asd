package org.example.graphics.Tools;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReversSTR {
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
    private static boolean isValidInteger_2(String name) {
        return name.matches("^([1-2])$");
    }
    public static void revers(int id) throws IOException {
        dbManager = new ConnectionDB();
        tbmanager = new CreateDB();
        tablename = tbmanager.getTablename();
        connection = dbManager.getConnection();

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(CreateSrt.class.getResource("ReversStr.fxml"));
        BorderPane root = loader.load();
        URL cssUrl = CreateDB.class.getResource("Style.css");
        if (cssUrl == null) {
            cssUrl = CreateDB.class.getResource("/org/example/graphics/Style.css");
        }
        if (cssUrl != null) {
            root.getStylesheets().add(cssUrl.toExternalForm());

        }
        TextField choies = (TextField) root.lookup("#choies");
        Button invert = (Button) root.lookup("#invert");
        Button retur = (Button) root.lookup("#return");

        retur.setOnAction(actionEvent -> window.close());

        invert.setOnAction(actionEvent -> {

            String choice = choies.getText();
            if (!isValidInteger_2(choice)){
                showAlert(Alert.AlertType.ERROR, "Ошибка", "Введите 1 или 2");
                return;
            }
            String querySelect;
            String queryUpdate;
            String columnName;
            switch (choice){
                case("1"):
                    queryUpdate = "UPDATE " + tablename + " SET STR1TURN = ? WHERE ID = ?";
                    querySelect = "SELECT STR1 FROM " + tablename + " WHERE ID = ?";
                    columnName = "STR1";

                    break;
                case ("2"):
                    queryUpdate = "UPDATE " + tablename + " SET STR2TURN = ? WHERE ID = ?";
                    querySelect = "SELECT STR2 FROM " + tablename + " WHERE ID = ?";
                    columnName = "STR2";
                    break;
                default:
                    return;

            }
            try (PreparedStatement psSelect = connection.prepareStatement(querySelect); PreparedStatement psUpdate = connection.prepareStatement(queryUpdate)) {
                psSelect.setInt(1, id);
                ResultSet rs = psSelect.executeQuery();
                if (rs.next()) {
                    String result = rs.getString(columnName);
                    String reversed = new StringBuilder(result).reverse().toString();
                    psUpdate.setString(1, reversed);
                    psUpdate.setInt(2, id);
                    psUpdate.executeUpdate();
                    showAlert(Alert.AlertType.INFORMATION, "Успех", "Инвертированная строка: " + reversed);
                    window.close();
                }


            }catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Ошибка", "Произошла ошибка при обновлении данных: " + e.getMessage());
            }



        });

        Scene scene = new Scene(root);
        window.setScene(scene);
        window.showAndWait();








    }
}

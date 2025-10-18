package org.example.graphics.Tools;

import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StickSTR {
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
    public static void Stick(int id) throws SQLException {
        dbManager = new ConnectionDB();
        tbmanager = new CreateDB();
        tablename = tbmanager.getTablename();
        connection = dbManager.getConnection();
        String querySelect1 = "SELECT STR1 FROM " + tablename + " WHERE ID = ?";
        String querySelect2 = "SELECT STR2 FROM " + tablename + " WHERE ID = ?";
        String queryUpdate = "UPDATE " + tablename + " SET CONSTR = ? WHERE ID = ?";
        try{
            PreparedStatement ps1 =connection.prepareStatement(querySelect1);
            PreparedStatement ps2 =connection.prepareStatement(querySelect2);
            PreparedStatement ps3 =connection.prepareStatement(queryUpdate);
            ps1.setInt(1, id);
            ps2.setInt(1, id);
            ResultSet rs1 = ps1.executeQuery();
            ResultSet rs2 = ps2.executeQuery();
            if (rs1.next() && rs2.next()){
                String res1 = rs1.getString("STR1");
                String res2 = rs2.getString("STR2");
                String rez = res1 + res2;
                showAlert(Alert.AlertType.INFORMATION,"Склеенные строки",rez);
                ps3.setString(1, rez);
                ps3.setInt(2, id);
                ps3.executeUpdate();
            }
        }catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Произошла ошибка при обновлении данных: " + e.getMessage());
            System.out.println(e.getMessage());
        }






    }
}

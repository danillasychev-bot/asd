package org.example.graphics;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.graphics.Tools.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import static org.example.graphics.Tools.CreateDB.isDBCreated;
import static org.example.graphics.Tools.CreateSrt.isSTRCreated;

public class MainApp extends Application {

    public static Integer id = 0;




    @Override
    public void start(Stage stage) throws IOException {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            ConnectionDB.connect();
            stage.setTitle("Task №1 - Java Database Manager");

            BorderPane root = loader.load();

            Button one = (Button) root.lookup("#one");
            Button two = (Button) root.lookup("#two");
            Button three = (Button) root.lookup("#three");
            Button four = (Button) root.lookup("#four");
            Button five = (Button) root.lookup("#five");
            Button six = (Button) root.lookup("#six");


            ImageView logoImage = (ImageView) root.lookup("#logoImage");
            loadImage(logoImage);


            three.setDisable(true);
            four.setDisable(true);
            five.setDisable(true);
            six.setDisable(true);

            Scene scene = new Scene(root, 800, 450);
            stage.setScene(scene);
            stage.show();




            one.setOnAction(actionEvent -> {
                try {
                    CreateDB.showTables();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            two.setOnAction(actionEvent -> {
                try {
                    CreateDB.create();
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }

                if (isDBCreated){
                    three.setDisable(false);
                    six.setDisable(false);
                    five.setDisable(true);
                    four.setDisable(true);
                    isDBCreated = false;

                }


            });

            three.setOnAction(actionEvent -> {
                try {
                    CreateSrt.createRows();
                } catch (SQLException | IOException e) {
                    throw new RuntimeException(e);

                }
                id += 1;
                if (isSTRCreated){
                    four.setDisable(false);
                    five.setDisable(false);
                    isSTRCreated = false;
                }

            });

            four.setOnAction(actionEvent -> {
                try {
                    ReversSTR.revers(id);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            five.setOnAction(actionEvent -> {
                try {
                    StickSTR.Stick(id);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            six.setOnAction(actionEvent -> {
                try {
                    ExportToExel.exportToExcel();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void loadImage(ImageView imageView) {
        try {

            String s = "java_logo.png";


            URL imageUrl = getClass().getResource(s);
            if (imageUrl == null) {
                imageUrl = getClass().getResource("/org/example/graphics/"+s);
            }
            if (imageUrl == null) {
                imageUrl = getClass().getResource("/"+s);
            }



            Image image = new Image(imageUrl.toString());
            imageView.setImage(image);
            System.out.println("Изображение успешно загружено: " + imageUrl);

        } catch (Exception e) {
            System.err.println("Ошибка загрузки изображения: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
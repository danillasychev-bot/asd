module org.example.graphics {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.ooxml;
    requires javafx.graphics;


    opens org.example.graphics to javafx.fxml;
    exports org.example.graphics;
}
module com.example.javafxunitconverter {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javafxunitconverter to javafx.fxml;
    exports com.example.javafxunitconverter;
}
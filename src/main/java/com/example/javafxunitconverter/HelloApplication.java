package com.example.javafxunitconverter;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.collections.FXCollections;

public class HelloApplication extends Application {

    Stage window;
    Scene scene;

    // distance units available to be converted between
    String distanceUnits[] = { "km", "m", "cm", "mm", "mi", "yd", "ft", "in" };

    // contains value to multiply original value unit for each converted unit
    // row to represent the original unit, columns to represent the converted unit
    Double distanceUnitsConversion[][] = {
            {1.0, 1000.0, 100000.0, 1000000.0,
                    0.62137139107611571998, 1093.6136482939637062, 3280.8409448818911187, 39370.091338582693425},
            {0.001, 1.0, 100.0, 10000.0,
                    0.00062137099999751426067, 1.0936129599956250225, 3.2808388799868746233, 0.39370066559842498144},
            {0.00001, 0.01, 1.0, 10.0,
                    0.000006213709999975141455, 0.010936129599956248906, 0.032808388799868748453, 0.39370066559842503695},
            {0.000001, 0.001, 0.1, 1.0,
                    0.0000006213709999975141031, 0.0010936129599956251942, 0.0032808388799868753657, 0.039370066559842507858},
            {1.6093400007802, 1609.3400007801999436, 160934.00007801997708, 1609340.0007801996544,
                    1.0, 1760.0, 5280.0, 63360.0},
            {0.0009144, 0.9144, 91.44, 914.4,
                    0.000568182, 1.0, 1760.0005632, 5280.0016896},
            {0009144002926080002213.0, 0.91440029260800026467, 91.440029260800031352, 914.40029260800042721,
                    00056818200000000030053.0, 0.33333344000000020291, 1.0, 12.000003840000006861},
            {0.00002540000812800000991, 0.025400008128000011592, 2.5400008128000011592, 25.400008128000010288,
                    0.00001578283333333334093, 0.02777778666666667895, 0.083333360000000036849, 1.0}
    };

    // index corresponding to selected units for original and selected units
    int fromIndex = 0;
    int toIndex = 1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;

        // original value column contents
        Label fromLabel = new Label("From");
        TextField fromTextField = new TextField();
        fromTextField.setMaxWidth(100);
        ComboBox fromComboBox =
                new ComboBox(FXCollections
                        .observableArrayList(distanceUnits));
        fromComboBox.getSelectionModel().selectFirst();

        // convert button column contents
        Label toLabel = new Label("To");
        Button convertButton = new Button("Convert");

        // converted value column contents
        TextField toTextField = new TextField();
        toTextField.setEditable(false);
        toTextField.setMaxWidth(100);
        ComboBox toComboBox =
                new ComboBox(FXCollections
                        .observableArrayList(distanceUnits));
        toComboBox.getSelectionModel().select(1);

        // add action to convert button
        convertButton.setOnAction(e -> convertValue(fromTextField.getText(), toTextField, fromIndex, toIndex));

        // link enter key in TextField to the convert button
        fromTextField.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                convertValue(fromTextField.getText(), toTextField, fromIndex, toIndex);
            }
        });

        // track changes to the ComboBoxes

        fromComboBox.getSelectionModel().selectedIndexProperty().addListener( (v, oldValue, newValue) -> {
            fromIndex = (int)newValue;
        });

        toComboBox.getSelectionModel().selectedIndexProperty().addListener( (v, oldValue, newValue) -> {
            toIndex = (int)newValue;
        });

        GridPane layout = new GridPane();

        // add elements for original value column in GridPane layout
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setVgap(10);
        layout.setHgap(10);
        layout.setAlignment(Pos.CENTER);
        layout.add(fromLabel, 0, 0);
        layout.add(fromTextField, 0, 1);
        layout.add(fromComboBox, 0, 2);
        layout.setHalignment(fromLabel, HPos.CENTER);
        layout.setHalignment(fromTextField, HPos.CENTER);
        layout.setHalignment(fromComboBox, HPos.CENTER);

        // add elements for convert button column in GridPane layout
        layout.add(convertButton, 1, 1);
        layout.setHalignment(convertButton, HPos.CENTER);

        // add elements for converted value column in GridPane layout
        layout.add(toLabel, 2, 0);
        layout.add(toTextField, 2, 1);
        layout.add(toComboBox, 2, 2);
        layout.setHalignment(toLabel, HPos.CENTER);
        layout.setHalignment(toTextField, HPos.CENTER);
        layout.setHalignment(toComboBox, HPos.CENTER);

        scene = new Scene(layout);
        window.setScene(scene);
        window.setTitle("Unit Converter");
        window.show();
    }

    // converts from original value and units to selected converted units and displays result
    private boolean convertValue(String input, TextField output, int fromIndex, int toIndex){

        try {

            double number = Double.parseDouble(input);
            double conversionValue = (double)distanceUnitsConversion[fromIndex][toIndex];
            double result = number * conversionValue;

            output.setText(String.valueOf(result));
            return true;
        }
        catch (NumberFormatException e) {

            if (!input.isEmpty()) {

                output.setText("NaN");
            }

            return false;
        }
    }
}
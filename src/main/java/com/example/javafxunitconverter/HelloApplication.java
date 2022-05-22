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
                    0.621371, 1093.61, 3280.84, 39370.1},
            {0.001, 1.0, 100.0, 10000.0,
                    0.000621371, 1.09361, 3.28084, 39.3701},
            {0.00001, 0.01, 1.0, 10.0,
                    0.0000062137, 0.0109361, 0.0328084, 0.393701},
            {0.000001, 0.001, 0.1, 1.0,
                    0.00000062137, 0.00109361, 0.00328084, 0.0393701},
            {1.60934, 1609.34, 160934.0, 1609000.0,
                    1.0, 1760.0, 5280.0, 63360.0},
            {0.0009144, 0.9144, 91.44, 914.4,
                    0.000568182, 1.0, 3.0, 36.0},
            {0.0003048, 0.3048, 30.48, 304.8,
                    0.000189394, 0.33333344, 1.0, 12.0},
            {0.0000254, 0.0254, 2.54, 25.4,
                    0.000015783, 0.0277778, 0.0833333, 1.0}
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
        Button swapSidesButton = new Button("<->");

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

        // add action to swap sides button
        swapSidesButton.setOnAction(e -> swapSides(fromTextField, fromComboBox,
                toTextField, toComboBox, fromIndex, toIndex));

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
        layout.add(swapSidesButton, 1, 2);
        layout.setHalignment(convertButton, HPos.CENTER);

        // add elements for converted value column in GridPane layout
        layout.setHalignment(swapSidesButton, HPos.CENTER);
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

    // converts from original value and units to selected converted unit and displays result
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

    // swaps original value and units with converted value and units
    private void swapSides(TextField fromTextField, ComboBox fromComboBox,
                           TextField toTextField, ComboBox toComboBox,
                           int fromIndex, int toIndex){

        int tempFromIndex = fromIndex;

        String tempFromInput = fromTextField.getText();
        fromComboBox.getSelectionModel().select(toIndex);
        toComboBox.getSelectionModel().select(tempFromIndex);

        boolean isValid = !toTextField.getText().equals("NaN");

        // if NaN is present, clear values
        // otherwise swap the values normally
        if (isValid) {

            fromTextField.setText(toTextField.getText());
            toTextField.setText(tempFromInput);

            if (!tempFromInput.contains(".")) {

                toTextField.setText(tempFromInput + ".0");
            }
        }
        else {

            fromTextField.setText("");
            toTextField.setText("");
        }
    }
}
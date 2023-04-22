package com.dugnys.labora4;

import com.dugnys.attendance.GroupTable;
import com.dugnys.attendance.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    ArrayList<GroupTable> tableList;
    @FXML
    ChoiceBox<GroupTable> choiceGroup;
    @FXML
    TextField fieldName;

    @FXML
    void groupCreation(){

        GroupTable table = new GroupTable();
        table.addNewColumn("name");

        tableList.add(table);
        table.setName(fieldName.getText());
        choiceGroup.getItems().add(table);

    }

    @FXML
    void attendanceChange() {

        Stage stage = new Stage();
        VBox box = new VBox();
        AnchorPane anchorPane = new AnchorPane();
        TextField newPerson = new TextField("Naujas asmuo");
        Button buttonPerson = new Button("add");
        DatePicker datePicker = new DatePicker();
        Button buttonDate = new Button("add");

        buttonPerson.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                GroupTable table = choiceGroup.getValue();
                table.addNewRow(newPerson.getText());
            }
        });

        buttonDate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                GroupTable table = choiceGroup.getValue();
                table.addNewColumn(datePicker.getValue().toString());
            }
        });

        AnchorPane.setRightAnchor(box, 1.0);
        AnchorPane.setLeftAnchor(box,  1.0);

        box.getChildren().addAll(choiceGroup.getValue().getTable(), newPerson, buttonPerson, datePicker, buttonDate);
        anchorPane.getChildren().add(box);


        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void attendanceView() {

        Stage stage = new Stage();
        AnchorPane anchorPane = new AnchorPane();

        TableView table = choiceGroup.getValue().getTable();
        table.setEditable(false);

        AnchorPane.setRightAnchor(table, 1.0);
        AnchorPane.setLeftAnchor(table,  1.0);

        anchorPane.getChildren().add(table);

        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                table.setEditable(true);
            }
        });

        stage.show();
    }

    @FXML
    void saveCsv() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage window = new Stage();

        File file = directoryChooser.showDialog(window);

        try {
            FileHandler.exportCSV(file.toString(), choiceGroup.getValue().getTable());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void saveXls () {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage window = new Stage();

        File file = directoryChooser.showDialog(window);

        try {
            FileHandler.exportXLS(file.toString(), choiceGroup.getValue().getTable());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void savePdf() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage window = new Stage();

        File file = directoryChooser.showDialog(window);

        try {
            FileHandler.exportPDF(file.toString(), choiceGroup.getValue().getTable());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void openCsv() {
        FileChooser fileChooser = new FileChooser();
        Stage window = new Stage();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("csv file", "*.csv")
        );
        File file = fileChooser.showOpenDialog(window);

        GroupTable table;
        try {
            table = FileHandler.importCSV(file.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tableList.add(table);
        table.setName(fieldName.getText());
        choiceGroup.getItems().add(table);
    }

    @FXML
    void openXls() {
        FileChooser fileChooser = new FileChooser();
        Stage window = new Stage();

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("xls file", "*.xls")
        );
        File file = fileChooser.showOpenDialog(window);

        GroupTable table;
        try {
            table = FileHandler.importXLS(file.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tableList.add(table);
        table.setName(fieldName.getText());
        choiceGroup.getItems().add(table);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableList = new ArrayList<>();

    }

}
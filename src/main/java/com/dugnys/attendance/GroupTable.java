package com.dugnys.attendance;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.TableColumn.CellEditEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupTable implements TableEditable{
    TableView<Map> table;
    String name;
    public ArrayList<String> keys;
    public GroupTable() {
        table = new TableView<>();
        table.setEditable(true);
        keys = new ArrayList<>();
    }

    @Override
    public void addNewRow(String name) {

        Map<String, String> row = new HashMap<>();

        //uzpildyt visus column tusciais
        for (String key: keys)
            row.put(key,"");

        row.put("name", name);

        table.getItems().add(row);
    }

    @Override
    public void addNewColumn(String name) {

        TableColumn<Map, String> column = new TableColumn<>(name);
        keys.add(name);

        //uzpildyt visas eilutes tusciais naujam stulpelyje
        for (int i=0; i<table.getItems().size(); i++ ){
            table.getItems().get(i).put(name,"");
        }

        //https://stackoverflow.com/questions/67696271/how-to-dynamically-add-columns-to-tableview-and-update-their-content
        //su map !!!!!!!! https://jenkov.com/tutorials/javafx/tableview.html

        //column.setCellValueFactory(new PropertyValueFactory<>("name"));
        column.setCellValueFactory(new MapValueFactory<>(name));
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(new EventHandler<CellEditEvent<Map, String>>() {
            @Override
            public void handle(CellEditEvent<Map, String> event) {
                Map<String, Object> person = event.getRowValue();
                person.put(name, event.getNewValue());
            }
        });

        table.getColumns().add(column);
    }

    @Override
    public void removeRow() {

    }

    @Override
    public void removeColumn() {

    }

    public TableView<Map> getTable() {
        return table;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setName(String name) {this.name = name;
    }
}

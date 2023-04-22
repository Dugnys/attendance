package com.dugnys.attendance;

public interface TableEditable {

    void addNewRow(String name);
    void addNewColumn(String name);
    void removeRow();
    void removeColumn();


}

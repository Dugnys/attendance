package com.dugnys.attendance;

public class Person {
    String name;
    String date;
    Person(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public String getDate(int index) { return date; }
    public void setName(String name) { this.name = name; }
    public void setDate(int index, String date){ this.date = date; }


}

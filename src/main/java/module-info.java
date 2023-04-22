module com.dugnys.labora4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires itextpdf;


    opens com.dugnys.labora4 to javafx.fxml;
    exports com.dugnys.labora4;
    exports com.dugnys.attendance;
    opens com.dugnys.attendance to javafx.fxml;
}
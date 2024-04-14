module ua.nure.cpp_lab_4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens ua.nure.cpp_lab_4 to javafx.fxml;
    exports ua.nure.cpp_lab_4;
}
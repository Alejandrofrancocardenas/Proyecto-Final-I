module co.edu.uniquindio.sistemagestionhospital {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquindio.sistemagestionhospital to javafx.fxml;
    exports co.edu.uniquindio.sistemagestionhospital;
}
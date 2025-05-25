module co.edu.uniquindio.sistemagestionhospital {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens co.edu.uniquindio.sistemagestionhospital.viewController to javafx.fxml;
    opens co.edu.uniquindio.sistemagestionhospital to javafx.fxml;
    exports co.edu.uniquindio.sistemagestionhospital;
    exports co.edu.uniquindio.sistemagestionhospital.Controller;
    opens co.edu.uniquindio.sistemagestionhospital.Controller to javafx.fxml;
}
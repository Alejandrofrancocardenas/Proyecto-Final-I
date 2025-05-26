module co.edu.uniquindio.sistemagestionhospital {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens co.edu.uniquindio.sistemagestionhospital.model to javafx.base;
    opens co.edu.uniquindio.sistemagestionhospital.viewController to javafx.fxml;
    opens co.edu.uniquindio.sistemagestionhospital.Controller to javafx.fxml;

    exports co.edu.uniquindio.sistemagestionhospital;
    exports co.edu.uniquindio.sistemagestionhospital.model;
    exports co.edu.uniquindio.sistemagestionhospital.viewController;
    exports co.edu.uniquindio.sistemagestionhospital.Controller;
}

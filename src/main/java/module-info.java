module com.tuempresa.RegistroEmpleadosFX {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.tuempresa.RegistroEmpleadosFX to javafx.fxml;
    exports com.tuempresa.RegistroEmpleadosFX;
    opens com.tuempresa.RegistroEmpleadosFX.controller to javafx.fxml;
    exports com.tuempresa.RegistroEmpleadosFX.controller;
    opens com.tuempresa.RegistroEmpleadosFX.model to javafx.base;
    exports com.tuempresa.RegistroEmpleadosFX.model;
}
module com.tuempresa.RegistroEmpleadosFX {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tuempresa.RegistroEmpleadosFX to javafx.fxml;
    exports com.tuempresa.RegistroEmpleadosFX;
}
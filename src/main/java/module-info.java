module com.tuempresa.guia_practica_pareja {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tuempresa.guia_practica_pareja to javafx.fxml;
    exports com.tuempresa.guia_practica_pareja;
}
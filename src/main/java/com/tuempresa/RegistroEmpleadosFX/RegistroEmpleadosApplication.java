package com.tuempresa.RegistroEmpleadosFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RegistroEmpleadosApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(
                RegistroEmpleadosApplication.class.getResource(
                        "empleado-view.fxml"
                )
        );

        Scene scene = new Scene(fxmlLoader.load(), 1200, 750);

        stage.setTitle("Registro de Empleados");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static class Launcher {
        public static void main(String[] args) {
            launch(HelloApplication.class, args);
        }
    }
}

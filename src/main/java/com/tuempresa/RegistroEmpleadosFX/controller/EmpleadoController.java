package com.tuempresa.RegistroEmpleadosFX.controller;

import com.tuempresa.RegistroEmpleadosFX.database.DatabaseConnection;
import com.tuempresa.RegistroEmpleadosFX.model.Empleado;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class EmpleadoController {

    // Componentes FXML del Formulario
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtCedula;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCargo;
    @FXML private ComboBox<String> cmbDepartamento;
    @FXML private TextField txtSalario;
    @FXML private DatePicker dpFechaContratacion;
    @FXML private ComboBox<String> cmbEstado;

    // Botones
    @FXML private Button btnGuardar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnActualizar; // Corresponde al botón "Actualizar tabla"
    @FXML private Button btnRegistro;   // Corresponde a "Actualizar registro"
    @FXML private Button btnEliminar;   // Corresponde a "Eliminar registro"

    // TableView y Columnas
    @FXML private TableView<Empleado> tblEmpleados;
    @FXML private TableColumn<Empleado, Integer> colId;
    @FXML private TableColumn<Empleado, String> colNombres;
    @FXML private TableColumn<Empleado, String> colApellidos;
    @FXML private TableColumn<Empleado, String> colCedula;
    @FXML private TableColumn<Empleado, String> colCorreo;
    @FXML private TableColumn<Empleado, String> colTelefono;
    @FXML private TableColumn<Empleado, String> colCargo;
    @FXML private TableColumn<Empleado, String> colDepartamento;
    @FXML private TableColumn<Empleado, Double> colSalario;
    @FXML private TableColumn<Empleado, LocalDate> colFechaContratacion;
    @FXML private TableColumn<Empleado, String> colEstado;

    private ObservableList<Empleado> listaEmpleados;

    @FXML
    public void initialize() {
        // 1. Configurar mapeo de columnas con atributos del modelo Empleado
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colCedula.setCellValueFactory(new PropertyValueFactory<>("cedula"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCargo.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        colDepartamento.setCellValueFactory(new PropertyValueFactory<>("departamento"));
        colSalario.setCellValueFactory(new PropertyValueFactory<>("salario"));
        colFechaContratacion.setCellValueFactory(new PropertyValueFactory<>("fechaContratacion"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // 2. Cargar opciones en ComboBoxes
        cmbDepartamento.setItems(FXCollections.observableArrayList(
                "Tecnología", "Recursos Humanos", "Finanzas", "Ventas", "Administración"
        ));

        cmbEstado.setItems(FXCollections.observableArrayList(
                "Activo", "Inactivo"
        ));

        // 3. Listener para seleccionar un registro de la tabla y llenar los campos del formulario
        tblEmpleados.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarDatosEnFormulario(newSelection);
            }
        });

        // 4. Inicializar lista y cargar empleados desde PostgreSQL
        listaEmpleados = FXCollections.observableArrayList();
        cargarEmpleados();
    }

    // Pasa los datos del Empleado seleccionado a los campos de texto
    private void cargarDatosEnFormulario(Empleado emp) {
        txtNombres.setText(emp.getNombres());
        txtApellidos.setText(emp.getApellidos());
        txtCedula.setText(emp.getCedula());
        txtCorreo.setText(emp.getCorreo());
        txtTelefono.setText(emp.getTelefono());
        txtCargo.setText(emp.getCargo());
        cmbDepartamento.setValue(emp.getDepartamento());
        txtSalario.setText(String.valueOf(emp.getSalario()));
        dpFechaContratacion.setValue(emp.getFechaContratacion());
        cmbEstado.setValue(emp.getEstado());
    }

    // Método para consultar y llenar el TableView
    @FXML
    public void cargarEmpleados() {
        listaEmpleados.clear();
        String sql = "SELECT id, nombres, apellidos, cedula, correo, telefono, cargo, departamento, salario, fecha_contratacion, estado FROM empleado ORDER BY id ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Empleado emp = new Empleado(
                        rs.getInt("id"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("cedula"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("cargo"),
                        rs.getString("departamento"),
                        rs.getDouble("salario"),
                        rs.getDate("fecha_contratacion").toLocalDate(),
                        rs.getString("estado")
                );
                listaEmpleados.add(emp);
            }

            tblEmpleados.setItems(listaEmpleados);

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Conexión", "No se pudieron cargar los empleados:\n" + e.getMessage());
        }
    }

    // Método asociado al botón Guardar
    @FXML
    public void guardarEmpleado(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }

        String sql = "INSERT INTO empleado (nombres, apellidos, cedula, correo, telefono, cargo, departamento, salario, fecha_contratacion, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, txtNombres.getText().trim());
            stmt.setString(2, txtApellidos.getText().trim());
            stmt.setString(3, txtCedula.getText().trim());
            stmt.setString(4, txtCorreo.getText().trim());
            stmt.setString(5, txtTelefono.getText().trim());
            stmt.setString(6, txtCargo.getText().trim());
            stmt.setString(7, cmbDepartamento.getValue());
            stmt.setDouble(8, Double.parseDouble(txtSalario.getText().trim()));
            stmt.setDate(9, Date.valueOf(dpFechaContratacion.getValue()));
            stmt.setString(10, cmbEstado.getValue());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Empleado registrado correctamente en PostgreSQL.");
                limpiarCampos(null);
                cargarEmpleados();
            }

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al Guardar", "Ocurrió un error en la base de datos:\n" + e.getMessage());
        }
    }

    // Método para Actualizar el registro seleccionado
    @FXML
    public void actualizarRegistro(ActionEvent event) {
        Empleado empSeleccionado = tblEmpleados.getSelectionModel().getSelectedItem();

        if (empSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección Requerida", "Por favor, selecciona un empleado de la tabla para actualizar.");
            return;
        }

        if (!validarCampos()) {
            return;
        }

        String sql = "UPDATE empleado SET nombres=?, apellidos=?, cedula=?, correo=?, telefono=?, cargo=?, departamento=?, salario=?, fecha_contratacion=?, estado=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, txtNombres.getText().trim());
            stmt.setString(2, txtApellidos.getText().trim());
            stmt.setString(3, txtCedula.getText().trim());
            stmt.setString(4, txtCorreo.getText().trim());
            stmt.setString(5, txtTelefono.getText().trim());
            stmt.setString(6, txtCargo.getText().trim());
            stmt.setString(7, cmbDepartamento.getValue());
            stmt.setDouble(8, Double.parseDouble(txtSalario.getText().trim()));
            stmt.setDate(9, Date.valueOf(dpFechaContratacion.getValue()));
            stmt.setString(10, cmbEstado.getValue());
            stmt.setInt(11, empSeleccionado.getId());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "El registro del empleado fue actualizado correctamente.");
                limpiarCampos(null);
                cargarEmpleados();
            }

        } catch (SQLException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al Actualizar", "Ocurrió un error al actualizar la base de datos:\n" + e.getMessage());
        }
    }

    // Método para Eliminar el registro seleccionado
    @FXML
    public void eliminarRegistro(ActionEvent event) {
        Empleado empSeleccionado = tblEmpleados.getSelectionModel().getSelectedItem();

        if (empSeleccionado == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección Requerida", "Por favor, selecciona un empleado de la tabla para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Estás seguro de que deseas eliminar a " + empSeleccionado.getNombres() + " " + empSeleccionado.getApellidos() + "?");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();

        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            String sql = "DELETE FROM empleado WHERE id = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, empSeleccionado.getId());
                int filasAfectadas = stmt.executeUpdate();

                if (filasAfectadas > 0) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "El empleado ha sido eliminado correctamente.");
                    limpiarCampos(null);
                    cargarEmpleados();
                }

            } catch (SQLException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error al Eliminar", "No se pudo eliminar el registro:\n" + e.getMessage());
            }
        }
    }

    // Método asociado al botón Limpiar
    @FXML
    public void limpiarCampos(ActionEvent event) {
        txtNombres.clear();
        txtApellidos.clear();
        txtCedula.clear();
        txtCorreo.clear();
        txtTelefono.clear();
        txtCargo.clear();
        cmbDepartamento.setValue(null);
        txtSalario.clear();
        dpFechaContratacion.setValue(null);
        cmbEstado.setValue(null);
        tblEmpleados.getSelectionModel().clearSelection();
    }

    // Método asociado al botón Actualizar tabla
    @FXML
    public void actualizarTabla(ActionEvent event) {
        cargarEmpleados();
    }

    // Validaciones básicas de entrada de datos
    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        if (txtNombres.getText() == null || txtNombres.getText().trim().isEmpty()) {
            errores.append("- El campo Nombres es obligatorio.\n");
        }
        if (txtApellidos.getText() == null || txtApellidos.getText().trim().isEmpty()) {
            errores.append("- El campo Apellidos es obligatorio.\n");
        }
        if (txtCedula.getText() == null || txtCedula.getText().trim().isEmpty()) {
            errores.append("- El campo Cédula es obligatorio.\n");
        }
        if (cmbDepartamento.getValue() == null) {
            errores.append("- Debe seleccionar un Departamento.\n");
        }
        if (dpFechaContratacion.getValue() == null) {
            errores.append("- Debe seleccionar la Fecha de Contratación.\n");
        }
        if (cmbEstado.getValue() == null) {
            errores.append("- Debe seleccionar un Estado.\n");
        }

        if (txtSalario.getText() == null || txtSalario.getText().trim().isEmpty()) {
            errores.append("- El campo Salario es obligatorio.\n");
        } else {
            try {
                double salario = Double.parseDouble(txtSalario.getText().trim());
                if (salario < 0) {
                    errores.append("- El salario debe ser un número mayor o igual a 0.\n");
                }
            } catch (NumberFormatException e) {
                errores.append("- El salario debe ser un valor numérico válido.\n");
            }
        }

        if (errores.length() > 0) {
            mostrarAlerta(Alert.AlertType.WARNING, "Validación de Formulario", errores.toString());
            return false;
        }

        return true;
    }

    // Método auxiliar para mostrar alertas de JavaFX
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
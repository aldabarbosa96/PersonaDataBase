package windows;

import database.BaseDatos;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import personas.Persona;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class MainDBWindow extends Application {
    private BaseDatos db;
    private GridPane formulario;
    TableView<Persona> table;
    private VBox root;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Personal DataBase");
        db = new BaseDatos("jdbc:sqlite:personal_database.db");
        db.conectarDB();
        db.crearTabla();

        createMainLayout();

        Scene scene = new Scene(root, 1426, 975);
        scene.getStylesheets().add(getClass().getResource("/temas/temaPrincipal.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void createMainLayout() {
        root = new VBox();
        root.setPadding(new Insets(35, 25, 35, 25));
        root.setSpacing(35);

        initializeForms();
        initializeButtons();
        initializeTables();
        updateTable();

        root.getChildren().addAll(formulario, table);
    }

    public void initializeForms() {
        formulario = new GridPane();
        formulario.setHgap(10);
        formulario.setVgap(10);

        TextField nombreTextField = new TextField();
        nombreTextField.setPromptText("Nombre");

        TextField apellido1TextField = new TextField();
        apellido1TextField.setPromptText("Primer Apellido");

        TextField apellido2TextField = new TextField();
        apellido2TextField.setPromptText("Segundo Apellido");

        DatePicker fechaField = new DatePicker();

        TextField direccionTextField = new TextField();
        direccionTextField.setPromptText("Dirección Postal");

        TextField emailTextField = new TextField();
        emailTextField.setPromptText("Correo Electrónico");

        TextField telefonoTextField = new TextField();
        telefonoTextField.setPromptText("Nº Teléfono");

        TextField rolTextField = new TextField();
        rolTextField.setPromptText("Rol");

        formulario.add(new Label("Nombre:"), 0, 0);
        formulario.add(nombreTextField, 1, 0);
        formulario.add(new Label("Primer Apellido:"), 0, 1);
        formulario.add(apellido1TextField, 1, 1);
        formulario.add(new Label("Segundo Apellido:"), 0, 2);
        formulario.add(apellido2TextField, 1, 2);
        formulario.add(new Label("Fecha Nacimiento:"), 0, 3);
        formulario.add(fechaField, 1, 3);
        formulario.add(new Label("Dirección Postal:"), 0, 5);
        formulario.add(direccionTextField, 1, 5);
        formulario.add(new Label("Correo Electrónico:"), 0, 6);
        formulario.add(emailTextField, 1, 6);
        formulario.add(new Label("Teléfono:"), 0, 7);
        formulario.add(telefonoTextField, 1, 7);
        formulario.add(new Label("Rol:") ,0,8);
        formulario.add(rolTextField,1,8);
    }

    public void initializeButtons() {
        Button botonGuardar = new Button("Guardar");
        Button botonEliminar = new Button("Eliminar");
        botonEliminar.getStyleClass().add("eliminar");

        botonGuardar.setOnAction(event -> {
            Persona personaSeleccionada = table.getSelectionModel().getSelectedItem();
            try {
                if (personaSeleccionada != null) {
                    personaSeleccionada.setNombre(((TextField) formulario.getChildren().get(1)).getText());
                    personaSeleccionada.setApellido1(((TextField) formulario.getChildren().get(3)).getText());
                    personaSeleccionada.setApellido2(((TextField) formulario.getChildren().get(5)).getText());

                    DatePicker fechaPicker = (DatePicker) formulario.getChildren().get(7);
                    if (fechaPicker.getValue() != null) {
                        personaSeleccionada.setFechaNaciemiento(java.sql.Date.valueOf(fechaPicker.getValue()));
                    } else {
                        personaSeleccionada.setFechaNaciemiento(null);
                    }

                    personaSeleccionada.setDireccion(((TextField) formulario.getChildren().get(9)).getText());
                    personaSeleccionada.setEmail(((TextField) formulario.getChildren().get(11)).getText());
                    personaSeleccionada.setTelefono(((TextField) formulario.getChildren().get(13)).getText());
                    personaSeleccionada.setRol(((TextField) formulario.getChildren().get(15)).getText());

                    db.actualizarPersona(personaSeleccionada);
                    updateTable();
                } else {
                    String nombre = ((TextField) formulario.getChildren().get(1)).getText();
                    String apellido1 = ((TextField) formulario.getChildren().get(3)).getText();
                    String apellido2 = ((TextField) formulario.getChildren().get(5)).getText();
                    Date fechaNacimiento = ((DatePicker) formulario.getChildren().get(7)).getValue() != null
                            ? java.sql.Date.valueOf(((DatePicker) formulario.getChildren().get(7)).getValue())
                            : null;

                    String direccion = ((TextField) formulario.getChildren().get(9)).getText();
                    String email = ((TextField) formulario.getChildren().get(11)).getText();
                    String telefono = ((TextField) formulario.getChildren().get(13)).getText();
                    String rol = ((TextField) formulario.getChildren().get(15)).getText();

                    Persona nuevaPersona = new Persona(0, nombre, apellido1, apellido2, fechaNacimiento, direccion, email, telefono, rol);
                    db.insertarPersona(nuevaPersona);
                    updateTable();
                }
                clearForm();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error al guardar la persona: " + e.getMessage());
                alert.show();
            }
        });


        botonEliminar.setOnAction(event -> {
            Persona personaSeleccionada = table.getSelectionModel().getSelectedItem();
            if (personaSeleccionada != null) {
                try {
                    db.eliminarPersona(personaSeleccionada.getId());
                    updateTable();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Persona eliminada exitosamente.");
                    alert.show();
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Error al eliminar la persona: " + e.getMessage());
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Selecciona una persona para eliminar.");
                alert.show();
            }
        });

        formulario.add(botonGuardar, 1, 9);
        formulario.add(botonEliminar, 1, 10);
    }

    private void clearForm() {
        formulario.getChildren().forEach(node -> {
            if (node instanceof TextField) {
                ((TextField) node).clear();
            } else if (node instanceof DatePicker) {
                ((DatePicker) node).setValue(null);
            }
        });
    }

    public void initializeTables() {
        table = new TableView<>();
        table.setPlaceholder(new Label("No hay personas registradas"));
        table.setMinHeight(500);

        TableColumn<Persona, Long> columnaID = new TableColumn<>("ID");
        columnaID.setPrefWidth(50);
        columnaID.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));

        TableColumn<Persona, String> columnaNombre = new TableColumn<>("Nombre");
        columnaNombre.setPrefWidth(150);
        columnaNombre.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNombre()));

        TableColumn<Persona, String> columnaApellido1 = new TableColumn<>("Primer Apellido");
        columnaApellido1.setPrefWidth(175);
        columnaApellido1.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getApellido1()));

        TableColumn<Persona, String> columnaApellido2 = new TableColumn<>("Segundo Apellido");
        columnaApellido2.setPrefWidth(175);
        columnaApellido2.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getApellido2()));

        TableColumn<Persona, Date> columnaFechaNacimiento = new TableColumn<>("Fecha Nacimiento");
        columnaFechaNacimiento.setPrefWidth(120);
        columnaFechaNacimiento.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getFechaNaciemiento()));

        TableColumn<Persona, String> columnaDireccion = new TableColumn<>("Dirección Postal");
        columnaDireccion.setPrefWidth(250);
        columnaDireccion.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDireccion()));

        TableColumn<Persona, String> columnaEmail = new TableColumn<>("Correo Electrónico");
        columnaEmail.setPrefWidth(200);
        columnaEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<Persona, String> columnaTelefono = new TableColumn<>("Teléfono");
        columnaTelefono.setPrefWidth(100);
        columnaTelefono.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTelefono()));

        TableColumn<Persona, String> columnaRol = new TableColumn<>("Rol");
        columnaRol.setPrefWidth(150);
        columnaRol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRol()));

        table.getColumns().addAll(columnaID, columnaNombre, columnaApellido1, columnaApellido2,
                columnaFechaNacimiento, columnaDireccion, columnaEmail, columnaTelefono, columnaRol);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarDatosEnFormulario((Persona) newSelection);
            }
        });
    }

    private void cargarDatosEnFormulario(Persona persona) {
        ((TextField) formulario.getChildren().get(1)).setText(persona.getNombre());
        ((TextField) formulario.getChildren().get(3)).setText(persona.getApellido1());
        ((TextField) formulario.getChildren().get(5)).setText(persona.getApellido2());

        if (persona.getFechaNaciemiento() != null) {
            ((DatePicker) formulario.getChildren().get(7)).setValue(
                    new java.sql.Date(persona.getFechaNaciemiento().getTime()).toLocalDate()
            );
        } else {
            ((DatePicker) formulario.getChildren().get(7)).setValue(null);
        }

        ((TextField) formulario.getChildren().get(9)).setText(persona.getDireccion());
        ((TextField) formulario.getChildren().get(11)).setText(persona.getEmail());
        ((TextField) formulario.getChildren().get(13)).setText(persona.getTelefono());
        ((TextField) formulario.getChildren().get(15)).setText(persona.getRol());
    }


    private void updateTable() {
        table.getItems().clear();

        try {
            List<Persona> personas = db.obtenerTodasPersonas();
            table.getItems().addAll(personas);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al cargar las personas: " + e.getMessage());
            alert.show();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}

package database;

import personas.Persona;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseDatos {
    private String pathDB;
    private Connection connection;

    public BaseDatos(String pathDB) {
        this.pathDB = pathDB;
    }

    public void conectarDB() throws SQLException {
        try {
            String userHome = System.getProperty("user.home");
            pathDB = userHome + File.separator + "PersonalDataBase.db";

            String url = "jdbc:sqlite:" + pathDB;
            System.out.println("Conectando a la base de datos en: " + pathDB);
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void desconectarDB() throws SQLException {
        if (connection != null && !connection.isClosed()) connection.close();
    }

    public void crearTabla() {
        String sqlCreate = """
                CREATE TABLE IF NOT EXISTS Personas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL,
                    apellido1 TEXT NOT NULL,
                    apellido2 TEXT,
                    fechaNacimiento DATE NULL,
                    direccion TEXT,
                    email TEXT,
                    telefono TEXT,
                    rol TEXT
                )
            """;



        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlCreate);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertarPersona(Persona persona) throws SQLException {
        String sql = """
            INSERT INTO Personas (nombre, apellido1, apellido2, fechaNacimiento, direccion, email, telefono, rol)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, persona.getNombre());
            statement.setString(2, persona.getApellido1());
            statement.setString(3, persona.getApellido2());
            if (persona.getFechaNaciemiento() != null) {
                statement.setDate(4, new java.sql.Date(persona.getFechaNaciemiento().getTime()));
            } else {
                statement.setNull(4, Types.DATE);
            }
            statement.setString(5, persona.getDireccion());
            statement.setString(6, persona.getEmail());
            statement.setString(7, persona.getTelefono());
            statement.setString(8, persona.getRol());

            statement.executeUpdate();
        }
    }


    public void eliminarPersona(long id) throws SQLException {
        String sql = "DELETE FROM Personas WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Persona con ID " + id + " eliminada exitosamente.");
            } else {
                System.out.println("No se encontró ninguna persona con ID " + id + ".");
            }
        }
    }

    public void actualizarPersona(Persona persona) throws SQLException {
        String sql = """
        UPDATE Personas
        SET nombre = ?, apellido1 = ?, apellido2 = ?, fechaNacimiento = ?, direccion = ?, email = ?, telefono = ?, rol = ?
        WHERE id = ?
        """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, persona.getNombre());
            statement.setString(2, persona.getApellido1());
            statement.setString(3, persona.getApellido2());

            if (persona.getFechaNaciemiento() != null) {
                statement.setDate(4, new java.sql.Date(persona.getFechaNaciemiento().getTime()));
            } else {
                statement.setNull(4, Types.DATE);
            }

            statement.setString(5, persona.getDireccion());
            statement.setString(6, persona.getEmail());
            statement.setString(7, persona.getTelefono());
            statement.setString(8, persona.getRol());
            statement.setLong(9, persona.getId());

            statement.executeUpdate();
        }
    }


    public List<Persona> obtenerTodasPersonas() throws SQLException {
        String sql = "SELECT * FROM Personas";
        List<Persona> personas = new ArrayList<>();

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Persona persona = new Persona(
                        resultSet.getLong("id"),
                        resultSet.getString("nombre"),
                        resultSet.getString("apellido1"),
                        resultSet.getString("apellido2"),
                        resultSet.getDate("fechaNacimiento"),
                        resultSet.getString("direccion"),
                        resultSet.getString("email"),
                        resultSet.getString("telefono"),
                        resultSet.getString("rol")
                );
                personas.add(persona);
            }
        }
        return personas;
    }
}

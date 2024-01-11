package GestorEmpleados;

import databaseConnection.DatabaseConnection;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class GestorEmpleados {
    public static void main(String[] args) {
        //createTable();
        //agregarDatosDePrueba();
        moverEmpleados(3, 2, 1);
    }

    public static void moverEmpleados(int numEmpleados, int idOficinaOrigen, int idOficinaDestino) {
        Connection conexion = null;
        try {
            conexion = DatabaseConnection.getConnection();

            // Iniciar la transacción
            conexion.setAutoCommit(false);

            // Actualizar oficina origen
            String sql = "UPDATE oficinas SET num_empleados = num_empleados - ? WHERE id = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, numEmpleados);
                pstmt.setInt(2, idOficinaOrigen);
                pstmt.executeUpdate();
            }

            // Actualizar oficina destino
            sql = "UPDATE oficinas SET num_empleados = num_empleados + ? WHERE id = ?";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setInt(1, numEmpleados);
                pstmt.setInt(2, idOficinaDestino);
                pstmt.executeUpdate();
            }

            // Confirmar la transacción
            conexion.commit();
        } catch (SQLException e) {
            try {
                // En caso de error, revertir la transacción
                if (conexion != null) {
                    conexion.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                // Restaurar el modo de commit automático
                if (conexion != null) {
                    try {
                        conexion.setAutoCommit(true);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            e.printStackTrace();
        } finally {
            // Cerrar la conexión
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void createTable(){
        String sql = """
                CREATE TABLE oficinas (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(255) NOT NULL,
                num_empleados INT DEFAULT 0);""";

        try (Connection c = DatabaseConnection.getConnection();
             Statement stmt = c.createStatement()){
            stmt.execute(sql);
            System.out.println("Tabla 'oficinas' creada con éxito");

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void agregarDatosDePrueba(){
        try (Connection conexion = DatabaseConnection.getConnection()){
            // Asegurar que no haya datos previos (opcional)
            String sql = "DELETE FROM oficinas";
            try (Statement stmt = conexion.createStatement()) {
                stmt.executeUpdate(sql);
            }

            // Insertar los datos de prueba
            sql = "INSERT INTO oficinas (nombre, num_empleados) VALUES (?, ?)";
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                // Oficina 1
                pstmt.setString(1, "Oficina 1");
                pstmt.setInt(2, 3);
                pstmt.executeUpdate();

                // Oficina 2
                pstmt.setString(1, "Oficina 2");
                pstmt.setInt(2, 6);
                pstmt.executeUpdate();

                // Oficina 3
                pstmt.setString(1, "Oficina 3");
                pstmt.setInt(2, 9);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

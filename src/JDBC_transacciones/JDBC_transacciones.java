package JDBC_transacciones;

import databaseConnection.DatabaseConnection;
import org.mariadb.jdbc.export.Prepare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBC_transacciones {
    public static void main(String[] args) {
        try (Connection c = DatabaseConnection.getConnection()){
            try (PreparedStatement sInsert = c.prepareStatement("INSERT INTO " +
                    "clientes1 (dni, apellido, cp) VALUES (?, ?, ?);")){

                c.setAutoCommit(false);

                sInsert.setString(1, "98237469V");
                sInsert.setString(2, "PEREZ");
                sInsert.setString(3, "45623");
                sInsert.executeUpdate();

                sInsert.setString(1, "92836477S");
                sInsert.setString(2, "MARTINEZ");
                sInsert.setString(3, "56987");
                sInsert.executeUpdate();

                sInsert.setString(1, "90123445A");
                sInsert.setString(2, "MOLINA");
                sInsert.setString(3, "35153");
                sInsert.executeUpdate();

                c.commit();

            } catch (SQLException e){
                try {
                    c.rollback();
                    System.err.println("Se hace ROLLBACK");
                } catch (SQLException er) {
                    System.err.println("Error haciendo ROLLBACK");

                }
            }

        } catch (SQLException e){
            System.err.println("Error de conexión");

            e.printStackTrace();
        }
    }
}

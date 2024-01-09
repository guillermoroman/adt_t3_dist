package JDBC_claves_autogeneradas;

import databaseConnection.DatabaseConnection;
import org.mariadb.jdbc.export.Prepare;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBC_claves_autogeneradas {
    public static void main(String[] args) {
        try (Connection c = DatabaseConnection.getConnection()){

            try(PreparedStatement sInsertFact = c.prepareStatement(
                    "INSERT INTO facturas (dni_cliente) VALUES (?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
                PreparedStatement sInsertLineaFact = c.prepareStatement(
                        """
                        INSERT INTO lineas_factura
                        (num_factura, linea_factura, concepto, cantidad)
                        VALUES (?, ?, ?, ?);""");){
                c.setAutoCommit(false);

                int i = 1;
                sInsertFact.setString(i++, "54320198V");
                sInsertFact.executeUpdate();
                ResultSet rs = sInsertFact.getGeneratedKeys();
                rs.next();
                int numFact=rs.getInt(1);

                int lineaFact = 1;
                i = 1;
                sInsertLineaFact.setInt(i++, numFact);
                sInsertLineaFact.setInt(i++, lineaFact++);
                sInsertLineaFact.setString(i++, "tuercas");
                sInsertLineaFact.setInt(i++, 25);
                sInsertLineaFact.executeUpdate();

                i = 1;
                sInsertLineaFact.setInt(i++, numFact);
                sInsertLineaFact.setInt(i++, lineaFact++);
                sInsertLineaFact.setString(i++, "tornillos");
                sInsertLineaFact.setInt(i++, 250);
                sInsertLineaFact.executeUpdate();

                c.commit();
            } catch (SQLException e) {
                muestraErrorSQL(e);
                try {
                    c.rollback();
                    System.err.println("Se hace ROLLBACK");
                } catch (Exception er) {
                    System.err.println("ERROR haciendo ROLLBACK");
                    er.printStackTrace(System.err);
                }
            }
        } catch (Exception e) {
            System.err.println("ERROR de conexión");
            e.printStackTrace(System.err);
        }
    }

    public static void muestraErrorSQL (SQLException e) {
        System.err.println("SQL ERROR message: " + e.getMessage());
        System.err.println("SQL Estado: " + e.getSQLState());
        System.err.println("SQL Código específico: " + e.getErrorCode());
    }
}

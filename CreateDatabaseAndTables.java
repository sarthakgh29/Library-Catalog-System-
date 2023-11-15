import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDatabaseAndTables {
    private static final String DB_URL = "jdbc:mysql://localhost/";
    private static final String DB_USER = "uruser";
    private static final String DB_PASSWORD = "urpass";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Create the database
            createDatabase(connection, "library");

            // Switch to the 'library' database
            connection = DriverManager.getConnection(DB_URL + "library", DB_USER, DB_PASSWORD);

            // Create the 'books' table
            createBooksTable(connection);

            connection.close();
            System.out.println("Database and tables created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createDatabase(Connection connection, String dbName) throws SQLException {
        String createDbSQL = "CREATE DATABASE IF NOT EXISTS " + dbName;
        Statement statement = connection.createStatement();
        statement.executeUpdate(createDbSQL);
        statement.close();
    }

    private static void createBooksTable(Connection connection) throws SQLException {
        String createBooksTableSQL = "CREATE TABLE IF NOT EXISTS books ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "title VARCHAR(255) NOT NULL, "
                + "author VARCHAR(255), "
                + "publication_year INT, "
                + "ISBN VARCHAR(13))";
        Statement statement = connection.createStatement();
        statement.executeUpdate(createBooksTableSQL);
        statement.close();
    }
}

package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Used to connect to database and create tables. One for users and one for contacts.
 *
 * @author Justin Myers
 */
public class Database {

    private static final String URL = "jdbc:sqlite:data/contacts.db";

    // Connect the program to the database.
    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL);
            createTables(connection);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    // Create the two tables needed
    private static void createTables(Connection connection) {

        String contactsSql = """
        CREATE TABLE IF NOT EXISTS contacts (
            contact_id INTEGER PRIMARY KEY AUTOINCREMENT,
            first_name TEXT NOT NULL,
            last_name TEXT NOT NULL,
            phone TEXT NOT NULL,
            address TEXT NOT NULL
        );""";

        String usersSql = """
        CREATE TABLE IF NOT EXISTS users (
            username TEXT PRIMARY KEY,
            password TEXT NOT NULL
        );""";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(contactsSql);
            stmt.execute(usersSql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create tables", e);
        }
    }
}

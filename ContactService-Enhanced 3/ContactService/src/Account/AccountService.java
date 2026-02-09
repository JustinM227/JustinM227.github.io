package Account;

import Database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Holds all the users
 *
 * @author Justin Myers
 * 2/5/2026
 */
public class AccountService {

    // Add user to the system
    public void register(String username, String password) {

        // Validate first
        new User(username, password);

        String sql = """
            INSERT INTO users (username, password)
            VALUES (?, ?)
        """;

        // Connect to database.
        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                throw new IllegalArgumentException("Username already exists");
            }
            throw new RuntimeException(e);
        }
    }

    // Attempts to login with given information
    public boolean login(String username, String password) {

        String sql = """
            SELECT password
            FROM users
            WHERE username = ?
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            return rs.next() && rs.getString("password").equals(password);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

package Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Database.Database;

/**
 * Holds all the contacts
 *
 * @author Justin Myers
 * 2/3/2026
 */
public class ContactService {

    // Adds contact to the database
    public Contact addContact(String firstName, String lastName, String phone, String address) {

        String sql = """
            INSERT INTO contacts (first_name, last_name, phone, address)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return new Contact(
                        keys.getInt(1),
                        firstName,
                        lastName,
                        phone,
                        address
                );
            }

            throw new RuntimeException("Failed to create contact");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Deletes contact from database
    public void deleteContact(int contactId) {

        String sql = "DELETE FROM contacts WHERE contact_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, contactId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Updates contact from database
    public void updateContact(int contactId, String firstName, String lastName, String phone, String address) {

        String sql = """
            UPDATE contacts
            SET first_name = ?, last_name = ?, phone = ?, address = ?
            WHERE contact_id = ?
        """;

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setInt(5, contactId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Gets contact from database
    public Contact getContact(int contactId) {

        String sql = "SELECT * FROM contacts WHERE contact_id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, contactId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Contact(
                        rs.getInt("contact_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("phone"),
                        rs.getString("address")
                );
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Gets all contacts from database
    public List<Contact> getAllContacts() {

        List<Contact> list = new ArrayList<>();

        String sql = "SELECT * FROM contacts ORDER BY contact_id";

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Contact(
                        rs.getInt("contact_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("phone"),
                        rs.getString("address")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    // Checks if an id exists in database. Not being used right now
    public boolean idExists(int contactId) {

        String sql = "SELECT 1 FROM contacts WHERE contact_id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, contactId);
            return ps.executeQuery().next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

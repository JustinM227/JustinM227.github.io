package Account;

/**
 * Used to hold a user's information
 *
 * @author Justin Myers
 * 1/23/2026
 */
class User {
    private final String username;
    private final String password;

    // Constructor
    public User(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Password must be at least 4 characters");
        }
        this.username = username;
        this.password = password;
    }

    // Gets username
    public String getUsername() { return username; }

    // Gets password
    public String getPassword() { return password; }
}

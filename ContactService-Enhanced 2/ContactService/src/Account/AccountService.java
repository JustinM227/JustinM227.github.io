package Account;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds all the users
 *
 * @author Justin Myers
 * 1/23/2026
 */
public class AccountService {
    private final Map<String, User> users = new HashMap<>();

    // Add user to the system
    public void register(String username, String password) {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        users.put(username, new User(username, password));
    }

    // Attempts to login with given information
    public boolean login(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }
}
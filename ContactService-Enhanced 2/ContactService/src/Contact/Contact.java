package Contact;

/**
 * Holds information for a single contact.
 *
 * @author Justin Myers
 * 1/23/2026
 */
public class Contact {

    // Variables
    private final int contactId;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    // Constructor with error handling.
    public Contact(int contactId, String firstName, String lastName, String phone, String address) {
        // Checks if the arguments are the right size and type.
        if (contactId <= 0) {
            throw new IllegalArgumentException("Invalid Contact.Contact ID");
        }
        if (firstName == null || firstName.length() > 30) {
            throw new IllegalArgumentException("Invalid First Name. Must be 30 or less characters");
        }
        if (lastName == null || lastName.length() > 30) {
            throw new IllegalArgumentException("Invalid Last Name. Must be 30 or less characters");
        }                                   // Makes sure its 10 numbers
        if (phone == null || !phone.matches("\\d{10}")) {
            throw new IllegalArgumentException("Invalid Phone Number. Must be exactly 10 digits");
        }
        if (address == null || address.length() > 100) {
            throw new IllegalArgumentException("Invalid Address. Must be 100 or less characters");
        }

        // Assigns values
        this.contactId = contactId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }

    // Gets contactID
    public int getContactId() {
        return contactId;
    }

    // Gets first name
    public String getFirstName() {
        return firstName;
    }

    // Sets first name
    public void setFirstName(String firstName) {
        if (firstName == null || firstName.length() > 30) {
            throw new IllegalArgumentException("Invalid First Name. Must be 30 or less characters");
        }
        this.firstName = firstName;
    }

    // Gets last name
    public String getLastName() {
        return lastName;
    }

    // Set last name
    public void setLastName(String lastName) {
        if (lastName == null || lastName.length() > 30) {
            throw new IllegalArgumentException("Invalid Last Name. Must be 30 or less characters");
        }
        this.lastName = lastName;
    }

    // Gets phone number
    public String getPhone() {
        return phone;
    }

    // Sets phone number
    public void setPhone(String phone) {
        if (phone == null || !phone.matches("\\d{10}")) {
            throw new IllegalArgumentException("Invalid Phone Number. Must be exactly 10 digits");
        }
        this.phone = phone;
    }

    // Gets address
    public String getAddress() {
        return address;
    }

    // Sets address
    public void setAddress(String address) {
        if (address == null || address.length() > 100) {
            throw new IllegalArgumentException("Invalid Address. Must be 100 or less characters");
        }
        this.address = address;
    }

    // Method to display everything as string
    @Override
    public String toString() {
        return contactId + " - " + firstName + " " + lastName;
    }
}

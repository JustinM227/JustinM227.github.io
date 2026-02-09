package Contact;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds a collection of contacts.
 *
 * @author Justin Myers
 * 1/23/2026
 */
public class ContactService {

    private final Map<Integer, Contact> contacts = new HashMap<Integer, Contact>(); // Holds contacts
    private int nextId = 1; // Used for the automatic ID

    // Adds a contact -- ID increments only if it was added.
    public Contact addContact(String firstName, String lastName, String phone, String address) {

        // Validate by attempting to create contact
        int tempId = nextId;
        Contact contact = new Contact(tempId, firstName, lastName, phone, address);

        // increment ID after validation is successful
        contacts.put(tempId, contact);
        nextId++;

        return contact;
    }

    // Removes a contact
    public void deleteContact(int contactId) {
        if (!contacts.containsKey(contactId)) {
            throw new IllegalArgumentException("Contact.Contact ID not found");
        }
        contacts.remove(contactId);
    }

    // Updates an existing contact
    public void updateContact(int contactId, String firstName, String lastName, String phone, String address) {

        if (!contacts.containsKey(contactId)) {
            throw new IllegalArgumentException("Contact.Contact ID not found");
        }

        Contact contact = contacts.get(contactId);

        // Checks if variables are correct.
        if (firstName != null && !firstName.isBlank()) contact.setFirstName(firstName);
        if (lastName != null && !lastName.isBlank()) contact.setLastName(lastName);
        if (phone != null && !phone.isBlank()) contact.setPhone(phone);
        if (address != null && !address.isBlank()) contact.setAddress(address);
    }

    // Returns the specified contact
    public Contact getContact(int contactId) {
        return contacts.get(contactId);
    }

    // Checks if ID exists
    public boolean idExists(int contactId) {
        return contacts.containsKey(contactId);
    }
}

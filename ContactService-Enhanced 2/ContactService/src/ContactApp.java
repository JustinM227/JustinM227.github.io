import Account.AccountService;
import Contact.Contact;
import Contact.ContactService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Random;

/**
 * GUI for contact manager
 *
 * @author Justin Myers
 * 1/30/2026
 */
public class ContactApp extends Application {

    private final AccountService accountService = new AccountService();
    private final ContactService contactService = new ContactService();
    private final ObservableList<Contact> contactList = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Contact Manager");
        showLogin(stage);
        stage.show();
    }

    // Login screen
    private void showLogin(Stage stage) {

        // Title
        Label title = new Label("Contact Manager");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        // Username and Password
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        usernameField.setPromptText("Username");
        passwordField.setPromptText("Password");

        // Login and sign up buttons
        Button loginButton = new Button("Login");
        Button signupButton = new Button("Create Account");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        signupButton.setMaxWidth(Double.MAX_VALUE);

        // Card background
        VBox card = new VBox(16,
                title,
                usernameField,
                passwordField,
                loginButton,
                signupButton
        );

        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(35));
        card.setMaxWidth(320);
        card.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 12;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 12, 0, 0, 6);
        """);

        VBox root = new VBox(card);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(80, 20, 20, 20));
        root.setStyle("-fx-background-color: #f4f6f8;");

        // Login button action event
        loginButton.setOnAction(e -> {
            if (accountService.login(usernameField.getText(), passwordField.getText())) {
                showContactManager(stage);
            } else {
                showErrorAlert("Invalid username or password");
            }
        });

        // Sign up button action event
        signupButton.setOnAction(e -> {
            try {
                accountService.register(usernameField.getText(), passwordField.getText());
                showGoodAlert("Account created! You can now log in.");
            } catch (Exception ex) {
                showGoodAlert(ex.getMessage());
            }
        });

        stage.setScene(new Scene(root, 420, 500));
    }

    // Contact.Contact manager screen
    private void showContactManager(Stage stage) {

        // ID field
        TextField idField = new TextField();
        idField.setPromptText("Auto ID");
        idField.setDisable(true);

        // Other variable fields
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();

        // Placeholder text
        firstNameField.setPromptText("First Name");
        lastNameField.setPromptText("Last Name");
        phoneField.setPromptText("Phone (10 digits)");
        addressField.setPromptText("Address");

        // Add, Update, and Delete buttons
        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");

        HBox row1 = new HBox(10, idField, firstNameField, lastNameField);
        HBox row2 = new HBox(10, phoneField, addressField);
        HBox row3 = new HBox(10, addButton, updateButton, deleteButton);

        // Card for buttons and text fields
        VBox form = new VBox(12, row1, row2, row3);
        form.setPadding(new Insets(20));
        form.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 10;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 4);
        """);

        // Table customization
        TableView<Contact> table = new TableView<>(contactList);

        TableColumn<Contact, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));

        TableColumn<Contact, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Contact, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Contact, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<Contact, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        table.getColumns().addAll(idCol, firstNameCol, lastNameCol, phoneCol, addressCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        // Adds lists needed for filtering and sorting
        FilteredList<Contact> filteredList = new FilteredList<>(contactList, p -> true);
        SortedList<Contact> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);

        // Card for the table
        VBox tableCard = new VBox(table);
        tableCard.setPadding(new Insets(10));
        tableCard.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 10;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 8, 0, 0, 4);
        """);

        // Add a search bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search");

        // Add a filter box
        ComboBox<String> filterBox = new ComboBox<>();
        filterBox.getItems().addAll("All", "First Name", "Last Name", "Phone", "Address");
        filterBox.setValue("All");

        // Add sort buttons
        Button sortIdButton = new Button("Sort ID");
        Button sortFirstNameButton = new Button("Sort First Name");
        Button sortLastButton = new Button("Sort Last Name");

        // Add action events to sort buttons
        sortIdButton.setOnAction(e -> table.getSortOrder().setAll(idCol));
        sortFirstNameButton.setOnAction(e -> table.getSortOrder().setAll(firstNameCol));
        sortLastButton.setOnAction(e -> table.getSortOrder().setAll(lastNameCol));

        // Adds listener to search fields
        searchField.textProperty().addListener((obs, o, n) -> {
            String input = n.toLowerCase();
            filteredList.setPredicate(contact -> {
                if (input.isBlank()) return true;
                return switch (filterBox.getValue()) {
                    case "First Name" -> contact.getFirstName().toLowerCase().contains(input);
                    case "Last Name" -> contact.getLastName().toLowerCase().contains(input);
                    case "Phone" -> contact.getPhone().contains(input);
                    case "Address" -> contact.getAddress().toLowerCase().contains(input);
                    default -> contact.getFirstName().toLowerCase().contains(input)
                            || contact.getLastName().toLowerCase().contains(input)
                            || contact.getPhone().contains(input)
                            || contact.getAddress().toLowerCase().contains(input);
                };
            });
        });

        // Add listener to filterbox
        filterBox.valueProperty().addListener((obs, o, n) ->
                searchField.setText(searchField.getText())
        );

        // Adds all the sort, filter (search) to screen
        HBox filterBar = new HBox(10, filterBox, searchField, sortIdButton, sortFirstNameButton, sortLastButton);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        // Background customization
        VBox root = new VBox(15, form, filterBar, tableCard);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #f4f6f8;");

        // Add button action event
        addButton.setOnAction(e -> {
            if (!validateFields(firstNameField, lastNameField, phoneField, addressField)) return;
            Contact c = contactService.addContact(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    phoneField.getText(),
                    addressField.getText()
            );
            contactList.add(c);
            clearFields(firstNameField, lastNameField, phoneField, addressField);
        });

        // Update button action event
        updateButton.setOnAction(e -> {
            Contact selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            if (!validateFields(firstNameField, lastNameField, phoneField, addressField)) return;
            contactService.updateContact(
                    selected.getContactId(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    phoneField.getText(),
                    addressField.getText()
            );
            table.refresh();
        });

        // Delete button action event
        deleteButton.setOnAction(e -> {
            Contact selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            contactService.deleteContact(selected.getContactId());
            contactList.remove(selected);
        });

        // Listens for if the user clicks in the table on a contact. If they do fill out the fields above.
        table.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            if (n != null) {
                idField.setText(String.valueOf(n.getContactId()));
                firstNameField.setText(n.getFirstName());
                lastNameField.setText(n.getLastName());
                phoneField.setText(n.getPhone());
                addressField.setText(n.getAddress());
            }
        });

        seedTestContacts();

        stage.setScene(new Scene(root, 900, 550));
    }

    // ------- Helper methods --------- //
    // Validates the fields and highlights ones that are wrong.
    private boolean validateFields(TextField firstName, TextField lastName, TextField phone, TextField address) {
        clearStyles(firstName, lastName, phone, address);
        boolean ok = true;

        if (firstName.getText().isBlank() || firstName.getText().length() > 10) { firstName.setStyle("-fx-border-color:red;"); ok = false; }
        if (lastName.getText().isBlank() || lastName.getText().length() > 10) { lastName.setStyle("-fx-border-color:red;"); ok = false; }
        if (!phone.getText().matches("\\d{10}")) { phone.setStyle("-fx-border-color:red;"); ok = false; }
        if (address.getText().isBlank() || address.getText().length() > 30) { address.setStyle("-fx-border-color:red;"); ok = false; }

        if (!ok) showErrorAlert("Fix highlighted fields");
        return ok;
    }

    // Clears all fields
    private void clearFields(TextField... fields) {
        for (TextField f : fields) f.clear();
    }

    // Clears all styles. Like the highlights
    private void clearStyles(TextField... fields) {
        for (TextField f : fields) f.setStyle("");
    }

    // Shows bad message
    private void showErrorAlert(String message) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }

    // Shows good message
    private void showGoodAlert(String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }

    // Creates 50 fake contacts for testing
    private void seedTestContacts() {

        // Prevent duplicates
        if (!contactList.isEmpty()) return;

        // Test names
        String[] firstNames = {
                "James","Emma","Liam","Olivia","Noah","Ava","Ethan","Sophia","Lucas","Mia"
        };

        // Test name
        String[] lastNames = {
                "Smith","Johnson","Brown","Taylor","Anderson",
                "Thomas","Jackson","White","Harris","Martin"
        };

        // Test streets
        String[] streets = {
                "Main St","Oak Ave","Pine Rd","Maple Dr",
                "Cedar Ln","Elm St","Washington Blvd"
        };

        Random rand = new Random();

        // Populates table
        for (int i = 1; i <= 50; i++) {

            String firstName = firstNames[rand.nextInt(firstNames.length)];
            String lastName = lastNames[rand.nextInt(lastNames.length)];

            String phone = String.format("%010d", rand.nextInt(1_000_000_000));

            String address =
                    (100 + rand.nextInt(900)) + " " +
                            streets[rand.nextInt(streets.length)];

            Contact contact = contactService.addContact(
                    firstName,
                    lastName,
                    phone,
                    address
            );

            contactList.add(contact);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

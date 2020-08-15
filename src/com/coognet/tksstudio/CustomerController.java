package com.coognet.tksstudio;

import com.coognet.tksstudio.entities.ContractorPurchaseOrder;
import com.coognet.tksstudio.entities.Customer;
import com.coognet.tksstudio.entities.QueryObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerController extends QueryObject implements Initializable {

    @FXML
    private TableColumn<Customer, Integer> iDColumn;
    @FXML
    private TableColumn<Customer, String> firstNameColumn;
    @FXML
    private TableColumn<Customer, String> lastNameColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;
    @FXML
    private TableView<Customer> customerTableView;

    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField addressTextField;

    private ObservableList<Customer> customerObservableList;
    private Customer selectedCustomer;

    private Alert successAlert;
    private Alert failureAlert;
    private Alert confirmationAlert;
    Optional<ButtonType> result;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        failureAlert = new Alert(Alert.AlertType.ERROR);
        failureAlert.setTitle("Failure");
        failureAlert.setHeaderText(null);
        confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to perform this operation?");

        // Loads the data into the TableView
        loadData();

        // When a row is selected, populates the text fields with the information from the row.
        customerTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                //populate the textfields with the info for edit
                selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
                firstNameTextField.setText(selectedCustomer.getCustFirstName());
                lastNameTextField.setText(selectedCustomer.getCustLastName());
                addressTextField.setText(selectedCustomer.getCustAddress());
            }
        });

        iDColumn.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("custID"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("custFirstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("custLastName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Customer, String>("custAddress"));

    }

    public void mainMenuPushed(ActionEvent event) throws IOException {
        loadScene(event, "MainMenu.fxml");
    }

    public void customerInvoicePushed(ActionEvent event) throws IOException {
        loadScene(event, "CustomerInvoice.fxml");
    }

    public void projectPushed(ActionEvent event) throws IOException {
        loadScene(event, "Project.fxml");
    }

    public void addButtonPushed() {
        String tempFirstName = firstNameTextField.getText();
        String tempLastName = lastNameTextField.getText();
        String tempAddress = addressTextField.getText();

        Customer temp = new Customer(-1, tempFirstName, tempLastName, tempAddress);

        result = confirmationAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (temp.addAccount(tempFirstName, tempLastName, tempAddress)) {

                firstNameTextField.setText("");
                lastNameTextField.setText("");
                addressTextField.setText("");

                loadData();

                successAlert.setContentText("Record successfully added");
                successAlert.showAndWait();
            } else {
                failureAlert.setContentText("There was an error adding the record to the database.");
                failureAlert.showAndWait();
            }
        }
    }

    public void deleteButtonPushed() {
        selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedCustomer.deleteAccount()) {
                    loadData();

                    firstNameTextField.setText("");
                    lastNameTextField.setText("");
                    addressTextField.setText("");

                    successAlert.setContentText("Record successfully deleted");
                    successAlert.showAndWait();
                } else {
                    failureAlert.setContentText("There was an error deleting the record from the database.");
                    failureAlert.showAndWait();
                }
            }
        } else {
            failureAlert.setContentText("Please select a valid architectural review.");
            failureAlert.showAndWait();
        }
    }

    public void editButtonPushed() {
        if (customerTableView.getSelectionModel().getSelectedIndex() != -1) {
            String tempFirstName = firstNameTextField.getText();
            String tempLastName = lastNameTextField.getText();
            String tempAddress = addressTextField.getText();

            selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedCustomer != null) {
                    if (selectedCustomer.editAccount(tempFirstName, tempLastName, tempAddress)) {

                        firstNameTextField.setText("");
                        lastNameTextField.setText("");
                        addressTextField.setText("");

                        loadData();

                        successAlert.setContentText("Record successfully updated");
                        successAlert.showAndWait();
                    } else {
                        failureAlert.setContentText("There was an error updating the record from the database.");
                        failureAlert.showAndWait();
                    }
                } else {
                    failureAlert.setContentText("Please select a valid record to update.");
                    failureAlert.showAndWait();
                }
            }
        } else {
            failureAlert.setContentText("Please select a valid record to update.");
            failureAlert.showAndWait();
        }
    }

    public void loadData() {
        // Load the Architectural Review data to the table view

        statement = "SELECT Cust_ID, Cust_FirstName, Cust_LastName, Cust_Address FROM Customer";
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(connectionString);
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                customerObservableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    Customer customer = new Customer(resultSet.getInt("Cust_ID"), resultSet.getString("Cust_FirstName"), resultSet.getString("Cust_LastName"), resultSet.getString("Cust_Address"));
                    customerObservableList.add(customer);
                }
                if (!customerObservableList.isEmpty())
                    customerTableView.setItems(customerObservableList);
                else
                    customerTableView.getItems().clear();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) { /* ignored */}
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) { /* ignored */}
            }
        }
    }

    public void loadScene(ActionEvent event, String file) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(file));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}

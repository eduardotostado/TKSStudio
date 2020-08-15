package com.coognet.tksstudio;

import com.coognet.tksstudio.entities.CustomerInvoice;
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

public class CustomerInvoiceController  extends QueryObject implements Initializable {

    @FXML
    private TableColumn<CustomerInvoice, Integer> custIDColumn;
    @FXML
    private TableColumn<CustomerInvoice, Integer> jobIDColumn;
    @FXML
    private TableColumn<CustomerInvoice, BigDecimal> amountColumn;
    @FXML
    private TableView<CustomerInvoice> customerInvoiceTableView;

    @FXML
    private TextField customerIDTextField;
    @FXML
    private TextField jobIDTextField;
    @FXML
    private TextField amountTextField;

    private ObservableList<CustomerInvoice> customerInvoiceObservableList;
    private CustomerInvoice selectedCustomerInvoice;

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
        customerInvoiceTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                //populate the textfields with the info for edit
                selectedCustomerInvoice = customerInvoiceTableView.getSelectionModel().getSelectedItem();
                customerIDTextField.setText(Integer.toString(selectedCustomerInvoice.getCustID()));
                jobIDTextField.setText(Integer.toString(selectedCustomerInvoice.getJobID()));
                amountTextField.setText(selectedCustomerInvoice.getInvoiceAmt().toString());
            }
        });

        custIDColumn.setCellValueFactory(new PropertyValueFactory<CustomerInvoice, Integer>("custID"));
        jobIDColumn.setCellValueFactory(new PropertyValueFactory<CustomerInvoice, Integer>("jobID"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<CustomerInvoice, BigDecimal>("invoiceAmt"));


    }

    public void mainMenuPushed(ActionEvent event) throws IOException {
        loadScene(event, "MainMenu.fxml");
    }

    public void backPushed(ActionEvent event) throws IOException {
        loadScene(event, "Customer.fxml");
    }

    public void addButtonPushed() {
        int tempJobID = Integer.parseInt(customerIDTextField.getText());
        int tempCustID = Integer.parseInt(jobIDTextField.getText());
        BigDecimal tempAmt;

        if (!amountTextField.getText().equals("")) {
            tempAmt = new BigDecimal(amountTextField.getText());
        } else {
            tempAmt = new BigDecimal("0");
        }

        CustomerInvoice temp = new CustomerInvoice(tempJobID, tempCustID, tempAmt);

        result = confirmationAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (temp.addAccount(tempJobID, tempCustID, tempAmt)) {

                jobIDTextField.setText("");
                customerIDTextField.setText("");
                amountTextField.setText("");

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
        selectedCustomerInvoice = customerInvoiceTableView.getSelectionModel().getSelectedItem();

        if (selectedCustomerInvoice != null) {
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedCustomerInvoice.deleteAccount()) {
                    loadData();

                    jobIDTextField.setText("");
                    customerIDTextField.setText("");
                    amountTextField.setText("");

                    successAlert.setContentText("Record successfully deleted");
                    successAlert.showAndWait();
                } else {
                    failureAlert.setContentText("There was an error deleting the record from the database.");
                    failureAlert.showAndWait();
                }
            }
        } else {
            failureAlert.setContentText("Please select a valid record.");
            failureAlert.showAndWait();
        }
    }

    public void editButtonPushed() {
        if (customerInvoiceTableView.getSelectionModel().getSelectedIndex() != -1) {
            int tempJobID = Integer.parseInt(customerIDTextField.getText());
            int tempCustID = Integer.parseInt(jobIDTextField.getText());
            BigDecimal tempAmt;

            if (!amountTextField.getText().equals("")) {
                tempAmt = new BigDecimal(amountTextField.getText());
            } else {
                tempAmt = new BigDecimal("0");
            }

            selectedCustomerInvoice = customerInvoiceTableView.getSelectionModel().getSelectedItem();
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedCustomerInvoice != null) {
                    if (selectedCustomerInvoice.editAccount(tempJobID, tempCustID, tempAmt)) {

                        jobIDTextField.setText("");
                        customerIDTextField.setText("");
                        amountTextField.setText("");

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

        statement = "SELECT Cust_ID, Job_ID, Invoice_amt FROM Customer_Invoice";
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(connectionString);
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                customerInvoiceObservableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    CustomerInvoice temp = new CustomerInvoice(resultSet.getInt("Cust_ID"), resultSet.getInt("Job_ID"), resultSet.getBigDecimal("Invoice_amt"));
                    customerInvoiceObservableList.add(temp);
                }
                if (!customerInvoiceObservableList.isEmpty())
                    customerInvoiceTableView.setItems(customerInvoiceObservableList);
                else
                    customerInvoiceTableView.getItems().clear();
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
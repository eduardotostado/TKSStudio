package com.coognet.tksstudio;

import com.coognet.tksstudio.entities.QueryObject;
import com.coognet.tksstudio.entities.Account;
import com.coognet.tksstudio.entities.Department;
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
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class AccountController extends QueryObject implements Initializable {

    @FXML private TableColumn<Account, String> accountNameColumn;
    @FXML private TableColumn<Account, String> departmentNameColumn;
    @FXML private TableColumn<Account, String> accountTypeColumn;
    @FXML private TableView<Account> accountTableView;
    @FXML private ComboBox<Department> departmentComboBox;
    @FXML private TextField nameTextField;
    @FXML private TextField typeTextField;

    private ObservableList<Account> accountsObservableList;
    private ObservableList<Department> departmentObservableList;
    private Account selectedAccount;

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
        accountTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                //populate the textfields with the info for edit
                selectedAccount = accountTableView.getSelectionModel().getSelectedItem();
                nameTextField.setText(selectedAccount.getAccountName());
                typeTextField.setText(selectedAccount.getAccountType());

                for(Department department : departmentObservableList) {
                    if (selectedAccount.getDeptID() == department.getDeptID()){
                        departmentComboBox.getSelectionModel().select(department);
                    }
                }
            }
        });

         accountNameColumn.setCellValueFactory(new PropertyValueFactory<Account, String>("accountName"));
         departmentNameColumn.setCellValueFactory(new PropertyValueFactory<Account, String>("deptName"));
         accountTypeColumn.setCellValueFactory(new PropertyValueFactory<Account, String>("accountType"));
    }

    public void mainMenuPushed(ActionEvent event) throws IOException {
        loadScene(event, "MainMenu.fxml");
    }

    public void accountsPayablePushed(ActionEvent event) throws IOException {
        loadScene(event, "AccountsPayable.fxml");
    }

    public void addButtonPushed(){
        if(departmentComboBox.getSelectionModel().getSelectedIndex() != -1) {
            int tempDepartmentID = departmentComboBox.getSelectionModel().getSelectedItem().getDeptID();
            String tempDepartmentName = departmentComboBox.getSelectionModel().getSelectedItem().getDeptName();
            String tempAccountName = nameTextField.getText();
            String tempAccountType = typeTextField.getText();
            Account temp = new Account(-1, tempDepartmentID, tempDepartmentName,  tempAccountName, tempAccountType);

            result = confirmationAlert.showAndWait();
            if(result.get() == ButtonType.OK) {
                if (temp.addAccount(tempDepartmentID, tempAccountName, tempAccountType)) {


                    departmentComboBox.getSelectionModel().clearSelection();
                    nameTextField.setText("");
                    typeTextField.setText("");
                    loadData();

                    successAlert.setContentText("Record successfully added");
                    successAlert.showAndWait();
                } else {
                    failureAlert.setContentText("There was an error adding the record to the database.");
                    failureAlert.showAndWait();
                }
            }
        } else {
            failureAlert.setContentText("Please select a department.");
            failureAlert.showAndWait();
        }

    }

    public void deleteButtonPushed(){
        selectedAccount = accountTableView.getSelectionModel().getSelectedItem();

        if(selectedAccount != null) {
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedAccount.deleteAccount()) {
                    loadData();

                    nameTextField.setText("");
                    typeTextField.setText("");

                    successAlert.setContentText("Record successfully deleted");
                    successAlert.showAndWait();
                } else {
                    failureAlert.setContentText("There was an error deleting the record from the database.");
                    failureAlert.showAndWait();
                }
            }
        } else {
            failureAlert.setContentText("Please select a valid account.");
            failureAlert.showAndWait();
        }
    }

    public void editButtonPushed(){
        if(accountTableView.getSelectionModel().getSelectedIndex() != -1) {
            int tempDepartmentID = departmentComboBox.getSelectionModel().getSelectedItem().getDeptID();

            String tempAccountName = nameTextField.getText();
            String tempAccountType = typeTextField.getText();

            selectedAccount = accountTableView.getSelectionModel().getSelectedItem();

            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedAccount != null) {
                    if (selectedAccount.editAccount(tempDepartmentID, tempAccountName, tempAccountType)) {
                        departmentComboBox.getSelectionModel().clearSelection();
                        nameTextField.setText("");
                        typeTextField.setText("");
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
            failureAlert.setContentText("Please select a valid account.");
            failureAlert.showAndWait();
        }
    }

    public void loadData(){
        // Load the Account data to the table view

        statement = "SELECT Account_ID, Account.Dept_ID, Dept_Name, Account_Name, Account_Type FROM Account JOIN Department on Account.Dept_ID = Department.Dept_ID\n";
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(connectionString);
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                accountsObservableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    Account account = new Account(resultSet.getInt("Account_ID"), resultSet.getInt("Dept_ID"), resultSet.getString("Dept_Name"), resultSet.getString("Account_Name"), resultSet.getString("Account_type"));
                    accountsObservableList.add(account);
                }
                if (!accountsObservableList.isEmpty())
                    accountTableView.setItems(accountsObservableList);
                else
                    accountTableView.getItems().clear();
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

        // Load the Department data to the Combo Box
        statement = "SELECT Dept_ID, Dept_Name FROM Department";
        resultSet = null;
        try {
            conn = DriverManager.getConnection(connectionString);
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                departmentObservableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    Department department = new Department(resultSet.getInt("Dept_ID"), resultSet.getString("Dept_Name"));
                    departmentObservableList.add(department);
                }
                if (!departmentObservableList.isEmpty())
                    departmentComboBox.setItems(departmentObservableList);
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

    public void loadScene(ActionEvent event, String file)throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(file));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}

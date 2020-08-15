package com.coognet.tksstudio;

import com.coognet.tksstudio.entities.Account;
import com.coognet.tksstudio.entities.AccountsPayable;
import com.coognet.tksstudio.entities.Department;
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

public class AccountsPayableController extends QueryObject implements Initializable {

    @FXML private TableColumn<Account, String> accountNameColumn;
    @FXML private TableColumn<AccountsPayable, BigDecimal> balanceColumn;
    @FXML private TableColumn<AccountsPayable, String> dueDateColumn;
    @FXML private TableView<AccountsPayable> accountsPayableTableView;
    @FXML private ComboBox<Account> accountComboBox;
    @FXML private TextField balanceTextField;
    @FXML private TextField dueDateTextField;

    private ObservableList<AccountsPayable> accountsPayableObservableList;
    private ObservableList<Account> accountObservableList;
    private AccountsPayable selectedAccountsPayable;

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
        accountsPayableTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                //populate the textfields with the info for edit
                selectedAccountsPayable = accountsPayableTableView.getSelectionModel().getSelectedItem();
                balanceTextField.setText(selectedAccountsPayable.getBalance().toString());
                dueDateTextField.setText(selectedAccountsPayable.getDueDate());

                for(Account account : accountObservableList) {
                    if (selectedAccountsPayable.getAccountID() == account.getAccountID()){
                        accountComboBox.getSelectionModel().select(account);
                    }
                }
            }
        });

        accountNameColumn.setCellValueFactory(new PropertyValueFactory<Account, String>("accountName"));
        balanceColumn.setCellValueFactory(new PropertyValueFactory<AccountsPayable, BigDecimal>("balance"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<AccountsPayable, String>("dueDate"));
    }

    public void mainMenuPushed(ActionEvent event) throws IOException {
        loadScene(event, "MainMenu.fxml");
    }

    public void backPushed(ActionEvent event) throws IOException {
        loadScene(event, "Account.fxml");
    }

    public void addButtonPushed(){
        BigDecimal tempBalance;

        if(accountComboBox.getSelectionModel().getSelectedIndex() != -1) {
            int tempAccountID = accountComboBox.getSelectionModel().getSelectedItem().getAccountID();
            String tempAccountName = accountComboBox.getSelectionModel().getSelectedItem().getDeptName();
            if(!balanceTextField.getText().equals("")) {
                 tempBalance = new BigDecimal(balanceTextField.getText());
            } else {
                tempBalance = new BigDecimal("0");
            }
            String tempDueDate = dueDateTextField.getText();
            AccountsPayable temp = new AccountsPayable(-1, tempAccountID, tempAccountName,  tempBalance, tempDueDate);

            result = confirmationAlert.showAndWait();
            if(result.get() == ButtonType.OK) {
                if (temp.addAccount(tempAccountID, tempBalance, tempDueDate)) {
                    accountComboBox.getSelectionModel().clearSelection();
                    balanceTextField.setText("");
                    dueDateTextField.setText("");
                    loadData();

                    successAlert.setContentText("Record successfully added");
                    successAlert.showAndWait();
                } else {
                    failureAlert.setContentText("There was an error adding the record to the database.");
                    failureAlert.showAndWait();
                }
            }
        } else {
            failureAlert.setContentText("Please select a valid account.");
            failureAlert.showAndWait();
        }
    }

    public void deleteButtonPushed(){
        selectedAccountsPayable = accountsPayableTableView.getSelectionModel().getSelectedItem();

        if(selectedAccountsPayable != null) {
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedAccountsPayable.deleteAccount()) {
                    loadData();

                    balanceTextField.setText("");
                    dueDateTextField.setText("");

                    successAlert.setContentText("Record successfully deleted");
                    successAlert.showAndWait();
                } else {
                    failureAlert.setContentText("There was an error deleting the record from the database.");
                    failureAlert.showAndWait();
                }
            }
        } else {
            failureAlert.setContentText("Please select a valid accounts payable record.");
            failureAlert.showAndWait();
        }
    }

    public void editButtonPushed(){
        if(accountsPayableTableView.getSelectionModel().getSelectedIndex() != -1) {
            int tempAccountID = accountComboBox.getSelectionModel().getSelectedItem().getAccountID();
            BigDecimal tempBalance = new BigDecimal(balanceTextField.getText());
            String tempDueDate = dueDateTextField.getText();
            selectedAccountsPayable = accountsPayableTableView.getSelectionModel().getSelectedItem();
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedAccountsPayable != null) {
                    if (selectedAccountsPayable.editAccount(tempAccountID, tempBalance, tempDueDate)) {
                        accountComboBox.getSelectionModel().clearSelection();
                        balanceTextField.setText("");
                        dueDateTextField.setText("");
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
        // Load the AccountPayable data to the table view

        statement = "SELECT AccountsPayable_ID, Account.Account_ID, Account_Name, Balance, DueDate FROM Accounts_Payable JOIN Account on Accounts_Payable.Account_ID = Account.Account_ID\n";
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(connectionString);
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                accountsPayableObservableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    AccountsPayable accountsPayable = new AccountsPayable(resultSet.getInt("AccountsPayable_ID"), resultSet.getInt("Account_ID"), resultSet.getString("Account_Name"), resultSet.getBigDecimal("Balance"), resultSet.getString("DueDate"));
                    accountsPayableObservableList.add(accountsPayable);
                }
                if (!accountsPayableObservableList.isEmpty())
                    accountsPayableTableView.setItems(accountsPayableObservableList);
                else
                    accountsPayableTableView.getItems().clear();
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

        // Load the Acocunt data to the Combo Box
        statement = "SELECT Account_ID, Account_Name FROM Account";
        resultSet = null;
        try {
            conn = DriverManager.getConnection(connectionString);
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                accountObservableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    Account account = new Account(resultSet.getInt("Account_ID"), resultSet.getString("Account_Name"));
                    accountObservableList.add(account);
                }
                if (!accountObservableList.isEmpty())
                    accountComboBox.setItems(accountObservableList);

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

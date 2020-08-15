package com.coognet.tksstudio;

import com.coognet.tksstudio.entities.ContractorPurchaseOrder;
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

public class ContractorPurchaseOrderController extends QueryObject implements Initializable {

    @FXML
    private TableColumn<ContractorPurchaseOrder, Integer> cPOIDColumn;
    @FXML
    private TableColumn<ContractorPurchaseOrder, Integer> contIDColumn;
    @FXML
    private TableColumn<ContractorPurchaseOrder, Integer> apIDColumn;
    @FXML
    private TableColumn<ContractorPurchaseOrder, BigDecimal> cPOTotalColumn;
    @FXML
    private TableColumn<ContractorPurchaseOrder, String> cPODateColumn;
    @FXML
    private TableView<ContractorPurchaseOrder> contractorPurchaseOrderTableView;

    @FXML
    private TextField cIDTextField;
    @FXML
    private TextField totalTextField;
    @FXML
    private TextField apIDTextField;
    @FXML
    private TextField dateTextField;

    private ObservableList<ContractorPurchaseOrder> contractorPurchaseOrderObservableList;
    private ContractorPurchaseOrder selectedContractorPurchaseOrder;

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
        contractorPurchaseOrderTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                //populate the textfields with the info for edit
                selectedContractorPurchaseOrder = contractorPurchaseOrderTableView.getSelectionModel().getSelectedItem();
                cIDTextField.setText(Integer.toString(selectedContractorPurchaseOrder.getContID()));
                apIDTextField.setText(Integer.toString(selectedContractorPurchaseOrder.getAccountsPayableID()));
                totalTextField.setText(selectedContractorPurchaseOrder.getCPOTotal().toString());
                dateTextField.setText(selectedContractorPurchaseOrder.getCPODate());
            }
        });

        cPOIDColumn.setCellValueFactory(new PropertyValueFactory<ContractorPurchaseOrder, Integer>("cPOID"));
        contIDColumn.setCellValueFactory(new PropertyValueFactory<ContractorPurchaseOrder, Integer>("contID"));
        apIDColumn.setCellValueFactory(new PropertyValueFactory<ContractorPurchaseOrder, Integer>("accountsPayableID"));
        cPOTotalColumn.setCellValueFactory(new PropertyValueFactory<ContractorPurchaseOrder, BigDecimal>("cPOTotal"));
        cPODateColumn.setCellValueFactory(new PropertyValueFactory<ContractorPurchaseOrder, String>("cPODate"));

    }

    public void mainMenuPushed(ActionEvent event) throws IOException {
        loadScene(event, "MainMenu.fxml");
    }

    public void backPushed(ActionEvent event) throws IOException {
        loadScene(event, "Contractor.fxml");
    }

    public void addButtonPushed() {
        int tempContID = Integer.parseInt(cIDTextField.getText());
        int tempAccountsPayableID = Integer.parseInt(apIDTextField.getText());
        BigDecimal tempcPOTotal;
        if (!totalTextField.getText().equals("")) {
            tempcPOTotal = new BigDecimal(totalTextField.getText());
        } else {
            tempcPOTotal = new BigDecimal("0");
        }
        String tempcPODate = dateTextField.getText();
        ContractorPurchaseOrder temp = new ContractorPurchaseOrder(-1, tempContID, tempAccountsPayableID, tempcPOTotal, tempcPODate);

        result = confirmationAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (temp.addAccount(tempContID, tempAccountsPayableID, tempcPOTotal, tempcPODate)) {

                cIDTextField.setText("");
                apIDTextField.setText("");
                totalTextField.setText("");
                dateTextField.setText("");

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
        selectedContractorPurchaseOrder = contractorPurchaseOrderTableView.getSelectionModel().getSelectedItem();

        if (selectedContractorPurchaseOrder != null) {
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedContractorPurchaseOrder.deleteAccount()) {
                    loadData();

                    cIDTextField.setText("");
                    apIDTextField.setText("");
                    totalTextField.setText("");
                    dateTextField.setText("");

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
        if (contractorPurchaseOrderTableView.getSelectionModel().getSelectedIndex() != -1) {
            int tempContID = Integer.parseInt(cIDTextField.getText());
            int tempAccountsPayableID = Integer.parseInt(apIDTextField.getText());
            BigDecimal tempcPOTotal;
            if (!totalTextField.getText().equals("")) {
                tempcPOTotal = new BigDecimal(totalTextField.getText());
            } else {
                tempcPOTotal = new BigDecimal("0");
            }
            String tempcPODate = dateTextField.getText();

            selectedContractorPurchaseOrder = contractorPurchaseOrderTableView.getSelectionModel().getSelectedItem();
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedContractorPurchaseOrder != null) {
                    if (selectedContractorPurchaseOrder.editAccount(tempContID, tempAccountsPayableID, tempcPOTotal, tempcPODate)) {

                        cIDTextField.setText("");
                        apIDTextField.setText("");
                        totalTextField.setText("");
                        dateTextField.setText("");

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

        statement = "SELECT CPO_ID, Cont_ID, AccountsPayable_ID, CPO_Total, CPO_Date FROM Contractor_Purchase_Order";
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(connectionString);
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                contractorPurchaseOrderObservableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    ContractorPurchaseOrder contractorPurchaseOrder = new ContractorPurchaseOrder(resultSet.getInt("CPO_ID"), resultSet.getInt("Cont_ID"), resultSet.getInt("AccountsPayable_ID"), resultSet.getBigDecimal("CPO_Total"), resultSet.getString("CPO_Date"));
                    contractorPurchaseOrderObservableList.add(contractorPurchaseOrder);
                }
                if (!contractorPurchaseOrderObservableList.isEmpty())
                    contractorPurchaseOrderTableView.setItems(contractorPurchaseOrderObservableList);
                else
                    contractorPurchaseOrderTableView.getItems().clear();
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

package com.coognet.tksstudio;

import com.coognet.tksstudio.entities.Contractor;
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

public class ContractorController extends QueryObject implements Initializable {

    @FXML private TableColumn<Contractor, String> contractorFirstNameColumn;
    @FXML private TableColumn<Contractor, String> contractorLastNameColumn;
    @FXML private TableColumn<Contractor, BigDecimal> contractorSalaryColumn;
    @FXML private TableView<Contractor> contractorTableView;

    @FXML private TextField firstNameTextfield;
    @FXML private TextField lastNameTextField;
    @FXML private TextField salaryTextField;

    private ObservableList<Contractor> contractorObservableList;
    private Contractor selectedContractor;

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
        contractorTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                //populate the textfields with the info for edit
                selectedContractor = contractorTableView.getSelectionModel().getSelectedItem();
                firstNameTextfield.setText(selectedContractor.getContFirstName());
                lastNameTextField.setText(selectedContractor.getContLastName());
                salaryTextField.setText(selectedContractor.getContSalary().toString());
            }
        });

        contractorFirstNameColumn.setCellValueFactory(new PropertyValueFactory<Contractor, String>("contFirstName"));
        contractorLastNameColumn.setCellValueFactory(new PropertyValueFactory<Contractor, String>("contLastName"));
        contractorSalaryColumn.setCellValueFactory(new PropertyValueFactory<Contractor, BigDecimal>("contSalary"));
    }

    public void mainMenuPushed(ActionEvent event) throws IOException {
        loadScene(event, "MainMenu.fxml");
    }

    public void contractorOrderLinePushed(ActionEvent event) throws IOException {
        loadScene(event, "ContractorOrderLine.fxml");
    }

    public void contractorPurchaseOrderPushed(ActionEvent event) throws IOException {
        loadScene(event, "ContractorPurchaseOrder.fxml");
    }

    public void addButtonPushed(){

            String tempFirstName = firstNameTextfield.getText();
            String tempLastName = lastNameTextField.getText();
            BigDecimal tempSalary;

            if(!salaryTextField.getText().equals("")) {
                tempSalary = new BigDecimal(salaryTextField.getText());
            } else {
                tempSalary = new BigDecimal("0");
            }
            Contractor temp = new Contractor(-1, tempFirstName, tempLastName,  tempSalary);

            result = confirmationAlert.showAndWait();
            if(result.get() == ButtonType.OK) {
                if (temp.addAccount(tempFirstName, tempLastName, tempSalary)) {

                    firstNameTextfield.setText("");
                    lastNameTextField.setText("");
                    salaryTextField.setText("");

                    loadData();

                    successAlert.setContentText("Record successfully added");
                    successAlert.showAndWait();
                } else {
                    failureAlert.setContentText("There was an error adding the record to the database.");
                    failureAlert.showAndWait();
                }
            }
    }

    public void deleteButtonPushed(){
        selectedContractor = contractorTableView.getSelectionModel().getSelectedItem();

        if(selectedContractor != null) {
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedContractor.deleteAccount()) {
                    loadData();

                    firstNameTextfield.setText("");
                    lastNameTextField.setText("");
                    salaryTextField.setText("");

                    successAlert.setContentText("Record successfully deleted");
                    successAlert.showAndWait();
                } else {
                    failureAlert.setContentText("There was an error deleting the record from the database.");
                    failureAlert.showAndWait();
                }
            }
        } else {
            failureAlert.setContentText("Please select a valid contractor.");
            failureAlert.showAndWait();
        }
    }

    public void editButtonPushed(){
        if(contractorTableView.getSelectionModel().getSelectedIndex() != -1) {

            String tempFirstName = firstNameTextfield.getText();
            String tempLastName = lastNameTextField.getText();
            BigDecimal tempSalary;

            if(!salaryTextField.getText().equals("")) {
                tempSalary = new BigDecimal(salaryTextField.getText());
            } else {
                tempSalary = new BigDecimal("0");
            }

            selectedContractor = contractorTableView.getSelectionModel().getSelectedItem();

            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedContractor != null) {
                    if (selectedContractor.editAccount(tempFirstName, tempLastName, tempSalary)) {

                        firstNameTextfield.setText("");
                        lastNameTextField.setText("");
                        salaryTextField.setText("");

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
            failureAlert.setContentText("Please select a valid contractor.");
            failureAlert.showAndWait();
        }
    }

    public void loadData(){
        // Load the Account data to the table view

        statement = "SELECT * FROM Contractor";
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(connectionString);
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                contractorObservableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    Contractor contractor = new Contractor(resultSet.getInt("Cont_ID"), resultSet.getString("Cont_FirstName"), resultSet.getString("Cont_LastName"), resultSet.getBigDecimal("Cont_Salary"));
                    contractorObservableList.add(contractor);
                }
                if (!contractorObservableList.isEmpty())
                    contractorTableView.setItems(contractorObservableList);
                else
                    contractorTableView.getItems().clear();
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

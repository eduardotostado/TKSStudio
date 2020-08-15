package com.coognet.tksstudio;

import com.coognet.tksstudio.entities.ContractorOrderLine;
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
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ContractorOrderLineController extends QueryObject implements Initializable {

    @FXML private TableColumn<ContractorOrderLine, Integer> cOLIDColumn;
    @FXML private TableColumn<ContractorOrderLine, Integer> cPOIDColumn;
    @FXML private TableColumn<ContractorOrderLine, Integer> materialSourceIDColumn;
    @FXML private TableColumn<ContractorOrderLine, Integer> cOLQuantityColumn;
    @FXML private TableView<ContractorOrderLine> contractorOrderLineTableView;

    @FXML private TextField cPOIDtextField;
    @FXML private TextField materialSourceIDTextField;
    @FXML private TextField cOLQuantityTextField;

    private ObservableList<ContractorOrderLine> contractorOrderLineObservableList;
    private ContractorOrderLine selectedContractorOrderLine;

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
        contractorOrderLineTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                //populate the textfields with the info for edit
                selectedContractorOrderLine = contractorOrderLineTableView.getSelectionModel().getSelectedItem();
                cPOIDtextField.setText(Integer.toString(selectedContractorOrderLine.getCPOID()));
                materialSourceIDTextField.setText(Integer.toString(selectedContractorOrderLine.getMaterialSourceID()));
                cOLQuantityTextField.setText(Integer.toString(selectedContractorOrderLine.getCOLQuantity()));
            }
        });

        cOLIDColumn.setCellValueFactory(new PropertyValueFactory<ContractorOrderLine, Integer>("cOLID"));
        cPOIDColumn.setCellValueFactory(new PropertyValueFactory<ContractorOrderLine, Integer>("cPOID"));
        materialSourceIDColumn.setCellValueFactory(new PropertyValueFactory<ContractorOrderLine, Integer>("materialSourceID"));
        cOLQuantityColumn.setCellValueFactory(new PropertyValueFactory<ContractorOrderLine, Integer>("cOLQuantity"));

    }

    public void mainMenuPushed(ActionEvent event) throws IOException {
        loadScene(event, "MainMenu.fxml");
    }

    public void backPushed(ActionEvent event) throws IOException {
        loadScene(event, "Contractor.fxml");
    }

    public void addButtonPushed(){
        int tempcPOID = Integer.parseInt(cPOIDtextField.getText());
        int tempMaterialSourceID = Integer.parseInt(materialSourceIDTextField.getText());
        int tempcOLQuantity = Integer.parseInt(cOLQuantityTextField.getText());
        ContractorOrderLine temp = new ContractorOrderLine(-1, tempcPOID, tempMaterialSourceID,  tempcOLQuantity);

        result = confirmationAlert.showAndWait();
        if(result.get() == ButtonType.OK) {
            if (temp.addAccount(tempcPOID, tempMaterialSourceID, tempcOLQuantity)) {

                cPOIDtextField.setText("");
                materialSourceIDTextField.setText("");
                cOLQuantityTextField.setText("");

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
        selectedContractorOrderLine = contractorOrderLineTableView.getSelectionModel().getSelectedItem();

        if(selectedContractorOrderLine != null) {
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedContractorOrderLine.deleteAccount()) {
                    loadData();

                    cPOIDtextField.setText("");
                    materialSourceIDTextField.setText("");
                    cOLQuantityTextField.setText("");

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

    public void editButtonPushed(){
        if(contractorOrderLineTableView.getSelectionModel().getSelectedIndex() != -1) {
            int tempcPOID = Integer.parseInt(cPOIDtextField.getText());
            int tempMaterialSourceID = Integer.parseInt(materialSourceIDTextField.getText());
            int tempcOLQuantity = Integer.parseInt(cOLQuantityTextField.getText());

            selectedContractorOrderLine = contractorOrderLineTableView.getSelectionModel().getSelectedItem();
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedContractorOrderLine != null) {
                    if (selectedContractorOrderLine.editAccount(tempcPOID, tempMaterialSourceID, tempcOLQuantity)) {

                        cPOIDtextField.setText("");
                        materialSourceIDTextField.setText("");
                        cOLQuantityTextField.setText("");

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

    public void loadData(){
        // Load the Architectural Review data to the table view

        statement = "SELECT COL_ID, CPO_ID, MaterialSource_ID, COL_Quantity FROM Contractor_Order_Line";
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(connectionString);
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                contractorOrderLineObservableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    ContractorOrderLine contractorOrderLine = new ContractorOrderLine(resultSet.getInt("COL_ID"), resultSet.getInt("CPO_ID"), resultSet.getInt("MaterialSource_ID"), resultSet.getInt("COL_Quantity"));
                    contractorOrderLineObservableList.add(contractorOrderLine);
                }
                if (!contractorOrderLineObservableList.isEmpty())
                    contractorOrderLineTableView.setItems(contractorOrderLineObservableList);
                else
                    contractorOrderLineTableView.getItems().clear();
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
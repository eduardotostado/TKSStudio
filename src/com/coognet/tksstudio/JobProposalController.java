package com.coognet.tksstudio;

import com.coognet.tksstudio.entities.JobProposal;
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

public class JobProposalController extends QueryObject implements Initializable {

    @FXML private TableColumn<JobProposal, Integer> column1;
    @FXML private TableColumn<JobProposal, Integer> column2;
    @FXML private TableColumn<JobProposal, String> column3;

    @FXML private TableView<JobProposal> tableView;

    @FXML private TextField textfield1;
    @FXML private TextField textfield2;

    private ObservableList<JobProposal> observableList;
    private JobProposal selectedObject;

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
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                //populate the textfields with the info for edit
                selectedObject = tableView.getSelectionModel().getSelectedItem();

                // String text field:      textfield1.setText(selectedObject.getAtributte());
                // Integer text field:     textfield2.setText(Integer.toString(selectedObject.getAtributte()));
                // BigDecimal text field:  textfield3.setText(selectedObject.getAtributte().toString());

                textfield1.setText(Integer.toString(selectedObject.getCustID()));
                textfield2.setText(selectedObject.getJobPropAddress());
            }
        });

        column1.setCellValueFactory(new PropertyValueFactory<JobProposal, Integer>("jobPropID"));
        column2.setCellValueFactory(new PropertyValueFactory<JobProposal, Integer>("custID"));
        column3.setCellValueFactory(new PropertyValueFactory<JobProposal, String>("jobPropAddress"));
    }

    public void mainMenuPushed(ActionEvent event) throws IOException {
        loadScene(event, "MainMenu.fxml");
    }

    public void backPushed(ActionEvent event) throws IOException {
        loadScene(event, "Job.fxml");
    }

    public void addButtonPushed() {
        int tempAtr1 = Integer.parseInt(textfield1.getText());
        String tempAtr2 = textfield2.getText();

        JobProposal temp = new JobProposal(-1, tempAtr1, tempAtr2);

        result = confirmationAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (temp.addAccount(tempAtr1, tempAtr2)) {

                clearTextBox();

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
        selectedObject = tableView.getSelectionModel().getSelectedItem();

        if (selectedObject != null) {
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedObject.deleteAccount()) {
                    loadData();

                    clearTextBox();

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
        if (tableView.getSelectionModel().getSelectedIndex() != -1) {

            int tempAtr1 = Integer.parseInt(textfield1.getText());
            String tempAtr2 = textfield2.getText();

            selectedObject = tableView.getSelectionModel().getSelectedItem();
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedObject != null) {
                    if (selectedObject.editAccount(tempAtr1, tempAtr2)) {

                        clearTextBox();

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

    public void clearTextBox(){

        textfield1.setText("");
        textfield2.setText("");

    }

    public void loadData() {
        // Load the Architectural Review data to the table view

        statement = "SELECT JobProp_ID, Cust_ID, JobProp_Address FROM Job_Proposal";
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(connectionString);
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                observableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    JobProposal temp = new JobProposal(resultSet.getInt("JobProp_ID"), resultSet.getInt("Cust_ID"), resultSet.getString("JobProp_Address"));
                    observableList.add(temp);
                }
                if (!observableList.isEmpty())
                    tableView.setItems(observableList);
                else
                    tableView.getItems().clear();
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


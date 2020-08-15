package com.coognet.tksstudio;

import com.coognet.tksstudio.entities.QueryObject;
import com.coognet.tksstudio.entities.Vehicle;
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

public class VehicleController extends QueryObject implements Initializable {

    @FXML private TableColumn<Vehicle, Integer> column1;
    @FXML private TableColumn<Vehicle, Integer> column2;
    @FXML private TableColumn<Vehicle, String> column3;
    @FXML private TableColumn<Vehicle, String> column4;
    @FXML private TableColumn<Vehicle, String> column5;
    @FXML private TableView<Vehicle> tableView;

    @FXML private TextField textfield1;
    @FXML private TextField textfield2;
    @FXML private TextField textfield3;
    @FXML private TextField textfield4;

    private ObservableList<Vehicle> observableList;
    private Vehicle selectedObject;

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

                textfield1.setText(Integer.toString(selectedObject.getFleetID()));
                textfield2.setText(selectedObject.getVehicleMake());
                textfield3.setText(selectedObject.getVehicleModel());
                textfield4.setText(selectedObject.getVehicleColor());

            }
        });

        column1.setCellValueFactory(new PropertyValueFactory<Vehicle, Integer>("vehicleID"));
        column2.setCellValueFactory(new PropertyValueFactory<Vehicle, Integer>("fleetID"));
        column3.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("vehicleMake"));
        column4.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("vehicleModel"));
        column5.setCellValueFactory(new PropertyValueFactory<Vehicle, String>("vehicleColor"));
    }

    public void mainMenuPushed(ActionEvent event) throws IOException {
        loadScene(event, "MainMenu.fxml");
    }

    public void backPushed(ActionEvent event) throws IOException {
        loadScene(event, "Department.fxml");
    }

    public void addButtonPushed() {
        int tempAtr1 = Integer.parseInt(textfield1.getText());
        String tempAtr2 = textfield2.getText();
        String tempAtr3 = textfield3.getText();
        String tempAtr4 = textfield4.getText();

        Vehicle temp = new Vehicle(-1, tempAtr1, tempAtr2, tempAtr3, tempAtr4);

        result = confirmationAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (temp.addAccount(tempAtr1, tempAtr2, tempAtr3, tempAtr4)) {

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
            String tempAtr3 = textfield3.getText();
            String tempAtr4 = textfield4.getText();

            selectedObject = tableView.getSelectionModel().getSelectedItem();
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedObject != null) {
                    if (selectedObject.editAccount(tempAtr1, tempAtr2, tempAtr3, tempAtr4)) {

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
        textfield3.setText("");
        textfield4.setText("");
    }

    public void loadData() {
        // Load the Architectural Review data to the table view

        statement = "SELECT Vehicle_ID, Fleet_ID, Vehicle_Make, Vehicle_Model, Vehicle_Color FROM Vehicle";
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(connectionString);
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                observableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    Vehicle temp = new Vehicle(resultSet.getInt("Vehicle_ID"), resultSet.getInt("Fleet_ID"), resultSet.getString("Vehicle_Make"), resultSet.getString("Vehicle_Model"), resultSet.getString("Vehicle_Color"));
                    observableList.add(temp);
                }
                if (!observableList.isEmpty()) {
                    tableView.setItems(observableList);
                } else {
                    tableView.getItems().clear();
                }            }
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


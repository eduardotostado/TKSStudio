package com.coognet.tksstudio;

import com.coognet.tksstudio.entities.EmployeeHistory;
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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeHistoryController extends QueryObject implements Initializable {

    @FXML private TableColumn<EmployeeHistory, String> column1;
    @FXML private TableColumn<EmployeeHistory, Integer> column2;
    @FXML private TableColumn<EmployeeHistory, BigDecimal> column3;
    @FXML private TableView<EmployeeHistory> tableView;

    @FXML private TextField textfield1;
    @FXML private TextField textfield2;
    @FXML private TextField textfield3;


    private ObservableList<EmployeeHistory> observableList;
    private EmployeeHistory selectedObject;

    private NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);

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
                // Integer text field:     textfield2.setText(Integer.parseInt(selectedObject.getAtributte()));
                // BigDecimal text field:  textfield3.setText(selectedObject.getAtributte().toString());

                textfield1.setText(selectedObject.getDateAssign());
                textfield2.setText(Integer.toString(selectedObject.getEmpID()));
                textfield3.setText(selectedObject.getEmpSalary().toString());

            }
        });

        column1.setCellValueFactory(new PropertyValueFactory<EmployeeHistory, String>("dateAssign"));
        column2.setCellValueFactory(new PropertyValueFactory<EmployeeHistory, Integer>("empID"));
        column3.setCellValueFactory(new PropertyValueFactory<EmployeeHistory, BigDecimal>("empSalary"));

    }

    public void mainMenuPushed(ActionEvent event) throws IOException {
        loadScene(event, "MainMenu.fxml");
    }

    public void backPushed(ActionEvent event) throws IOException {
        loadScene(event, "Employee.fxml");
    }

    public void addButtonPushed() throws ParseException {
        String tempAtr1 = textfield1.getText();
        int tempAtr2 = Integer.parseInt(textfield2.getText());

        BigDecimal tempAtr3;

        if (!textfield3.getText().equals("")) {
            tempAtr3 = new BigDecimal(format.parse(textfield3.getText()).toString());
        } else {
            tempAtr3 = new BigDecimal("0");
        }

        EmployeeHistory temp = new EmployeeHistory(tempAtr1, tempAtr2, tempAtr3);

        result = confirmationAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (temp.addAccount(tempAtr1, tempAtr2, tempAtr3)) {

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

    public void editButtonPushed() throws ParseException {
        if (tableView.getSelectionModel().getSelectedIndex() != -1) {

            String tempAtr1 = textfield1.getText();
            Integer tempAtr2 = Integer.parseInt(textfield2.getText());

            BigDecimal tempAtr3;

            if (!column3.getText().equals("")) {
                tempAtr3 = new BigDecimal(format.parse(textfield3.getText()).toString());
            } else {
                tempAtr3 = new BigDecimal("0");
            }

            selectedObject = tableView.getSelectionModel().getSelectedItem();
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedObject != null) {
                    if (selectedObject.editAccount(tempAtr1, tempAtr2, tempAtr3)) {

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

    }

    public void loadData() {
        // Load the Architectural Review data to the table view

        statement = "SELECT Date_Assign, Emp_Id, Emp_Salary FROM Employee_History";
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(connectionString);
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                observableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    EmployeeHistory temp = new EmployeeHistory(resultSet.getString("Date_Assign"), resultSet.getInt("Emp_Id"), resultSet.getBigDecimal("Emp_Salary"));
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


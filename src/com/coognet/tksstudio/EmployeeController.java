package com.coognet.tksstudio;

import com.coognet.tksstudio.entities.Employee;
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

public class EmployeeController extends QueryObject implements Initializable {

    @FXML private TableColumn<Employee, Integer> column1;
    @FXML private TableColumn<Employee, Integer> column2;
    @FXML private TableColumn<Employee, Integer> column3;
    @FXML private TableColumn<Employee, String> column4;
    @FXML private TableColumn<Employee, String> column5;
    @FXML private TableColumn<Employee, BigDecimal> column6;
    @FXML private TableView<Employee> tableView;

    @FXML private TextField textfield1;
    @FXML private TextField textfield2;
    @FXML private TextField textfield3;
    @FXML private TextField textfield4;
    @FXML private TextField textfield5;

    private ObservableList<Employee> observableList;
    private Employee selectedObject;

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

                textfield1.setText(Integer.toString(selectedObject.getDeptID()));
                textfield2.setText(Integer.toString(selectedObject.getEmpTypeCode()));
                textfield3.setText(selectedObject.getEmpFirstName());
                textfield4.setText(selectedObject.getEmpLastName());
                textfield5.setText(selectedObject.getEmpSalary().toString());

            }
        });

        column1.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("empID"));
        column2.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("deptID"));
        column3.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("empTypeCode"));
        column4.setCellValueFactory(new PropertyValueFactory<Employee, String>("empFirstName"));
        column5.setCellValueFactory(new PropertyValueFactory<Employee, String>("empLastName"));
        column6.setCellValueFactory(new PropertyValueFactory<Employee, BigDecimal>("empSalary"));

    }

    public void mainMenuPushed(ActionEvent event) throws IOException {
        loadScene(event, "MainMenu.fxml");
    }

    public void employeeHistoryPushed(ActionEvent event) throws IOException {
        loadScene(event, "EmployeeHistory.fxml");
    }

    public void employeeOrderLinePushed(ActionEvent event) throws IOException {
        loadScene(event, "EmployeeOrderLine.fxml");
    }

    public void employeeProjectPushed(ActionEvent event) throws IOException {
        loadScene(event, "EmployeeProject.fxml");
    }

    public void employeePurchaseOrderPushed(ActionEvent event) throws IOException {
        loadScene(event, "EmployeePurchaseOrder.fxml");
    }

    public void employeeTypePushed(ActionEvent event) throws IOException {
        loadScene(event, "EmployeeType.fxml");
    }

    public void dependentPushed(ActionEvent event) throws IOException {
        loadScene(event, "Dependent.fxml");
    }



    public void addButtonPushed() {
        int tempAtr1 = Integer.parseInt(textfield1.getText());
        int tempAtr2 = Integer.parseInt(textfield2.getText());
        String tempAtr3 = textfield3.getText();
        String tempAtr4 = textfield4.getText();
        BigDecimal tempAtr5;

        if (!textfield5.getText().equals("")) {
            tempAtr5 = new BigDecimal(textfield5.getText());
        } else {
            tempAtr5 = new BigDecimal("0");
        }



        Employee temp = new Employee(-1, tempAtr1, tempAtr2, tempAtr3, tempAtr4, tempAtr5);

        result = confirmationAlert.showAndWait();
        if (result.get() == ButtonType.OK) {
            if (temp.addAccount(tempAtr1, tempAtr2, tempAtr3, tempAtr4, tempAtr5)) {

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
            int tempAtr2 = Integer.parseInt(textfield2.getText());
            String tempAtr3 = textfield3.getText();
            String tempAtr4 = textfield4.getText();
            BigDecimal tempAtr5;

            if (!textfield5.getText().equals("")) {
                tempAtr5 = new BigDecimal(textfield5.getText());
            } else {
                tempAtr5 = new BigDecimal("0");
            }

            selectedObject = tableView.getSelectionModel().getSelectedItem();
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedObject != null) {
                    if (selectedObject.editAccount(tempAtr1, tempAtr2, tempAtr3, tempAtr4, tempAtr5)) {

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
        textfield5.setText("");

    }

    public void loadData() {
        // Load the Architectural Review data to the table view

        statement = "SELECT Emp_ID, Dept_ID, EmpType_Code, Emp_FirstName, Emp_LastName, Emp_Salary FROM Employee";
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(connectionString);
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                observableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    Employee temp = new Employee(resultSet.getInt("Emp_ID"), resultSet.getInt("Dept_ID"), resultSet.getInt("EmpType_Code"), resultSet.getString("Emp_FirstName"), resultSet.getString("Emp_LastName"), resultSet.getBigDecimal("Emp_Salary"));
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


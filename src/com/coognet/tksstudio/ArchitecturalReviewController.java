package com.coognet.tksstudio;

import com.coognet.tksstudio.entities.*;
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

public class ArchitecturalReviewController extends QueryObject implements Initializable {

    @FXML private TableColumn<ArchitecturalReview, Integer> archRvwIDColumn;
    @FXML private TableColumn<ArchitecturalReview, Integer> surveyIDColumn;
    @FXML private TableColumn<ArchitecturalReview, Integer> RvwTypeIDColumn;
    @FXML private TableColumn<ArchitecturalReview, Integer> RvwStatusIDColumn;
    @FXML private TableView<ArchitecturalReview> architecturalReviewTableView;

    @FXML private TextField surveyIDTextField;
    @FXML private TextField RvwTypeIDTextField;
    @FXML private TextField RvwStatusIDTextField;

    private ObservableList<ArchitecturalReview> architecturalReviewObservableList;
    private ArchitecturalReview selectedArchitecturalReview;

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
        architecturalReviewTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                //populate the textfields with the info for edit
                selectedArchitecturalReview = architecturalReviewTableView.getSelectionModel().getSelectedItem();
                surveyIDTextField.setText(Integer.toString(selectedArchitecturalReview.getSurveyID()));
                RvwTypeIDTextField.setText(Integer.toString(selectedArchitecturalReview.getRvwTypeID()));
                RvwStatusIDTextField.setText(Integer.toString(selectedArchitecturalReview.getRvwStatusID()));
            }
        });

        archRvwIDColumn.setCellValueFactory(new PropertyValueFactory<ArchitecturalReview, Integer>("archRvwID"));
        surveyIDColumn.setCellValueFactory(new PropertyValueFactory<ArchitecturalReview, Integer>("surveyID"));
        RvwTypeIDColumn.setCellValueFactory(new PropertyValueFactory<ArchitecturalReview, Integer>("rvwTypeID"));
        RvwStatusIDColumn.setCellValueFactory(new PropertyValueFactory<ArchitecturalReview, Integer>("rvwStatusID"));

    }

    public void mainMenuPushed(ActionEvent event) throws IOException {
        loadScene(event, "MainMenu.fxml");
    }

    public void backPushed(ActionEvent event) throws IOException {
        loadScene(event, "Job.fxml");
    }

    public void addButtonPushed(){
        int tempSurveyID = Integer.parseInt(surveyIDTextField.getText());
        int tempRvwTypeID = Integer.parseInt(RvwTypeIDTextField.getText());
        int tempRvwStatusID = Integer.parseInt(RvwStatusIDTextField.getText());
        ArchitecturalReview temp = new ArchitecturalReview(-1, tempSurveyID, tempRvwTypeID,  tempRvwStatusID);

        result = confirmationAlert.showAndWait();
        if(result.get() == ButtonType.OK) {
            if (temp.addAccount(tempSurveyID, tempRvwTypeID, tempRvwStatusID)) {

                surveyIDTextField.setText("");
                RvwTypeIDTextField.setText("");
                RvwStatusIDTextField.setText("");

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
        selectedArchitecturalReview = architecturalReviewTableView.getSelectionModel().getSelectedItem();

        if(selectedArchitecturalReview != null) {
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedArchitecturalReview.deleteAccount()) {
                    loadData();

                    surveyIDTextField.setText("");
                    RvwTypeIDTextField.setText("");
                    RvwStatusIDTextField.setText("");

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
        if(architecturalReviewTableView.getSelectionModel().getSelectedIndex() != -1) {
            int tempSurveyID = Integer.parseInt(surveyIDTextField.getText());
            int tempRvwTypeID = Integer.parseInt(RvwTypeIDTextField.getText());
            int tempRvwStatusID = Integer.parseInt(RvwStatusIDTextField.getText());

            selectedArchitecturalReview = architecturalReviewTableView.getSelectionModel().getSelectedItem();
            result = confirmationAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                if (selectedArchitecturalReview != null) {
                    if (selectedArchitecturalReview.editAccount(tempSurveyID, tempRvwTypeID, tempRvwStatusID)) {

                        surveyIDTextField.setText("");
                        RvwTypeIDTextField.setText("");
                        RvwStatusIDTextField.setText("");

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

        statement = "SELECT ArchRvw_ID, Survey_ID, Rvw_TypeID, RvwStatus_ID FROM Architectural_Review";
        ResultSet resultSet = null;
        try {
            conn = DriverManager.getConnection(connectionString);
            stmt = conn.prepareStatement(statement);
            resultSet = stmt.executeQuery();
            if (resultSet != null) {
                architecturalReviewObservableList = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    ArchitecturalReview architecturalReview = new ArchitecturalReview(resultSet.getInt("ArchRvw_ID"), resultSet.getInt("Survey_ID"), resultSet.getInt("Rvw_TypeID"), resultSet.getInt("RvwStatus_ID"));
                    architecturalReviewObservableList.add(architecturalReview);
                }
                if (!architecturalReviewObservableList.isEmpty())
                    architecturalReviewTableView.setItems(architecturalReviewObservableList);
                else
                    architecturalReviewTableView.getItems().clear();
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
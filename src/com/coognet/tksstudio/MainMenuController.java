package com.coognet.tksstudio;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML private Label dateLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        Date date = new Date();
        dateLabel.setText(dateFormat.format(date));
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

    public void accountingButtonPressed(ActionEvent event) throws IOException{
        loadScene(event, "Account.fxml");
    }
    public void departmentsButtonPressed(ActionEvent event)throws IOException{
        loadScene(event, "Department.fxml");
    }
    public void employeesButtonPressed(ActionEvent event)throws IOException{
        loadScene(event, "Employee.fxml");
    }
    public void contractorsButtonPressed(ActionEvent event)throws IOException{
        loadScene(event, "Contractor.fxml");
    }
    public void customersButtonPressed(ActionEvent event)throws IOException{
        loadScene(event, "Customer.fxml");
    }
    public void jobsButtonPressed(ActionEvent event)throws IOException{
        loadScene(event, "Job.fxml");
    }
}

package com.coognet.tksstudio;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    private final String USERNAME="admin";
    private final String PASSWORD="password";

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private Alert wrongCredentialsAlert;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        wrongCredentialsAlert = new Alert(Alert.AlertType.WARNING);
        wrongCredentialsAlert.setTitle("Error");
    }

    public void enterButtonPressed(ActionEvent event) throws IOException {
        if(usernameField.getText().equals("")){
            wrongCredentialsAlert.setHeaderText("Empty Username");
            wrongCredentialsAlert.setContentText("Please enter a valid username");
            wrongCredentialsAlert.showAndWait();
        }else if(passwordField.getText().equals("")){
            wrongCredentialsAlert.setHeaderText("Empty Password");
            wrongCredentialsAlert.setContentText("Please enter a valid password");
            wrongCredentialsAlert.showAndWait();
        } else if(usernameField.getText().equals(USERNAME) && passwordField.getText().equals(PASSWORD)){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("MainMenu.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }else {
            wrongCredentialsAlert.setHeaderText("Wrong username/password");
            wrongCredentialsAlert.setContentText("Please enter a valid username/password.");
            wrongCredentialsAlert.showAndWait();
        }
    }
}

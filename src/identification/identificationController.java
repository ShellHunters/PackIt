package identification;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class identificationController {

    @FXML public JFXTextField emailField, nameField;
    @FXML public JFXPasswordField passwordField;

    @FXML public AnchorPane identificationContainer;

    @FXML public void toRegister() throws IOException{
        AnchorPane register = FXMLLoader.load(getClass().getResource("registerMain.fxml"));
        identificationContainer.getChildren().setAll(register);
    }

    @FXML public void toLogin() throws IOException{
        AnchorPane login = FXMLLoader.load(getClass().getResource("loginMain.fxml"));
        identificationContainer.getChildren().setAll(login);
    }

    @FXML public void login(){
        System.out.println(emailField.getText());
        System.out.println(passwordField.getText());
    }

    @FXML public void register(){

    }

    @FXML public void close(){
        System.exit(0);
    }
}

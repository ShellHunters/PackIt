package interfaceMagazinier.providers;

import basicClasses.Email;
import basicClasses.product;
import basicClasses.user;
import javafx.event.ActionEvent;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static interfaceMagazinier.providers.SendEmailMessageController.InsertProductToCommand;
import static interfaceMagazinier.providers.SendEmailMessageController.ProductList;

public class settingSenderInformationController implements Initializable {


    @FXML
    private JFXButton confirmButton;

    @FXML
    private JFXButton exitButton;

    @FXML
    private JFXTextField emailTextField;
    @FXML
    public Label errorLabel;
    @FXML
    private JFXPasswordField passwordField;
    //@FXML
   // private Label errorLabel;
    @FXML
        public void confirmInfo ( ActionEvent event) throws SQLException {

        if (VerifyValidateEmail(emailTextField)&& passwordField.getText().length()>0) {

            Email.senderMail = emailTextField.getText();
            Email.senderPassword = passwordField.getText();

            Email.send();

            if (Email.ItSent){

                errorLabel.setTextFill(Paint.valueOf("green"));
errorLabel.setText("Email Was Sent");
                SendEmailMessageController.settingInfoDialog.close();
                if (Email.ItSent) {
                    System.out.println("in condition");
                    DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    InsertProductToCommand(ProductList,SendEmailMessageController.tempoProvider , dateformat.format(System.currentTimeMillis()));

SendEmailMessageController.forDisablingTextArea.set(false);


                }

            }

            else {
                errorLabel.setTextFill(Paint.valueOf("red"));
                errorLabel.setText("Your Email Wasn't Sent");}


        }

      else {
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText("Verify Your Email Or Password");
        }
      }
    @FXML
        public void exit ( ActionEvent event){
        SendEmailMessageController.settingInfoDialog.close();

        SendEmailMessageController.forDisablingTextArea.set(false);
        }

    static boolean VerifyValidateEmail(JFXTextField textField) {
        return Pattern.matches("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9-[-]]+([.][a-zA-Z]+)+", textField.getText());

    }
boolean verifyIfEmpty(){
       return passwordField.getText().equals("")&&emailTextField.getText().equals("");

}

    @Override
    public void initialize (URL location, ResourceBundle resources) {

        errorLabel.setText("");
        emailTextField.setText(user.getEmail());
passwordField.textProperty().addListener((observable, oldValue, newValue) ->{

    SendEmailMessageController.settingInfoDialog.setOverlayClose(verifyIfEmpty());

});
        emailTextField.textProperty().addListener((observable, oldValue, newValue) ->{

            SendEmailMessageController.settingInfoDialog.setOverlayClose(verifyIfEmpty());

        });
    }
}

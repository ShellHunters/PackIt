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
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static interfaceMagazinier.providers.SendEmailMessageController.*;

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
                    String path = "src/resource/File/commande1.jasper";

                    try {
                        // Indentation CTRL + ALT + L
                        // Path documentPath
                        // HashMap<String, Object> params
                        // JRDataSource jasperDataSource/
                        Path documentPath = Paths.get(path);
                        Map<String, Object> params = new HashMap<>();
                        params.put("providerName", providerName); // get it from login
                        JREmptyDataSource emptyDatasource = new JREmptyDataSource();
                        JRBeanCollectionDataSource jasperDataSource = new JRBeanCollectionDataSource(ProductList);
                        params.put("DataSource", jasperDataSource);
                        JasperPrint jasperPrint = JasperFillManager.fillReport(documentPath.toAbsolutePath().toString(), params, jasperDataSource);
                        JasperViewer.viewReport(jasperPrint, false);

                    } catch (JRException e) {
                        e.printStackTrace();
                    }

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

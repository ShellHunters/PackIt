package interfaceMagazinier.providers;

import basicClasses.product;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
public class settingProductNotInListController implements Initializable {

    @FXML
    public JFXTextField productNameTextfield;

    @FXML
    public JFXTextField neededQuantityTextField;

    @FXML
    public JFXButton confirmButton;

    @FXML
    public JFXButton exitButton;

    @FXML
    public Label errorLabel;

    @FXML
    public void confirmQuantity (ActionEvent event) {
        if (productNameTextfield.getText().equals("") && neededQuantityTextField.getText().equals(""))
            errorLabel.setText("Verify Your Information");


        else {
SendEmailMessageController.ProductList.add(new product(productNameTextfield.getText(),Integer.parseInt(neededQuantityTextField.getText())));

        }
        productNameTextfield.setText("");
        neededQuantityTextField.setText("");
    }

    @FXML
    public void exit (ActionEvent event) {
        SendEmailMessageController.sendProductNotInListDialog.close();
    }

    @Override
    public void initialize (URL location, ResourceBundle resources) {
        errorLabel.getStyleClass().add("ErrorMessage");
        neededQuantityTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*"))
                    neededQuantityTextField.setText(newValue.replaceAll("[^\\d]", ""));


            }
        });

errorLabel.setText("");

neededQuantityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
    SendEmailMessageController.sendProductNotInListDialog.setOverlayClose(verifyIfEmpty());

});
        productNameTextfield.textProperty().addListener((observable, oldValue, newValue) -> {
            SendEmailMessageController.sendProductNotInListDialog.setOverlayClose(verifyIfEmpty());

        });
    }
    boolean verifyIfEmpty(){
        return productNameTextfield.getText().equals("")&&neededQuantityTextField.getText().equals("");

    }
}

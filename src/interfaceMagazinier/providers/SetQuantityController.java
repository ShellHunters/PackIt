package interfaceMagazinier.providers;

import basicClasses.product;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import static interfaceMagazinier.providers.SendEmailController.*;


public class SetQuantityController implements Initializable {
    @FXML
    private JFXTextField Quantity;

    @FXML
    private Label ProductName;
    @FXML
    private StackPane SetQuantityRoot;
    public static boolean IfExit, IfExitToModifySendEmailMessage;
    public static product Product = new product();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SendEmailController.ForceCheck.set(true);
        IfExit = false;
        IfExitToModifySendEmailMessage = false;
        if (SendEmailMessageController.IfEmailMessageIsOpen)
            Quantity.setText(SendEmailMessageController.Quantity);
        ProductName.setText("Product : " + SendEmailController.ProductName);
        Quantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*"))
                    Quantity.setText(newValue.replaceAll("[^\\d]", ""));


            }
        });
    }

    public void ConfirmQuantity() {
        if (!Quantity.getText().equals("")) {

            if (SendEmailMessageController.IfMultipleProductSelect.get()) {
                // System.out.println("Test 1 In SetQuantity \n");
                int i;
                Iterator<product> iterator = SendEmailMessageController.SelectedProductList.iterator();
                while (iterator.hasNext()) {
                    product infoProduct = iterator.next();
                    i = SendEmailMessageController.ProductList.indexOf(infoProduct);
                    //    System.out.println("Test 1 In SetQuantity   "+ i+"\n");

                    infoProduct.setNeededQuantity(Integer.parseInt(Quantity.getText()));
                    SendEmailMessageController.ProductList.set(i, infoProduct);
                    iterator.remove();
                }
                SendEmailMessageController.SelectedProductList.clear();
            }
            if (SendEmailMessageController.IfEmailMessageIsOpen) {
                //  System.out.println("Test 2 In SetQuantity \n");

                int i = SendEmailMessageController.ProductList.indexOf(SendEmailMessageController.infoProducts);
                SendEmailMessageController.infoProducts.setNeededQuantity(Integer.parseInt(Quantity.getText()));

                SendEmailMessageController.ProductList.set(i, SendEmailMessageController.infoProducts);
                System.out.println("Test 2 In SetQuantity  " + i + "\n");

            }
            if (SendEmailController.IfMultipleSelected) {
                Iterator<product> iterator = MultipleSelectionList.iterator();
                // System.out.println("Test 3 In SetQuantity");

                while (iterator.hasNext()) {
                    product product = iterator.next();
                    product.setNeededQuantity(Integer.parseInt(Quantity.getText()));
                    SendEmailMessageController.ProductList.add(product);


                    SendEmailController.ProductList.remove(product);

                    SendEmailController.NeededProduct.remove(product);
                    SendEmailController.TempoListOfProducts.remove(product);
                    SendEmailMessageController.TotalProducts.add(product);
                    iterator.remove();
                }

                SendEmailController.ForceCheckForMultipleSelectCheckbox.set(false);
            } else {
                //   System.out.println("Test 4 In SetQuantity");

                if (!SendEmailMessageController.IfEmailMessageIsOpen && !SendEmailMessageController.IfMultipleProductSelect.get()) {
                    theProduct.setNeededQuantity(Integer.parseInt(Quantity.getText()));
                    SendEmailMessageController.ProductList.add(theProduct);

                    SendEmailController.ProductList.remove(theProduct);
                    SendEmailController.NeededProduct.remove(theProduct);
                    SendEmailController.TempoListOfProducts.remove(theProduct);
                    ;
                }
            }


            SendEmailController.InitValues();
            SendEmailMessageController.IfMultipleProductSelect.set(false);
            SendEmailMessageController.IfEmailMessageIsOpen = false;
            SendEmailController.ForDisableSetQuantityButton.set(true);
            SendEmailController.ForDisableMultipleCheck.set(false);
            if (SendEmailMessageController.IfNeededProductBoxIsChecked)
                SendEmailController.ForDisableMultipleCheck.set(NeededProduct.size() < 2);

            else SendEmailController.ForDisableMultipleCheck.set(SendEmailController.ProductList.size() < 2);
            SendEmailMessageController.IfModifyIsClicled = false;
            SetQuantityRoot.getScene().getWindow().hide();
        }

    }


    public void Exit() {


        IfExitToModifySendEmailMessage = SendEmailMessageController.IfModifyIsClicled;

        SendEmailMessageController.IfMultipleProductSelect.set(false);
        SendEmailMessageController.IfEmailMessageIsOpen = false;
        SendEmailMessageController.IfModifyIsClicled = false;
        IfExitToModifySendEmailMessage = true;
        if (!IfMultipleSelected)
            SendEmailController.ForceCheck.set(false);
        IfExit = true;
        // ProductForEmail.IfWasSentVerify=false;
        SetQuantityRoot.getScene().getWindow().hide();


    }

    public void OnKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            ConfirmQuantity();
        if (keyEvent.getCode() == KeyCode.ESCAPE)
            Exit();
    }
}

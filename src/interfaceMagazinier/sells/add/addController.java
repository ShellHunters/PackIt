package interfaceMagazinier.sells.add;

import basicClasses.product;
import basicClasses.sell;
import com.jfoenix.controls.JFXTextField;
import interfaceMagazinier.imMainController;
import interfaceMagazinier.sells.imSellsController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static interfaceMagazinier.sells.imSellsController.sellCollection;

public class addController implements Initializable {
    public JFXTextField whieghtField;
    public CheckBox checkBox;
    public JFXTextField whieghtField2;
    public CheckBox checkBox2;
    public AnchorPane addFull;
    public JFXTextField newProductName;
    public JFXTextField newProductQuantity;
    public JFXTextField newProductPrice;
    public JFXTextField quantity;
    public JFXTextField barcode;
    public JFXTextField productName;
    public JFXTextField amountPrice;
    public JFXTextField amountName;
    public JFXTextField amountQuantity;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void weightedProduct(ActionEvent event) {
          if (checkBox.isSelected()){
          whieghtField.setDisable(false);}
          else whieghtField.setDisable(true);
    }

    public void weightedProduct2(ActionEvent event) {
        if (checkBox2.isSelected()) whieghtField2.setDisable(false);
        else whieghtField2.setDisable(true);
    }

    public void close(ActionEvent event) throws IOException {

    }

    public void addAmount(ActionEvent event) {
        product product = new product(amountName.getText(), -1, 500, 500, 1, null);
        sellCollection.addProduct(product);
        sellCollection.getTotalPrice();
        sellCollection.getSoldProducts();

        imSellsController sellsController = imMainController.sellLoader.getController();

        sellsController.sellTable.setItems((ObservableList<product>) sellCollection.getSoldProducts());
        sellsController.prix.setText(String.valueOf(sellCollection.getTotalPrice()));
    }

    public void addExistingProduct(ActionEvent event) {
    }

    public void addNotExisting(Event event) {
    }
}

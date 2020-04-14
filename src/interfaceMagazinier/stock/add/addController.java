package interfaceMagazinier.stock.add;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import  interfaceMagazinier.stock.imStockController;
import basicClasses.product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class addController {

    @FXML JFXTextField productname;
    @FXML JFXTextField barcode;
    @FXML JFXTextField sellprice;
    @FXML JFXTextField buyprice;
    @FXML JFXTextField quantity;
    @FXML JFXDatePicker expirationdate;
    @FXML Label errorLabel;

    @FXML private void addProduct() throws SQLException {
//        Connection connection = ConnectionClass.getConnection();
//        Statement statement = connection.createStatement();
        if (errorCheck()) return;
        int barcode = Integer.parseInt(this.barcode.getText());
        float sellPrice = Float.parseFloat(this.sellprice.getText());
        float buyPrice = Float.parseFloat(buyprice.getText());
        int quanity = Integer.parseInt(quantity.getText());
        product product = new product(productname.getText(), barcode, sellPrice, buyPrice, quanity, expirationdate.getValue());
        imStockController.products.add(product);// darte had ster pour ajouter f tableau machi f el base de donnee
//        String sql = "INSERT INTO products (nom,codebar, prix_a , prix_v, quantit, date) VALUES ('" + TF_productName + "', '" + TF_Barcode + "', '" + TF_buyPrice + "', '" + TF_sellPrice + "', '" + TF_Quanity + "', '" + DP_expiratioDate + "')";
//        statement.executeUpdate(sql);
    }

    private boolean errorCheck(){
        if (productname.getText().isEmpty() || barcode.getText().isEmpty() || sellprice.getText().isEmpty() || buyprice.getText().isEmpty() || quantity.getText().isEmpty()){
            errorLabel.setText("Fill all the text fields");
            return true;
        }
        else if (!barcode.getText().chars().allMatch(Character::isDigit) || !quantity.getText().chars().allMatch(Character::isDigit) || !sellprice.getText().chars().allMatch(Character::isDigit) || !buyprice.getText().chars().allMatch(Character::isDigit)){
            errorLabel.setText("Some text fields must be numbers only");
            return true;
        }
        else {
            errorLabel.setText("");
            return false;
        }
    }

    @FXML private void close() {
        productname.getScene().getWindow().hide();
        imStockController.stage = null;
    }
}

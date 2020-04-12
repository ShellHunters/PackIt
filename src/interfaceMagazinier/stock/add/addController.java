package interfaceMagazinier.stock.add;

import Connection.ConnectionClass;
import  interfaceMagazinier.stock.imStockController;
import basicClasses.product;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class addController {

    @FXML private TextField TF_productName;
    @FXML private TextField TF_Barcode;
    @FXML private TextField TF_sellPrice;
    @FXML private TextField TF_buyPrice;
    @FXML private TextField TF_Quanity;
    @FXML private DatePicker DP_expiratioDate;

    @FXML
    public void add_product() throws SQLException {
//        Connection connection = ConnectionClass.getConnection();
//        Statement statement = connection.createStatement();
        int barcode = Integer.parseInt(TF_Barcode.getText());
        float sellPrice = Float.parseFloat(TF_sellPrice.getText());
        float buyPrice = Float.parseFloat(TF_buyPrice.getText());
        int quanity = Integer.parseInt(TF_Quanity.getText());
        product product = new product(TF_productName.getText(), barcode, sellPrice, buyPrice, quanity, DP_expiratioDate.getValue());
        imStockController.products.add(product);// darte had ster pour ajouter f tableau machi f el base de donnee
//        String sql = "INSERT INTO products (nom,codebar, prix_a , prix_v, quantit, date) VALUES ('" + TF_productName + "', '" + TF_Barcode + "', '" + TF_buyPrice + "', '" + TF_sellPrice + "', '" + TF_Quanity + "', '" + DP_expiratioDate + "')";
//        statement.executeUpdate(sql);
    }

    public void ExitButton(javafx.event.ActionEvent event) { TF_productName.getScene().getWindow().hide();}

    public void addProduct(javafx.event.ActionEvent event) throws IOException {
        Parent root1= FXMLLoader.load(getClass().getResource("add/addProduct.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.show();
    }
}

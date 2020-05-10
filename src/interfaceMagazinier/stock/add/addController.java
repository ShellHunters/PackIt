package interfaceMagazinier.stock.add;

import Connection.ConnectionClass;
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
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class addController {

    @FXML JFXTextField productname;
    @FXML JFXTextField barcode;
    @FXML JFXTextField sellprice;
    @FXML JFXTextField buyprice;
    @FXML JFXTextField quantity;
    @FXML JFXDatePicker expirationdate;
    @FXML Label errorLabel;

    @FXML private void addProduct() throws SQLException {
        Connection connection = ConnectionClass.getConnection();
        Statement statement = connection.createStatement();
        String sql;
        if (errorCheck()) return;
        int barcode = Integer.parseInt(this.barcode.getText());
        float sellPrice = Float.parseFloat(this.sellprice.getText());
        float buyPrice = Float.parseFloat(buyprice.getText());
        int quanity = Integer.parseInt(quantity.getText());
        String expirationdateString;
        if (expirationdate.getValue() != null) expirationdateString = expirationdate.getValue().toString(); else expirationdateString = "";
        product product = new product(productname.getText(), barcode, sellPrice, buyPrice, quanity, expirationdateString);
        imStockController.products.add(product);
        if (expirationdateString.equals("")) sql = "INSERT INTO stock (name,barcode,buyprice , sellprice, quantity) VALUES ('" + productname.getText() + "', '" + barcode + "', '" + buyPrice + "', '" +sellPrice + "', '" +quanity + "')";
            else sql = "INSERT INTO stock (name,barcode,buyprice , sellprice, quantity,expirationdate) VALUES ('" + productname.getText() + "', '" + barcode + "', '" + buyPrice + "', '" +sellPrice + "', '" +quanity+ "', '" + expirationdateString + "')";
        statement.executeUpdate(sql);
        resetFields();
        connection.close();
    }

    private boolean errorCheck(){
        if (productname.getText().isEmpty() || barcode.getText().isEmpty() || sellprice.getText().isEmpty() || buyprice.getText().isEmpty() || quantity.getText().isEmpty()){
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText("Fill all the text fields");
            return true;
        }
        else if (!barcode.getText().matches("[0-9]*") || !quantity.getText().matches("[0-9]*") || !sellprice.getText().matches("[0-9]*\\.?[0-9]+") || !buyprice.getText().matches("[0-9]*\\.?[0-9]+")){
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText("Some text fields must be numbers only");
            return true;
        }
        else {
            errorLabel.setTextFill(Paint.valueOf("green"));
            errorLabel.setText("product added succefully");
            return false;
        }
    }

    private void resetFields(){
        productname.setText(""); barcode.setText(""); buyprice.setText(""); sellprice.setText(""); quantity.setText(""); expirationdate.setValue(null);
    }
}

package interfaceMagazinier.stock.update;

import Connector.ConnectionClass;
import basicClasses.product;
import basicClasses.user;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import interfaceMagazinier.settings.preference.preferencesController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

import java.lang.reflect.Type;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class updateController implements Initializable {

    @FXML
    private JFXTextField productname;
    @FXML
    private JFXTextField barcode;
    @FXML
    private JFXTextField sellprice;
    @FXML
    private JFXTextField buyprice;
    @FXML
    private JFXTextField quantity;
    @FXML
    private JFXDatePicker expirationdate;
    @FXML
    private Label errorLabel;
    @FXML
    private JFXComboBox<String> productTypeComboBox;
    @FXML
    private JFXCheckBox productTypeCheck;
    @FXML
    private CheckBox placeCheck;
    @FXML
    private JFXTextField containerName;
    @FXML
    private JFXTextField floorNumber;
    @FXML
    private CheckBox expirationCheck;
    public static product productSelected;

    public static product getProductSelected() {
        return productSelected;
    }

    public static void setProductSelected(product productSelected) {
        updateController.productSelected = productSelected;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productTypeComboBox.setItems(preferencesController.productTypes);
        if (productSelected.getProductName() == null) productTypeComboBox.setValue(null);
        else if (preferencesController.productTypes.contains(productSelected.getProductType())) productTypeComboBox.setValue(productSelected.getProductType());
        else productTypeComboBox.setValue(null);

        productname.setText(productSelected.getProductName());
        barcode.setText((String.valueOf(productSelected.getBarcode())));
        sellprice.setText(String.valueOf(productSelected.getSellPrice()));
        buyprice.setText(String.valueOf(productSelected.getBuyPrice()));
        quantity.setText(String.valueOf(productSelected.getQuantity()));
        if (productSelected.getExpirationDate() == ""){
            expirationdate.setValue(null);
            expirationCheck.setSelected(false);
        }
        else expirationdate.setValue(LocalDate.parse(productSelected.getExpirationDate()));
    }

    @FXML
    private void updateProduct(ActionEvent event) throws SQLException {
        if (errorCheck()) return;

        //update product
        Connection connection = ConnectionClass.getConnection();
        String query = "Update stock set name=?, barcode=? , buyprice=? , sellprice=? , quantity=? ,expirationdate=?,floor=?,containerName=?, productType=?  where barcode=? and userID=? ";
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, productname.getText());
        pst.setString(2, barcode.getText());
        pst.setString(3, buyprice.getText());
        pst.setString(4, String.valueOf(sellprice.getText()));
        pst.setString(5, String.valueOf(quantity.getText()));
        if (expirationdate.getValue() == null) {
            pst.setNull(6, Types.DATE);
        } else {
            pst.setString(6, expirationdate.getValue().toString());
        }
        if (containerName.isDisable()) {
            pst.setNull(7, Types.INTEGER);
            pst.setNull(8, Types.VARCHAR);
        } else {
            pst.setInt(7, Integer.parseInt(floorNumber.getText()));
            pst.setString(8, containerName.getText());
        }
        pst.setString(9, productTypeComboBox.getValue());
        pst.setInt(10, productSelected.getBarcode());
        pst.setInt(11, user.getUserID());
        pst.execute();

        productSelected.setBarcode(Integer.parseInt(barcode.getText()));
        productSelected.setProductName(productname.getText());
        productSelected.setBuyPrice(Float.parseFloat(buyprice.getText()));
        productSelected.setSellPrice(Float.parseFloat(sellprice.getText()));
        productSelected.setQuantity(Integer.parseInt(quantity.getText()));
        if (expirationdate.getValue() == null) productSelected.setExpirationDate("");
        else productSelected.setExpirationDate(expirationdate.getValue().toString());
    }


    private boolean errorCheck() throws SQLException {


        if (productname.getText().isEmpty()
                || barcode.getText().isEmpty()
                || sellprice.getText().isEmpty()
                || buyprice.getText().isEmpty()
                || quantity.getText().isEmpty()
                || (!containerName.isDisable() && (containerName.getText().isEmpty() || floorNumber.getText().isEmpty()))
                || (!expirationdate.isDisable() && expirationdate.getValue() == null)
                || (!productTypeComboBox.isDisable() && productTypeComboBox.getValue() == null)) {
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText("Fill all the text fields and informations");
            return true;
        }
        if (!barcode.getText().matches("[0-9]*")
                || !quantity.getText().matches("[0-9]*")
                || !sellprice.getText().matches("[0-9]*\\.?[0-9]+")
                || !buyprice.getText().matches("[0-9]*\\.?[0-9]+")
                || !floorNumber.getText().matches("[0-9]*")
                || (!containerName.isDisable() && !floorNumber.getText().matches("[0-9]*"))) {
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText("Some text fields must be numbers only");
            return true;
        }
        Connection connection = ConnectionClass.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM stock WHERE barcode=" + barcode.getText();
        ResultSet rs = statement.executeQuery(query);
        if (rs.next()) {
            if (!rs.getString("name").equals(productname.getText()) && rs.getInt("barcode") == Integer.parseInt(barcode.getText())) {
                errorLabel.setTextFill(Paint.valueOf("red"));
                errorLabel.setText("The barcode is already used for an other product");
            }
            if (rs.getString("name").equals(productname.getText()) && rs.getInt("barcode") != Integer.parseInt(barcode.getText())) {
                errorLabel.setTextFill(Paint.valueOf("red"));
                errorLabel.setText("This product already exists with an other barcode");
            }
        }
        errorLabel.setTextFill(Paint.valueOf("green"));
        errorLabel.setText("product updated succefully");
        return false;
    }

    @FXML
    private void expirationCheckAction() {
        expirationdate.setDisable(!expirationCheck.isSelected());
    }

    @FXML
    private void placeCheckAction() {
        containerName.setDisable(!placeCheck.isSelected());
        floorNumber.setDisable(!placeCheck.isSelected());
    }

    @FXML
    private void productTypeCheckAction() {
        productTypeComboBox.setDisable(!productTypeCheck.isSelected());
    }
}
package interfaceMagazinier.stock.add;

import Connector.ConnectionClass;
import basicClasses.Provider;
import basicClasses.product;
import basicClasses.user;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import interfaceMagazinier.providers.ApplyingCommandController;
import interfaceMagazinier.providers.CommandHistoryController;
import interfaceMagazinier.providers.SendEmailController;
import interfaceMagazinier.providers.imProviderController;
import interfaceMagazinier.stock.imStockController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.sql.*;
import java.util.Collection;
import java.util.ResourceBundle;

public class addController implements Initializable {

    @FXML
    JFXTextField productname;
    @FXML
    JFXTextField barcode;
    @FXML
    JFXTextField sellprice;
    @FXML
    JFXTextField buyprice;
    @FXML
    JFXTextField quantity;
    @FXML
    JFXDatePicker expirationdate;
    @FXML
    private CheckBox expirationCheck;
    @FXML
    Label errorLabel;
    @FXML private JFXComboBox<Provider> ProvidersComboBox;
    public static ObservableList<Provider> ProvidersListForComboBox = FXCollections.observableArrayList();
    public boolean IfFromApplyingCommand;
    Integer TheIndex;
    private boolean addInFullStock = false;
    private boolean addOnlyQuantity = false;
    private boolean isAddOnlyQuantityInFullStock = false;
    private int oldQuantity = 0;

    public addController() throws SQLException {
    }

    @FXML
    private void addProduct() throws SQLException {
        float totalfigure;
        Connection connection = ConnectionClass.getConnection();
        Statement statement = connection.createStatement();
        String sql;
//        System.out.println("this is test in add   "+ProvidersComboBox.getValue().getFirstName()+"  "+ProvidersComboBox.getValue().getId());
        if (errorCheck()) return;
        if (addOnlyQuantity || isAddOnlyQuantityInFullStock){
            String tableName;
            if (addOnlyQuantity) {
                addOnlyQuantity = false;
                tableName = "stock";
                imStockController.products.forEach(product -> {
                    if (product.getBarcode() == Integer.parseInt(barcode.getText())){
                        product.setQuantity(oldQuantity + Integer.parseInt(quantity.getText()));
                        return;
                    }
                });
            }
            else {
                isAddOnlyQuantityInFullStock = false;
                tableName = "fullStock";
            }
            sql = "UPDATE "+ tableName+ " SET quantity=" + (oldQuantity + Integer.parseInt(quantity.getText())) + " WHERE barcode=" + Integer.parseInt(barcode.getText());
            statement.execute(sql);
            return;
        }
        int barcode = Integer.parseInt(this.barcode.getText());
        float sellPrice = Float.parseFloat(this.sellprice.getText());
        float buyPrice = Float.parseFloat(buyprice.getText());
        int quanity = Integer.parseInt(quantity.getText());
        String expirationdateString;
        if (expirationdate.getValue() != null) expirationdateString = expirationdate.getValue().toString();
        else expirationdateString = "";
        product product = new product(productname.getText(), barcode, sellPrice, buyPrice, quanity, expirationdateString);
        if (SendEmailController.IfTabPaneIsOpen) {
            SendEmailController.ProductList.add(product);
            product brin = SendEmailController.ProductList.get(SendEmailController.ProductList.size() - 1);
            //   System.out.println("this is  my test "+brin.getBarcode() +"   "+ brin.getProductName()+"   "+brin.getInitialQuantity()  +"   "+brin.getStockPercentage() +"  "+brin.getQuantity() +"   "+brin.getNeededQuantity()+"  "+brin.getExpirationDate()+"   "+brin.getIfWasSent()+"  "+ brin.getBuyPrice()+"   "+brin.getSellPrice() +"  "+brin.getNumberOfSells());
            SendEmailController.TempoListOfProducts.add(product);
        }
        imStockController.products.add(product);
        String tableName = "stock";
        if (addInFullStock) tableName = "fullStock";
        addInFullStock = false;
        if (expirationdateString.equals(""))
            sql = "INSERT INTO " + tableName + " (name,barcode,buyprice , sellprice, quantity, initialQuantity,userID) VALUES ('" + productname.getText() + "', '" + barcode + "', '" + buyPrice + "', '" + sellPrice + "', '" + quanity + "', '" + quanity + "','" + user.getUserID() + "')";
        else
            sql = "INSERT INTO " + tableName + " (name,barcode,buyprice , sellprice, quantity,expirationdate, initialQuantity,userID) VALUES ('" + productname.getText() + "', '" + barcode + "', '" + buyPrice + "', '" + sellPrice + "', '" + quanity + "', '" + expirationdateString + "', '" + quanity + "','" + user.getUserID() + "')";
        statement.executeUpdate(sql);
        if (IfFromApplyingCommand) {
            totalfigure = ((ApplyingCommandController.TheProvider.getTotalFigure() + (float) quanity * buyPrice));
            System.out.println("in add scene  " + totalfigure);
            ApplyingCommandController.TheProvider.setTotalFigure(totalfigure);
            UpdateProvider(ApplyingCommandController.TheProvider.getId(), totalfigure);
            product theProduct = ApplyingCommandController.ProductList.get(TheIndex);
            theProduct.setIfWasAdded(true);
            CommandHistoryController.command.getListOfProducts().set(TheIndex, theProduct);
            //ApplyingCommandController.ProductList.set(TheIndex, theProduct);
            SetThatWasAdded(barcode, CommandHistoryController.command.getId());
            ApplyingCommandController.InitTable();
        } else {
            totalfigure = ((ProvidersComboBox.getValue().getTotalFigure() + (float) quanity * buyPrice));
            UpdateProvider(ProvidersComboBox.getValue().getId(), totalfigure);
        }
        resetFields();
        connection.close();
        // SendEmailController. NeededProduct.add( new ProductForEmail(barcode, productname.getText() ,buyPrice , quanity, quanity , (float) 100 ,false));
        //SendEmailController.  ProductList.add(  new ProductForEmail(barcode, productname.getText() ,buyPrice , quanity, quanity , (float) 100 ,false));
        // SendEmailController.  TempoListOfProducts.add(  new ProductForEmail(barcode, productname.getText() ,buyPrice , quanity, quanity , (float) 100 ,false));
    }

    private boolean errorCheck() throws SQLException {
        Connection connection = ConnectionClass.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM stock WHERE barcode=" + barcode.getText();
        ResultSet rs = statement.executeQuery(query);
        if (rs.next()) {
            if (rs.getFloat("buyprice") == Float.parseFloat(buyprice.getText()) && rs.getFloat("sellprice") == Float .parseFloat(sellprice.getText())){
                addOnlyQuantity = true;
                oldQuantity = rs.getInt("quantity");
            }
            else {
                query = "SELECT * FROM fullStock WHERE barcode=" + barcode.getText() + " AND sellPrice=" + sellprice.getText() +" AND buyPrice=" + buyprice.getText();
                rs = statement.executeQuery(query);
                if (rs.next()) {
                    isAddOnlyQuantityInFullStock = true;
                    oldQuantity = rs.getInt("quantity");
                }
                else addInFullStock = true;
            }

        }


        if (productname.getText().isEmpty() || barcode.getText().isEmpty() || sellprice.getText().isEmpty() || buyprice.getText().isEmpty() || quantity.getText().isEmpty()) {
            // mba3ed zideha || (ProvidersComboBox.isVisible() && ProvidersComboBox.getValue()==null)
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText("Fill all the text fields and informations");
            return true;
        } else if (!barcode.getText().matches("[0-9]*") || !quantity.getText().matches("[0-9]*") || !sellprice.getText().matches("[0-9]*\\.?[0-9]+") || !buyprice.getText().matches("[0-9]*\\.?[0-9]+")) {
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText("Some text fields must be numbers only");
            return true;
        } else {
            errorLabel.setTextFill(Paint.valueOf("green"));
            errorLabel.setText("product added succefully");
            return false;
        }
    }

    private void resetFields() {
        productname.setText("");
        barcode.setText("");
        buyprice.setText("");
        sellprice.setText("");
        quantity.setText("");
        expirationdate.setValue(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IfFromApplyingCommand = ApplyingCommandController.IfApplyingCommandIsOpen;
        if (ApplyingCommandController.IfApplyingCommandIsOpen) {
            ProvidersComboBox.setVisible(false);
            barcode.setText(ApplyingCommandController.TheProduct.getBarcode().toString());
            quantity.setText(ApplyingCommandController.TheProduct.getNeededQuantity().toString());
            productname.setText(ApplyingCommandController.TheProduct.getProductName());
            TheIndex = ApplyingCommandController.IndexOfProduct;


        } else {
            ProvidersComboBox.setVisible(true);

            ProvidersComboBox.setItems(imProviderController.ProviderList);
        }
    }



    public static void UpdateProvider(Integer Id, float TotalFigure) throws SQLException {
        String Sql = "UPDATE ProvidersInfo set TotalFigure=? WHERE id=? and userID=?";
        Connection connection = ConnectionClass.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(Sql);
        preparedStatement.setFloat(1, TotalFigure);
        preparedStatement.setInt(2, Id);
        preparedStatement.setInt(3, user.getUserID());
        preparedStatement.executeUpdate();
    }

    void SetThatWasAdded(Integer Barcode, Integer Id) throws SQLException {
        String Sql = "UPDATE ProductsCommand set IfWasAdded=? WHERE  id =? and barcode=? and userID=?";
        Connection connection = ConnectionClass.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(Sql);
        preparedStatement.setBoolean(1, true);
        preparedStatement.setInt(2, Id);
        preparedStatement.setInt(3, Barcode);
        preparedStatement.setInt(4, user.getUserID());
        preparedStatement.executeUpdate();
    }

    @FXML
    private void checkboxAction() {
        if (expirationCheck.isSelected()) {
            expirationdate.setVisible(true);
        }
        if (!expirationCheck.isSelected()) {
            expirationdate.setVisible(false);
        }
    }
}

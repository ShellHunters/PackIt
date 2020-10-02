package interfaceMagazinier.stock.add;

import Connector.ConnectionClass;
import basicClasses.Provider;
import basicClasses.product;
import basicClasses.user;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import interfaceMagazinier.providers.ApplyingCommandController;
import interfaceMagazinier.providers.CommandHistoryController;
import interfaceMagazinier.providers.SendEmailController;
import interfaceMagazinier.providers.imProviderController;
import interfaceMagazinier.settings.preference.preferencesController;
import interfaceMagazinier.stock.imStockController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.math.RoundingMode;
import java.net.URL;
import java.sql.*;
import java.util.Collection;
import java.util.ResourceBundle;

public class addController implements Initializable {

    @FXML
    JFXTextField productname;
    @FXML
  public   JFXTextField barcode;
    @FXML
    JFXTextField sellprice;
    @FXML
    JFXTextField buyprice;
    @FXML
    JFXTextField quantity;
    @FXML
    JFXDatePicker expirationdate;
    @FXML
    private JFXCheckBox expirationCheck;
    @FXML
    private JFXComboBox<String> productTypeComboBox;
    @FXML
    Label errorLabel;
    @FXML
    private JFXComboBox<Provider> providersComboBox;
    @FXML
    private JFXCheckBox placeCheck;
    @FXML
    private JFXCheckBox providerCheck;
    @FXML
    private JFXCheckBox productTypeCheck;
    @FXML
    private JFXTextField containerName;
    @FXML
    private JFXTextField floorNumber;
    public static ObservableList<Provider> ProvidersListForComboBox = FXCollections.observableArrayList();
    public boolean IfFromApplyingCommand;
    Integer TheIndex;
    private boolean addInFullStock = false;
    private boolean addOnlyQuantity = false;
    private boolean isAddOnlyQuantityInFullStock = false;
    private int oldQuantity = 0;
private String providerEmail;
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
        if (addOnlyQuantity || isAddOnlyQuantityInFullStock) {
            String tableName;
            if (addOnlyQuantity) {
                addOnlyQuantity = false;
                tableName = "stock";
                imStockController.products.forEach(product -> {
                    if (product.getBarcode() == Integer.parseInt(barcode.getText())) {
                        product.setQuantity(oldQuantity + Integer.parseInt(quantity.getText()));
                        product.setInitialQuantity(product.getQuantity());
                        return;
                    }
                });
            } else {
                isAddOnlyQuantityInFullStock = false;
                tableName = "fullStock";
            }
            sql = "UPDATE " + tableName + " SET quantity=" + (oldQuantity + Integer.parseInt(quantity.getText())) + ", initialQuantity=" + (oldQuantity + Integer.parseInt(quantity.getText())) + " WHERE barcode=" + Integer.parseInt(barcode.getText());
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
        product.setContainerName(containerName.getText());
        System.out.println("solved " + productTypeComboBox.getValue());
        product.setProductType(productTypeComboBox.getValue());
        try {
            product.setFloor(Integer.parseInt(floorNumber.getText()));
        }catch (Exception e){

        }

        if (SendEmailController.IfTabPaneIsOpen) {
            SendEmailController.ProductList.add(product);
            product brin = SendEmailController.ProductList.get(SendEmailController.ProductList.size() - 1);
            SendEmailController.TempoListOfProducts.add(product);
        }

        if (!addInFullStock) imStockController.products.add(product);

        String tableName = "stock";
        if (addInFullStock) tableName = "fullStock";
        addInFullStock = false;
        if (IfFromApplyingCommand) {
            totalfigure = ((ApplyingCommandController.TheProvider.getTotalFigure() + (float) quanity * buyPrice));
            System.out.println("in add scene  " + totalfigure);
         //   addProvider(ApplyingCommandController.TheProvider.getEmail(),tableName);

            SendEmailController. df.setRoundingMode(RoundingMode.UP);
            ApplyingCommandController.TheProvider.setTotalFigure(Float.parseFloat( SendEmailController.df.format( totalfigure)));
            providerEmail=ApplyingCommandController.TheProvider.getEmail();
            UpdateProvider(ApplyingCommandController.TheProvider.getId(), totalfigure);
imProviderController.ProviderList.set(ApplyingCommandController.TheProvider.getId(),ApplyingCommandController.TheProvider);
            product theProduct = ApplyingCommandController.ProductList.get(TheIndex);
            theProduct.setIfWasAdded(true);
            CommandHistoryController.command.getListOfProducts().set(TheIndex, theProduct);
            //ApplyingCommandController.ProductList.set(TheIndex, theProduct);
            SetThatWasAdded(barcode, CommandHistoryController.command.getId());
            ApplyingCommandController.InitTable();
        } else if (providerCheck.isSelected()) {
            totalfigure = ((providersComboBox.getValue().getTotalFigure() + (float) quanity * buyPrice));
            providerEmail=providersComboBox.getValue().getEmail();
            Provider addingProvider;
            addingProvider=imProviderController.ProviderList.get(providersComboBox.getValue().getId());
            addingProvider.setTotalFigure(totalfigure);
            imProviderController.ProviderList.set(providersComboBox.getValue().getId(),addingProvider);
            UpdateProvider(providersComboBox.getValue().getId(), totalfigure);
        }


        String query = "INSERT INTO " + tableName + " (barcode, buyprice, expirationdate, name, quantity, sellprice, initialQuantity, userID, floor, containerName, productType,providerEmail) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, barcode);
        preparedStatement.setString(2, buyprice.getText());
        if (!expirationdateString.equals("") && expirationCheck.isSelected())
            preparedStatement.setString(3, expirationdateString);
        else preparedStatement.setNull(3, Types.DATE);
        preparedStatement.setString(4, productname.getText());
        preparedStatement.setInt(5, quanity);
        preparedStatement.setString(6, sellprice.getText());
        preparedStatement.setInt(7, quanity);
        preparedStatement.setInt(8, user.getUserID());
        if (!floorNumber.getText().isEmpty() && placeCheck.isSelected())
            preparedStatement.setString(9, floorNumber.getText());
        else preparedStatement.setNull(9, Types.INTEGER);
        if (!containerName.getText().isEmpty() && placeCheck.isSelected())
            preparedStatement.setString(10, containerName.getText());
        else preparedStatement.setNull(10, Types.VARCHAR);
        if (productTypeComboBox.getValue() != null) preparedStatement.setString(11, productTypeComboBox.getValue());
        else preparedStatement.setNull(11, Types.VARCHAR);
if (providerEmail!=null)
    preparedStatement.setString(12,providerEmail);
        preparedStatement.execute();



        connection.close();
        int i=0;
        System.out.println("testing barcode "+(barcode));

        for (product myProduct : imStockController.products){
            if (myProduct.getBarcode().equals(barcode))
            {

                if (myProduct.getTotalStock()!=null)
                myProduct.setTotalStock(myProduct.getTotalStock()+Integer.parseInt( this.quantity.getText()));
                else
                    myProduct.setTotalStock(myProduct.getQuantity()+Integer.parseInt( this.quantity.getText()));
                imStockController.products.set(i, myProduct);
                break;
            }

                i++;

        }


        resetFields();
    }

    private boolean errorCheck() throws SQLException {
        if (productname.getText().isEmpty()
                || barcode.getText().isEmpty()
                || sellprice.getText().isEmpty()
                || buyprice.getText().isEmpty()
                || quantity.getText().isEmpty()
                || (!containerName.isDisable() && (containerName.getText().isEmpty() || floorNumber.getText().isEmpty()))
                || (!expirationdate.isDisable() && expirationdate.getValue() == null)
                || (!productTypeComboBox.isDisable() && productTypeComboBox.getValue() == null)
                || (!providersComboBox.isDisable() && providersComboBox.getValue() == null)) {
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
        errorLabel.setTextFill(Paint.valueOf("green"));
        errorLabel.setText("product added succefully");

        Connection connection = ConnectionClass.getConnection();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM stock WHERE barcode=" + barcode.getText();
        ResultSet rs = statement.executeQuery(query);
        if (rs.next()) {
            if (!rs.getString("name").equals(productname.getText()) && rs.getInt("barcode") == Integer.parseInt(barcode.getText())) {
                errorLabel.setTextFill(Paint.valueOf("red"));
                errorLabel.setText("The barcode is already used for an other product");
            }
            if (rs.getFloat("buyprice") == Float.parseFloat(buyprice.getText()) && rs.getFloat("sellprice") == Float.parseFloat(sellprice.getText())) {
                addOnlyQuantity = true;
                oldQuantity = rs.getInt("quantity");
            } else {
                query = "SELECT * FROM fullStock WHERE barcode=" + barcode.getText() + " AND sellPrice=" + sellprice.getText() + " AND buyPrice=" + buyprice.getText();
                rs = statement.executeQuery(query);
                if (rs.next()) {
                    isAddOnlyQuantityInFullStock = true;
                    oldQuantity = rs.getInt("quantity");
                } else addInFullStock = true;
            }

        }
        return false;
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
            providersComboBox.setVisible(false);
            barcode.setText(ApplyingCommandController.TheProduct.getBarcode().toString());
            quantity.setText(ApplyingCommandController.TheProduct.getNeededQuantity().toString());
            productname.setText(ApplyingCommandController.TheProduct.getProductName());
            TheIndex = ApplyingCommandController.IndexOfProduct;
        } else {
            providersComboBox.setVisible(true);
            providersComboBox.setItems(imProviderController.ProviderList);
        }

        productTypeComboBox.setItems(preferencesController.productTypes);
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
    private void expirationCheckAction() {
        expirationdate.setDisable(!expirationCheck.isSelected());
    }

    @FXML
    private void placeCheckAction() {
        containerName.setDisable(!placeCheck.isSelected());
        floorNumber.setDisable(!placeCheck.isSelected());
    }

    @FXML
    private void providerCheckAction() {
        providersComboBox.setDisable(!providerCheck.isSelected());
    }

    @FXML
    private void productTypeCheckAction() {
        productTypeComboBox.setDisable(!productTypeCheck.isSelected());
    }
}

package interfaceMagazinier.stock.fullStock;

import Connector.ConnectionClass;
import basicClasses.Provider;
import basicClasses.product;
import basicClasses.user;
import com.jfoenix.controls.JFXTextField;
import interfaceMagazinier.stock.imStockController;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static interfaceMagazinier.stock.imStockController.products;
import static interfaceMagazinier.stock.imStockController.theFullStockProduct;

public class fullStockController implements Initializable {


    public static ObservableList<product> fullStockProductList = FXCollections.observableArrayList();
    @FXML
    public StackPane stackPane;

    @FXML
    public JFXTextField searchTextField;

    @FXML
    public TableView<product> table;

    @FXML
    TableColumn<product, Number> barcodeColumn;
    @FXML
    TableColumn<product, String> nameColumn;
    @FXML
    TableColumn<product, Number> quantityColumn;
    @FXML
    TableColumn<product, Number> buypriceColumn;
    @FXML
    TableColumn<product, Number> sellpriceColumn;
    @FXML
    TableColumn<product, String> expirationdateColumn;

    @FXML
    private Label productProviderLabel;
    @FXML
    private Label productLabel;
    @FXML
    private TableColumn<product, String> providerEmailColumn;
    @FXML
    public TableColumn<Provider, Boolean> applyingProductColumn;
    public static product fullStackApplyProduct;

    public class CustomButtonCell<T, S> extends TableCell<T, S> {
        private Button ApplyButton = new Button("Apply");


        @Override
        protected void updateItem(S item, boolean empty) {

            super.updateItem(item, empty);
            if (empty || item == null) {
                this.setText("");
                this.setGraphic(null);
            } else {
                product MyProduct = (product) this.getTableView().getItems().get(getIndex());
                ApplyButton.setOnAction(event -> {
                    Integer index = this.getIndex();
                    System.out.println("the index of apply " + fullStockProductList);

                    fullStackApplyProduct = (product) this.getTableView().getItems().get(index);
                    product zeroElement=(product) this.getTableView().getItems().get(0);

                    fullStackApplyProduct.setTotalStock(imStockController.theFullStockProduct.getTotalStock());
                    fullStockProductList.set(index,zeroElement);
                    fullStockProductList.set(0,fullStackApplyProduct );
                    products.set(imStockController.indexOfProduct, fullStackApplyProduct);


                    try {

                        overriding(imStockController.theFullStockProduct, fullStackApplyProduct);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    product tempoProduct = fullStackApplyProduct;
                    fullStackApplyProduct = theFullStockProduct;
                    theFullStockProduct = tempoProduct;
                    imStockController.buyPriceProduct=theFullStockProduct.getBuyPrice();
                    imStockController.sellPriceProduct=theFullStockProduct.getSellPrice();



                });
if (MyProduct.getBuyPrice()==imStockController.buyPriceProduct&&MyProduct.getSellPrice()==imStockController.sellPriceProduct)
{
    this.setText("");
    this.setGraphic(null);
}
              else  this.setGraphic(ApplyButton);
            }
        }


    }

    ObservableList<product> searchInFullStockTable(Integer barcode) throws SQLException {

        Connection connection = ConnectionClass.getConnection();
        ObservableList<product> products = FXCollections.observableArrayList();
        try {
            String Sql = "SELECT * FROM fullStock WHERE barcode=? and UserID=?";
            PreparedStatement preparedStatement = connection.prepareStatement(Sql);
            preparedStatement.setInt(1, barcode);
            preparedStatement.setInt(2, user.getUserID());
            ResultSet rs = preparedStatement.executeQuery();


            while (rs.next()) {
                product newProduct;
                try {
                    newProduct = new product(rs.getString("name"), rs.getInt("barcode"), rs.getFloat("buyprice"), rs.getFloat("sellprice"), rs.getInt("quantity"), rs.getDate("expirationdate").toString());
                } catch (Exception e) {
                    newProduct = new product(rs.getString("name"), rs.getInt("barcode"), rs.getFloat("buyprice"), rs.getFloat("sellprice"), rs.getInt("quantity"), "");

                }
                newProduct.setProviderEmail(rs.getString("providerEmail"));
                newProduct.setInitialQuantity(rs.getInt("initialQuantity"));
                newProduct.setNumberOfSells(rs.getInt("numberOfSells"));


                if (rs.getString("productType") != null) newProduct.setProductType(rs.getString("productType"));
                else newProduct.setProductType("");
                if (rs.getString("containerName") != null) {
                    newProduct.setContainerName(rs.getString("containerName"));
                }
                else newProduct.setContainerName("");
                newProduct.setFloor(rs.getInt("floor"));
                newProduct.setProviderEmail(rs.getString("providerEmail"));
                products.add(newProduct);
            }
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    void overriding(product theFullStockProduct, product theFullStockApplyProduct) throws SQLException {
        System.out.println("the number of sells1  " + theFullStockProduct.getNumberOfSells());
        System.out.println("the number of sells2  " + theFullStockApplyProduct.getNumberOfSells());
        if (theFullStockProduct.getExpirationDate() == "" && theFullStockApplyProduct.getExpirationDate() == "") {
            theFullStockProduct.setExpirationDate(null);
            theFullStockApplyProduct.setExpirationDate(null);
        }

        Connection connection = ConnectionClass.getConnection();
        String Sql = "UPDATE  fullStock set buyprice=? , sellprice=? , numberOfSells=? , initialQuantity=? , quantity=? , expirationdate=? , floor=?,containerName=?,providerEmail=? WHERE userID=? AND barcode=? and buyprice=? and sellprice=?";
        PreparedStatement preparedStatement = connection.prepareStatement(Sql);
        preparedStatement.setFloat(1, theFullStockProduct.getBuyPrice());
        preparedStatement.setFloat(2, theFullStockProduct.getSellPrice());
        preparedStatement.setInt(3, theFullStockProduct.getNumberOfSells());
        preparedStatement.setInt(4, theFullStockProduct.getInitialQuantity());
        preparedStatement.setInt(5, theFullStockProduct.getQuantity());
        preparedStatement.setString(6, theFullStockProduct.getExpirationDate());
        preparedStatement.setInt(7, theFullStockProduct.getFloor());
        preparedStatement.setString(8, theFullStockProduct.getContainerName());
        preparedStatement.setString(9, theFullStockProduct.getProviderEmail());
        preparedStatement.setInt(10, user.getUserID());
        preparedStatement.setInt(11, theFullStockProduct.getBarcode());
        preparedStatement.setFloat(12, theFullStockApplyProduct.getBuyPrice());
        preparedStatement.setFloat(13, theFullStockApplyProduct.getSellPrice());
        preparedStatement.executeUpdate();

        Sql = "UPDATE  stock set buyprice=? , sellprice=? , numberOfSells=? , initialQuantity=? , quantity=? , expirationdate=?,  floor=?,containerName=?,providerEmail=? WHERE userID=? AND barcode=?";
        preparedStatement = connection.prepareStatement(Sql);

        preparedStatement.setFloat(1, theFullStockApplyProduct.getBuyPrice());
        preparedStatement.setFloat(2, theFullStockApplyProduct.getSellPrice());
        preparedStatement.setInt(3, theFullStockApplyProduct.getNumberOfSells());
        preparedStatement.setInt(4, theFullStockApplyProduct.getInitialQuantity());
        preparedStatement.setInt(5, theFullStockApplyProduct.getQuantity());
        preparedStatement.setString(6, theFullStockApplyProduct.getExpirationDate());
        preparedStatement.setInt(7, theFullStockApplyProduct.getFloor());
        preparedStatement.setString(8, theFullStockApplyProduct.getContainerName());
        preparedStatement.setString(9, theFullStockApplyProduct.getProviderEmail());
        preparedStatement.setInt(10, user.getUserID());
        preparedStatement.setInt(11, theFullStockApplyProduct.getBarcode());
        preparedStatement.executeUpdate();


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        barcodeColumn.setCellValueFactory(param -> {
            return param.getValue().barcodeProperty();
        });
        nameColumn.setCellValueFactory(param -> {
            return param.getValue().productNameProperty();
        });
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        quantityColumn.setCellValueFactory(param -> {
            return param.getValue().quantityProperty();
        });
        buypriceColumn.setCellValueFactory(param -> {
            return param.getValue().buyPriceProperty();
        });
        sellpriceColumn.setCellValueFactory(param -> {
            return param.getValue().sellPriceProperty();
        });
        expirationdateColumn.setCellValueFactory(param -> {
            return param.getValue().expirationDateProperty();
        });
        providerEmailColumn.setCellValueFactory(param -> {
            return param.getValue().providerEmailProperty();
        });


        applyingProductColumn.setCellValueFactory(call -> new SimpleBooleanProperty(true).asObject());
        applyingProductColumn.setCellFactory(call -> {
            return new CustomButtonCell<>();
        });

        fullStockProductList.clear();

        try {
            fullStockProductList.add(imStockController.theFullStockProduct);
            fullStockProductList.addAll(searchInFullStockTable(imStockController.theFullStockProduct.getBarcode()));
            for (product Myproduct :fullStockProductList){

                System.out.println("sell price "+Myproduct.getSellPrice() +" Buy price "+Myproduct.getBuyPrice());

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        FilteredList<product> Results = new FilteredList<>(fullStockProductList, b -> true);
        searchTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            Results.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                String lowerCaseFilter = newValue.toLowerCase();
                if (product.getProductName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (String.valueOf(product.getBarcode()).toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (String.valueOf(product.getBuyPrice()).toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (String.valueOf(product.getInitialQuantity()).toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (String.valueOf(product.getQuantity()).toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (String.valueOf(product.getStockPercentage()).toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                    //else if (product.getProductType().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    //  return true;
                else
                    return false;
            });
        }));

        SortedList<product> SortedResult = new SortedList<>(Results);
        SortedResult.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(SortedResult);
        System.out.println("initialize");
    }

}

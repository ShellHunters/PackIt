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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
                ApplyButton.setOnAction(event -> {
                    Integer index = this.getIndex();
                    System.out.println("the index of apply " + fullStockProductList);

                    fullStackApplyProduct = (product) this.getTableView().getItems().get(index);
                    products.set(imStockController.indexOfProduct, fullStackApplyProduct);

                    fullStockProductList.set(index, imStockController.theFullStockProduct);
                    try {
                        overriding(imStockController.theFullStockProduct, fullStackApplyProduct);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    product tempoProduct = fullStackApplyProduct;
                    fullStackApplyProduct = theFullStockProduct;
                    theFullStockProduct = tempoProduct;

                });

                this.setGraphic(ApplyButton);
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
                newProduct.setInitialQuantity(rs.getInt("initialQuantity"));
                newProduct.setNumberOfSells(rs.getInt("numberOfSells"));
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
        String Sql = "UPDATE  fullStock set buyprice=? , sellprice=? , numberOfSells=? , initialQuantity=? , quantity=? , expirationdate=? WHERE userID=? AND barcode=? and buyprice=? and sellprice=?";
        PreparedStatement preparedStatement = connection.prepareStatement(Sql);
        preparedStatement.setFloat(1, theFullStockProduct.getBuyPrice());
        preparedStatement.setFloat(2, theFullStockProduct.getSellPrice());
        preparedStatement.setInt(3, theFullStockProduct.getNumberOfSells());
        preparedStatement.setInt(4, theFullStockProduct.getInitialQuantity());
        preparedStatement.setInt(5, theFullStockProduct.getQuantity());
        preparedStatement.setString(6, theFullStockProduct.getExpirationDate());
        preparedStatement.setInt(7, user.getUserID());
        preparedStatement.setInt(8, theFullStockProduct.getBarcode());
        preparedStatement.setFloat(9, theFullStockApplyProduct.getBuyPrice());
        preparedStatement.setFloat(10, theFullStockApplyProduct.getSellPrice());
        preparedStatement.executeUpdate();

        Sql = "UPDATE  stock set buyprice=? , sellprice=? , numberOfSells=? , initialQuantity=? , quantity=? , expirationdate=? WHERE userID=? AND barcode=?";
        preparedStatement = connection.prepareStatement(Sql);

        preparedStatement.setFloat(1, theFullStockApplyProduct.getBuyPrice());
        preparedStatement.setFloat(2, theFullStockApplyProduct.getSellPrice());
        preparedStatement.setInt(3, theFullStockApplyProduct.getNumberOfSells());
        preparedStatement.setInt(4, theFullStockApplyProduct.getInitialQuantity());
        preparedStatement.setInt(5, theFullStockApplyProduct.getQuantity());
        preparedStatement.setString(6, theFullStockApplyProduct.getExpirationDate());
        preparedStatement.setInt(7, user.getUserID());
        preparedStatement.setInt(8, theFullStockApplyProduct.getBarcode());
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
        applyingProductColumn.setCellValueFactory(call -> new SimpleBooleanProperty(true).asObject());
        applyingProductColumn.setCellFactory(call -> {
            return new CustomButtonCell<>();
        });

        fullStockProductList.clear();
        try {
            fullStockProductList.addAll(searchInFullStockTable(imStockController.theFullStockProduct.getBarcode()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        table.setItems(fullStockProductList);
    }

}

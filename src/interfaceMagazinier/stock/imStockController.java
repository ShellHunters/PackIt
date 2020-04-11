package interfaceMagazinier.stock;

import Connection.ConnectionClass;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import basicClasses.product;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class imStockController implements Initializable {
    @FXML TableView<product> table;
    @FXML TableColumn<product, Number> barcodeColumn;
    @FXML TableColumn<product, String> nameColumn;
    @FXML TableColumn<product, Number> quantityColumn;
    @FXML TableColumn<product, Number> buypriceColumn;
    @FXML TableColumn<product, Number> sellpriceColumn;
    @FXML TableColumn<product, LocalDate> expirationdateColumn;
    @FXML TableColumn selectedColumn;
    @FXML JFXTextField searchTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableSetUp();
    }

    void tableSetUp(){
        //Table structure
        barcodeColumn.setCellValueFactory(param -> { return param.getValue().barcodeProperty(); });

        nameColumn.setCellValueFactory(param -> { return  param.getValue().productNameProperty(); });
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        quantityColumn.setCellValueFactory(param -> { return param.getValue().quantityProperty(); });

        buypriceColumn.setCellValueFactory(param -> { return param.getValue().buyPriceProperty(); });

        sellpriceColumn.setCellValueFactory(param -> { return  param.getValue().sellPriceProperty(); });

        expirationdateColumn.setCellValueFactory(param -> { return param.getValue().expirationDateProperty(); });

        selectedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectedColumn));

        //Table content
        ObservableList<product> products = FXCollections.observableArrayList();

        //Using database to read products
        products = loadProducts();

        FilteredList<product> filteredData = new FilteredList<>(products, product -> true);
        searchTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowercaseFilter = newValue.toLowerCase();
                if (product.getProductName().toLowerCase().indexOf(lowercaseFilter) != -1) return true;
                else if (Integer.toString(product.getBarcode()).indexOf(lowercaseFilter) != -1) return true;
                else if (Integer.toString(product.getQuantity()).indexOf(lowercaseFilter) != -1) return true;
                else if (Float.toString(product.getBuyPrice()).indexOf(lowercaseFilter) != -1) return true;
                else if (Float.toString(product.getSellPrice()).indexOf(lowercaseFilter) != -1) return true;
                else return false;
            });
        }));
        SortedList<product> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }

    private ObservableList<product> loadProducts() {
        ObservableList<product> products = FXCollections.observableArrayList();
        Connection connection = ConnectionClass.getConnection();
        String query = "SELECT * FROM stock"; //Change this to read only the products who belongs to the user
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                product newProduct = new product(rs.getString("name"), rs.getInt("barcode"), rs.getFloat("buyprice"), rs.getFloat("sellprice"), rs.getInt("quantity"), rs.getDate("localdate").toLocalDate());
                products.add(newProduct);
            }
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
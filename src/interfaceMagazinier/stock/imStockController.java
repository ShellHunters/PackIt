package interfaceMagazinier.stock;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import basicClasses.product;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    @FXML TableColumn<product, Boolean> selectedColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableSetUp();
    }

    void tableSetUp(){
        barcodeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<product, Number>, ObservableValue<Number>>() {
            @Override
            public ObservableValue<Number> call(TableColumn.CellDataFeatures<product, Number> param) {
                return param.getValue().barcodeProperty();
            }
        });

        nameColumn.setCellValueFactory(param -> { return  param.getValue().productNameProperty(); });
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        quantityColumn.setCellValueFactory(param -> { return param.getValue().quantityProperty(); });

        buypriceColumn.setCellValueFactory(param -> { return param.getValue().buyPriceProperty(); });

        sellpriceColumn.setCellValueFactory(param -> { return  param.getValue().sellPriceProperty(); });

        expirationdateColumn.setCellValueFactory(param -> { return param.getValue().expirationDateProperty(); });

        selectedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectedColumn));

        ObservableList<product> products = FXCollections.observableArrayList();
        //Using database to read products
        products.add(new product("merguez", 2, 3, 3.5f, 2, null));
        products.add(new product("ja3far", 23, 5, 53, 2, null));

        table.setItems(products);
    }
}



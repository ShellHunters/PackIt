package interfaceMagazinier.stock;

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
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class imStockController implements Initializable {
    @FXML JFXTreeTableView<product> table;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableSetUp();
    }

    void tableSetUp(){
        //Table construction
        JFXTreeTableColumn<product, Number> barcode = new JFXTreeTableColumn<>("Barcode");
        barcode.setPrefWidth(200);
        barcode.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<product, Number>, ObservableValue<Number>>() {
            @Override
            public ObservableValue<Number> call(TreeTableColumn.CellDataFeatures<product, Number> param) {
                return param.getValue().getValue().barcodeProperty();
            }
        });

        JFXTreeTableColumn<product, String> productName = new JFXTreeTableColumn<>("Name");
        productName.setPrefWidth(300);
        productName.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<product, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<product, String> param) {
                return param.getValue().getValue().productNameProperty();
            }
        });


        JFXTreeTableColumn<product, Number> sellPrice = new JFXTreeTableColumn<>("Sell price");
        sellPrice.setPrefWidth(200);
        sellPrice.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<product, Number>, ObservableValue<Number>>() {
            @Override
            public ObservableValue<Number> call(TreeTableColumn.CellDataFeatures<product, Number> param) {
                return param.getValue().getValue().sellPriceProperty();
            }
        });

        JFXTreeTableColumn<product, Number> buyPrice = new JFXTreeTableColumn<>("Buy price");
        buyPrice.setPrefWidth(200);
        buyPrice.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<product, Number>, ObservableValue<Number>>() {
            @Override
            public ObservableValue<Number> call(TreeTableColumn.CellDataFeatures<product, Number> param) {
                return param.getValue().getValue().buyPriceProperty();
            }
        });

        JFXTreeTableColumn<product, Number> quantity = new JFXTreeTableColumn<>("Quantity");
        quantity.setPrefWidth(100);
        quantity.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<product, Number>, ObservableValue<Number>>() {
            @Override
            public ObservableValue<Number> call(TreeTableColumn.CellDataFeatures<product, Number> param) {
                return param.getValue().getValue().quantityProperty();
            }
        });

        JFXTreeTableColumn<product, LocalDate> expirationDate = new JFXTreeTableColumn<>("Expiration Date");
        expirationDate.setPrefWidth(250);
        expirationDate.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<product, LocalDate>, ObservableValue<LocalDate>>() {
            @Override
            public ObservableValue<LocalDate> call(TreeTableColumn.CellDataFeatures<product, LocalDate> param) {
                return param.getValue().getValue().expirationDateProperty();
            }
        });

        ObservableList<product> products = FXCollections.observableArrayList();
        //Using database to read products
        products.add(new product(new SimpleStringProperty("MERGUEZ"), new SimpleIntegerProperty(40), new SimpleFloatProperty(40.5f), new SimpleFloatProperty(40.51f), new SimpleIntegerProperty(40), null));
        products.add(new product(new SimpleStringProperty("sosig"), new SimpleIntegerProperty(40), new SimpleFloatProperty(40.5f), new SimpleFloatProperty(40.51f), new SimpleIntegerProperty(40), null));
        products.add(new product(new SimpleStringProperty("ja3far"), new SimpleIntegerProperty(40), new SimpleFloatProperty(40.5f), new SimpleFloatProperty(40.51f), new SimpleIntegerProperty(40), null));

        final TreeItem<product> root = new RecursiveTreeItem<product>(products, RecursiveTreeObject::getChildren);
        table.getColumns().setAll(barcode, productName, quantity, buyPrice, sellPrice, expirationDate);
        table.setRoot(root);
        table.setShowRoot(false);
    }
}

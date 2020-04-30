package interfaceMagazinier.stock;

import Connection.ConnectionClass;
import com.jfoenix.controls.*;
import basicClasses.product;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.controls.events.JFXDialogEvent;
import interfaceMagazinier.stock.update.updateController;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import javax.swing.text.DateFormatter;
import javax.xml.transform.Source;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class imStockController implements Initializable {
    @FXML StackPane stackPane;
    @FXML TableView<product> table;
    public static ObservableList<product> products;
    @FXML TableColumn<product, Number> barcodeColumn;
    @FXML TableColumn<product, String> nameColumn;
    @FXML TableColumn<product, Number> quantityColumn;
    @FXML TableColumn<product, Number> buypriceColumn;
    @FXML TableColumn<product, Number> sellpriceColumn;
    @FXML TableColumn<product, String> expirationdateColumn;
    @FXML TableColumn selectedColumn;
    @FXML JFXTextField searchTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        shortcutSetUp();
        tableSetUp();
    }

    void shortcutSetUp(){
        stackPane.getScene().setOnKeyPressed(event -> {
        });
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

        selectedColumn.setCellValueFactory(new PropertyValueFactory<product,String>("checkbox"));

        //Table content
        products = FXCollections.observableArrayList();
        products = loadProducts();

        FilteredList<product> filteredData = new FilteredList<>(products, product -> true);
        searchTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowercaseFilter = newValue.toLowerCase();
                if (product.getProductName().toLowerCase().indexOf(lowercaseFilter) != -1) return true;
                else if (Integer.toString(product.getBarcode()).indexOf(lowercaseFilter) != -1) return true;
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
                product newProduct = new product(rs.getString("name"), rs.getInt("barcode"), rs.getFloat("buyprice"), rs.getFloat("sellprice"), rs.getInt("quantity"), rs.getDate("localdate").toString());
                products.add(newProduct);
            }
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addProduct(javafx.event.ActionEvent event) throws IOException {
//        get source to change the button color
        JFXButton source = (JFXButton) event.getSource();
        source.getStyleClass().add("pressed");
//        Used region for the animation
        Region root1 = FXMLLoader.load(getClass().getResource("add/addProduct.fxml"));
        JFXDialog add = new JFXDialog(stackPane, root1, DialogTransition.RIGHT);
        add.show();
        //Change back the button to default color
        add.setOnDialogClosed(event1 ->{ source.getStyleClass().removeAll("pressed"); });
    }

    public void removeProduct() throws SQLException{
        Connection connection = ConnectionClass.getConnection();
        Statement statement = connection.createStatement();
        ObservableList<product> removedProduct = FXCollections.observableArrayList();
        for (product bean : products )
            if (bean.getCheckbox().isSelected()) {
                removedProduct.add(bean);
                String sql = "INSERT INTO removedproducts (name,barcode,sellprice,buyprice,quantity,localdate) VALUES ('" + bean.getProductName() + "', '" + bean.getBarcode() + "', '" + bean.getSellPrice() + "', '" +bean.getBuyPrice() + "', '" +bean.getQuantity()+ "', '" + bean.getExpirationDate() + "')";
                statement.executeUpdate(sql);

                String sqlDelete = "DELETE FROM stock WHERE barcode=?"   ;
                PreparedStatement pst = connection.prepareStatement(sqlDelete);
                pst.setInt(1,bean.getBarcode());
                pst.executeUpdate();
                pst.close() ;
            }
        products.removeAll(removedProduct) ;
    }

    public void updateProduct(javafx.event.ActionEvent event) throws IOException {
        ObservableList<product> updatedProduct = FXCollections.observableArrayList();
        for (product updated : products )
            if (updated.getCheckbox().isSelected()) {
                updateController.setProductSelected(updated);
                updatedProduct.add(updated);
                //get source to change the button color
                JFXButton source = (JFXButton) event.getSource();
                source.getStyleClass().add("pressed");
                //Used region for the animation
                Region root1 = FXMLLoader.load(getClass().getResource("update/updateProduct.fxml"));
                JFXDialog update = new JFXDialog(stackPane, root1, DialogTransition.RIGHT);
                update.show();
                //Change back the button to default color
                update.setOnDialogClosed(event1 -> { source.getStyleClass().removeAll("pressed"); });
            }
    }
}
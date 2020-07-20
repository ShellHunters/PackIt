package interfaceClient;

import Connector.ConnectionClass;
import basicClasses.product;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import identification.identificationMain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class icMainController implements Initializable {
    @FXML StackPane stackPane;
    @FXML TableView<product> table;
    public static ObservableList<product> products;
    @FXML TableColumn<product, Number> barcodeColumn;
    @FXML TableColumn<product, String> nameColumn;
    @FXML TableColumn<product, Number> priceColumn;
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

        priceColumn.setCellValueFactory(param -> { return  param.getValue().sellPriceProperty(); });

        //Table content
        products = FXCollections.observableArrayList();
        products = loadProducts();

        FilteredList<product> filteredData = new FilteredList<>(products, product -> true);
        searchTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowercaseFilter = newValue.toLowerCase();
                if (product.getProductName().toLowerCase().contains(lowercaseFilter)) return true;
                else if (Integer.toString(product.getBarcode()).contains(lowercaseFilter)) return true;
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
                product newProduct;
                try {
                    newProduct = new product(rs.getString("name"), rs.getInt("barcode"), rs.getFloat("buyprice"), rs.getFloat("sellprice"), rs.getInt("quantity"), rs.getDate("expirationdate").toString());
                } catch (Exception e){
                    newProduct = new product(rs.getString("name"), rs.getInt("barcode"), rs.getFloat("buyprice"), rs.getFloat("sellprice"), rs.getInt("quantity"), "");
                }
                products.add(newProduct);
            }
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }




    @FXML
    void close(ActionEvent event) throws Exception {
        ((Stage) stackPane.getScene().getWindow()).close();
        Stage loginStage = new Stage();
        identificationMain loginInterface = new identificationMain();
        loginInterface.start(loginStage);

    }
    public void exit() {System.exit(0);}

    public void requestProduct(ActionEvent event) throws IOException {
        //        get source to change the button color
        JFXButton source = (JFXButton) event.getSource();
        source.getStyleClass().add("pressed");
//        Used region for the animation
        Region root1 = FXMLLoader.load(getClass().getResource("request/request.fxml"));
        JFXDialog add = new JFXDialog(stackPane, root1, JFXDialog.DialogTransition.RIGHT);
        add.show();
        //Change back the button to default color
        add.setOnDialogClosed(event1 ->{ source.getStyleClass().removeAll("pressed"); });
    }
}
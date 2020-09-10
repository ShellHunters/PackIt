package interfaceFournisseur;

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
import Connector.* ;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import static interfaceMagazinier.stock.imStockController.products;


public class ifMainController implements Initializable {
    @FXML private TableView<product> needTable;
    @FXML private TableColumn<product,Number> barcodeColumn;
    @FXML private TableColumn<product,String> nameColumn;
    @FXML private TableColumn<product,Number> quantityColumn;
    @FXML private JFXTextField searchfield;
    @FXML private StackPane providerStackPane;



    @FXML
    void close(ActionEvent event) throws Exception {
        ((Stage) providerStackPane.getScene().getWindow()).close();
        Stage loginStage = new Stage();
        identificationMain loginInterface = new identificationMain();
        loginInterface.start(loginStage);

    }
     public void exit() {System.exit(0);}

    public void suggestProduct(ActionEvent event) throws IOException {
        //        get source to change the button color
        JFXButton source = (JFXButton) event.getSource();
        source.getStyleClass().add("pressed");
//        Used region for the animation
        Region root1 = FXMLLoader.load(getClass().getResource("suggest/suggest.fxml"));
        JFXDialog add = new JFXDialog(providerStackPane, root1, JFXDialog.DialogTransition.RIGHT);
        add.show();
        //Change back the button to default color
        add.setOnDialogClosed(event1 ->{ source.getStyleClass().removeAll("pressed"); });
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {tableSetUp();}
        void tableSetUp(){
            //Table structure
            barcodeColumn.setCellValueFactory(param -> { return param.getValue().barcodeProperty(); });

            nameColumn.setCellValueFactory(param -> { return  param.getValue().productNameProperty(); });
            nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

            quantityColumn.setCellValueFactory(param -> { return  param.getValue().quantityProperty(); });

            //Table content
            products = FXCollections.observableArrayList();
            products = loadProducts();

            FilteredList<product> filteredData = new FilteredList<>(products, product -> true);
            searchfield.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredData.setPredicate(product -> {
                    if (newValue == null || newValue.isEmpty()) return true;
                    String lowercaseFilter = newValue.toLowerCase();
                    if (product.getProductName().toLowerCase().contains(lowercaseFilter)) return true;
                    else if (Integer.toString(product.getBarcode()).contains(lowercaseFilter)) return true;
                    else return false;
                });
            }));
            SortedList<product> sortedList = new SortedList<>(filteredData);
            sortedList.comparatorProperty().bind(needTable.comparatorProperty());
            needTable.setItems(sortedList);
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
    }


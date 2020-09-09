package interfaceMagazinier.sells;

import basicClasses.product;
import basicClasses.sell;
import basicClasses.user;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import Connector.ConnectionClass;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

public class imSellsController implements Initializable {

    @FXML
    public TableView<product> sellTable;
    public static sell sellCollection;
    public StackPane content;
    @FXML
    TableColumn<product, Number> barcodeColumn;
    @FXML
    TableColumn<product, String> nameColumn;
    @FXML
    TableColumn<product, Number> quantityColumn;
    @FXML
    TableColumn<product, Number> sellpriceColumn;
    @FXML
    TableColumn<product, String> expirationdateColumn;
    @FXML
    TableColumn selectedColumn;
    @FXML
    JFXTextField searchTextField;
    @FXML
    private JFXButton one;
    @FXML
    private JFXButton printFacture;
    @FXML
    private JFXButton clearE;
    @FXML
    private JFXButton repeat;
    @FXML
    private JFXButton six;
    @FXML
    private JFXButton five;
    @FXML
    private JFXButton nine;
    @FXML
    private JFXButton four;
    @FXML
    private JFXButton addition;
    @FXML
    private JFXButton eight;
    @FXML
    private JFXButton seven;
    @FXML
    private JFXButton substraction;
    @FXML
    private JFXButton two;
    @FXML
    private JFXButton three;
    @FXML
    private JFXButton zero;
    @FXML
    private JFXButton doubleZero;
    @FXML
    private JFXButton clear;
    @FXML
    Label dateNow;
    @FXML
    Label clockNow;
    @FXML
    private JFXButton tripleZero;
    @FXML
    private HBox searchHbox;
    @FXML
    public Label prix;
    @FXML
    private JFXButton ok;
    @FXML
    private Label status;
    @FXML
    private JFXTextField barcodeLabel;
    public static SimpleFloatProperty totalPrice = new SimpleFloatProperty(0);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableSetUp();

        totalPrice.addListener((observableValue, number, t1) -> {
            prix.setText(String.valueOf(totalPrice.get()));
            addToTable();
        });

        //  runClock();
    }

    public void addToTable() {
        sellTable.setItems((ObservableList<product>) sellCollection.getSoldProducts());
    }

    void tableSetUp() {
        sellCollection = new sell();

        //Table structure
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

        sellpriceColumn.setCellValueFactory(param -> {
            return param.getValue().sellPriceProperty();
        });

        expirationdateColumn.setCellValueFactory(param -> {
            return param.getValue().expirationDateProperty();
        });

        selectedColumn.setCellValueFactory(new PropertyValueFactory<product, String>("checkbox"));

        //Search

/*
        FilteredList<product> filteredData = new FilteredList<>(sellCollection, product -> true);
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
        sortedList.comparatorProperty().bind(sellTable.comparatorProperty());
        sellTable.setItems(sortedList);*/
    }

    //date and clock
  /*  public void runClock(){
        Thread clock = new Thread() {
            public void run(){
                try {
                    while (true){
                        Calendar calendar= new GregorianCalendar();
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int month = calendar.get(Calendar.MONTH);
                        int year = calendar.get(Calendar.YEAR);
                        dateNow.setText(year+"/"+month+"/"+day);
                        int second = calendar.get(Calendar.SECOND);
                        int munite = calendar.get(Calendar.MINUTE);
                        int hour = calendar.get(Calendar.HOUR);
                        clockNow.setText(hour+":"+munite+":"+second);
                        sleep(1000);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        clock.start();
    }*/

    // calculatrice
    String barcode = "";

    public void repeatClick() {
        barcodeLabel.setText(barcode);
    }

    public void zeroClick() {
        String oldValue = barcodeLabel.getText().toString();
        String set = "0";
        barcodeLabel.setText(oldValue + set);
    }

    public void doubleZeroClick() {
        String oldValue = barcodeLabel.getText().toString();
        String set = "00";
        barcodeLabel.setText(oldValue + set);
    }

    public void tripleZeroClick() {
        String oldValue = barcodeLabel.getText().toString();
        String set = "000";
        barcodeLabel.setText(oldValue + set);

    }

    public void oneClick() {
        String oldValue = barcodeLabel.getText().toString();
        String set = "1";
        barcodeLabel.setText(oldValue + set);
    }

    public void twoClick() {
        String oldValue = barcodeLabel.getText().toString();
        String set = "2";
        barcodeLabel.setText(oldValue + set);
    }

    public void threeClick() {
        String oldValue = barcodeLabel.getText().toString();
        String set = "3";
        barcodeLabel.setText(oldValue + set);
    }

    public void fourClick() {
        String oldValue = barcodeLabel.getText().toString();
        String set = "4";
        barcodeLabel.setText(oldValue + set);
    }

    public void fiveClick() {
        String oldValue = barcodeLabel.getText().toString();
        String set = "5";
        barcodeLabel.setText(oldValue + set);
    }

    public void sixClick() {
        String oldValue = barcodeLabel.getText().toString();
        String set = "6";
        barcodeLabel.setText(oldValue + set);
    }

    public void sevenClick() {
        String oldValue = barcodeLabel.getText().toString();
        String set = "7";
        barcodeLabel.setText(oldValue + set);
    }

    public void eightClick() {
        String oldValue = barcodeLabel.getText().toString();
        String set = "8";
        barcodeLabel.setText(oldValue + set);
    }

    public void nineClick() {
        String oldValue = barcodeLabel.getText().toString();
        String set = "9";
        barcodeLabel.setText(oldValue + set);
    }

    public void clearClick() {
        barcodeLabel.setText("");
    }

    public void clearE() {
        String oldValue = barcodeLabel.getText();
        int length = oldValue.length();
        barcodeLabel.setText(oldValue.substring(0, length - 1));
    }

    public void resetTable() {

        sellTable.setItems(null);
        prix.setText("00.00");
        barcodeLabel.setText("");
        // clear
        sellCollection = new sell();
    }

    public void removeProduct() {
        ObservableList<product> removedProduct = FXCollections.observableArrayList();
        for (product bean : sellCollection.getSoldProducts())
            if (bean.getCheckbox().isSelected()) {
                removedProduct.add(bean);
            }
        sellCollection.removeProduct(removedProduct);
        prix.setText(String.valueOf(sellCollection.getTotalPrice()));

    }


    public void okClick() throws SQLException {
        try {
            if (barcodeLabel.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("");
                alert.setContentText("You must fill the label ");
                alert.showAndWait();
                barcodeLabel.setText("");
            } else {
                Connection connection = ConnectionClass.getConnection();
                String query = "SELECT * FROM stock where barcode=? and userID=?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, Integer.parseInt(barcodeLabel.getText()));
                preparedStatement.setInt(2, user.getUserID());
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {
                    product newProduct;
                    if (rs.getDate("expirationdate") == null)
                        newProduct = new product(rs.getString("name"), rs.getInt("barcode"), rs.getFloat("buyprice"), rs.getFloat("sellprice"), rs.getInt("quantity"), "", rs.getInt("numberOfSells"));
                    else {
                        newProduct = new product(rs.getString("name"), rs.getInt("barcode"), rs.getFloat("buyprice"), rs.getFloat("sellprice"), rs.getInt("quantity"), rs.getDate("expirationdate").toString(), rs.getInt("numberOfSells"));
                    }

                    newProduct.setQuantity(1);
                    preparedStatement.close();
                    rs.close();
                    barcode = barcodeLabel.getText();
                    barcodeLabel.setText("");
                    sellCollection.addProduct(newProduct);
                    prix.setText(String.valueOf(sellCollection.getTotalPrice()));
                    sellTable.setItems((ObservableList<product>) sellCollection.getSoldProducts());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("");
                    alert.setContentText("Product not exist ");
                    alert.showAndWait();
                    barcode = barcodeLabel.getText();
                    barcodeLabel.setText("");
                }
            }


        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("");
            alert.setContentText("Entre a valid barcode ");
            alert.showAndWait();
            barcode = barcodeLabel.getText();
            barcodeLabel.setText("");
        }
    }

    public void confirmSell() {
        if (sellCollection.getSoldProducts() != null) {
            try {
                sellCollection.pushSell();
                sellCollection = new sell();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            resetTable();
        }
    }


    public void printFacture() {


        String path = "src\\resource\\File\\facture1_2.jasper";


        try {
            // Path documentPath
            // HashMap<String, Object> params
            // JRDataSource jasperDataSource/
            // Indentation CTRL + ALT + L

            Path documentPath = Paths.get(path);
            Map<String, Object> params = new HashMap<>();
            params.put("Cashier", "Moncif Bendada"); // get it from login
            params.put("Numfacture", "00001");
            params.put("Total", String.valueOf(sellCollection.getTotalPrice()));
            JREmptyDataSource emptyDatasource = new JREmptyDataSource();


            JRBeanCollectionDataSource jasperDataSource = new JRBeanCollectionDataSource(sellCollection.getSoldProducts());
            if (sellCollection.getSoldProducts().isEmpty()) {
                JasperPrint jasperPrint = JasperFillManager.fillReport(path, params, emptyDatasource);
                JasperViewer.viewReport(jasperPrint, false);
            } else {
                JasperPrint jasperPrint = JasperFillManager.fillReport(documentPath.toAbsolutePath().toString(), params, jasperDataSource);
                JasperViewer.viewReport(jasperPrint, false);
                confirmSell();
            }

        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    public void add(ActionEvent event) throws IOException {
        Region root1 = FXMLLoader.load(getClass().getResource("add/add.fxml"));
        JFXDialog add = new JFXDialog(content, root1, JFXDialog.DialogTransition.RIGHT);
        add.show();
        //  addToTable();
    }

    public void discount(ActionEvent event) throws IOException {
        Region root1 = FXMLLoader.load(getClass().getResource("discount/discount.fxml"));
        JFXDialog discount = new JFXDialog(content, root1, JFXDialog.DialogTransition.RIGHT);
        discount.show();
        //  addToTable();
    }

    public void card(ActionEvent event) throws IOException {
        Region root1 = FXMLLoader.load(getClass().getResource("card/card.fxml"));
        JFXDialog card = new JFXDialog(content, root1, JFXDialog.DialogTransition.RIGHT);
        card.show();
        //  addToTable();
    }
}
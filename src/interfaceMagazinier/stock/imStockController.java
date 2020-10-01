package interfaceMagazinier.stock;

import Connector.ConnectionClass;
import basicClasses.product;
import basicClasses.user;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXTextField;
import interfaceMagazinier.settings.preference.preferencesController;
import interfaceMagazinier.stock.fullStock.fullStockController;
import interfaceMagazinier.stock.update.updateController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.controlsfx.control.CheckComboBox;

import java.awt.geom.FlatteningPathIterator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class imStockController implements Initializable {
    @FXML
    StackPane stackPane;
    @FXML
    TableView<product> table;
    public StackPane fullStockCoutainer;
    public static JFXDialog fullStockDialog;
    public static ObservableList<product> products;
    @FXML
    TableColumn<product, Number> barcodeColumn;
    @FXML
    TableColumn<product, String> nameColumn;
    @FXML
    TableColumn<product, Number> totalQuantityColumn;
    @FXML
    TableColumn<product, Number> buypriceColumn;
    @FXML
    TableColumn<product, Number> sellpriceColumn;
    @FXML
    TableColumn<product, String> expirationdateColumn;
    @FXML
    TableColumn selectedColumn;
    @FXML
    TableColumn<product, String> floorColumn;
    @FXML
    TableColumn<product, String> ContainerColumn;
    @FXML
    JFXTextField searchTextField;
    @FXML
    CheckComboBox typeCheckComboBox;
    @FXML
    public TableColumn<product, Boolean> detailsColumn;
    public static Integer indexOfProduct;
    public static Float buyPriceProduct,sellPriceProduct;
    public static product theFullStockProduct;
    private SortedList<product> sortedList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        shortcutSetUp();
        tableSetUp();
        filterSetUp();

    }

    Integer loadFullStockForProduct(Integer barcode) throws SQLException {
       String Sql="select sum(quantity) from fullStock where userID=? and barcode=?";

       Connection connection= ConnectionClass.getConnection();
       PreparedStatement preparedStatement =connection.prepareStatement(Sql);
preparedStatement.setInt(1,user.getUserID());
        preparedStatement.setInt(2,barcode);
ResultSet resultSet = preparedStatement.executeQuery();

if (resultSet.next()){
    return resultSet.getInt(1);

}
else return 0;
    }
    private void filterSetUp() {
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

        typeCheckComboBox.getItems().setAll(preferencesController.productTypes);
        typeCheckComboBox.getItems().add("Others");
        for (int i = 0; i < typeCheckComboBox.getItems().size(); i++) typeCheckComboBox.getCheckModel().check(i);

        FilteredList<product> filteredDataSecond = new FilteredList<>(filteredData, product -> true);
        typeCheckComboBox.getCheckModel().getCheckedIndices().addListener((ListChangeListener) c -> {
            filteredDataSecond.setPredicate(product -> {
                if (typeCheckComboBox.getCheckModel().isChecked("Others") && product.getProductType().equals("")) return true;
                if (typeCheckComboBox.getCheckModel().getCheckedItems().contains(product.getProductType())) return true;
                return false;
            });
        });

        sortedList = new SortedList<>(filteredDataSecond);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }

    public class CustomButtonCell<T, S> extends TableCell<T, S> {
        private Button Details = new Button("Details");


        @Override
        protected void updateItem(S item, boolean empty) {

            super.updateItem(item, empty);
            if (empty || item == null) {
                this.setText("");
                this.setGraphic(null);
            } else {
                Details.setOnAction(event -> {
                    try {
                        indexOfProduct = this.getIndex();
                        System.out.println("the index " + indexOfProduct);
                        theFullStockProduct = (product) this.getTableView().getItems().get(this.getIndex());
                        buyPriceProduct=theFullStockProduct.getBuyPrice();
                                sellPriceProduct=theFullStockProduct.getSellPrice();
                        showFullStock();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });
                this.setGraphic(Details);
            }
        }


    }

    void shortcutSetUp() {
        stackPane.getScene().setOnKeyPressed(event -> {
        });
    }

    public void showFullStock() throws IOException {

        fullStockCoutainer = FXMLLoader.load(getClass().getResource("fullStock/fullStock.fxml"));
        fullStockDialog = new JFXDialog(stackPane, fullStockCoutainer, DialogTransition.BOTTOM);

        fullStockDialog.show();
        fullStockDialog.setOnDialogClosed(event -> {
            theFullStockProduct=null;
            fullStockController.fullStackApplyProduct=null;
            sellPriceProduct=null;
            buyPriceProduct=null;
            System.out.println("abdelkader boussaid");
        });


    }

    void tableSetUp() {
        //Table structure
        barcodeColumn.setCellValueFactory(param -> {
            return param.getValue().barcodeProperty();
        });
        nameColumn.setCellValueFactory(param -> {
            return param.getValue().productNameProperty();
        });
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        totalQuantityColumn.setCellValueFactory(param -> {
            return param.getValue().totalStockProperty();
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
        ContainerColumn.setCellValueFactory(param -> {
            return param.getValue().containerNameProperty();
        });
        floorColumn.setCellValueFactory(param -> {
            //Darte had el khourti hnaya bsh ki ikoun l floor number null f el bdd ma3awedhach bzwara automatiquement. syi egla3 star li tahte had el comment chouf wesh ysra f stock bsh tfham
            if (param.getValue().getFloor() == 0) return new SimpleStringProperty("");
            return new SimpleStringProperty(String.valueOf(param.getValue().getFloor()));
        });
        selectedColumn.setCellValueFactory(new PropertyValueFactory<product, String>("checkbox"));
        detailsColumn.setCellValueFactory(call -> new SimpleBooleanProperty(true).asObject());
        detailsColumn.setCellFactory(call -> {
            return new CustomButtonCell<>();
        });
        //Table content
        products = FXCollections.observableArrayList();
        products = loadProducts();
    }

    private ObservableList<product> loadProducts() {
        ObservableList<product> products = FXCollections.observableArrayList();
        Connection connection = ConnectionClass.getConnection();
        //Change this to read only the products who belongs to the user

        try {
            String query = "SELECT * FROM stock where userID=" + String.valueOf(user.getUserID());
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);


            while (rs.next()) {
                product newProduct;
                try {
                    newProduct = new product(rs.getString("name"), rs.getInt("barcode"), rs.getFloat("buyprice"), rs.getFloat("sellprice"), rs.getInt("quantity"), rs.getDate("expirationdate").toString());
                } catch (Exception e) {
                    newProduct = new product(rs.getString("name"), rs.getInt("barcode"), rs.getFloat("buyprice"), rs.getFloat("sellprice"), rs.getInt("quantity"), "");
                }
                newProduct.setInitialQuantity(rs.getInt("initialQuantity"));
                newProduct.setNumberOfSells(rs.getInt("numberOfSells"));

                newProduct.setTotalStock(  loadFullStockForProduct(rs.getInt("barcode"))+rs.getInt("quantity"));
                newProduct.setProviderEmail(rs.getString("providerEmail"));
                if (rs.getString("productType") != null) newProduct.setProductType(rs.getString("productType"));
                else newProduct.setProductType("");
                if (rs.getString("containerName") != null) {
                    newProduct.setContainerName(rs.getString("containerName"));
                }
                else newProduct.setContainerName("");
                newProduct.setFloor(rs.getInt("floor"));

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
        add.setOnDialogClosed(event1 -> {
            source.getStyleClass().removeAll("pressed");
        });
    }

    public void removeProduct() throws SQLException {
        Connection connection = ConnectionClass.getConnection();
        Statement statement = connection.createStatement();
        ObservableList<product> removedProduct = FXCollections.observableArrayList();
        for (product bean : products)
            if (bean.getCheckbox().isSelected()) {
                removedProduct.add(bean);
                String sql;
                if (!bean.getExpirationDate().equals(""))
                    sql = "INSERT INTO removedproducts (name,barcode,sellprice,buyprice,quantity,expirationdate,userID) VALUES ('" + bean.getProductName() + "', '" + bean.getBarcode() + "', '" + bean.getSellPrice() + "', '" + bean.getBuyPrice() + "', '" + bean.getQuantity() + "', '" + bean.getExpirationDate() + "','" + user.getUserID() + "')";
                else
                    sql = "INSERT INTO removedproducts (name,barcode,sellprice,buyprice,quantity,userID) VALUES ('" + bean.getProductName() + "', '" + bean.getBarcode() + "', '" + bean.getSellPrice() + "', '" + bean.getBuyPrice() + "', '" + bean.getQuantity() + "','" + user.getUserID() + "')";
                statement.executeUpdate(sql);

                String sqlDelete = "DELETE FROM stock WHERE barcode=? and userID=?";
                PreparedStatement pst = connection.prepareStatement(sqlDelete);
                pst.setInt(1, bean.getBarcode());
                pst.setInt(2, user.getUserID());
                pst.executeUpdate();
                pst.close();
            }
        products.removeAll(removedProduct);
    }

    public void updateProduct(javafx.event.ActionEvent event) throws IOException {
        ObservableList<product> updatedProduct = FXCollections.observableArrayList();
        for (product updated : products)
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
                update.setOnDialogClosed(event1 -> {
                    source.getStyleClass().removeAll("pressed");
                    if (updatedProduct.size() != 1) {
                        updatedProduct.remove(updateController.productSelected);
                        updateController.productSelected = updatedProduct.get(updatedProduct.size() - 1);
                    }
                });
            }
    }

    public void exportToExcel() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("JavaFX Projects");
        File defaultDirectory = new File("c:/");
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(searchTextField.getScene().getWindow());
        if (selectedDirectory == null) return;
        try {
            // Connect with database
            String query = "SELECT * FROM stock ";
            PreparedStatement pst = ConnectionClass.getConnection().prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            // creating excel sheet
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
            XSSFSheet sheet = xssfWorkbook.createSheet("Stock");

            //Create and Fill the first row of sheet
            XSSFRow header = sheet.createRow(0);
            header.createCell(0).setCellValue("Barcode");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Quantity");
            header.createCell(3).setCellValue("Buy Price");
            header.createCell(4).setCellValue("Sell Price");
            header.createCell(5).setCellValue("Expiration Date ");

            //Fill the sheet from database
            int index = 1;
            while (rs.next()) {
                XSSFRow row = sheet.createRow(index);
                row.createCell(0).setCellValue(rs.getString("barcode"));
                row.createCell(1).setCellValue(rs.getString("name"));
                row.createCell(2).setCellValue(rs.getString("quantity"));
                row.createCell(3).setCellValue(rs.getString("buyprice"));
                row.createCell(4).setCellValue(rs.getString("sellprice"));
                row.createCell(5).setCellValue(rs.getString("expirationdate"));
                index++;
            }
            // creating A file
            FileOutputStream fileOutputStream = new FileOutputStream(selectedDirectory.getAbsolutePath() + "/stock.xlsx");
            // puting data in the file
            xssfWorkbook.write(fileOutputStream);
            fileOutputStream.close();
            // Alert About succes of operation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Stock has been exported to excel file succefully");
            alert.showAndWait();
            //Closing connection
            pst.close();
            rs.close();

        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
    }
}
package interfaceMagazinier.sells;
import basicClasses.product;
import basicClasses.sell;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import Connection.ConnectionClass ;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static javafx.scene.text.Font.loadFont;

public class imSellsController implements Initializable {

    @FXML
    public TableView<product> sellTable;
    public static ObservableList<product> sellCollection;
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
    @FXML Label dateNow ;
    @FXML Label clockNow ;
    @FXML
    private JFXButton tripleZero;
    @FXML private HBox searchHbox ;
    @FXML private Label prix  ;
    @FXML private JFXButton ok;
    @FXML private Label status;
    @FXML private JFXTextField barcodeLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableSetUp();
        //  runClock();
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
        sellCollection = FXCollections.observableArrayList();

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
        sellTable.setItems(sortedList);
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
    String barcode="";
    public void repeatClick(){
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
    public void clearE(){
        String oldValue= barcodeLabel.getText();
        int length = oldValue.length();
        barcodeLabel.setText(oldValue.substring(0,length-1));
    }
    public void resetTable () {

        sellTable.setItems(null);
        prix.setText("00.00");
        barcodeLabel.setText("");
        sellCollection.removeAll(sellCollection);
    }
    public void removeProduct() {
        ObservableList<product> removedProduct = FXCollections.observableArrayList();
        for (product bean : sellCollection )
            if (bean.getCheckbox().isSelected()) {
                removedProduct.add(bean);
                prix.setText(Float.toString(Float.parseFloat(prix.getText())-bean.getSellPrice()));
            }
        sellCollection.removeAll(removedProduct) ;

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
                String query = "SELECT * FROM stock where barcode=" + Integer.parseInt(barcodeLabel.getText());
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {
                    product newProduct;
                    if (rs.getDate("expirationdate") == null) newProduct = new product(rs.getString("name"), rs.getInt("barcode"), rs.getFloat("buyprice"), rs.getFloat("sellprice"), rs.getInt("quantity"), "");
                    else{
                        newProduct = new product(rs.getString("name"), rs.getInt("barcode"), rs.getFloat("buyprice"), rs.getFloat("sellprice"), rs.getInt("quantity"), rs.getDate("expirationdate").toString());
                    }
                    String oldValue = prix.getText();
                    prix.setText(Float.toString(Float.parseFloat(oldValue) + newProduct.getSellPrice()));
                    newProduct.setQuantity(1);
                    preparedStatement.close();
                    rs.close();
                    barcode = barcodeLabel.getText();
                    barcodeLabel.setText("");
                    AtomicBoolean productAlreadyExists = new AtomicBoolean(false);
                    sellCollection.forEach(product -> {
                        if (product.getBarcode() == newProduct.getBarcode()) {
                            product.setQuantity(product.getQuantity() + 1);
                            productAlreadyExists.set(true);
                        }
                        return;
                    });
                    if (!productAlreadyExists.get()) {
                        sellCollection.add(newProduct);
                        sellTable.setItems(sellCollection);
                        productAlreadyExists.set(false);
                    }
                } else /*if ((barcodeLabel.getText().length() > 8) || barcodeLabel.getText().length() < 6) {
//                    Alert alert = new Alert(Alert.AlertType.ERROR);
//                    alert.setTitle("Error");
//                    alert.setHeaderText("");
//                    alert.setContentText("Please entre a valid barcode");
//                    alert.showAndWait();
//                    barcode = barcodeLabel.getText();
//                    barcodeLabel.setText("");
//
//                } else */{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("");
                    alert.setContentText("Product not exist ");
                    alert.showAndWait();
                    barcode = barcodeLabel.getText();
                    barcodeLabel.setText("");
                }


            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("");
            alert.setContentText("Entre a valid barcode ");
            alert.showAndWait();
            barcode = barcodeLabel.getText();
            barcodeLabel.setText("");
        }
    }



    public void confirmSell(){
        if (sellCollection != null) {
            try {
                sell.pushSell(new sell(sellCollection));
                resetTable();
            } catch (SQLException e) {
                resetTable();
            }
        }
    }

    public void exportToExcel (){

        Iterator<product> iterator = sellCollection.iterator() ;
        try {

            // creating excel sheet
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook() ;
            XSSFSheet sheet = xssfWorkbook.createSheet("Facture") ;

            //Create and Fill the first row of sheet
            XSSFRow header = sheet.createRow(0) ;
            header.createCell(0).setCellValue("Barcode");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Quantity");
            header.createCell(4).setCellValue("Price");
            header.createCell(5).setCellValue("Expiration Date ");

            //Fill the sheet from database
            int index = 1 ;
            for (product bean : sellCollection)
                if(iterator.hasNext()){
                    XSSFRow row = sheet.createRow(index) ;
                    row.createCell(0).setCellValue(bean.getBarcode());
                    row.createCell(1).setCellValue(bean.getProductName());
                    row.createCell(2).setCellValue(bean.getQuantity());
                    row.createCell(3).setCellValue(bean.getSellPrice());
                    row.createCell(5).setCellValue(bean.getExpirationDate());
                    index ++ ;
                    iterator.next();
                }
            // creating A file
            FileOutputStream fileOutputStream = new FileOutputStream("Facture.xlsx") ;
            // puting data in the file
            xssfWorkbook.write(fileOutputStream);
            fileOutputStream.close();
            // Alert About succes of operation
            Alert alert = new Alert(Alert.AlertType.INFORMATION) ;
            alert.setHeaderText(null);
            alert.setContentText("Facture has been exported to excel file succefully");
            alert.showAndWait();


        } catch ( IOException ex) {
            ex.printStackTrace();
        }

    }


    public void printFacture()  {
/*
            try {
                JasperReport jr = JasperCompileManager.compileReport("src/resource/File/Blank_A4.jrxml") ;
                HashMap<String,Object> input = new HashMap<>() ;
              //  input.put("Cashier","Moncif Bendada") ; // get it from login
               // input.put("Numfacture","00001");
              //  input.put("Total",prix.getText());
                for (product bean:sellCollection) {
                    input.put("productName",bean.getProductName());
                    input.put("barcode",Integer.toString(bean.getBarcode()));
                    input.put("sellPrice",Float.toString(bean.getSellPrice()));
                    input.put("quantity",Integer.toString(bean.getQuantity()));
                }

                //JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(sellCollection);
                JasperPrint fillReport = JasperFillManager.fillReport(jr,input);
                JasperViewer.viewReport(fillReport,false);


            } catch (JRException e) {
                e.printStackTrace();
            }
*/
        confirmSell();
    }
}
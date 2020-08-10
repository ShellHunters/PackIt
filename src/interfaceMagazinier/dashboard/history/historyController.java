package interfaceMagazinier.dashboard.history;

import basicClasses.sell;
import com.jfoenix.controls.*;
import interfaceMagazinier.dashboard.history.sellDetails.sellDetailsController;
import interfaceMagazinier.dashboard.imDashboardController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class historyController implements Initializable {
    @FXML StackPane stackPane;
    @FXML TableView<sell> table;
    @FXML javafx.scene.control.TableColumn<sell, Number> idColumn;
    @FXML TableColumn<sell, Number> sellAmountColumn;
    @FXML TableColumn<sell, Number> sellProfitColumn;
    @FXML TableColumn<sell, Number> sellNumberProductsColumn;
    @FXML TableColumn<sell, String> sellDateTime;
    @FXML JFXTextField searchTextField;
    @FXML JFXDatePicker fromDatePicker;
    @FXML JFXDatePicker toDatePicker;
    @FXML JFXTimePicker fromTimePicker;
    @FXML JFXTimePicker toTimePicker;
    @FXML JFXTextField minValue;
    @FXML JFXTextField maxValue;
    @FXML JFXCheckBox profitCheckBox;

    public static sell selectedSell;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableSetUp();
        filterSetUp();
    }

    private void tableSetUp(){
        idColumn.setCellValueFactory(param -> param.getValue().idProperty());
        sellAmountColumn.setCellValueFactory(param -> param.getValue().totalPriceProperty());
        sellProfitColumn.setCellValueFactory(param -> param.getValue().totalProfitProperty());
        sellNumberProductsColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getSoldProducts().size()));
        sellDateTime.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSellTime().toString()));

        table.setItems(imDashboardController.sells);

        table.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                selectedSell = table.getSelectionModel().getSelectedItem();
                try {
                    Region root1 = FXMLLoader.load(getClass().getResource("sellDetails/sellDetails.fxml"));
                    JFXDialog sellDetails = new JFXDialog(stackPane, root1, JFXDialog.DialogTransition.BOTTOM);
                    sellDetails.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void filterSetUp(){
        fromTimePicker.setValue(LocalTime.of(0,0,0));
        toTimePicker.setValue(LocalTime.of(23, 59,59));
        fromTimePicker.set24HourView(true);
        toTimePicker.set24HourView(true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> refreshList());
        fromDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> refreshList());
        fromTimePicker.valueProperty().addListener((observable, oldValue, newValue) -> refreshList());
        toDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> refreshList());
        toTimePicker.valueProperty().addListener((observable, oldValue, newValue) -> refreshList());
        minValue.textProperty().addListener((observable, oldValue, newValue) -> refreshList());
        maxValue.textProperty().addListener((observable, oldValue, newValue) -> refreshList());
        profitCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> refreshList());
    }

    private void refreshList(){

        ObservableList<sell> newSells = FXCollections.observableArrayList();
        imDashboardController.sells.forEach(sell -> {
            boolean b= true;
            if (String.valueOf(sell.getId()).contains(searchTextField.getText())) {
                if (profitCheckBox.isSelected()) {
                    if (!maxValue.getText().isEmpty()) b = (Float.parseFloat(maxValue.getText()) >= sell.getTotalProfit());
                    if (!minValue.getText().isEmpty()) b &= (Float.parseFloat(minValue.getText()) <= sell.getTotalProfit());
                } else {
                    if (!maxValue.getText().isEmpty()) b = (Float.parseFloat(maxValue.getText()) >= sell.getTotalPrice());
                    if (!minValue.getText().isEmpty()) b &= (Float.parseFloat(minValue.getText()) <= sell.getTotalPrice());
                }
                if (b) {
                    if (fromDatePicker.getValue() != null) b =  (sell.getSellTime().isAfter(LocalDateTime.of(fromDatePicker.getValue(), fromTimePicker.getValue())));
                    else b = (sell.getSellTime().toLocalTime().isAfter(fromTimePicker.getValue()));

                    if (toDatePicker.getValue() != null) b &= (sell.getSellTime().isBefore(LocalDateTime.of(toDatePicker.getValue(), toTimePicker.getValue())));
                    else b &= (sell.getSellTime().toLocalTime().isBefore(toTimePicker.getValue()));

                    if (b) newSells.add(sell);
                }
            }
        });
        table.setItems(newSells);
    }

    public void resetFilter(){
        searchTextField.setText("");
        minValue.setText("");
        maxValue.setText("");
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);
        fromTimePicker.setValue(LocalTime.of(0,0,0));
        toTimePicker.setValue(LocalTime.of(23, 59, 59));
        profitCheckBox.setSelected(false);
    }

    public void exportToExcel() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("JavaFX Projects");
        File defaultDirectory = new File("c:/");
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(searchTextField.getScene().getWindow());
        if (selectedDirectory == null) return;
        try {
            // creating excel sheet
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook() ;
            XSSFSheet sheet = xssfWorkbook.createSheet("Stock") ;

            //Create and Fill the first row of sheet
            XSSFRow header = sheet.createRow(0) ;
            header.createCell(0).setCellValue("Sell id");
            header.createCell(1).setCellValue("Sell amount");
            header.createCell(2).setCellValue("Sell profit");
            header.createCell(3).setCellValue("Number of products");
            header.createCell(4).setCellValue("Sell date and time");

            //Fill the sheet from database
            AtomicInteger index = new AtomicInteger(1);

            imDashboardController.sells.forEach(sell -> {
                XSSFRow row = sheet.createRow(index.get()) ;
                row.createCell(0).setCellValue(sell.getId());
                row.createCell(1).setCellValue(sell.getTotalPrice());
                row.createCell(2).setCellValue(sell.getTotalProfit());
                row.createCell(3).setCellValue(sell.getSoldProducts().size());
                row.createCell(4).setCellValue(sell.getSellTime().toString());
                index.getAndIncrement();
            });
            // creating A file
            FileOutputStream fileOutputStream = new FileOutputStream(selectedDirectory.getAbsolutePath() + "/sells.xlsx");
            // puting data in the file
            xssfWorkbook.write(fileOutputStream);
            fileOutputStream.close();
            // Alert About succes of operation
            Alert alert = new Alert(Alert.AlertType.INFORMATION) ;
            alert.setHeaderText(null);
            alert.setContentText("Stock has been exported to excel file succefully");
            alert.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

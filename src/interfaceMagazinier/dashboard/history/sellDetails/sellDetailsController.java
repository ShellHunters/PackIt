package interfaceMagazinier.dashboard.history.sellDetails;


import basicClasses.product;
import basicClasses.sell;
import interfaceMagazinier.dashboard.history.historyController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class sellDetailsController  implements Initializable{

    @FXML
    private TableView<product> table;
    @FXML private TableColumn<product, Number> barcodeColumn;
    @FXML private TableColumn<product, String> nameColumn;
    @FXML private TableColumn<product, Number> quantityColumn;
    @FXML private TableColumn<product, Number> sellpriceColumn;
    @FXML private TableColumn<product, Number> profitColumn;
    @FXML private TableColumn<product, String> expirationdateColumn;

    @FXML private Label sellIdLabel;
    @FXML private Label numberOfProductsLabel;
    @FXML private Label sellPriceLabel;
    @FXML private Label sellProfitLabel;
    @FXML private Label sellTimeLabel;
    @FXML private Label discountLabel;
    @FXML private Label loyalityCardLabel;
    @FXML private Label discountAmountLabel;
    @FXML private Label clientIdLabel;

    sell currentSell = historyController.selectedSell;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelsSetUp();
        tableSetUp();
    }

    private void tableSetUp(){
        barcodeColumn.setCellValueFactory(param -> { return param.getValue().barcodeProperty(); });
        nameColumn.setCellValueFactory(param -> { return param.getValue().productNameProperty(); });
        quantityColumn.setCellValueFactory(param -> { return param.getValue().quantityProperty(); });
        sellpriceColumn.setCellValueFactory(param -> { return param.getValue().sellPriceProperty(); });
        profitColumn.setCellValueFactory(param -> { return param.getValue().buyPriceProperty(); });
        expirationdateColumn.setCellValueFactory(param -> { return param.getValue().expirationDateProperty(); });

        table.setItems((ObservableList<product>) historyController.selectedSell.getSoldProducts());
    }

    private void labelsSetUp(){
        sellIdLabel.setText("Sell id : " + currentSell.getId());
        numberOfProductsLabel.setText("Number of products : " + currentSell.getSoldProducts().size());
        sellPriceLabel.setText("Sell amount : " + currentSell.getTotalPrice());
        sellProfitLabel.setText("Sell profit : " + currentSell.getTotalProfit());
        sellTimeLabel.setText("Sell time : " + currentSell.getSellTime().toString());
        discountAmountLabel.setText("Discount amount : " + currentSell.getDiscountAmount());
        if (currentSell.getClientID() != -1) {
            loyalityCardLabel.setText("Loyality Card : yes");
            clientIdLabel.setText("Client ID : " + currentSell.getClientID());
        }
        else {
            loyalityCardLabel.setText("Loyality Card : no");
            clientIdLabel.setText("Client ID : none");
        }

        if (currentSell.getDiscountAmount() != 0) discountLabel.setText("Discount : yes");
        else discountLabel.setText("Discount : no");

    }
}

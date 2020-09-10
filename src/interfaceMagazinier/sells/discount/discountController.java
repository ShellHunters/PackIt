package interfaceMagazinier.sells.discount;

import com.jfoenix.controls.JFXTextField;
import interfaceMagazinier.sells.imSellsController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class discountController implements Initializable {

    @FXML
    private JFXTextField discountPercent;
    @FXML
    private JFXTextField discountDA;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textfieldsSetUp();
    }

    private void textfieldsSetUp() {

    }

    public void apply() {
        float oldValue = imSellsController.sellCollection.getTotalPrice();
        float discount = Float.parseFloat(discountPercent.getText());
        float newValue = oldValue - oldValue * discount / 100;

        imSellsController.totalPrice.set(newValue);
        imSellsController.sellCollection.setTotalPrice(newValue);
        imSellsController.sellCollection.setTotalProfit(oldValue - newValue + imSellsController.sellCollection.getTotalProfit());
        imSellsController.sellCollection.setDiscountAmount(imSellsController.sellCollection.getDiscountAmount() + oldValue - newValue);
    }

}

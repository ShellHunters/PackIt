package interfaceMagazinier.sells.card;

import Connector.ConnectionClass;
import com.jfoenix.controls.JFXTextField;
import interfaceMagazinier.sells.imSellsController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.jfree.ui.IntegerDocument;

import java.sql.*;

public class cardController {

    @FXML private Label errorLabel;
    @FXML private JFXTextField clientID;


    public void apply() throws SQLException {
        Connection connection = ConnectionClass.getConnection();
        String query = "SELECT * FROM clients WHERE id=" + clientID.getText();
        Statement ps = connection.createStatement();
        ResultSet rs = ps.executeQuery(query);
        if (rs.next()) {
            errorLabel.setText("Sell added successfully to client");
            errorLabel.setTextFill(Color.GREEN);

            imSellsController.sellCollection.setClientID(Integer.parseInt(clientID.getText()));
            int numberOfSells = rs.getInt(4) + 1;

            query = "UPDATE clients SET numberOfSells=" + numberOfSells + " WHERE id=" + clientID.getText();
            ps.execute(query);

            //APPLY DISCOUNT
            //for more customisation 10 & 20 would be a variables
            if (numberOfSells % 10 == 0){
                float oldValue = imSellsController.sellCollection.getTotalPrice();
                float discount = 20;
                float newValue = oldValue - oldValue * discount / 100;

                imSellsController.totalPrice.set(newValue);
                imSellsController.sellCollection.setTotalPrice(newValue);
                imSellsController.sellCollection.setTotalProfit(oldValue - newValue + imSellsController.sellCollection.getTotalProfit());
                imSellsController.sellCollection.setDiscountAmount(imSellsController.sellCollection.getDiscountAmount() + oldValue - newValue);

            }
        }
        else {
            errorLabel.setText("Client not found");
            errorLabel.setTextFill(Color.RED);
        }
    }
}

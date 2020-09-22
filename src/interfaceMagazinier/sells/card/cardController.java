package interfaceMagazinier.sells.card;

import Connector.ConnectionClass;
import basicClasses.user;
import com.jfoenix.controls.JFXTextField;
import interfaceMagazinier.sells.imSellsController;
import interfaceMagazinier.settings.preference.preferencesController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.sql.*;

public class cardController {

    @FXML
    private Label errorLabel;
    @FXML
    private JFXTextField clientID;


    public void apply() throws SQLException {
        Connection connection = ConnectionClass.getConnection();
        String query = "SELECT * FROM clients WHERE id=? and userID=? ";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, Integer.parseInt(clientID.getText()));
        ps.setInt(2, user.getUserID());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            errorLabel.setText("Sell added successfully to client");
            errorLabel.setTextFill(Color.GREEN);

            imSellsController.sellCollection.setClientID(Integer.parseInt(clientID.getText()));
            int numberOfSells = rs.getInt(4) + 1;

            String query2 = "UPDATE clients SET numberOfSells=? WHERE id=? AND userID=?";
            ps = connection.prepareStatement(query2);
            ps.setInt(1, numberOfSells);
            ps.setInt(2, Integer.parseInt(clientID.getText()));
            ps.setInt(3, user.getUserID());
            ps.executeUpdate();

            //APPLY DISCOUNT
            //for more customisation 10 & 20 would be a variables
            if (numberOfSells % preferencesController.getNumberOfSellsForDiscount() == 0) {
                float oldValue = imSellsController.sellCollection.getTotalPrice();
                float discount = preferencesController.getDiscountAmount();
                float newValue = oldValue - oldValue * discount / 100;

                imSellsController.totalPrice.set(newValue);
                imSellsController.sellCollection.setTotalPrice(newValue);
                imSellsController.sellCollection.setTotalProfit(oldValue - newValue + imSellsController.sellCollection.getTotalProfit());
                imSellsController.sellCollection.setDiscountAmount(imSellsController.sellCollection.getDiscountAmount() + oldValue - newValue);

            }
        } else {
            errorLabel.setText("Client not found");
            errorLabel.setTextFill(Color.RED);
        }
    }
}

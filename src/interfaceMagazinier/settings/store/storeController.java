package interfaceMagazinier.settings.store;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import Connector.ConnectionClass ;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class storeController implements Initializable {
    public Label status1;
    public JFXPasswordField passworrd2;
    public JFXTextField street;
    public JFXTextField city;
    public ChoiceBox Wilaya;
    public Label status;
    public JFXTextField newShopName;
    public JFXTextField shopeName;
    public JFXPasswordField passworrd1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        shopeName.setText("Moncif Shop");
        
        
    }

    public void saveShopLocation(ActionEvent event) {
    }

    public void saveShopName(ActionEvent event) throws SQLException {
        Connection connection = ConnectionClass.getConnection();
        Statement statement = connection.createStatement();
        if(shopeName.getText().equals("")||newShopName.getText().equals("")||passworrd1.getText().equals(""))
        {
            status.setTextFill(Color.RED);
            status.setText("You must fill all the text fields");
        }
        else if (shopeName.getText().equals(newShopName.getText()))
        {
            status.setTextFill(Color.RED);
            status.setText("The new and old shop name are same ");
        }
        // here we compare with the password from the user class
        else if (!passworrd1.getText().equals("moncif"))
        {
            status.setTextFill(Color.RED);
            status.setText("Wrong password");
        }

        else if (!existinShopCheck(connection)) return;
        else
            {
                status.setTextFill(Color.GREEN);
                status.setText("Your shope name changed succefully");
                shopeName.setText(newShopName.getText());
                // here we change the shop name in the user class
            }

    }

    boolean existinShopCheck(Connection connection) throws SQLException {
        String query = "SELECT * FROM logins where shopName= ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1,newShopName.getText());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            status.setTextFill(Color.RED);
            status.setText("Your new shop name already exists");
            newShopName.setText("");

            return false;
        }
        return true;
    }
}

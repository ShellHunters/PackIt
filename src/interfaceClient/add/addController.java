package interfaceClient.add;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

import static interfaceClient.icMainController.*;

public class addController  implements Initializable {

    @FXML
    private TextField quantity;
    @FXML
    private Label status;
    @FXML private StackPane stackPane ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       quantity.setText("1");
    }

    @FXML  void confirm() {
        if (quantity.getText().equals("")||!quantity.getText().matches("[0-9]+"))
        {
           status.setText("Please fill the quantity value ");
        }
        else if (clientProduct.getQuantity()<Integer.parseInt(quantity.getText()))
        {
            status.setText("The markett contain only "+clientProduct.getQuantity()+" product in the stock");
        }
        else
            {
             clientProduct.setQuantity(Integer.parseInt(quantity.getText()));
             addDialog.close();
            }
    }
}

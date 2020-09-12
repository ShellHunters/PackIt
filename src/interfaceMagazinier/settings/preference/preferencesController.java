package interfaceMagazinier.settings.preference;

import Connector.ConnectionClass;
import basicClasses.client;
import basicClasses.user;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class preferencesController implements Initializable {

    @FXML
    private Label errorLabel;
    @FXML
    private JFXTextField productTypeTextField;
    @FXML
    private TableView<String> productTypeTable;
    @FXML
    private TableColumn<String, String> productTypeColumn;

    public static ObservableList<String> productTypes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableSetUp();
    }

    private void tableSetUp() {
        productTypeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        productTypeTable.setItems(productTypes);
    }

    @FXML
    void addType(ActionEvent event) throws SQLException {
        if (errorCheck(true)) return;

        productTypes.add(productTypeTextField.getText());
        productTypeTable.setItems(productTypes);

        Connection connection = ConnectionClass.getConnection();
        String query = "INSERT INTO productTypes(userID, productType) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, String.valueOf(user.getUserID()));
        preparedStatement.setString(2, productTypeTextField.getText());
        preparedStatement.execute();

    }

    private boolean errorCheck(boolean add) {
        if (productTypeTextField.getText().equals("")) {
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText("Fill all the text fields and informations");
            return true;
        }

        if (add & productTypes.contains(productTypeTextField.getText())) {
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText("Type already exists");
            return true;
        }

        errorLabel.setTextFill(Paint.valueOf("green"));
        if (add) errorLabel.setText("product added succefully");
        else errorLabel.setText("product deleted succefully");
        return false;

    }

    @FXML
    void deleteType(ActionEvent event) throws SQLException {
        if (errorCheck(false)) return;
        productTypes.remove(productTypeTextField.getText());
        productTypeTable.setItems(productTypes);

        Connection connection = ConnectionClass.getConnection();
        String query = "DELETE FROM productTypes WHERE userID=? AND productType=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, String.valueOf(user.getUserID()));
        preparedStatement.setString(2, productTypeTextField.getText());
        preparedStatement.execute();

        productTypeTextField.setText("");
    }

    @FXML
    void getSelected(javafx.scene.input.MouseEvent mouseEvent){
        int index = productTypeTable.getSelectionModel().getSelectedIndex();
        if (index < 0) return;
        productTypeTextField.setText(productTypeTable.getItems().get(index));
    }
}

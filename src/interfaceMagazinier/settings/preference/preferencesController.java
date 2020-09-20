package interfaceMagazinier.settings.preference;

import Connector.ConnectionClass;
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
import javafx.scene.paint.Paint;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
    @FXML
    private JFXTextField numberOfSellsForDiscountTextField;
    @FXML
    private JFXTextField discountAmountTextField;
    @FXML
    private Label saveLabel;

    public static ObservableList<String> productTypes;
    public static int numberOfSellsForDiscount;
    public static int discountAmount;

    public static void setNumberOfSellsForDiscount(int numberOfSellsForDiscount) {
        preferencesController.numberOfSellsForDiscount = numberOfSellsForDiscount;
    }

    public static int getNumberOfSellsForDiscount() {
        return numberOfSellsForDiscount;
    }

    public static void setDiscountAmount(int discountAmount) {
        preferencesController.discountAmount = discountAmount;
    }

    public static int getDiscountAmount() {
        return discountAmount;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        productTypeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        productTypeTable.setItems(productTypes);
        numberOfSellsForDiscountTextField.setText(String.valueOf(numberOfSellsForDiscount));
        discountAmountTextField.setText(String.valueOf(discountAmount));
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
    void getSelected(javafx.scene.input.MouseEvent mouseEvent) {
        int index = productTypeTable.getSelectionModel().getSelectedIndex();
        if (index < 0) return;
        productTypeTextField.setText(productTypeTable.getItems().get(index));
    }

    @FXML
    void save() throws SQLException {
        Connection connection = ConnectionClass.getConnection();
        String query = "UPDATE logins SET numberOfSellsForDiscount=?, discountAmount=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, numberOfSellsForDiscountTextField.getText());
        preparedStatement.setString(2, discountAmountTextField.getText());
        preparedStatement.setInt(3, user.getUserID());
        preparedStatement.executeUpdate();

        saveLabel.setTextFill(Paint.valueOf("green"));
        saveLabel.setText("Preferences updated successfully");
    }

}

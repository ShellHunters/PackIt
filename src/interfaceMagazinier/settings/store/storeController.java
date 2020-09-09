package interfaceMagazinier.settings.store;

import Connector.ConnectionClass;
import basicClasses.user;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class storeController implements Initializable {
    public Label status1;
    public JFXPasswordField passworrd2;
    public JFXTextField street;
    public JFXTextField city;
    public Label status;
    public JFXTextField newShopName;
    public JFXTextField shopeName;
    public JFXPasswordField passworrd1;
    public ChoiceBox <String> wilaya;
    public JFXTextField postalCode;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            shopeName.setText(user.getShopName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // add items to choicebox

        wilaya.getItems().add("01-Adrar");wilaya.getItems().add("02-Chlef");wilaya.getItems().add("03-Laghouat");
        wilaya.getItems().add("04-Oum el bouaghi");wilaya.getItems().add("05-Batna");wilaya.getItems().add("06-Béjaia");
        wilaya.getItems().add("07-Biskra");wilaya.getItems().add("08-Bechar");wilaya.getItems().add("09-Blida");
        wilaya.getItems().add("10-Bouira");wilaya.getItems().add("11-Tamanrasset");wilaya.getItems().add("12-Tbessa");
        wilaya.getItems().add("13-Tlemcen");wilaya.getItems().add("14-Tiaret");wilaya.getItems().add("15-Tizi-Ouzou");
        wilaya.getItems().add("16-Alger");wilaya.getItems().add("17-Djelfa");wilaya.getItems().add("18-Jijel");

        wilaya.getItems().add("19-Sétif");wilaya.getItems().add("20-Saida");wilaya.getItems().add("21-Skikda");
        wilaya.getItems().add("22-Sidi Bel-abbes");wilaya.getItems().add("23-Annaba");wilaya.getItems().add("24-Guelma");
        wilaya.getItems().add("25-Costantine");wilaya.getItems().add("26-Medéa");wilaya.getItems().add("27-Mostaganem");
        wilaya.getItems().add("28-Msila");wilaya.getItems().add("29-Mascara");wilaya.getItems().add("30-Ouergla");
        wilaya.getItems().add("31-Oran");wilaya.getItems().add("32-El-Bayadh");wilaya.getItems().add("33-Illizi");
        wilaya.getItems().add("34-Bordj Bou Arreridj");wilaya.getItems().add("35-Boumerdes");wilaya.getItems().add("36-El-Tarf");

        wilaya.getItems().add("37-Tindouf");wilaya.getItems().add("38-Tissemssilet");wilaya.getItems().add("39-El-Oued");
        wilaya.getItems().add("40-Khenchela");wilaya.getItems().add("41-Souk Ahras");wilaya.getItems().add("42-Tipaza");
        wilaya.getItems().add("43-Mila");wilaya.getItems().add("44-Ain Defla");wilaya.getItems().add("45-Naama");
        wilaya.getItems().add("46-Ain Timouchent");wilaya.getItems().add("47-Ghardaia");wilaya.getItems().add("48-Relizane");

    }

    public void saveShopLocation(ActionEvent event) {
        if (wilaya.getItems().toString().equals("")||city.getText().equals("")||street.getText().equals("")||passworrd2.getText().equals("")||postalCode.getText().equals(""))
        {
            status1.setTextFill(Color.RED);
            status1.setText("You must fill all the text fields");
        }
        else if (!(postalCode.getText().length()==5)||!postalCode.getText().matches("[0-9]+"))
        {
            status1.setTextFill(Color.RED);
            status1.setText("Wrong postal code");
            postalCode.setText("");
            passworrd2.setText("");
        }
        else if (!passworrd2.getText().equals(""))
        {
            status1.setTextFill(Color.RED);
            status1.setText("Wrong password");
            passworrd2.setText("");
        }
        else
        {
            wilaya.setValue(wilaya.getItems().toString());
            city.setText(city.getText());
            street.setText(street.getText());
            passworrd2.setText("");
            status1.setTextFill(Color.GREEN);
            status1.setText("Location saved Succefully ");
        }

    }


    public void saveShopName(ActionEvent event) throws SQLException {
        Connection connection = ConnectionClass.getConnection();
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
        else if (!passworrd1.getText().equals(user.getPassword()))
        {
            status.setTextFill(Color.RED);
            status.setText("Wrong password");
        }

        else if (!existinShopCheck(connection)) return;
        else
            {

                shopeName.setText(newShopName.getText());
                // here we change the shop name in the user class
                user.setShopName(shopeName.getText());
                status.setTextFill(Color.GREEN);
                status.setText("Your shope name changed succefully");
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

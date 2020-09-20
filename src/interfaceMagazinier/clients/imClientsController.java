package interfaceMagazinier.clients;

import Connector.ConnectionClass;
import basicClasses.client;
import basicClasses.user;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class imClientsController implements Initializable {

    public ObservableList<client> listM;
    ObservableList<client> Data;
    int index = -1;
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TextField txt_firstname;
    @FXML
    private TextField txt_familyname;
    @FXML
    private TableView<client> table_user;
    @FXML
    private TableColumn<client, Integer> col_id;
    @FXML
    private TableColumn<client, String> col_fn;
    @FXML
    private TableColumn<client, String> col_n;
    @FXML
    private TableColumn<client, Integer> numberOfSells;
    @FXML
    private JFXTextField searchfield;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tableSetUp();
        UpdateTable();
        search_user();
    }

    private void tableSetUp() {
        col_id.setCellValueFactory(new PropertyValueFactory<client, Integer>("id"));
        col_n.setCellValueFactory(new PropertyValueFactory<client, String>("firstName"));
        col_fn.setCellValueFactory(new PropertyValueFactory<client, String>("FamilyName"));
        numberOfSells.setCellValueFactory(new PropertyValueFactory<client, Integer>("numberOfSells"));
    }

    public void Add() {
        conn = Connector.ConnectionClass.getConnection();
        String sql = "insert into clients (firstname,familyname,userID)values(?,?,?) ";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, txt_firstname.getText());
            pst.setString(2, txt_familyname.getText());
            pst.setInt(3, user.getUserID());
            pst.execute();
            txt_familyname.setText("");
            txt_firstname.setText("");
            UpdateTable();
            search_user();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void getSelected(javafx.scene.input.MouseEvent mouseEvent) {
        index = table_user.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        txt_firstname.setText(col_n.getCellData(index));
        txt_familyname.setText(col_fn.getCellData(index));
    }

    public void Delete() {

        conn = Connector.ConnectionClass.getConnection();
        String sql = "delete from users where id = ? and userID=?";
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, col_id.getCellData(index).toString());
            pst.setInt(2,user.getUserID());
            pst.execute();
            txt_familyname.setText("");
            txt_firstname.setText("");
            UpdateTable();
            search_user();
        } catch (Exception e) {
        }
    }

    public void Update() {
        try {
            conn = Connector.ConnectionClass.getConnection();
            String Value1 = col_id.getCellData(index).toString();
            String Value2 = txt_familyname.getText();
            String Value3 = txt_firstname.getText();
            String sql = "update clients set firstName= '" + Value3 + "', FamilyName='" + Value2 + "' where id ='" + Value1 + "' where userID=?";
            pst = conn.prepareStatement(sql);
            pst.setInt(1,user.getUserID());
            pst.execute();
            txt_familyname.setText("");
            txt_firstname.setText("");
            UpdateTable();
            search_user();
        } catch (Exception e) {

        }
    }

    public void UpdateTable() {
        try {
            listM = getDatausers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        table_user.setItems(listM);
    }

    @FXML
    void search_user() {
//    col_id.setCellValueFactory(new PropertyValueFactory<clients, Integer>("id"));
//    col_fn.setCellValueFactory(new PropertyValueFactory<clients, String>("FamilyName"));
//    col_n.setCellValueFactory(new PropertyValueFactory<clients, String>("firstName"));
//
//    Data = mysqlconnect.getDatausers();
//    table_user.setItems(Data);
//    FilteredList<clients> filteredData = new FilteredList<>(Data, b -> true);
//    searchfield.textProperty().addListener((observable, oldValue, newValue) -> {
//      filteredData.setPredicate(person -> {
//        if (newValue == null || newValue.isEmpty()) {
//          return true;
//        }
//        String lowerCaseFilter = newValue.toLowerCase();
//        if (person.getFamilyName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
//          return true; // Filter matches Family Name
//        } else if (person.getFirstName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
//          return true; // Filter matches email
//        } else {
//          return false; // Does not match.
//        }
//      });
//    });
//    SortedList<clients> sortedData = new SortedList<>(filteredData);
//    sortedData.comparatorProperty().bind(table_user.comparatorProperty());
//    table_user.setItems(sortedData);
    }

    private ObservableList<client> getDatausers() throws SQLException {
        ObservableList<client> clients = FXCollections.observableArrayList();

        Connection connection = ConnectionClass.getConnection();
        String query = "SELECT * FROM clients where userID=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query) ;
        preparedStatement.setInt(1,user.getUserID());
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            client newClient = new client(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
            clients.add(newClient);
        }
        return clients;
    }
}

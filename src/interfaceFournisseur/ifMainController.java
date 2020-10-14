package interfaceFournisseur;

import basicClasses.Provider;
import basicClasses.user;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import identification.identificationMain;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import Connector.ConnectionClass;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import notification.notificationItem;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ifMainController implements Initializable {

    public abstract static class JFXCheckboxProductCell<T> extends TableCell<T, Boolean> {
        protected JFXCheckBox isChecked = new JFXCheckBox();

        public JFXCheckboxProductCell() {
            HBox hbox = new HBox();
            hbox.getChildren().addAll(this.isChecked);
            hbox.setAlignment(Pos.CENTER);
            hbox.setSpacing(5.0);

            this.initCheckBoxes();
        }

        private void initCheckBoxes() {
            this.isChecked.setOnAction(event -> {
                boolean newValue = this.isChecked.isSelected();
                T row = (T) this.getTableView().getItems().get(this.getIndex());
                this.onBeforeUpdateRow(row, newValue);
                this.onUpdateRow(row, newValue);
                (new Thread(() -> {
                    this.onUpdateEntity(row);
                })).start();
            });

        }

        @Override
        public void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || empty) {
                setGraphic(null);
                setText("");
            } else {
                this.isChecked.setSelected(this.getItem());
                this.setGraphic(this.isChecked.getParent());
                this.setText("");
            }
        }

        public abstract void onUpdateRow(T row, Boolean newValue);

        public abstract void onUpdateEntity(T entity);

        public void onBeforeUpdateRow(T row, Boolean newValue) {

        }
    }
    @FXML public StackPane providerStackPane;
    @FXML public JFXTextField searchfield;
    @FXML private TableView<tableItem> tableView;
    @FXML private TableColumn<tableItem,Boolean> checkCell;
    @FXML private TableColumn<tableItem, SimpleStringProperty> product;
    @FXML private TableColumn<tableItem, SimpleIntegerProperty> quantity;

    private Connection connection = ConnectionClass.getConnection();
    private ObservableList<tableItem> observableList = FXCollections.observableArrayList();
    private  ResultSet rs,rs1,rs2;
    private  tableItem theProduct;
    private ObservableList<tableItem> removeList=FXCollections.observableArrayList();
    private Provider provider;
    private int id=0;
    private PreparedStatement pr;
    private static int quant=0;
   public static JFXDialog dialog;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            provider=identification.identificationController.getProvider();
            System.out.println(user.getUserID()+" "+provider.getEmail());
            pr=connection.prepareStatement("SELECT name,quantity FROM stock WHERE DATEDIFF(expirationdate,now())<=0 and userID=?");
            pr.setInt(1,user.getUserID());
            rs=pr.executeQuery();
            while (rs.next()){
                observableList.add(new tableItem(rs.getString(1),rs.getInt(2)));
            }
            pr=connection.prepareStatement("SELECT name,quantity,initialQuantity FROM stock WHERE quantity=initialQuantity*0.2 and userID=?");
            pr.setInt(1,user.getUserID());
            rs=pr.executeQuery();
            while (rs.next()){
                boolean exist=false;
                for (tableItem item:observableList) if (item.getProduct().equals(rs.getString(2))) exist=true;
                if(!exist) observableList.add(new tableItem(rs.getString(1),rs.getInt(2)));
            }
        } catch (SQLException e) {
            System.err.println("SQL error:1 " + e);
        }
        checkCell.setCellValueFactory(call-> new SimpleBooleanProperty(false).asObject());
        checkCell.setCellFactory(call->{
            return new ifMainController.JFXCheckboxProductCell() {
                @Override
                public void onUpdateRow(Object row, Boolean newValue) {
                    theProduct = tableView.getItems().get(getIndex());
                    removeList.add(theProduct);
                }

                @Override
                public void onUpdateEntity(Object entity) {

                }
            };});
        product.setCellValueFactory(new PropertyValueFactory<>("Product"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        tableView.setItems(observableList);
        FilteredList<tableItem> filteredData = new FilteredList<>(observableList, b -> true);
        searchfield.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<tableItem> searching = FXCollections.observableArrayList();
            filteredData.setPredicate(employee -> {
                if (newValue == null || newValue.isEmpty() || newValue == "") {
                    return true;
                }
                String lowercase = newValue.toLowerCase();
                if (employee.getProduct().toLowerCase().contains(lowercase)) return true;
                else if (String.valueOf(employee.getQuantity()).contains(lowercase)) return true;
                else return false;
            });
            searching.addAll(filteredData);
            tableView.setItems(searching);
        });
    }

    public void suggestProduct(ActionEvent actionEvent) throws IOException {
        JFXButton source=(JFXButton) actionEvent.getSource();
        source.getStyleClass().add("pressed");
        Region root= FXMLLoader. load(getClass().getResource("suggest/suggest.fxml"));
        JFXDialog dialog=new JFXDialog(providerStackPane,root, JFXDialog.DialogTransition.TOP);
        dialog.show();
        dialog.setOnDialogClosed(jfxDialogEvent -> {source.getStyleClass().removeAll("pressed");});
    }


    @FXML
    public void exit(ActionEvent event) {
    System.exit(0);
    }
    public void confirm(ActionEvent actionEvent) throws SQLException{
        for (tableItem item: removeList){
            ResultSet rs2;
            int id=0;
            observableList.removeAll(removeList);
            for (tableItem items:removeList){
                rs2=connection.createStatement().executeQuery("select  initialQuantity from stock where name='"+items.getProduct()+"'");
                while(rs2.next()){
                    quant=rs2.getInt(1);
                    PreparedStatement pr=connection.prepareStatement("update stock set expirationdate=now(),quantity=? where name=?");
                    pr.setInt(1,quant);
                    pr.setString(2,items.getProduct());
                    pr.executeUpdate();
                }
            }
        }
    }

    @FXML
    public void close(ActionEvent event) throws Exception{
        ((Stage)providerStackPane.getScene().getWindow()).close();
        Stage loginStage=new Stage();
        identificationMain loginIerface=new identificationMain();
        loginIerface.start(loginStage);
    }
    @FXML
    public void specialOreders(ActionEvent event) throws IOException {
        JFXButton source=(JFXButton) event.getSource();
        Region root=FXMLLoader.load(getClass().getResource("specialOrders/specialOrder.fxml"));
         dialog= new JFXDialog(providerStackPane,root, JFXDialog.DialogTransition.TOP);
        dialog.show();
        dialog.setOnDialogClosed(jfxDialogEvent -> {source.getStyleClass().removeAll("pressed");});
    }
}
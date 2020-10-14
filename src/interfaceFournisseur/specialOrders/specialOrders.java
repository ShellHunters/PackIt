package interfaceFournisseur.specialOrders;

import Connector.ConnectionClass;
import basicClasses.Provider;
import com.jfoenix.controls.JFXCheckBox;
import interfaceFournisseur.ifMainController;
import interfaceFournisseur.tableItem;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class specialOrders implements Initializable {
    public void close (ActionEvent event) {
        ifMainController.dialog.close();
    }

    public abstract class JFXCheckboxProductCell<T> extends TableCell<T, Boolean> {
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

    public TableView<tableItem> tableView;
    public TableColumn<tableItem,Boolean> checkCell;
    public TableColumn<tableItem, SimpleIntegerProperty> quantity;
    public TableColumn<tableItem, SimpleStringProperty> name;
    private ResultSet rs;
    private Connection connection= ConnectionClass.getConnection();
    private ObservableList<tableItem> observableList= FXCollections.observableArrayList();
    private Provider provider;
    private tableItem theProduct;
    private ObservableList<tableItem> removeList=FXCollections.observableArrayList();
    private static  int id=0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            try {

                provider=identification.identificationController.getProvider();
                rs=connection.createStatement().executeQuery("SELECT id from ProvidersInfo where Email='"+provider.getEmail()+"'");
                while(rs.next()) id=rs.getInt(1);
                rs=connection.createStatement().executeQuery("SELECT * FROM ProductsCommand where idOfProvider="+id);
                while(rs.next()) observableList.add(new tableItem(rs.getString(2),rs.getInt(5),rs.getInt(1)));
            }catch (SQLException e){
                System.err.println(e);
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
            name.setCellValueFactory(new PropertyValueFactory<>("Product"));
            quantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
            tableView.setItems(observableList);
    }

    public void confirm(ActionEvent event) throws SQLException {
        for (tableItem item: removeList){
            PreparedStatement pr=connection.prepareStatement("update ProductsCommand SET confirmedProduct=true WHERE idOfProvider=? AND id=?");
            pr.setInt(1,id);
            pr.setInt(2,item.getIdOfCommand());
            pr.executeUpdate();
            observableList.removeAll(removeList);
            tableView.setItems(observableList);
        }

    }
}

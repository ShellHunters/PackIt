package interfaceClient;
import Connection.ConnectionClientClass;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
/*
* so i add a new database with name "product" and other method for interface client getClientConnection()
* for get connect to product database
*
* */
public class clientsController implements Initializable {
    ConnectionClientClass connect=new ConnectionClientClass();
    Connection connection = connect.getClientConnection();
    String sql = "SELECT * FROM productlist"; // put in sql your database name
    @FXML public TextField Rechercher; // rechercher
    @FXML public TableView<tableViewModule> table;
    @FXML public TableColumn<tableViewModule, SimpleStringProperty> nom;
    @FXML public TableColumn<tableViewModule, SimpleIntegerProperty> id;
    @FXML public TableColumn<tableViewModule,SimpleIntegerProperty> prix;
          public ObservableList<tableViewModule> list= FXCollections.observableArrayList();

    @FXML public Button dashboardButton, ventesButton, fournisseursButton, clientsButton, parametresButton;
    @FXML public JFXHamburger hamburger;
    @FXML public MenuButton menuButton;
    @FXML public ImageView notificationImage;
    @FXML public StackPane mainStackPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        //Hamburger menu
        HamburgerSlideCloseTransition hamburgerTransition = new HamburgerSlideCloseTransition(hamburger);
        hamburgerTransition.setRate(-1);
        menuButton.showingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                hamburgerTransition.setRate(hamburgerTransition.getRate()*-1);
                hamburgerTransition.play();
            }
        });
        //Notifications
        JFXDialog notification = new JFXDialog();
        notificationImage.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            notification.setContent(new Label("NOTIFICATIONSSQSDFQSDDFHGQSTFGQSTYFQSDF"));
            notification.show(mainStackPane);
        });


        // product table
        {
            try {
                connection = connect.getConnection();
                sql="SELECT * FROM productList";
                ResultSet rs=connection.createStatement().executeQuery(sql);
                while (rs.next()){
                    list.add(new tableViewModule(rs.getInt(1),rs.getString(2),rs.getInt(3)+" DA"));
                }

            } catch (SQLException e) {
                System.out.println("error:"+e);
            }

        }
        // Searching function
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        prix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        table.setItems(list);
        FilteredList<tableViewModule> filteredData= new FilteredList<>(list, b ->true);
        Rechercher.textProperty().addListener((observable,oldValue,newValue)->{
            filteredData.setPredicate( employee ->{
                if (newValue == null || newValue.isEmpty()){
                    return true;
                }
                String lowerCaseFilter=newValue.toLowerCase();
                if (String.valueOf(employee.getId()).indexOf(lowerCaseFilter)!=-1)
                    return true;
                else if (employee.getNom().toLowerCase().indexOf(lowerCaseFilter)!=-1)
                    return true;
                else if (String.valueOf(employee.getPrix()).indexOf(lowerCaseFilter)!=-1)
                    return true;
                else
                    return false;
            });

        });
        SortedList<tableViewModule> sortedList=new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }

    @FXML  public void button(ActionEvent event) {
        //Change color to current button pressed
        int i = 0,j;
        Button buttons[] = {dashboardButton, ventesButton, fournisseursButton, clientsButton, parametresButton};
        while (i<5 && event.getSource() != buttons[i]) i++;
        for(j=0; j < 5; j++) {
            if (j != i) buttons[j].getStyleClass().removeAll("activeButton");
        }
        buttons[i].getStyleClass().add("activeButton");
        //
    }

    public void logOut(){
        System.out.println("logout");
    }

    public void exit(){
        System.exit(0);
    }

    public void getNotifications(){
    }
}

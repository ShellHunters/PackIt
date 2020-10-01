

package notifications;

import Connector.ConnectionClass;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;


public class notificaitonController implements Initializable {
    @FXML public  JFXListView<notificationItem> listview= new JFXListView<>();
    @FXML public JFXTextField reherche_text;
    @FXML public HBox hbox;
    @FXML public ImageView close_image;
    @FXML public ImageView image_rechercher;
    @FXML public StackPane pane_of_close;
    @FXML public StackPane mainStack;
    private ObservableList<notificationItem> Oblist=FXCollections.observableArrayList();

    private LinkedList<notificationItem>  linkedList=new LinkedList<>();
    private ConnectionClass connect=new ConnectionClass();
    private Connection connection=connect.getConnection();
    private String SQL=null;
    private String description="";
    private ResultSet rs, rs1,rs2;
    private Boolean exist;

    //================================================ notification cell ========================================================================
    static class notification_cell extends ListCell<notificationItem>{
        private  HBox hBox=new HBox(10);
        private VBox vBox=new VBox(5);
        private Text titel=new Text();
        private HBox HBOX =new HBox();
        private Text description1=new Text();
        private Text duration=new Text();
        private ImageView image_item=new ImageView();
        private ImageView image_delte=new ImageView();
        private  Stage info=new Stage();
        private  Label infoLabel=new Label();
        public notification_cell() {
            super();
            //================ notification text style ==========================
            vBox.setId("vBox");
            hBox.setId("hBox");
            HBOX.setId("HBOX");
            titel.setId("title");
            description1.setId("description1");
            duration.setId("duration");
            HBOX.setAlignment(Pos.CENTER);
            HBOX.setPrefWidth(32);
            HBOX.setPrefHeight(32);
            HBOX.setStyle("-fx-background-radius:15");
            titel.setStyle("-fx-font-size: 16;" +
                    "-fx-text-fill: #163143");
            description1.setStyle("-fx-font-size: 12;" +
                    "-fx-text-fill: #266f8c");
            duration.setStyle("-fx-font-size:8;"+
                    "-fx-text-fill: #858585");
            image_delte.setFitWidth(25);
            image_delte.setFitHeight(25);

            hBox.setOnMouseEntered(e -> {
                image_delte.setImage(new Image("/resource/icons/delete.png"));
                image_delte.setFitWidth(25);
                image_delte.setFitHeight(25);
            });
            hBox.setOnMouseExited(e->{
                image_delte.setFitWidth(1);
                image_delte.setFitHeight(1);
            });
            hBox.setAlignment(Pos.CENTER);
            //====================================================================
            HBOX.getChildren().add(image_delte);
            hBox.getChildren().addAll(image_item, vBox, HBOX);
            vBox.getChildren().addAll(titel, description1, duration);
        }
        @Override
        protected void updateItem(notificationItem item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);  // No text in label of super class
            if (item==null || empty) {
                setText("");
                setGraphic(null);
            }else {
                image_delte.setOnMouseClicked(event -> {
                    getListView().getItems().remove(item);
                    try {
                        PreparedStatement pr= ConnectionClass.getConnection().prepareStatement("DELETE FROM notification WHERE description='"+item.getDescription_text()+"'");
                        pr.executeUpdate();

                    }catch (SQLException e){
                        System.err.println(e);
                    }
                });
                String text=item.getDescription_text();
                Scene scene=new Scene(infoLabel);
                infoLabel.setText(text);
                info.initStyle(StageStyle.TRANSPARENT);
                infoLabel.setStyle("-fx-background-color: #5b5b5b;-fx-text-fill: #ffffff");
                info.setScene(scene);
                info.setResizable(false);
                if (item.getDescription_text().length()>25) {
                    item.setDescription_text(text.substring(0,20)+" ...");
                    hBox.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
                        if(newValue){
                            infoLabel.setPadding(new Insets(0,5,0,5));
                            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
                            double x = bounds.getMinX() + (bounds.getWidth() - scene.getWidth()) * 0.3;
                            double y = bounds.getMinY() + (bounds.getHeight() - scene.getHeight()) * 0.7;
                            info.setX(x);
                            info.setY(y);
                            info.show();
                        }else{
                            info.hide();
                        }
                    });
                }
                titel.setText(item.getTitel_text());
                description1.setText(item.getDescription_text());
                duration.setText(item.getDuration_text());
                image_item.setImage(item.getImage_item());
                setGraphic(hBox);
            }
        }

    }

    //======================================================================================================================
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //============================================Searching bar animation============================================
        image_rechercher.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            hbox.setPrefWidth(250);
            hbox.setLayoutX(184.0);
            hbox.setLayoutY(11.0);
            close_image.setFitHeight(29);
            close_image.setFitWidth(29);
        });
        pane_of_close.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            hbox.setPrefWidth(48);
            hbox.setLayoutX(386.0);
            close_image.setFitHeight(1);
            close_image.setFitWidth(1);
        });
//============================================== notification controlle=================================================
        try {
            rs1 = connection.createStatement().executeQuery("SELECT * FROM user.notification");
            while (rs1.next()) {
                String URL = null;
                if (rs1.getString(1).equals("Expired Products"))
                    URL = "/resource/icons/Expired_date.png";
                else if (rs1.getString(1).equals("Expired date comming"))
                  URL = "/resource/icons/Expired_date_comming.png";
                else if (rs1.getString(1).equals("Will Finish"))URL="/resource/icons/fuel.png";
                else URL="/resource/icons/check.png";
                linkedList.addFirst(new notificationItem(rs1.getString(1), rs1.getString(2), durationDate(rs1.getString(3)), URL));
            }
//            ======================================== test expired products =========================================================

            recherNotification("Expired Products","SELECT * FROM user.stock WHERE DATEDIFF(stock.expirationdate,now()) <= 0" );
            recherNotification("Expired date comming","SELECT*FROM user.stock WHERE DATEDIFF(stock.expirationdate,now()) BETWEEN 1 AND 7");
            recherNotification("Will Finish","SELECT * FROM user.stock WHERE quantity=initialQuantity*0.2 AND DATEDIFF(expirationdate,now())>0");
            recherNotification("Confirmed Products","SELECT ProductName FROM ProductsCommand WHERE confirmedProduct=true");

            Oblist.addAll(linkedList);
            listview.setItems(Oblist);
            listview.setCellFactory(new Callback<ListView<notificationItem>, ListCell<notificationItem>>() {
                @Override
                public ListCell<notificationItem> call(ListView<notificationItem> item_typeListView) {
                    return new notification_cell(){

                    };
                }
            });

            FilteredList<notificationItem> filteredData = new FilteredList<>(Oblist, b -> true);
            reherche_text.textProperty().addListener((observable, oldValue, newValue) -> {
                ObservableList<notificationItem> searching = FXCollections.observableArrayList();
                filteredData.setPredicate(employee -> {
                    if (newValue == null || newValue.isEmpty() || newValue == "") {
                        return true;
                    }
                    String lowercase = newValue.toLowerCase();
                    if (employee.getDescription_text().toLowerCase().contains(lowercase)) return true;
                    else if (employee.getDuration_text().toLowerCase().contains(lowercase)) return true;
                    else if (employee.getTitel_text().toLowerCase().contains(lowercase)) return true;
                    else return false;
                });
                searching.addAll(filteredData);
                listview.setItems(searching);
            });


        } catch (SQLException e) {
            System.err.println("1 "+e);
        }
    }
    public String durationDate(String time) throws SQLException {
        String duration_sql = null;
        int proide = 0;
        rs = connection.createStatement().executeQuery("SELECT DATEDIFF(NOW(),'" + time + "')");
        while (rs.next()) proide = rs.getInt(1);
        if (proide == 0) {
            rs = connection.createStatement().executeQuery("SELECT hour(TIMEDIFF(TIME(NOW()),TIME('"+time+"')))");
            while (rs.next()) proide = rs.getInt(1);
            if (proide == 0) {
                rs = connection.createStatement().executeQuery("SELECT minute((TIMEDIFF(TIME(NOW()),TIME('" + time + "'))))");
                while (rs.next()) proide = rs.getInt(1);
                if (proide==0){
                    rs = connection.createStatement().executeQuery("SELECT second((TIMEDIFF(TIME(NOW()),TIME('" + time + "'))))");
                    while (rs.next()) proide = rs.getInt(1);
                    if (proide<=10) duration_sql="just now";
                    else duration_sql=proide+" seconds ago";
                }else if (proide==1) duration_sql="1 minute ago";
                else duration_sql=proide+" minutes ago";
            } else if (proide == 1) duration_sql = "1 hour ago";
            else duration_sql = proide + " hours ago";
        }
        else if (proide < 365){
            if (proide == 1) duration_sql="1 day ago";
            else if (proide < 30) duration_sql= proide +" days ago";
            else if (proide/30==1) duration_sql="1 month ago";
            else duration_sql=(proide/30)+" months ago";
        }
        else{
            if ((proide/365) == 1) duration_sql="1 year ago";
            else duration_sql=(proide/365) +" years ago";}
        return duration_sql ;
    }
    public void checkExistingAndAdd(String descr,String typeTitle) throws SQLException{
        String URL=null;
        String SQL;
        if(descr!="") {
            rs1 = connection.createStatement().executeQuery("SELECT * FROM user.notification");
            exist = true;
            while (rs1.next()&&exist) {
                if ((rs1.getString(1).equals(typeTitle))&&(rs1.getString(2).equals(descr))) exist = false;
            }
            if (exist) {
                    if(typeTitle.equals("Expired Products"))
                        URL="/resource/icons/Expired_date.png";
                    else
                         if(typeTitle.equals("Expired date comming"))
                             URL="/resource/icons/Expired_date_comming.png";
                         else
                             URL="/resource/icons/fuel.png";
                         linkedList.addFirst(new notificationItem(typeTitle,descr,"few seconds",URL));
                         SQL = "INSERT INTO user.notification VALUES ('"+typeTitle+"','"+descr+"',"+"now(),1)";
                         connection.createStatement().executeUpdate(SQL);
            }
        }
    }
    public void recherNotification(String type,String SQL) throws SQLException{
        ResultSet rs;
        String description="";
        rs= ConnectionClass.getConnection().createStatement().executeQuery(SQL);
        while(rs.next()){
            System.out.println(rs.getString(4));
            description+=rs.getString(4)+" ";
        }
        System.out.println(description);
        checkExistingAndAdd(description,type)   ;
    }
}


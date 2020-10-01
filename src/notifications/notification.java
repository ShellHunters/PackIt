package notifications;

import Connector.ConnectionClass;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class notification implements Initializable {
    @FXML
    public JFXListView<notificationItem> listview= new JFXListView<>();
    @FXML public JFXTextField reherche_text;
    @FXML public HBox hbox;
    @FXML public ImageView close_image;
    @FXML public ImageView image_rechercher;
    @FXML public StackPane pane_of_close;
    public Connection connection= ConnectionClass.getConnection();
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void fillListView(LinkedList<notificationItem> listview) throws SQLException{
        ResultSet rs,rs1;
        String sql;
        sql="SELECT * FROM notification";
        rs= ConnectionClass.getConnection().createStatement().executeQuery(sql);
        while(rs.next()){
            listview.addFirst(new notificationItem(rs.getString(1),rs.getString(2),durationDate(rs.getString(3)),returnImgUrl(rs.getString(1))));
        }
    }
    public String returnImgUrl(String type){
        String URL;
        switch(type){
            case "Expired Products ":
                return "/resource/icons/expired_product_white.png";
            case"Expired date comming":
                return "/resource/icons/remand_product_White.png";
            case"Will Finish":
                return "/resource/icons/OutOfstock.png";
            default:
                return null;
        }
    }
    public String durationDate(String time) throws SQLException {
        String duration_sql = null;
        ResultSet rs;
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
                    if (proide==0) duration_sql="just now";
                    else if (proide==1) duration_sql="1 second ago ";
                    else duration_sql=proide+" seconds ago";
                }else if (proide==1) duration_sql="1 minute ago";
                else duration_sql=proide+"minutes ago";
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
}

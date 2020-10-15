package interfaceMagazinier;

import Connector.ConnectionClass;
import basicClasses.user;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import identification.identificationMain;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.ResourceBundle;

public class imMainController implements Initializable {
    @FXML public Button dashboardButton, stockButton, sellsButton, providersButton, clientsButton, settingsButton;
    @FXML public JFXHamburger hamburger;
    @FXML public MenuButton menuButton;
    @FXML public ImageView notificationImage;
    @FXML public StackPane mainStackPane;
    @FXML public AnchorPane contentPane;
    public AnchorPane container;
    public Circle imageCircle;
    public Label username;
    @FXML public Label notificationCont;
    public static HashSet<String> notificationHashList=new HashSet<>();
    @FXML public Label notification_cont;
    private int nomuber_notifi=0,newNotification=0;
    Node contents[] = new Node[6];
    public static FXMLLoader sellLoader;
    public static FXMLLoader imMainLoader;
    private Connection connection=ConnectionClass.getConnection();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        //username
        try {
            username.setText(user.getFullName());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //profile picture
        try {
            loadProfilePicture();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



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

       /* notificationImage.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                Region rootNotification=FXMLLoader.load(getClass().getResource("../notification/notification.fxml"));
                JFXDialog notification = new JFXDialog(mainStackPane,rootNotification, JFXDialog.DialogTransition.LEFT);
                notification.getStylesheets().add(getClass().getResource("../notification/notification_style.css").toString());
                notification.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });*/

        loadContent();

        dashboardButton.getStyleClass().add("activeButton");
        contentPane.getChildren().setAll(contents[0]);
        try {
            newNotification=checkNotification(newNotification);
            System.out.println(newNotification);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (newNotification > 0) {
            nomuber_notifi=newNotification;
            newNotification=0;
            notification_cont.setText("" + nomuber_notifi);
            notification_cont.setStyle("-fx-background-color: #ff5646;-fx-background-radius: 50;-fx-font-size:14px;-fx-padding: 0 5 0 5");
           /* Media sound=new Media(getClass().getResource("/resource/sound/notification-sound.mp3").toString());
            MediaPlayer mediaPlayer=new MediaPlayer(sound);
            mediaPlayer.play();*/
        }
        notificationImage.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            nomuber_notifi=0;
            notification_cont.setText("");
            notification_cont.setStyle("-fx-background-radius: 30;-fx-font-size:14px;-fx-padding: 0 5 0 5");
            try {
                Region root1=FXMLLoader.load(getClass().getResource("/notification/notification.fxml"));
                JFXDialog dialog =new JFXDialog(mainStackPane,root1, JFXDialog.DialogTransition.RIGHT);
                dialog.getStylesheets().add(getClass().getResource("/notification/notification_style.css").toString());
                dialog.show();
            } catch (IOException e) {
                System.err.println(e);
            }
        });

    }

    public void button(ActionEvent event) throws IOException {
        //Change color to current button pressed
        int i = 0,j;
        Button buttons[] = {dashboardButton, stockButton, sellsButton, providersButton, clientsButton, settingsButton};
        while (i<6 && event.getSource() != buttons[i]) i++;
        //reload content in certain cases
        if (i == 1) contents[1] = FXMLLoader.load(getClass().getResource("stock/imStock.fxml"));
        if (i == 0) contents[0] = FXMLLoader.load(getClass().getResource("dashboard/imDashboard.fxml"));
        if (i == 4) contents[4] = FXMLLoader.load(getClass().getResource("clients/imClients.fxml"));

        buttons[i].getStyleClass().add("activeButton");
        for(j=0; j < 6; j++) if (j != i) buttons[j].getStyleClass().removeAll("activeButton");
        //SWAP CONTENT
        contentPane.getChildren().setAll(contents[i]);
    }

    private void loadContent(){
        //Right here the things u need to reload when u press a button
        try {

            contents[0] = FXMLLoader.load(getClass().getResource("dashboard/imDashboard.fxml"));
            contents[1] = FXMLLoader.load(getClass().getResource("stock/imStock.fxml"));

            sellLoader = new FXMLLoader(getClass().getResource("sells/imSells.fxml"));
            contents[2] = sellLoader.load();

            contents[3] = FXMLLoader.load(getClass().getResource("providers/imProviders.fxml"));
            contents[4] = FXMLLoader.load(getClass().getResource("clients/imClients.fxml"));
            contents[5] = FXMLLoader.load(getClass().getResource("settings/imSettings.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadProfilePicture() throws SQLException, IOException {
        Connection connection = ConnectionClass.getConnection();
        String sql = "SELECT image From logins where email= ? and shopName= ? " ;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,user.getEmail());
        preparedStatement.setString(2,user.getShopName());
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){

            InputStream imageDataBase = resultSet.getBinaryStream("image");
            OutputStream imageFile = new FileOutputStream(new File("profile.jpg"));
            byte [] content = new byte[1024] ;
            int size=0;
            while ((size=imageDataBase.read(content)) != -1)
            {
                imageFile.write(content,0,size);

            }
            imageFile.close();
            imageDataBase.close();
            Image image = new Image("file:profile.jpg",150,150,true,true);
            imageCircle.setStroke(Color.TRANSPARENT);
            imageCircle.setFill(new ImagePattern(image));
        }
        else {
            System.out.println("hello");
        }

    }

    public void notificationDisplay() throws IOException {
        /*Region root1 = FXMLLoader.load(getClass().getResource("notification/notification.fxml"));
        JFXDialog notification = new JFXDialog(mainStackPane, root1, JFXDialog.DialogTransition.LEFT);
        notification.getStylesheets().add(getClass().getResource("notification/notification_style.css").toString());
        notification.show();*/
    }

    public void logOut() throws Exception {
        ((Stage) mainStackPane.getScene().getWindow()).close();
        Stage loginStage = new Stage();
        identificationMain loginInterface = new identificationMain();
        loginInterface.start(loginStage);
    }

    public void exit()  { ((Stage) mainStackPane.getScene().getWindow()).close(); }
    public int checkNotification(int n) throws SQLException{
        String products=null;
        ResultSet rs,rs1;
        boolean exist;
        if(checkIf("Expired Products", getdesc("SELECT name FROM user.stock where (DATEDIFF(stock.expirationdate,now()) <= 0) and userID="+user.getUserID()))) n++;
        if (checkIf("Expired date comming",getdesc("SELECT name FROM user.stock WHERE (DATEDIFF(stock.expirationdate,now()) BETWEEN 1 AND 7) and userID="+user.getUserID()))) n++;
        if (checkIf("Will Finish",getdesc("SELECT name FROM user.stock WHERE quantity=initialQuantity*0.2 AND DATEDIFF(expirationdate,now())>0 and userID="+user.getUserID())))n++;
        if (checkIf("Confirmed Products",getDescForConfirmed("SELECT ProductName FROM ProductsCommand WHERE confirmedProduct=true and userID="+user.getUserID())))n++;
        if (checkIf("Suggest Product",getDescForConfirmed("SELECT name from suggestproducts where userID="+user.getUserID()))) n++;
        return n;
    }
    public boolean checkIf(String type,String des) throws SQLException {
        boolean exist=true;
        String SQL;
        ResultSet rs = ConnectionClass.getConnection().createStatement().executeQuery("SELECT * FROM user.notification WHERE userID="+user.getUserID());
        if (des!=""&&des!=" ")
            while (rs.next()&& exist) {
                if (rs.getString(1).equals(type) && rs.getString(2).equals(des)) exist=false;
                else if (notificationHashList.contains(des)) exist=false;
            }
        else exist=false;
        if (exist) {
            notificationHashList.add(des);
            //----------------------------------------set user id -----------------------------------------------------------------------
            //------------------------------------------------------------------------------------
            SQL = "INSERT INTO user.notification (title, description, date, userID) VALUES ('"+type+"','"+des+"',now(),"+user.getUserID()+") ";
            connection.createStatement().executeUpdate(SQL);
        }
        System.out.println(exist+" "+notificationHashList.isEmpty());
        return exist;
    }
    public String getDescForConfirmed(String sql) throws SQLException{
        String des="";
        HashSet<String> products=new HashSet<>();
        ResultSet rs=connection.createStatement().executeQuery(sql);
        while (rs.next()) products.add(rs.getString(1));
        rs=connection.createStatement().executeQuery("SELECT name from stock where quantity=initialQuantity*0.2 and DATEDIFF(expirationdate,NOW())>0 and userID="+user.getUserID());
        while (rs.next()) if (products.contains(rs.getString(1))) des+=rs.getString(1)+" ";
        rs=connection.createStatement().executeQuery("SELECT name from stock where datediff(expirationdate,now())<=0 and userID="+user.getUserID());
        while (rs.next()) if (products.contains(rs.getString(1))) des+=rs.getString(1)+" ";
        return des;
    }
    public String getdesc(String sql) throws SQLException{
        String products="";
        ResultSet rs= ConnectionClass.getConnection().createStatement().executeQuery(sql);
        while (rs.next()){
            products+=rs.getString(1)+" ";
        }
        System.out.println(products);
        return products;
    }
}

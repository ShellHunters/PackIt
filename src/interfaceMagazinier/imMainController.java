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

    Node contents[] = new Node[6];
    public static FXMLLoader sellLoader;
    public static FXMLLoader imMainLoader;

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
        JFXDialog notification = new JFXDialog();
        notificationImage.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                notification.setContent(FXMLLoader.load(getClass().getResource("../notification/notification.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            notification.show(mainStackPane);
        });

        loadContent();

        dashboardButton.getStyleClass().add("activeButton");
        contentPane.getChildren().setAll(contents[0]);
    }

    public void button(ActionEvent event) throws IOException {
        //Change color to current button pressed
        int i = 0,j;
        Button buttons[] = {dashboardButton, stockButton, sellsButton, providersButton, clientsButton, settingsButton};
        while (i<6 && event.getSource() != buttons[i]) i++;
        //reload content in certain cases
        if (i == 1) contents[1] = FXMLLoader.load(getClass().getResource("stock/imStock.fxml"));
        if (i == 0) contents[0] = FXMLLoader.load(getClass().getResource("dashboard/imDashboard.fxml"));

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
        Region root1 = FXMLLoader.load(getClass().getResource("notification/notification.fxml"));
        JFXDialog notification = new JFXDialog(mainStackPane, root1, JFXDialog.DialogTransition.RIGHT);
        notification.show();
    }

    public void logOut() throws Exception {
        ((Stage) mainStackPane.getScene().getWindow()).close();
        Stage loginStage = new Stage();
        identificationMain loginInterface = new identificationMain();
        loginInterface.start(loginStage);
    }

    public void exit()  { ((Stage) mainStackPane.getScene().getWindow()).close(); }
}

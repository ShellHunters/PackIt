package interfaceMagazinier;

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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sun.plugin.javascript.navig.Anchor;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class imMainController implements Initializable {
    @FXML public Button dashboardButton, stockButton, sellsButton, providersButton, clientsButton, settingsButton;
    @FXML public JFXHamburger hamburger;
    @FXML public MenuButton menuButton;
    @FXML public ImageView notificationImage;
    @FXML public StackPane mainStackPane;
    @FXML public AnchorPane contentPane;

    Node contents[] = new Node[6];

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        dashboardButton.getStyleClass().add("activeButton");
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
        //Content load
        try {
            contents[0] = FXMLLoader.load(getClass().getResource("dashboard/imDashboard.fxml"));
            contents[1] = FXMLLoader.load(getClass().getResource("stock/imStock.fxml"));
            contents[2] = FXMLLoader.load(getClass().getResource("sells/imSells.fxml"));
            contents[3] = FXMLLoader.load(getClass().getResource("providers/imProviders.fxml"));
            contents[4] = FXMLLoader.load(getClass().getResource("clients/imClients.fxml"));
            contents[5] = FXMLLoader.load(getClass().getResource("settings/imSettings.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void button(ActionEvent event) throws IOException {
        //Change color to current button pressed
        int i = 0,j;
        Button buttons[] = {dashboardButton, stockButton, sellsButton, providersButton, clientsButton, settingsButton};
        while (i<6 && event.getSource() != buttons[i]) i++;
        buttons[i].getStyleClass().add("activeButton");
        for(j=0; j < 6; j++) if (j != i) buttons[j].getStyleClass().removeAll("activeButton");
        //SWAP CONTENT
        contentPane.getChildren().setAll(contents[i]);
    }

    public void logOut() throws Exception {
        ((Stage) mainStackPane.getScene().getWindow()).close();
        Stage loginStage = new Stage();
        identificationMain loginInterface = new identificationMain();
        loginInterface.start(loginStage);
    }

    public void exit(){
        System.exit(0);
    }
}

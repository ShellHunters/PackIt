package interfaceMagazinier;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class imMainController implements Initializable {
    @FXML public Button dashboardButton, ventesButton, fournisseursButton, clientsButton, parametresButton;
    @FXML public JFXHamburger hamburger;
    @FXML public MenuButton menuButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        //
    }

    public void button(ActionEvent event) {
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
}

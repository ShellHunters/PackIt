package interfaceMagazinier.settings;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class imSettingsController implements Initializable {


    public AnchorPane contentPane;
    public Button shopButton;
    public Button securityButton;
    public Button termButton;
    public Button aboutButton;
    public Button contactButton;
    public Button personalButton;
    public Button preferenceButton;
    Node contents[] = new Node[7];



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadContent();
        personalButton.getStyleClass().add("activeButton");
        contentPane.getChildren().setAll(contents[0]);


    }

    public void button(ActionEvent event) throws IOException {
        //Change color to current button pressed
        int i = 0,j;
        Button buttons[] = {personalButton,shopButton, securityButton, termButton, aboutButton, contactButton,preferenceButton};
        while (i<7 && event.getSource() != buttons[i]) i++;
        //reload content in certain cases
       /* if (i == 2) contents[2] = FXMLLoader.load(getClass().getResource("security/security.fxml"));
        if (i == 1) contents[1] = FXMLLoader.load(getClass().getResource("store/store.fxml"));
        if (i == 0) contents[0] = FXMLLoader.load(getClass().getResource("personal/personal.fxml"));*/
        buttons[i].getStyleClass().add("activeButton");
        for(j=0; j <7; j++) if (j != i) buttons[j].getStyleClass().removeAll("activeButton");
        //SWAP CONTENT
        contentPane.getChildren().setAll(contents[i]);
    }
    private void loadContent(){
        //Right here the things u need to reload when u press a button
        try {
            contents[0] = FXMLLoader.load(getClass().getResource("personal/personal.fxml"));
            contents[1] = FXMLLoader.load(getClass().getResource("store/store.fxml"));
            contents[4] = FXMLLoader.load(getClass().getResource("about/about.fxml"));
            contents[2] = FXMLLoader.load(getClass().getResource("security/security.fxml"));
            contents[3] = FXMLLoader.load(getClass().getResource("term/term.fxml"));
            contents[5] = FXMLLoader.load(getClass().getResource("contact/contact.fxml"));
            contents[6] = FXMLLoader.load(getClass().getResource("preference/preferences.fxml"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

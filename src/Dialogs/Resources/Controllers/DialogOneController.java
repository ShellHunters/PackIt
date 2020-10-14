package Dialogs.Resources.Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static Dialogs.Resources.Controllers.ShowAllDialogs.*;

public class DialogOneController implements Initializable {
    @FXML
    public Label DialogHeader;

    @FXML
    public Label DialogBody;

    @FXML
    private JFXButton DialogOneButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }
    @FXML
    void DialogOneButtonAction(ActionEvent event) {
ShowAllDialogs.ClickedButton.dialogsONEBUTTON.set(true);
        ExitDialog();
    }

    @FXML
    void OneDialogDragged(MouseEvent event) {
        DialogSceneDraggedMouse(event);
    }

    @FXML
    void OneDialogPressed(MouseEvent event) {
        DialogScenePressedMouse(event);

    }
    public void settingHeaderAndBody(String Header , String Body){
        DialogBody.setText(Body);
                DialogHeader.setText(Header);
    }

}

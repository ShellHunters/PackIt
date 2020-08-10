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
    private Label DialogHeader;

    @FXML
    private Label DialogBody;

    @FXML
    private JFXButton DialogOneButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DialogHeader.setText(ShowAllDialogs.Header);
        DialogBody.setText(ShowAllDialogs.Body);

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

}

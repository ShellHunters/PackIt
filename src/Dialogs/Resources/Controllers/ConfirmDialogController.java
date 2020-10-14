package Dialogs.Resources.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

import static Dialogs.Resources.Controllers.ShowAllDialogs.*;

public class ConfirmDialogController implements Initializable {

    @FXML
    public Label DialogBody;

    @FXML
    public Label DialogHeader;
    @FXML
  static  public StackPane ConfirmDialogRoot;
    public void ConfirmDialogYes(ActionEvent event) {
        ClickedButton.dialogsYESBUTTON.set(true);
        ExitDialog();
    }

    public void ConfirmDialogNo(ActionEvent event) {
ClickedButton.dialogsNOBUTTON.set(true);
ExitDialog();
}
    public void settingHeaderAndBody(String Header , String Body){
        DialogBody.setText(Body);
        DialogHeader.setText(Header);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void ConfirmDialogDragged(MouseEvent mouseEvent) {
        DialogSceneDraggedMouse(mouseEvent);
    }

    public void ConfirmDialogPressed(MouseEvent mouseEvent) {
        DialogScenePressedMouse(mouseEvent);
    }
}

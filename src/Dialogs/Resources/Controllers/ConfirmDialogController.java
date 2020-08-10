package Dialogs.Resources.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

import static Dialogs.Resources.Controllers.ShowAllDialogs.*;

public class ConfirmDialogController implements Initializable {


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

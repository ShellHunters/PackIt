package Dialogs.Resources.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

;
import static Dialogs.Resources.Controllers.ShowAllDialogs.*;

public class DeleteDialogController implements Initializable {
    @FXML
    public Label DialogBody;

    @FXML
    public Label DialogHeader;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void DeleteDialogDelete(ActionEvent event) {
        ClickedButton.dialogsDELETEBUTTON.set(true);
        ExitDialog();
    }

    public void DeleteDialogCancel(ActionEvent event) {
        ClickedButton.dialogsCANCELBUTTON.set(true);
        ExitDialog();
    }

    public void DeleteDialogDragged(MouseEvent mouseEvent) {
        DialogSceneDraggedMouse(mouseEvent);
    }

    public void DeleteDialogPressed(MouseEvent mouseEvent) {
        DialogScenePressedMouse(mouseEvent);
    }

    public void settingHeaderAndBody(String Header , String Body){
        DialogBody.setText(Body);
        DialogHeader.setText(Header);
    }

}

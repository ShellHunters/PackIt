package Dialogs.Resources.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

;
import static Dialogs.Resources.Controllers.ShowAllDialogs.*;

public class DeleteDialogController implements Initializable {

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
}

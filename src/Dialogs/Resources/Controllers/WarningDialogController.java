package Dialogs.Resources.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

import static Dialogs.Resources.Controllers.ShowAllDialogs.*;

public class WarningDialogController implements Initializable {

 public void WarningDialogSave(ActionEvent event) {
  ClickedButton.dialogsSAVEBUTTON.set(true);
  ExitDialog();

 }

 public void WarningDialogExit(ActionEvent event) {
  ClickedButton.dialogsEXITBUTTON.set(true);
  ExitDialog();
 }

 public void WarningDialogCancel(ActionEvent event) {
  ClickedButton.dialogsCANCELBUTTON.set(true);
  ExitDialog();
 }


 @Override
 public void initialize(URL location, ResourceBundle resources) {

 }

 public void WarningDialogDragged(MouseEvent mouseEvent) {
DialogSceneDraggedMouse(mouseEvent);
 }

 public void WarningDialogPressed(MouseEvent mouseEvent) {
  DialogScenePressedMouse(mouseEvent);
 }
}



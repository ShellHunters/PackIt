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

public class WarningDialogController implements Initializable {
 @FXML
 public Label DialogBody;

 @FXML
 public Label DialogHeader;
 @FXML
 public JFXButton confirmDialogAddButton;


 public void WarningDialogSave(ActionEvent event) {

 }

 public void WarningDialogExit(ActionEvent event) {
  ClickedButton.dialogsEXITBUTTON.set(true);
  ExitDialog();
 }

 public void WarningDialogCancel(ActionEvent event) {
  ClickedButton.dialogsCANCELBUTTON.set(true);
  ExitDialog();
 }

 public void settingHeaderAndBody(String Header , String Body){
  DialogBody.setText(Body);
  DialogHeader.setText(Header);
 }

 @Override
 public void initialize(URL location, ResourceBundle resources) {

 }
 public void settingAddButtonText(String text){
  confirmDialogAddButton.setText(text);
 }

 public void WarningDialogDragged(MouseEvent mouseEvent) {
DialogSceneDraggedMouse(mouseEvent);
 }

 public void WarningDialogPressed(MouseEvent mouseEvent) {
  DialogScenePressedMouse(mouseEvent);
 }

 public void WarningDialogAdd (ActionEvent event) {
  ClickedButton.dialogsAddBUTTON.set(true);
  ExitDialog();

 }
}



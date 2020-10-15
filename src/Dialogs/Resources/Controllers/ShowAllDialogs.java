package Dialogs.Resources.Controllers;

import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.stage.Window;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;

public  class ShowAllDialogs  {
    public static Stage DialogStage;
    public static StackPane DialogParent = new StackPane();
    public static Scene DialogScene ;
    public static boolean DialogIsOpen;
    public static String Header="";
    public static  String Body="";
    public static String alertType;
    public static String threeButtonTheFirst="",threeButtonTheSecond="",threeButtonTheThird;

    public static SimpleBooleanProperty YESBUTTON = new SimpleBooleanProperty(false)  ;
    public static SimpleBooleanProperty NOBUTTON  = new SimpleBooleanProperty(false) ;

    public static SimpleBooleanProperty CANCELBUTTON = new SimpleBooleanProperty() ;
    public static SimpleBooleanProperty DELETEBUTTON = new SimpleBooleanProperty() ;

    public static SimpleBooleanProperty ADDBUTTON = new SimpleBooleanProperty()  ;
    public static SimpleBooleanProperty EXITBUTTON = new SimpleBooleanProperty()  ;
    public static SimpleBooleanProperty ONEBUTTON = new SimpleBooleanProperty()  ;
  public static  ConfirmDialogController confirmDialogController;
  public static DeleteDialogController deleteDialogController;
  public static WarningDialogController warningDialogController;
  public static DialogOneController dialogOneController;
  static   CursorPosition dragPosition = new CursorPosition();

    public static void DialogScaleTransition(Node TransitionNode , double FromX,double FromY , double ToX , double ToY , String Mode ){
    ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2),TransitionNode);
    scaleTransition.setFromX(FromX);
    scaleTransition.setFromY(FromY);
    scaleTransition.setToX(ToX);
    scaleTransition.setToY(ToY);
    scaleTransition.play();
    if (Mode.equals(TransitionMode.CLOSING)){
        scaleTransition.setOnFinished(e-> DialogStage.close());
    }
}

public static void initDialogWithShow(Window Owner , String AlertType) throws IOException {


    alertType=AlertType;
    FXMLLoader loader = new FXMLLoader();
        if (AlertType.equals(AlertTypeDialog.CONFIRMATION)) {
            loader.setLocation(ShowAllDialogs.class.getResource("/Dialogs/Resources/FxmlFiles/DialogConfirmation.fxml"));

            DialogParent = loader.load() ;
            confirmDialogController=    loader.getController();
            if (!Header.equals("")&&!Body.equals(""))

                confirmDialogController.settingHeaderAndBody(Header,Body);

        }
 if (AlertType.equals(AlertTypeDialog.WARNING)) {
     loader.setLocation(ShowAllDialogs.class.getResource("/Dialogs/Resources/FxmlFiles/DialogWarning.fxml"));
     DialogParent = loader.load();
     warningDialogController=    loader.getController();
     if (!Header.equals("")&&!Body.equals(""))
     warningDialogController.settingHeaderAndBody(Header,Body);
     if(!threeButtonTheFirst.equals(""))
         warningDialogController.settingAddButtonText(threeButtonTheFirst);
 }
    if (AlertType.equals(AlertTypeDialog.DELETE)) {
        loader.setLocation(ShowAllDialogs.class.getResource("/Dialogs/Resources/FxmlFiles/DialogDelete.fxml"));

        DialogParent = loader.load();
        deleteDialogController=    loader.getController();
        if (!Header.equals("")&&!Body.equals(""))
        deleteDialogController.settingHeaderAndBody(Header,Body);
    }
    if (AlertType.equals(AlertTypeDialog.ONEBUTTON)){
        loader.setLocation(ShowAllDialogs.class.getResource("/Dialogs/Resources/FxmlFiles/DialogOneButton.fxml"));

        DialogParent = loader.load();
        dialogOneController=    loader.getController();
        if (!Header.equals("")&&!Body.equals(""))

            dialogOneController.settingHeaderAndBody(Header,Body);
    }
ClickedButton.InitBoolean();
    DialogStage = new Stage();
    DialogStage.initStyle(StageStyle.TRANSPARENT);
    DialogStage.initModality(Modality.WINDOW_MODAL);
    DialogStage.initOwner(Owner);
    DialogScene = new Scene(DialogParent);
DialogScene.setFill(Color.TRANSPARENT);
       DialogStage.setScene(DialogScene);

    ShowAllDialogs.DialogScaleTransition(DialogParent,0,0,1,1 , TransitionMode.OPENING);
    ClickedButton.dialogsYESBUTTON.addListener((observable, oldValue, newValue) -> YESBUTTON.set(ClickedButton.dialogsYESBUTTON.get()));
    ClickedButton.dialogsNOBUTTON.addListener((observable, oldValue, newValue) -> NOBUTTON.set(ClickedButton.dialogsNOBUTTON.get()));
    ClickedButton.dialogsAddBUTTON.addListener((observable, oldValue, newValue) -> ADDBUTTON.set(ClickedButton.dialogsAddBUTTON.get()));
    ClickedButton.dialogsDELETEBUTTON.addListener((observable, oldValue, newValue) -> DELETEBUTTON.set(ClickedButton.dialogsDELETEBUTTON.get()));
    ClickedButton.dialogsCANCELBUTTON.addListener((observable, oldValue, newValue) -> CANCELBUTTON.set(ClickedButton.dialogsCANCELBUTTON.get()));
    ClickedButton.dialogsEXITBUTTON.addListener((observable, oldValue, newValue) -> EXITBUTTON.set(ClickedButton.dialogsEXITBUTTON.get()));
    ClickedButton.dialogsONEBUTTON.addListener((observable, oldValue, newValue) -> ONEBUTTON.set(ClickedButton.dialogsEXITBUTTON.get()));

    DialogStage.showAndWait();
    Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
    DialogStage.setX((primScreenBounds.getWidth() - DialogStage.getWidth()) / 2);
    DialogStage.setY((primScreenBounds.getHeight() - DialogStage.getHeight()) / 2);
}
public static void ExitDialog(){
    ShowAllDialogs.DialogScaleTransition(DialogParent,1,1,0,0 , TransitionMode.CLOSING);
    Header="";
    Body="";
;    }
   static public void DialogScenePressedMouse(MouseEvent event) {


            dragPosition.x = DialogStage.getX() - event.getScreenX();
            dragPosition.y = DialogStage.getY() - event.getScreenY();

    }
    static public void DialogSceneDraggedMouse(MouseEvent event) {


           DialogStage.setX(event.getScreenX() + dragPosition.x);
            DialogStage.setY(event.getScreenY() + dragPosition.y);

        }
    static public void InitContentDialog (String TheHeader , String TheBody){
        Header =TheHeader;
        Body = TheBody;
    }
    public static  void threeButtonSetText(String text){
threeButtonTheFirst=text;
}



    public static class AlertTypeDialog{
        public static final String CONFIRMATION="CONFIRMATION";
    public static final String WARNING="WARNING";
    public static final String DELETE="DELETE";
    public static final String ONEBUTTON="ONEBUTTON";
    public static final String TWOBUTTONS="ONEBUTTON";


}
public static class TransitionMode {
    public static final String OPENING="OPENING";
    public static final String CLOSING="CLOSING";
}

public static class ClickedButton{
        public static SimpleBooleanProperty dialogsYESBUTTON  = new SimpleBooleanProperty() ;
    public static SimpleBooleanProperty dialogsNOBUTTON  = new SimpleBooleanProperty() ;
    public static SimpleBooleanProperty dialogsCANCELBUTTON  = new SimpleBooleanProperty() ;
    public static SimpleBooleanProperty dialogsAddBUTTON = new SimpleBooleanProperty() ;
    public static SimpleBooleanProperty dialogsEXITBUTTON   = new SimpleBooleanProperty() ;
    public static SimpleBooleanProperty dialogsDELETEBUTTON   = new SimpleBooleanProperty() ;
    public static SimpleBooleanProperty dialogsONEBUTTON   = new SimpleBooleanProperty() ;

    static   public void InitBoolean(){
      dialogsYESBUTTON.setValue(false);
      dialogsNOBUTTON.setValue(false);
      dialogsAddBUTTON.setValue(false);
      dialogsCANCELBUTTON.setValue(false);
      dialogsEXITBUTTON.setValue(false);
        dialogsDELETEBUTTON.set(false);
      YESBUTTON.setValue(false);
      NOBUTTON.setValue(false);
      ADDBUTTON.setValue(false);
      CANCELBUTTON.setValue(false);
      DELETEBUTTON.set(false);
      EXITBUTTON.setValue(false);
        ONEBUTTON.set(false);

  }
}

    static class CursorPosition {double x,y;}


}


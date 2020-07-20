package Dialogs.Resources.Controllers;

import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;

public  class ShowAllDialogs {
    public static Stage DialogStage;
    public static StackPane DialogParent = new StackPane();
    public static Scene DialogScene ;
    public static boolean DialogIsOpen;
    public static String Header;
    public static  String Body;

    public static SimpleBooleanProperty YESBUTTON = new SimpleBooleanProperty(false)  ;
    public static SimpleBooleanProperty NOBUTTON  = new SimpleBooleanProperty(false) ;

    public static SimpleBooleanProperty CANCELBUTTON = new SimpleBooleanProperty() ;
    public static SimpleBooleanProperty DELETEBUTTON = new SimpleBooleanProperty() ;

    public static SimpleBooleanProperty SAVEBUTTON = new SimpleBooleanProperty()  ;
    public static SimpleBooleanProperty EXITBUTTON = new SimpleBooleanProperty()  ;
    public static SimpleBooleanProperty ONEBUTTON = new SimpleBooleanProperty()  ;

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
    Header  = "Something Went Wrong !";
    Body = "You Must Verify Your Information Or Another Thing";
    FXMLLoader loader = new FXMLLoader();
        if (AlertType.equals(AlertTypeDialog.CONFIRMATION))
        loader.setLocation(ShowAllDialogs.class.getResource("/Dialogs/Resources/FxmlFiles/DialogConfirmation.fxml"));
 if (AlertType.equals(AlertTypeDialog.WARNING))
    loader.setLocation(ShowAllDialogs.class.getResource("/Dialogs/Resources/FxmlFiles/DialogWarning.fxml"));
    if (AlertType.equals(AlertTypeDialog.DELETE))
        loader.setLocation(ShowAllDialogs.class.getResource("/Dialogs/Resources/FxmlFiles/DialogDelete.fxml"));
    if (AlertType.equals(AlertTypeDialog.ONEBUTTON))
        loader.setLocation(ShowAllDialogs.class.getResource("/Dialogs/Resources/FxmlFiles/DialogOneButton.fxml"));
ClickedButton.InitBoolean();
        DialogStage = new Stage();
        DialogParent = loader.load();
    DialogStage.initStyle(StageStyle.TRANSPARENT);
    DialogStage.initModality(Modality.WINDOW_MODAL);
    DialogStage.initOwner(Owner);
    DialogScene = new Scene(DialogParent);
DialogScene.setFill(Color.TRANSPARENT);
       DialogStage.setScene(DialogScene);
DialogStage.centerOnScreen();
    ShowAllDialogs.DialogScaleTransition(DialogParent,0,0,1,1 , TransitionMode.OPENING);
    ClickedButton.dialogsYESBUTTON.addListener((observable, oldValue, newValue) -> YESBUTTON.set(ClickedButton.dialogsYESBUTTON.get()));
    ClickedButton.dialogsNOBUTTON.addListener((observable, oldValue, newValue) -> NOBUTTON.set(ClickedButton.dialogsNOBUTTON.get()));
    ClickedButton.dialogsSAVEBUTTON.addListener((observable, oldValue, newValue) -> SAVEBUTTON.set(ClickedButton.dialogsSAVEBUTTON.get()));
    ClickedButton.dialogsDELETEBUTTON.addListener((observable, oldValue, newValue) -> DELETEBUTTON.set(ClickedButton.dialogsDELETEBUTTON.get()));
    ClickedButton.dialogsCANCELBUTTON.addListener((observable, oldValue, newValue) -> CANCELBUTTON.set(ClickedButton.dialogsCANCELBUTTON.get()));
    ClickedButton.dialogsEXITBUTTON.addListener((observable, oldValue, newValue) -> EXITBUTTON.set(ClickedButton.dialogsEXITBUTTON.get()));
    ClickedButton.dialogsONEBUTTON.addListener((observable, oldValue, newValue) -> ONEBUTTON.set(ClickedButton.dialogsEXITBUTTON.get()));

    DialogStage.showAndWait();
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
        TheHeader.replaceAll("\\s+","");
        TheBody.replaceAll("\\s+","");

        if (TheHeader.isEmpty()) {
            Header  = "Something Went Wrong !";
            System.out.println("hhhhhhhh true");
        }
        else
            Header = TheHeader ;
        if (TheBody.isEmpty()) {
            System.out.println("hhhhhhhh hhhhhhh true");

            Body = "You Must Verify Your Information Or Another Thing";
        }
        else
            Body=TheBody;
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
    public static SimpleBooleanProperty dialogsSAVEBUTTON   = new SimpleBooleanProperty() ;
    public static SimpleBooleanProperty dialogsEXITBUTTON   = new SimpleBooleanProperty() ;
    public static SimpleBooleanProperty dialogsDELETEBUTTON   = new SimpleBooleanProperty() ;
    public static SimpleBooleanProperty dialogsONEBUTTON   = new SimpleBooleanProperty() ;

    static   public void InitBoolean(){
      dialogsYESBUTTON.setValue(false);
      dialogsNOBUTTON.setValue(false);
      dialogsSAVEBUTTON.setValue(false);
      dialogsCANCELBUTTON.setValue(false);
      dialogsEXITBUTTON.setValue(false);
        dialogsDELETEBUTTON.set(false);
      YESBUTTON.setValue(false);
      NOBUTTON.setValue(false);
      SAVEBUTTON.setValue(false);
      CANCELBUTTON.setValue(false);
      DELETEBUTTON.set(false);
      EXITBUTTON.setValue(false);
        ONEBUTTON.set(false);

  }
}

    static class CursorPosition {double x,y;}
}


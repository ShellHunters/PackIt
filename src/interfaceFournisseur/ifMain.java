package interfaceFournisseur;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ifMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ifMain.fxml"));
        Scene interfaceClient = new Scene(root);
        interfaceClient.getStylesheets().add(getClass().getResource("ifMain.css").toString());
        interfaceClient.setFill(Color.TRANSPARENT);

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(interfaceClient);
        primaryStage.show();
        primaryStage.setResizable(false);
    }
}

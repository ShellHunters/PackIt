package interfaceMagazinier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class imMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("imMain.fxml"));
        Scene imMainScene = new Scene(root);
        imMainScene.getStylesheets().add(getClass().getResource("imMain.css").toString());
    }
}

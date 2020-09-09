package interfaceMagazinier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class imMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        imMainController.imMainLoader = new FXMLLoader(getClass().getResource("imMain.fxml"));
        Parent root = imMainController.imMainLoader.load();


        Scene imMainScene = new Scene(root);
        imMainScene.getStylesheets().add(getClass().getResource("imMain.css").toString());
        imMainScene.setFill(Color.TRANSPARENT);

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(imMainScene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }
}

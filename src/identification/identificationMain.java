package identification;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class identificationMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent loginRoot = FXMLLoader.load(getClass().getResource("loginMain.fxml"));

        Scene loginScene = new Scene(loginRoot);
        loginScene.getStylesheets().add(getClass().getResource("identificationMain.css").toString());
        loginScene.setFill(Color.TRANSPARENT);

        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setScene(loginScene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }
}

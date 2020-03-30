package interfaceClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class interfaceClientMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("clients.fxml"));
        Scene interfaceClientMainScene = new Scene(root);
        interfaceClientMainScene.getStylesheets().add(getClass().getResource("Client.css").toString());
        primaryStage.setTitle("CLIENTS");

        primaryStage.setScene(interfaceClientMainScene);
        primaryStage.show();
    }
}


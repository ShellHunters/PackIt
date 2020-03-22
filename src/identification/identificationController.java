package identification;

import Connection.ConnectionClass;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class identificationController implements Initializable {

    @FXML public JFXTextField emailField, nameField;
    @FXML public JFXPasswordField passwordField, passwordConfirmationField;

    @FXML public AnchorPane identificationContainer;

    Object[] registerErrors;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Errors setup for tests and displays
        registerErrors = new Object[]{
                new Label("Registeration not completed"),
                new Label("Your password confirmation is not correct"),
                new Label("Your email already exists")
        };
        for (int i = 0; i< registerErrors.length; i++){
            ((Label) registerErrors[i]).setStyle("-fx-text-fill: red;");
            ((Label) registerErrors[i]).setId("error");
        }
    }

    @FXML public void login(){
        //LA BENDAD HNA TEKTEB LOGIN TA3K
    }

    @FXML public void register() throws SQLException, IOException {
        VBox content =(VBox)identificationContainer.getChildren().get(2);

        if (!registrationCompleteCheck(content)) return;
        if (!passwordConfirmationCheck(content)) return;

        ConnectionClass connectionClass = new ConnectionClass();
        Connection connection = connectionClass.getConnection();
        Statement statement = connection.createStatement();

        if (!existinEmailCheck(content, statement, connection)) return;
        String sql = "INSERT INTO logins (email, nom, password) VALUES ('" + emailField.getText() + "', '" + nameField.getText() + "', '" + passwordField.getText() + "')";
        statement.executeUpdate(sql);
        connection.close();
        toLogin();
    }

    @FXML public void toRegister() throws IOException{
        AnchorPane register = FXMLLoader.load(getClass().getResource("registerMain.fxml"));
        identificationContainer.getChildren().setAll(register);
    }

    @FXML public void toLogin() throws IOException{
        AnchorPane login = FXMLLoader.load(getClass().getResource("loginMain.fxml"));
        identificationContainer.getChildren().setAll(login);
    }

    @FXML public void close(){
        System.exit(0);
    }

    //Errors check methods
    void deleteErrorMessage(VBox content){
        if(content.getChildren().get(0).getId() == "error") content.getChildren().remove(0);
    }

    boolean registrationCompleteCheck(VBox content){
        if (passwordField.getText().equals("") || passwordConfirmationField.getText().equals("") || emailField.getText().equals("") || nameField.getText().equals("")) {
            if (content.getChildren().get(0).getId().equals("error")) content.getChildren().remove(0);
            content.getChildren().add(0, (Label)registerErrors[0]);
            return false;
        }
        else {
            deleteErrorMessage(content);
            return true;
        }
    }

    boolean passwordConfirmationCheck(VBox content){
        if (!passwordField.getText().equals(passwordConfirmationField.getText())){
            if (!content.getChildren().get(0).getId().equals("error")) content.getChildren().add(0, (Label)registerErrors[1]);
            passwordField.setText("");
            passwordConfirmationField.setText("");
            return false;
        }
        else {
            deleteErrorMessage(content);
            return true;
        }
    }

    boolean existinEmailCheck(VBox content, Statement statement, Connection connection) throws SQLException {
        String query = "SELECT * FROM logins";
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()){
            String email = rs.getString("email");
            if (email.equals(emailField.getText())){
                if (content.getChildren().get(0).getId() != "error") content.getChildren().add(0, (Label)registerErrors[2]);
                connection.close();
                return false;
            }
        }
        deleteErrorMessage(content);
        return true;
    }
}

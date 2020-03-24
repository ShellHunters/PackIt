package identification;

import Connection.ConnectionClass;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class identificationController{

    @FXML public JFXTextField emailField, nameField;
    @FXML public JFXPasswordField passwordField, passwordConfirmationField;

    @FXML public AnchorPane identificationContainer;

    @FXML public Label status;

    @FXML public void login() throws SQLException {
        if (!loginCompleteCheck()) return;

        Connection connection = ConnectionClass.getConnection();
        String sql = "SELECT * FROM logins where email= ? and password= ?  ";//Query
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,emailField.getText());
        preparedStatement.setString(2,passwordField.getText());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            status.setText("Enter a correct Email/Password ");
            emailField.setText(""); passwordField.setText("");
        } else {
            status.setTextFill(Color.GREEN);
            status.setText("Login successful..Redirecting..." );
        }
    }

    @FXML public void register() throws SQLException, IOException {

        if ((!registrationCompleteCheck()))return;
        if (!passwordConfirmationCheck()) return ;


        Connection connection = ConnectionClass.getConnection();
        Statement statement = connection.createStatement();
        if (!validateEmail()) return;
        if (!existinEmailCheck(connection)) return;
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
    void deleteErrorMessage(){
        if(!status.getText().equals("")) status.setText("");
    }

    boolean loginCompleteCheck(){
        if (passwordField.getText().equals("") || emailField.getText().equals("")) {
            status.setText("Fill all the text fields");
            return false;
        }
        return true;
    }

    boolean registrationCompleteCheck(){
        if (passwordField.getText().equals("") || passwordConfirmationField.getText().equals("") || emailField.getText().equals("") || nameField.getText().equals("")) {
            status.setText("Registeration not completed");
            return false;
        }
        else {
            deleteErrorMessage();
            return true;
        }
    }

    boolean passwordConfirmationCheck(){
        if (!passwordField.getText().equals(passwordConfirmationField.getText())){
            if (status.getText().equals("")) status.setText("Your password confirmation is not correct");
            passwordField.setText("");
            passwordConfirmationField.setText("");
            return false;
        }
        else {
            deleteErrorMessage();
            return true;
        }
    }

    boolean existinEmailCheck(Connection connection) throws SQLException {
        String query = "SELECT * FROM logins where email= ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, emailField.getText());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            status.setText("Your email already exists");
            emailField.setText("");
            return false;
        }
        deleteErrorMessage();
        return true;
    }
    private boolean validateEmail () {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9-[-]]+([.][a-zA-Z]+)+") ;
        Matcher matcher = pattern.matcher(emailField.getText()) ;
        if (matcher.find() && matcher.group().equals(emailField.getText())) {
            return true ;
        }
        else
        {
            status.setText("Please Entre Valid Email ");

            return false ;
        }
    }
}
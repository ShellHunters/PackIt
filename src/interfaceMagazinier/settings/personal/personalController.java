package interfaceMagazinier.settings.personal;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Connection.ConnectionClass ;
public class personalController implements Initializable {
    public Label status;
    public Label status1;
    public JFXPasswordField passworrd;
    public JFXTextField newEmail;
    public JFXTextField email;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // here we get the email from the user class
         email.setText("a.bendada@esi-sba.dz");
        
    }

    public void deleteImage(ActionEvent event) {
    }

    public void uploadImage(ActionEvent event) {
    }

    public void saveEmail(ActionEvent event) throws SQLException {
        Connection connection = ConnectionClass.getConnection();
        Statement statement = connection.createStatement();
        if (email.getText().equals("")||newEmail.getText().equals("")||passworrd.getText().equals(""))
        {
            status1.setTextFill(Color.RED);
            status1.setText("You must fill all the text fields");
        }
        else if (email.getText().equals(newEmail.getText()))
        {
            status1.setTextFill(Color.RED);
            status1.setText("The new and old text are same");
            newEmail.setText("");
        }
        else if(!validateEmail()) return;
        else if(!existinEmailCheck(connection)) return;
        else if (!passworrd.getText().equals("moncif"))
        {
            status1.setTextFill(Color.RED);
            status1.setText("Wrong Password");
            passworrd.setText("");
        }
        else
            {
                // here we make the change in the user class and database
                status1.setTextFill(Color.GREEN);
                status1.setText("Your email changed succefully");
                email.setText(newEmail.getText());
                newEmail.setText("");
                passworrd.setText("");
            }

    }
    private boolean validateEmail () {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9-[-]]+([.][a-zA-Z]+)+") ;
        Matcher matcher = pattern.matcher(newEmail.getText());
        if (matcher.find() && matcher.group().equals(newEmail.getText())) {
            return true ;
        }
        else
        {
            status1.setTextFill(Color.RED);
            status1.setText("Entre a valid new email");
            newEmail.setText("");
            return false ;
        }
    }
    boolean existinEmailCheck(Connection connection) throws SQLException {
        String query = "SELECT * FROM logins where email= ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, newEmail.getText());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            status1.setTextFill(Color.RED);
            status1.setText("Your new email already exists");
            newEmail.setText("");
            return false;
        }

        return true;
    }
}

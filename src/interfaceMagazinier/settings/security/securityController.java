package interfaceMagazinier.settings.security;

import basicClasses.user;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

public class securityController implements Initializable {
    public JFXPasswordField password;
    public HBox phoneHbox;
    public JFXTextField phoneLabel;
    public JFXPasswordField newPassword;
    public JFXPasswordField confirmatiion;
    public HBox passworfHbox;
    public JFXPasswordField passwordPhoneLabel;
    public HBox hboxConfirmation;
    public JFXTextField confirmationPhone;
    public Button savePhone;
    public Button back;
    public Button send;
    public Label statusPhone1;
    public Label statusPhone2;
    public Label status;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            phoneLabel.setText(user.getPhoneNumber());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void savePhone() {
        if (confirmationPhone.getText().equals("")) {
            statusPhone2.setTextFill(Color.RED);
            statusPhone2.setText("You must fill the label");

        }
        // here we compare with the code
        else if (!confirmationPhone.getText().equals("Bc559Ae")) {
            statusPhone2.setTextFill(Color.RED);
            statusPhone2.setText("Wrong text confirmation");
            confirmationPhone.setText("");
        } else {
            // here we change the phone and saved it
            phoneLabel.setText(phoneLabel.getText());
            // Changing in the user class
            try {
                user.setPhoneNumber(phoneLabel.getText());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            back();
            confirmationPhone.setText("");
            statusPhone2.setText("");
            statusPhone1.setTextFill(Color.GREEN);
            statusPhone1.setText("Number phone changed succefully");
            passwordPhoneLabel.setText("");


        }
    }

    public void back() {
        statusPhone1.setText("");
        hboxConfirmation.setVisible(false);
        statusPhone2.setVisible(false);
        back.setVisible(false);
        savePhone.setVisible(false);
        // we back to the first scene
        passworfHbox.setVisible(true);
        phoneHbox.setVisible(true);
        statusPhone1.setVisible(true);
        send.setVisible(true);

    }

    public void send() throws IOException {
        if (passwordPhoneLabel.getText().equals("") || phoneLabel.getText().equals("")) {
            statusPhone1.setTextFill(Color.RED);
            statusPhone1.setText("You must fill all the label");
        } else if (!validPhoneNumber(phoneLabel.getText())) {
            statusPhone1.setTextFill(Color.RED);
            statusPhone1.setText("Please write a valid number");
            phoneLabel.setText("");
        }

        // here we compare with the password from user class
        else if (!passwordPhoneLabel.getText().equals(user.getPassword())) {
            statusPhone1.setTextFill(Color.RED);
            statusPhone1.setText("Wrong old password");
            passwordPhoneLabel.setText("");
        } else {
            statusPhone2.setText("");
            passworfHbox.setVisible(false);
            phoneHbox.setVisible(false);
            statusPhone1.setVisible(false);
            send.setVisible(false);

            hboxConfirmation.setVisible(true);
            statusPhone2.setVisible(true);
            back.setVisible(true);
            savePhone.setVisible(true);
            sendSms();

        }
    }
    

    public void savePassword() {
        if (password.getText().equals("") || newPassword.getText().equals("") || confirmatiion.getText().equals("")) {
            status.setTextFill(Color.RED);
            status.setText("You must fill all the label");
        } else if (!newPassword.getText().equals(confirmatiion.getText())) {
            status.setTextFill(Color.RED);
            status.setText("Your password confirmation is not correct");
            confirmatiion.setText("");
        }
        // here we compare with the password from user class
        else if (!password.getText().equals(user.getPassword())) {
            status.setTextFill(Color.RED);
            status.setText("Wrong old password");
            password.setText("");
        } else if (password.getText().equals(newPassword.getText())) {
            status.setTextFill(Color.RED);
            status.setText("The new and old password are same ");
            password.setText("");
            confirmatiion.setText("");
            newPassword.setText("");
        } else {
            //here we change the password froom database
            user.setPassword(newPassword.getText());
            status.setTextFill(Color.GREEN);
            status.setText("Password Changed succefully");
            confirmatiion.setText("");
            newPassword.setText("");
        }
    }

    private boolean validPhoneNumber(String number) {
        return number.charAt(0) == '0' && (number.charAt(1) == '7' || number.charAt(1) == '5' || number.charAt(1) == '6') && number.length() == 10 && number.matches("[0-9]+");

    }

    public void sendSms() throws IOException {
        // This URL is used for sending messages
        String myURI = "https://api.bulksms.com/v1/messages";

        // change these values to match your own account
        String myUsername = "moncifbendada";
        String myPassword = "vYcGfvmap3.H3ji";

        // the details of the message we want to send
        String myData = "{to: \"+213699033219\", body: \"Bienvenue dans le service mobile Packitin . Your code confirmation is 55684DD03\"}";

        // if your message does not contain unicode, the "encoding" is not required:
        // String myData = "{to: \"1111111\", body: \"Hello Mr. Smith!\"}";

        // build the request based on the supplied settings
        URL url = new URL(myURI);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setDoOutput(true);

        // supply the credentials
        String authStr = myUsername + ":" + myPassword;
        String authEncoded = Base64.getEncoder().encodeToString(authStr.getBytes());
        request.setRequestProperty("Authorization", "Basic " + authEncoded);

        // we want to use HTTP POST
        request.setRequestMethod("POST");
        request.setRequestProperty("Content-Type", "application/json");

        // write the data to the request
        OutputStreamWriter out = new OutputStreamWriter(request.getOutputStream());
        out.write(myData);
        out.close();

        // try ... catch to handle errors nicely
        try {
            // make the call to the API
            InputStream response = request.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(response));
            String replyText;
            while ((replyText = in.readLine()) != null) {
                System.out.println(replyText);
            }
            in.close();
        } catch (IOException ex) {
            System.out.println("An error occurred:" + ex.getMessage());
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getErrorStream()));
            // print the detail that comes with the error
            String replyText;
            while ((replyText = in.readLine()) != null) {
                System.out.println(replyText);
            }
            in.close();
        }
        request.disconnect();
    }

}

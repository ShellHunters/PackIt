package interfaceMagazinier.settings.security;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.net.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
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

        
    }

    public void savePhone() {
        if(confirmationPhone.getText().equals(""))
        {
            statusPhone2.setTextFill(Color.RED);
            statusPhone2.setText("You must fill the label");

        }
             // here we compare with the code
        else if(!confirmationPhone.getText().equals("Bc559Ae"))
        {
            statusPhone2.setTextFill(Color.RED);
            statusPhone2.setText("Wrong text confirmation");
            confirmationPhone.setText("");
        }
        else {
            // here we change the phone and saved it
            back() ;
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

    public void send() {
        if(passwordPhoneLabel.getText().equals("")||phoneLabel.getText().equals(""))
        {
            statusPhone1.setTextFill(Color.RED);
            statusPhone1.setText("You must fill all the label");
        }
        else if(!validPhoneNumber(phoneLabel.getText()))
        {
            statusPhone1.setTextFill(Color.RED);
            statusPhone1.setText("Please write a valid number");
            phoneLabel.setText("");
        }

        // here we compare with the password from user class
        else if(!passwordPhoneLabel.getText().equals("moncif"))
        {
            statusPhone1.setTextFill(Color.RED);
            statusPhone1.setText("Wrong old password");
            passwordPhoneLabel.setText("");
        }
        else
        {
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
        if(password.getText().equals("")||newPassword.getText().equals("")||confirmatiion.getText().equals(""))
          {
              status.setTextFill(Color.RED);
              status.setText("You must fill all the label");
          }
        else if (!newPassword.getText().equals(confirmatiion.getText()))
         {
            status.setTextFill(Color.RED);
            status.setText("Your password confirmation is not correct");
            confirmatiion.setText("");
         }
        // here we compare with the password from user class
        else if (!password.getText().equals("moncif"))
        {
            status.setTextFill(Color.RED);
            status.setText("Wrong old password");
            password.setText("");
        }
        else if(password.getText().equals(newPassword.getText()))
        {
            status.setTextFill(Color.RED);
            status.setText("The new and old password are same ");
            password.setText("");
            confirmatiion.setText("");
            newPassword.setText("");
        }
        else
            {
                //here we change the password froom database
            status.setTextFill(Color.GREEN);
            status.setText("Password Changed succefully");
            password.setText("");
            confirmatiion.setText("");
            newPassword.setText("");
            }
    }
    private boolean  validPhoneNumber(String number)
    {
        return number.charAt(0)=='0' && (number.charAt(1)=='7'||number.charAt(1)=='5'||number.charAt(1)=='6') && number.length()==10 && number.matches("[0-9]+");

    }

        public void sendSms() {
            try {
                // Construct data
                String apiKey = "apikey=" + "ibAeMMZN+fk-eCNwaTEziwhi4gxHTCFnkvJfgwK874";
                String message = "&message=" + "Your confirmation code is : bc59dd0303"  ;
                String sender = "&sender=" + "PackitIn";
                String numbers = "&numbers=" + phoneLabel.getText();

                // Send data
                HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
                String data = apiKey + numbers + message + sender;
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                conn.getOutputStream().write(data.getBytes("UTF-8"));
                final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                final StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = rd.readLine()) != null) {
                    stringBuffer.append(line);
                }
                rd.close();


            } catch (Exception e) {
                System.out.println("Error SMS "+e);


        }
    }
}

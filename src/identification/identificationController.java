package identification;


import Connector.ConnectionClass;
import basicClasses.user;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import interfaceClient.icMain;
import interfaceFournisseur.ifMain;
import interfaceMagazinier.imMain;
import interfaceMagazinier.settings.preference.preferencesController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class identificationController  implements Initializable {

    @FXML
    public JFXTextField emailField, nameField;
    public CheckBox saveMe;
    public Button view;
    public JFXTextField emailFieldMain;
    public JFXPasswordField passwordFieldMain;
    @FXML
    private JFXTextField shopNameField;
    @FXML
    public JFXPasswordField passwordField, passwordConfirmationField;
    @FXML
    public AnchorPane identificationContainer;
    @FXML
    public Label status;
    @FXML
    public JFXTextField textField;
    @FXML
    private ImageView visible;
    @FXML
    private ImageView invisible;
    @FXML
    public VBox loginContent;
    private static String emailFromLogin, passwordFromLogin;

    Preferences preferences ;

    public identificationController() throws IOException, ClassNotFoundException {
    }

    public static String getEmailFromLogin() {
        return emailFromLogin;
    }

    public static void setEmailFromLogin(String emailFromLogin) {
        identificationController.emailFromLogin = emailFromLogin;
    }

    public static String getPasswordFromLogin() {
        return passwordFromLogin;
    }

    public static void setPasswordFromLogin(String passwordFromLogin) {
        identificationController.passwordFromLogin = passwordFromLogin;
    }
   

    @FXML
    public void loginAsProvider() throws Exception {
        if (!loginProviderCompleteCheck()) return;
        if (!validateEmail(emailField.getText())) {emailField.setText("");return;}


        Connection connection = ConnectionClass.getConnection();
        String sql = "SELECT * FROM logins where shopName= ?   ";//Query
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, shopNameField.getText());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            status.setText("Enter a correct shop name ");
            shopNameField.setText("");
        } else {
            String Sql = "SELECT * FROM providers where email= ?   ";//Query
            PreparedStatement preparedStatement1 = connection.prepareStatement(Sql);
            preparedStatement1.setString(1, emailField.getText());
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            if (!resultSet1.next()) {
                status.setText("Please entre a valid provider email ");
                emailField.setText("");
            } else {
                status.setTextFill(Color.GREEN);
                status.setText("Login successful..Redirecting...");
            }

            //Linking between interface and login
            ((Stage) identificationContainer.getScene().getWindow()).close();
            Stage providerStage = new Stage();
            ifMain userInterface = new ifMain();
            //ADD CONDITION FOR EACH USER
            userInterface.start(providerStage);
        }

    }

    boolean loginProviderCompleteCheck() {
        if (emailField.getText().equals("") || shopNameField.getText().equals("")) {
            status.setText("Fill all the text fields");
            return false;
        }
        return true;
    }

    @FXML
    public void loginAsClient() throws Exception {
        if (!loginClientCompleteCheck()) return;
        Connection connection = ConnectionClass.getConnection();
        String sql = "SELECT * FROM logins where shopName= ?   ";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, shopNameField.getText());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            status.setText("Enter a correct shop name ");
            shopNameField.setText("");
        } else {
            status.setTextFill(Color.GREEN);
            status.setText("Login successful..Redirecting...");

            //Linking between interface and login
            ((Stage) identificationContainer.getScene().getWindow()).close();
            Stage userStage = new Stage();
            icMain userInterface = new icMain();
            //ADD CONDITION FOR EACH USER
            userInterface.start(userStage);
        }
    }

    boolean loginClientCompleteCheck() {
        if (shopNameField.getText().equals("")) {
            status.setText("Fill the text field");
            return false;
        }
        return true;
    }

    @FXML
    public void login() throws Exception {

        if (passwordFieldMain.getText().equals("") || emailFieldMain.getText().equals("")) {
            status.setText("Fill all the text fields");}

        Connection connection = ConnectionClass.getConnection();
        String sql = "SELECT * FROM logins where email= ? and password= ?  ";//Query
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, emailFieldMain.getText());
        preparedStatement.setString(2, passwordFieldMain.getText());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (!resultSet.next()) {
            status.setText("Enter a correct Email/Password ");
            emailFieldMain.setText("");
            passwordFieldMain.setText("");
        } else {
            user.setUserID(resultSet.getInt("id"));

            status.setTextFill(Color.GREEN);
            status.setText("Login successful..Redirecting...");
            //getting data to user class


            //Saving Data in a preferences

            if (saveMe.isSelected()) {
                 preferences.put("username",emailFieldMain.getText());
                 preferences.put("password",passwordFieldMain.getText());
                 preferences.putBoolean("rememberme", true);
            }
            else {
                preferences.clear();
            }
            emailFromLogin = emailFieldMain.getText();
            passwordFromLogin= passwordFieldMain.getText();

            //set preferences
            preferencesController.setDiscountAmount(resultSet.getInt("discountAmount"));
            preferencesController.setNumberOfSellsForDiscount(resultSet.getInt("numberOfSellsForDiscount"));
            loadProductTypePreferences();

            //Linking between interface and login
            ((Stage) identificationContainer.getScene().getWindow()).close();
            Stage userStage = new Stage();

            imMain userInterface = new imMain();
            //ADD CONDITION FOR EACH USER
            userInterface.start(userStage);


        }
    }

    private void loadProductTypePreferences() throws SQLException {
        preferencesController.productTypes = FXCollections.observableArrayList();
        Connection connection = ConnectionClass.getConnection();
        String query = "SELECT productType FROM productTypes WHERE userID=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, String.valueOf(user.getUserID()));
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()){
            preferencesController.productTypes.add(rs.getString("productType"));
        }
    }

    @FXML
    public void show() {
        if (!(passwordField.getText().equals(""))) {
            if (passwordField.isVisible()) {
                textField.setText(passwordField.getText());
                passwordField.setVisible(false);
                textField.setVisible(true);
                visible.setVisible(false);
                invisible.setVisible(true);
            } else {
                textField.setVisible(false);
                passwordField.setText(textField.getText());
                passwordField.setVisible(true);
                invisible.setVisible(false);
                visible.setVisible(true);

            }
        }
    }

    @FXML
    public void register() throws SQLException, IOException {

        if ((!registrationCompleteCheck())) return;
        if (!validateEmail(emailField.getText())) {emailField.setText("") ;return ;}
        if (!passwordConfirmationCheck()) return;

        Connection connection = ConnectionClass.getConnection();
        Statement statement = connection.createStatement();

        if (!existinEmailCheck(connection)) return;
        if (!existinShopCheck(connection)) return;
        FileInputStream fis = new FileInputStream("src/resource/pictures/Avatar.jpg");
        String query = "INSERT INTO logins(email, nom, password, shopname, image) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, emailField.getText());
        pstmt.setString(2, nameField.getText());
        pstmt.setString(3, passwordField.getText());
        pstmt.setString(4, shopNameField.getText());
        pstmt.setBinaryStream(5,fis);
        pstmt.execute();
        toLogin();
    }

    @FXML
    public void toRegister() throws IOException {
        AnchorPane register = FXMLLoader.load(getClass().getResource("registerMain.fxml"));
        identificationContainer.getChildren().setAll(register);
    }

    @FXML
    public void toLogin() throws IOException {
        AnchorPane login = FXMLLoader.load(getClass().getResource("loginMain.fxml"));
        identificationContainer.getChildren().setAll(login);
    }

    @FXML
    public void close() {
        ((Stage) identificationContainer.getScene().getWindow()).close();
    }

    //Errors check methods
    void deleteErrorMessage() {
        if (!status.getText().equals("")) status.setText("");
    }

    boolean loginCompleteCheck() {
        if (passwordField.getText().equals("") || emailField.getText().equals("")) {
            status.setText("Fill all the text fields");
            return false;
        }
        return true;
    }

    boolean registrationCompleteCheck() {
        if (passwordField.getText().equals("") || passwordConfirmationField.getText().equals("") || emailField.getText().equals("") || nameField.getText().equals("") || shopNameField.getText().equals("")) {
            status.setText("Registeration not completed");
            return false;
        } else {
            deleteErrorMessage();
            return true;
        }
    }

    boolean passwordConfirmationCheck() {
        if (!passwordField.getText().equals(passwordConfirmationField.getText())) {
            if (status.getText().equals("")) status.setText("Your password confirmation is not correct");
            passwordField.setText("");
            passwordConfirmationField.setText("");
            return false;
        } else {
            deleteErrorMessage();
            return true;
        }
    }

    boolean existinEmailCheck(Connection connection) throws SQLException {
        String query = "SELECT * FROM logins where email= ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, emailField.getText());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            status.setText("Your email already exists");
            emailField.setText("");
            return false;
        }
        deleteErrorMessage();
        return true;
    }

    private boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9-[-]]+([.][a-zA-Z]+)+");
        Matcher matcher = pattern.matcher(email);
        if (matcher.find() && matcher.group().equals(email)) {
            return true;
        } else {
            status.setText("Entre a valid email");
            return false;
        }
    }

    @FXML
    public void toLoginAsProvider() throws IOException {
        AnchorPane loginAsProvider = FXMLLoader.load(getClass().getResource("loginAsProvider.fxml"));
        identificationContainer.getChildren().setAll(loginAsProvider);
    }

    @FXML
    public void toLoginAsClient() throws IOException {
        AnchorPane loginAsClient = FXMLLoader.load(getClass().getResource("loginAsClient1.fxml"));
        identificationContainer.getChildren().setAll(loginAsClient);
    }

    @FXML
    public void forgotPassword() throws IOException {
        AnchorPane forgotPassword = FXMLLoader.load(getClass().getResource("forgotPassword1.fxml"));
        identificationContainer.getChildren().setAll(forgotPassword);
    }

    @FXML
    public void SentCodeToEmail() throws MessagingException, SQLException {
        //testing email validation
        Connection connection = ConnectionClass.getConnection();
        Statement statement = connection.createStatement();
        if (!(emailField.getText().equals(""))) {
            status.setText("Fill the email field please");
        }
        if (!(validateEmail(emailField.getText()))) {
            emailField.setText("");
            status.setText("Please entre a valid Email");
        } else if ((existinEmail(connection))) {
            status.setText("You are not registred");
        } else {
            //recovering password from database
            String code = null;
            String query = "SELECT password FROM logins where email= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, emailField.getText());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                code = resultSet.getString("password");
            }
            // creating connection with sever and host gmail services
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            String myMail = "packitin.contact@gmail.com";
            String password = "shellhunters";
            String reciever = emailField.getText().toString();


            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(myMail, password);
                }
            });
            Message message = prepareMessage(session, myMail, reciever, code);
            Transport.send(message);
            status.setTextFill(Color.GREEN);
            status.setText("Code sent succefully verify your email");
        }
    }

    private Message prepareMessage(Session session, String myMail, String reciever, String code) {
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(myMail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(reciever));
            message.setSubject("Recovering Password ");
            message.setText("Your Password is : " + code + "\n Packitin Team \n Cordially");
            return message;
        } catch (MessagingException e) {

            status.setText("Please repeat operation again");
            e.printStackTrace();
        }
        return null;
    }

    boolean existinEmail(Connection connection) throws SQLException {
        String query = "SELECT * FROM logins where email= ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, emailField.getText());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return false;
        }
        deleteErrorMessage();
        return true;
    }

    boolean existinShopCheck(Connection connection) throws SQLException {
        String query = "SELECT * FROM logins where shopName= ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, shopNameField.getText());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            status.setText("Your shop name already exists");
            shopNameField.setText("");
            return false;
        }
        deleteErrorMessage();
        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
        //show the login information if they are saved
        preferences = Preferences.userNodeForPackage(identificationController.class) ;
        if (preferences!= null)
            if((preferences.get("username", null) != null) && (preferences.get("password", null) != null))
            {
                saveMe.setSelected(preferences.getBoolean("rememberme", true));
                emailFieldMain.setText(preferences.get("username",null));
                passwordFieldMain.setText(preferences.get("password",null));
                view.setDisable(true);
            }}
        catch (Exception e ) {}

    }
}
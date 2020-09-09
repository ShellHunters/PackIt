package interfaceMagazinier.settings.personal;

import Connector.ConnectionClass;
import basicClasses.user;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import interfaceMagazinier.imMainController;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

public class personalController implements Initializable {
    public Label status;
    public Label status1;
    public JFXPasswordField passworrd;
    public JFXTextField newEmail;
    public JFXTextField email;
    public Rectangle profileRectangle;
    private FileInputStream fileInputStream ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        try {
            loadProfilePicture();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // here we get the email from the user class
        email.setText(user.getEmail());
    }

    public void loadProfilePicture() throws SQLException, IOException {
        Connection connection = ConnectionClass.getConnection();
        String sql = "SELECT image From logins where email= ? and shopName= ? " ;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,user.getEmail());
        preparedStatement.setString(2,user.getShopName());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            InputStream imageDataBase = resultSet.getBinaryStream("image");
            OutputStream imageFile = new FileOutputStream(new File("profile.jpg"));
            byte [] content = new byte[1024] ;
            int size=0;
            while ((size=imageDataBase.read(content)) != -1)
            {
                imageFile.write(content,0,size);

            }
            imageFile.close();
            imageDataBase.close();
            Image image = new Image("file:profile.jpg",150,150,true,true);
            profileRectangle.setFill(new ImagePattern(image));

        }
        else {
           System.out.println("failed");
        }

    }

    public void deleteImage(ActionEvent event) throws SQLException {



        /*
        Connection connection = ConnectionClass.getConnection();
        String sql = "Update logins set image=? where email=? " ;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setBlob(1,fileInputStream);
        preparedStatement.setString(2,email.getText());
        preparedStatement.executeUpdate();*/
    }

    public void uploadImage(ActionEvent event) throws SQLException, FileNotFoundException {

        Connection connection = ConnectionClass.getConnection();

        //open file chooser

        Node node = (Node) event.getSource();
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open profile image ");
        File file = chooser.showOpenDialog(node.getScene().getWindow()) ;
        fileInputStream = new FileInputStream(file);

        //chose image and changing image profile

        Image image = new Image(file.toURI().toString(),150,150,true,true);
        profileRectangle.setFill(new ImagePattern(image));
        //update image in database
        String sql = "Update logins set image=? where email=? " ;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setBlob(1,fileInputStream);
        preparedStatement.setString(2,email.getText());
        preparedStatement.executeUpdate();

        imMainController controller = imMainController.imMainLoader.getController();
        controller.imageCircle.setStroke(Color.TRANSPARENT);
        controller.imageCircle.setFill(new ImagePattern(image));

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
        else if (!passworrd.getText().equals(user.getPassword()))
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
                user.setEmail(email.getText());
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

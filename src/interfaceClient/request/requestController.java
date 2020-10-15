package interfaceClient.request;

import Connector.ConnectionClass;
import basicClasses.user;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class requestController implements Initializable {

    @FXML void close(ActionEvent event) {System.exit(0); }
    @FXML public JFXTextField description;
    @FXML public JFXButton submit;
    @FXML private JFXTextField productName;
    @FXML private JFXTextField productPrice;
    private Connection connect= ConnectionClass.getConnection();
    @FXML private StackPane frameStack;
    @FXML private Label noteLabel;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        submit.setOnAction(event -> {
            try {
                if (!(productName.getText().isEmpty())&&!(productPrice.getText().isEmpty())) {
                    PreparedStatement pr;
                    String SQL = "INSERT INTO suggestproducts VALUES (?,?,?,?)";
                    pr = connect.prepareStatement(SQL);
                    pr.setString(1, productName.getText());
                    if (Integer.valueOf(productPrice.getText())>=0)
                        pr.setInt(2, Integer.valueOf(productPrice.getText()));
                    else noteLabel.setText("Note:Set number in price");
                    pr.setString(3,description.getText());
                    pr.setInt(4,user.getUserID());
                    pr.executeUpdate();

                    pr=connect.prepareStatement("SELECT * FROM suggestproducts where userID=?");
                    pr.setInt(1,user.getUserID());
                    ResultSet resultSet=pr.executeQuery();
                    while(resultSet.next()){
                        if (resultSet.getString(1).equals(productName.getText())&&(resultSet.getInt(2)==Integer.valueOf(productPrice.getText()))){
                            JFXDialogLayout jfxDialogLayout=new JFXDialogLayout();
                            jfxDialogLayout.setBody(new Label("Product already exists ! thank you for suggest"));
                            JFXDialog dialog=new JFXDialog(frameStack,jfxDialogLayout, JFXDialog.DialogTransition.BOTTOM);
                            dialog.show();
                        }
                    }
                    resultSet=connect.createStatement().executeQuery("SELECT * FROM suggestproducts where userID='"+user.getUserID()+"'");
                    while(resultSet.next()){
                        if (resultSet.getString(4).equals(productName.getText())&&(resultSet.getInt(2)==Integer.valueOf(productPrice.getText()))){
                            JFXDialogLayout jfxDialogLayout=new JFXDialogLayout();
                            jfxDialogLayout.setBody(new Label("Product already exists ! thank you for suggest"));
                            JFXDialog dialog=new JFXDialog(frameStack,jfxDialogLayout, JFXDialog.DialogTransition.BOTTOM);
                            dialog.show();
                        }
                    }
                    productPrice.setText("");
                    productName.setText("");
                    description.setText(""); }

                else noteLabel.setText("Note: Fill all information please !");
            }catch (Exception e){
                productPrice.setText("");
                productName.setText("");
                description.setText("");
                System.out.println("SQL error: "+e);
            }
        });
    }
}
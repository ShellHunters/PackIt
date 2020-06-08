package interfaceMagazinier.settings.contact;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class contactController {


    public javafx.scene.control.Label email;
    public Label phone;
    public Polygon triangle;
    public Label notif;
    public Label notif1;
    public Polygon triangle1;

    @FXML
    private void hyperlinkInstagram(){
        try {
            Desktop.getDesktop().browse(new URI("https://www.instagram.com/moncif_bendada/").toURL().toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void hyperlinkLinkdin(){
        try {
            Desktop.getDesktop().browse(new URI("https://www.linkedin.com/in/moncif-bendada/").toURL().toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void hyperlinkFacebook(){
        try {
            Desktop.getDesktop().browse(new URI("https://www.facebook.com/rakikoove").toURL().toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    @FXML private void copyMail(){
        exitNotif1();
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(email.getText()), null);
        triangle.setVisible(true);
        notif.setVisible(true);
    }
    @FXML private void copyPhone(){

        exitNotif();
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(phone.getText()), null);
        triangle1.setVisible(true);
        notif1.setVisible(true);
        

    }

    public void exitNotif() {
        triangle.setVisible(false);
        notif.setVisible(false);
    }

    public void exitNotif1() {
        triangle1.setVisible(false);
        notif1.setVisible(false);
    }
}

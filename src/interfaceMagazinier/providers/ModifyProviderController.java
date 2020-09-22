package interfaceMagazinier.providers;

import Connector.ConnectionClass;
import Dialogs.Resources.Controllers.ShowAllDialogs;
import basicClasses.Provider;
import basicClasses.user;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static Dialogs.Resources.Controllers.ShowAllDialogs.*;

public class ModifyProviderController implements Initializable {
    @FXML
    public AnchorPane ModifyRoot;
    @FXML
    public JFXTextField ModifyEmailField;
    @FXML
    public JFXTextField ModifyFirstField;

    @FXML
    public JFXTextField ModifyLastField;

    @FXML
    public JFXTextField ModifyPhoneField;

    @FXML
    public JFXTextField ModifyAddrField;

    @FXML
    public JFXButton ConfirmModify;

    @FXML
    public JFXButton ExitModify;
    @FXML
    public Label ErrorMessage;
    @FXML
    public JFXButton DeleteProvider;

    @FXML
    public void ExitModify() throws IOException, SQLException {
        if (!hasAnythingChanged()) {
            initDialogWithShow(ModifyRoot.getScene().getWindow(), AlertTypeDialog.WARNING);
            if (SAVEBUTTON.get()) {
                if (FinalValidity(ModifyEmailField, ModifyPhoneField, ModifyFirstField, ModifyLastField, ModifyAddrField, imProviderController.provider.getEmail(), imProviderController.provider.getPhoneNumber())) {
                    SetFirstCharToUpper(ModifyFirstField);
                    SetFirstCharToUpper(ModifyLastField);
                    UpdateElements();
                    imProviderController.ModifyDialog.close();

                }
                ErrorMessage.setText(ErrorString(ModifyEmailField, ModifyPhoneField, ModifyFirstField, ModifyLastField, ModifyAddrField, imProviderController.provider.getEmail(), imProviderController.provider.getPhoneNumber()));
            }

            if (EXITBUTTON.get())
                imProviderController.ModifyDialog.close();

        } else
            imProviderController.ModifyDialog.close();
    }

    public void DeleteProvider() throws IOException, SQLException {
        initDialogWithShow(ModifyRoot.getScene().getWindow(), AlertTypeDialog.DELETE);
        if (DELETEBUTTON.get()) {
            DeleteProviders(imProviderController.provider.getId());
            imProviderController.ModifyDialog.close();
        }


    }

    @FXML
    public void ConfirmModify() throws IOException, SQLException {
        if (FinalValidity(ModifyEmailField, ModifyPhoneField, ModifyFirstField, ModifyLastField, ModifyAddrField, imProviderController.provider.getEmail(), imProviderController.provider.getPhoneNumber())) {
            ErrorMessage.setText("");
            initDialogWithShow(ModifyRoot.getScene().getWindow(), AlertTypeDialog.CONFIRMATION);
            if (YESBUTTON.get()) {
                SetFirstCharToUpper(ModifyFirstField);
                SetFirstCharToUpper(ModifyLastField);
                UpdateElements();
                imProviderController.ModifyDialog.close();
            }
        } else
            ErrorMessage.setText(ErrorString(ModifyEmailField, ModifyPhoneField, ModifyFirstField, ModifyLastField, ModifyAddrField, imProviderController.provider.getEmail(), imProviderController.provider.getPhoneNumber()));

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ModifyAddrField.setText(imProviderController.provider.getAddress());
        ModifyFirstField.setText(imProviderController.provider.getFirstName());
        ModifyLastField.setText(imProviderController.provider.getLastName());
        ModifyPhoneField.setText(imProviderController.provider.getPhoneNumber());
        ModifyEmailField.setText(imProviderController.provider.getEmail());
        ConfirmModify.setDisable(hasAnythingChanged());


        this.ModifyFirstField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                imProviderController.ModifyDialog.setOverlayClose(hasAnythingChanged());
                ConfirmModify.setDisable(hasAnythingChanged());
            }
        });
        this.ModifyLastField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                imProviderController.ModifyDialog.setOverlayClose(hasAnythingChanged());
                ConfirmModify.setDisable(hasAnythingChanged());
            }
        });
        this.ModifyEmailField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                imProviderController.ModifyDialog.setOverlayClose(hasAnythingChanged());
                ConfirmModify.setDisable(hasAnythingChanged());

            }
        });
        this.ModifyPhoneField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                imProviderController.ModifyDialog.setOverlayClose(hasAnythingChanged());
                ConfirmModify.setDisable(hasAnythingChanged());
            }
        });
        this.ModifyAddrField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                imProviderController.ModifyDialog.setOverlayClose(hasAnythingChanged());
                ConfirmModify.setDisable(hasAnythingChanged());

            }
        });
        InsertOnlyNumber(ModifyPhoneField);
        InsertOnlyCharacter(ModifyFirstField);
        InsertOnlyCharacter(ModifyLastField);

    }


    public boolean hasAnythingChanged() {
        boolean FirstChanged = this.ModifyFirstField.getText().equals(imProviderController.provider.getFirstName());
        boolean LastChanged = this.ModifyLastField.getText().equals(imProviderController.provider.getLastName());
        boolean EmailChanged = this.ModifyEmailField.getText().equals(imProviderController.provider.getEmail());
        boolean PhoneChanged = this.ModifyPhoneField.getText().equals(imProviderController.provider.getPhoneNumber());
        boolean AddrChanged = this.ModifyAddrField.getText().equals(imProviderController.provider.getAddress());
        return FirstChanged && LastChanged && EmailChanged && PhoneChanged && AddrChanged;
    }


    static boolean VerifyValidateEmail(JFXTextField textField) {
        return Pattern.matches("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9-[-]]+([.][a-zA-Z]+)+", textField.getText());

    }

    static boolean VerifyValidatePhone(JFXTextField textField) {

        return Pattern.matches("^[0][765][0-9]{8}", textField.getText());
    }

    public static String ErrorString(JFXTextField textField, JFXTextField textField2, JFXTextField textField3, JFXTextField textField4, JFXTextField textField5, String itsInfos, String itsInfo2) throws SQLException {

        if ((!VerifyValidateEmail(textField)) && (!VerifyValidatePhone(textField2)) && textField3.getText().isEmpty() && textField4.getText().isEmpty() && textField5.getText().isEmpty())
            return "Incomplete Information";
        else if (textField3.getText().isEmpty() || textField4.getText().isEmpty() || textField5.getText().isEmpty())
            return "Incomplete Information";
        else if ((!VerifyValidateEmail(textField)) && (!VerifyValidatePhone(textField2)))
            return "The Email & Phone Number Is Invalid";
        else if (!VerifyValidateEmail(textField))
            return "The Email Is Invalid";
        else if (!VerifyValidatePhone(textField2))
            return "The Phone Number  Is Invalid";
        else if (VerifyIfExist("Email", textField.getText(), itsInfos) && VerifyIfExist("PhoneNumber", textField2.getText(), itsInfo2))
            return "The Email & Phone Number Already Exist";
        else if (VerifyIfExist("Email", textField.getText(), itsInfos))
            return "The Email Already Exist";
        else if (VerifyIfExist("PhoneNumber", textField2.getText(), itsInfo2))
            return "The Phone Number Already Exist";
        else return "";

    }

    public static boolean FinalValidity(JFXTextField textField, JFXTextField textField2, JFXTextField textField3, JFXTextField textField4, JFXTextField textField5, String itsInfos, String itsInfos2) throws SQLException {

        return (VerifyValidateEmail(textField) && VerifyValidatePhone(textField2) && !textField3.getText().isEmpty() && !textField4.getText().isEmpty() && !textField5.getText().isEmpty() && !VerifyIfExist("Email", textField.getText(), itsInfos) && !VerifyIfExist("PhoneNumber", textField2.getText(), itsInfos2));
    }

    void UpdateElements() throws SQLException {
        String SqlQuarry = "UPDATE ProvidersInfo set FirstName=? , LastName=? ,  PhoneNumber=? , Email=? , Address=? WHERE id=? and userID=?";
        Connection connection = ConnectionClass.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SqlQuarry);
        preparedStatement.setString(1, ModifyFirstField.getText());
        preparedStatement.setString(2, ModifyLastField.getText());
        preparedStatement.setString(3, ModifyPhoneField.getText());
        preparedStatement.setString(4, ModifyEmailField.getText());
        preparedStatement.setString(5, ModifyAddrField.getText());
        preparedStatement.setInt(6, imProviderController.provider.getId());
        preparedStatement.setInt(7, user.getUserID());
        preparedStatement.executeUpdate();
        imProviderController.ProviderList.clear();
        imProviderController.InitTable();
    }

    public static void DeleteProviders(int Id) throws SQLException {
        String DeleteQuarry = "DELETE FROM ProvidersInfo WHERE id=? and userID=?";
        String CopyQuarry = "INSERT INTO DeletedProviders SELECT * FROM ProvidersInfo WHERE id=? and userID=?";
        Connection CopyConnection = ConnectionClass.getConnection();
        Connection DeleteConnection = ConnectionClass.getConnection();
        PreparedStatement CopyStatement = CopyConnection.prepareStatement(CopyQuarry);
        CopyStatement.setInt(1, Id);
        CopyStatement.setInt(2, user.getUserID());
        CopyStatement.execute();
        PreparedStatement DeleteStatement = DeleteConnection.prepareStatement(DeleteQuarry);
        DeleteStatement.setInt(1, Id);
        DeleteStatement.setInt(2,user.getUserID());
        DeleteStatement.executeUpdate();

        imProviderController.ProviderList.clear();
        imProviderController.InitTable();
    }

    static boolean VerifyIfExist(String Column, String Content, String ItsInformation) {
        Provider provider;
        Iterator<Provider> providerIterator = imProviderController.ProviderList.iterator();

        if (Column.equals("Email")) {
            boolean bool = false;
            while (providerIterator.hasNext()) {
                provider = providerIterator.next();
                if (provider.getEmail().equals(Content)) {
                    bool = true;
                    break;
                }
            }
            return bool && !ItsInformation.equals(Content);

        } else if (Column.equals("PhoneNumber")) {
            boolean bool = false;
            while (providerIterator.hasNext()) {
                provider = providerIterator.next();
                if (provider.getPhoneNumber().equals(Content)) {
                    bool = true;
                    break;
                }
            }
            return bool && !ItsInformation.equals(Content);
        } else return false;
    }

    public static void InsertOnlyNumber(JFXTextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*"))
                    textField.setText(newValue.replaceAll("[^\\d]", ""));


            }
        });

    }

    public static void InsertOnlyCharacter(JFXTextField textField) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\sa-zA-Z*"))
                    textField.setText(newValue.replaceAll("[^\\sa-zA-Z]", ""));


            }
        });


    }

    public static void SetFirstCharToUpper(JFXTextField textField) {
        String output = textField.getText().substring(0, 1).toUpperCase() + textField.getText().substring(1);
        textField.setText(output);
    }
}
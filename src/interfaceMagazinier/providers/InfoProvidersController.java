package interfaceMagazinier.providers;

import Connector.ConnectionClass;
import Dialogs.Resources.Controllers.ShowAllDialogs;


import basicClasses.user;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static interfaceMagazinier.providers.imProviderController.infoProviderDialog;

public class InfoProvidersController implements Initializable {

    @FXML
    public AnchorPane InfoRoot;
    @FXML
    public JFXTextField EmailField;

    @FXML
    public JFXTextField FirstField;

    @FXML
    public JFXTextField LastField;

    @FXML
    public JFXTextField PhoneField;
    @FXML
    public Label ErrorMessage;
    @FXML
    public JFXTextField AddrField;
    @FXML
    public Button ExitTop;
    @FXML
    public JFXButton ExitInfoProvider;
    @FXML
    public JFXButton AddProvider;

    @FXML
    public void AddProvider(ActionEvent event) throws SQLException, IOException {
        if (ModifyProviderController.FinalValidity(EmailField, PhoneField, FirstField, LastField, AddrField, "", "")) {
            ErrorMessage.setText("");
            ShowAllDialogs.initDialogWithShow(InfoRoot.getScene().getWindow(), ShowAllDialogs.AlertTypeDialog.CONFIRMATION);
            if (ShowAllDialogs.YESBUTTON.get()) {
                ModifyProviderController.SetFirstCharToUpper(FirstField);
                ModifyProviderController.SetFirstCharToUpper(LastField);
                AddNewProvider();
            }
        } else
            ErrorMessage.setText(ModifyProviderController.ErrorString(EmailField, PhoneField, FirstField, LastField, AddrField, "", ""));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AddProvider.setDisable(VerifyIfEmpty());
        FirstField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                infoProviderDialog.setOverlayClose(VerifyIfEmpty());
                AddProvider.setDisable(VerifyIfEmpty());
            }
        });
        this.LastField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                infoProviderDialog.setOverlayClose(VerifyIfEmpty());
                AddProvider.setDisable(VerifyIfEmpty());
            }
        });
        this.EmailField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                infoProviderDialog.setOverlayClose(VerifyIfEmpty());
                AddProvider.setDisable(VerifyIfEmpty());

            }
        });
        this.PhoneField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                infoProviderDialog.setOverlayClose(VerifyIfEmpty());
                AddProvider.setDisable(VerifyIfEmpty());
            }
        });
        this.AddrField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                infoProviderDialog.setOverlayClose(VerifyIfEmpty());
                AddProvider.setDisable(VerifyIfEmpty());

            }
        });
        ModifyProviderController.InsertOnlyNumber(PhoneField);
        ModifyProviderController.InsertOnlyCharacter(FirstField);
        ModifyProviderController.InsertOnlyCharacter(LastField);
    }


    @FXML
    void ExitProviderInfo(ActionEvent event) throws IOException, SQLException {
        if (!VerifyIfEmpty()) {
            ShowAllDialogs.initDialogWithShow(InfoRoot.getScene().getWindow(), ShowAllDialogs.AlertTypeDialog.WARNING);
            if (ShowAllDialogs.ADDBUTTON.get()) {
                if (ModifyProviderController.FinalValidity(EmailField, PhoneField, FirstField, LastField, AddrField, "", "")) {
                    ErrorMessage.setText("");
                    ModifyProviderController.SetFirstCharToUpper(FirstField);
                    ModifyProviderController.SetFirstCharToUpper(LastField);
                    AddNewProvider();
                } else
                    ErrorMessage.setText(ModifyProviderController.ErrorString(EmailField, PhoneField, FirstField, LastField, AddrField, "", ""));

            }
            if (ShowAllDialogs.EXITBUTTON.get())
                imProviderController.infoProviderDialog.close();

        } else
            imProviderController.infoProviderDialog.close();
    }


    public boolean VerifyIfEmpty() {
        return FirstField.getText().isEmpty() && LastField.getText().isEmpty() && EmailField.getText().isEmpty() && PhoneField.getText().isEmpty() && AddrField.getText().isEmpty();
    }

    void AddNewProvider() {
        try {
            Connection conn = ConnectionClass.getConnection();
            String sql2 = "INSERT INTO ProvidersInfo (FirstName, LastName,PhoneNumber,Email,Address,userID) VALUES (?,?,?,?,?,?);";

            PreparedStatement preparedStatement = conn.prepareStatement(sql2);
            preparedStatement.setString(1, FirstField.getText());
            preparedStatement.setString(2, LastField.getText());

            preparedStatement.setString(3, PhoneField.getText());

            preparedStatement.setString(4, EmailField.getText());

            preparedStatement.setString(5, AddrField.getText());
            preparedStatement.setInt(6, user.getUserID());

            preparedStatement.executeUpdate();
            imProviderController.InitTable();

        } catch (Exception e) {
            e.printStackTrace();
        }
        FirstField.setText("");
        LastField.setText("");
        EmailField.setText("");
        PhoneField.setText("");
        AddrField.setText("");

    }


}


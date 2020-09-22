package interfaceMagazinier.providers;

import Dialogs.Resources.Controllers.ShowAllDialogs;
import basicClasses.Provider;
import basicClasses.user;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

public class imProviderController implements Initializable {


    @FXML
    public JFXTextField SearchBox;
    @FXML
    public TableView<Provider> AllProvidersTable;

    @FXML
    public TableColumn<Provider, Boolean> SelectedItem;

    @FXML
    public TableColumn<Provider, String> FirstCol;

    @FXML
    public TableColumn<Provider, String> LastCol;
    @FXML
    public TableColumn<Provider, String> AddrCol;
    @FXML
    public TableColumn<Provider, Boolean> ModifyCol;
    @FXML
    public TableColumn<Provider, String> PhoneCol;
    @FXML
    public TableColumn<Provider, String> EmailCol;
    @FXML
    public TableColumn<Provider, Float> TotalCol;

    @FXML
    private JFXButton DeleteSelectedButton;
    @FXML
    private JFXButton SendEmailButton;

    @FXML
    private JFXButton AddProvidersButton;

    @FXML
    public StackPane ProvidersRootStackPane;
    @FXML
    private HBox SearchBoxContainer;
    @FXML
    private AnchorPane ProvidersRoot;
    @FXML
    public ContextMenu ContextList;
    public static JFXDialog ModifyDialog;
    @FXML
    private MenuItem DeleteSelectedContext;
    public AnchorPane ModifyContainer;
    public static StackPane ProvidersRootContainerTemp = new StackPane();
    public static StackPane test = new StackPane();
    public static Provider provider = null;
    public static ObservableList<Provider> ProviderList = FXCollections.observableArrayList();
    public static ArrayList<Provider> SelectedProvider = new ArrayList<Provider>();
    public static ArrayList<Provider> SelectedProvidersListForEmail = new ArrayList<Provider>();
    public static ArrayList<Provider> SelectedProvidersForTheListOfEmail = new ArrayList<Provider>();
    public static ArrayList<Provider> SelectedProvidersLoadEmail = new ArrayList<Provider>();

    public static Stage AddProviderStage;
    public static SimpleBooleanProperty ForDisableButtons = new SimpleBooleanProperty(true);


    void LoadModifyProviderScene() throws IOException {
        ModifyContainer = FXMLLoader.load(getClass().getResource("ModifyProviders.fxml"));
        ModifyDialog = new JFXDialog(ProvidersRootStackPane, ModifyContainer, JFXDialog.DialogTransition.BOTTOM);

        ModifyDialog.show();
    }

    public void AddProviders(ActionEvent event) throws IOException {

        if (SendEmailMessageController.ForDisableModifyButton.get()) {
            for (Provider provider : SelectedProvidersForTheListOfEmail) {
                if (!SendEmailMessageController.ProvidersList.contains(provider))
                    SendEmailMessageController.ProvidersList.add(provider);
            }
            for (Provider provider : SelectedProvidersListForEmail) {
                if (!SendEmailMessageController.ProvidersList.contains(provider))
                    SendEmailMessageController.ProvidersList.add(provider);
            }
            ProviderList.removeAll(SelectedProvidersForTheListOfEmail);
            ProviderList.removeAll(SelectedProvidersListForEmail);

        } else {
            AnchorPane root = FXMLLoader.load(getClass().getResource("InfoProviders.fxml"));
            Scene scene = new Scene(root);
            AddProviderStage = new Stage();
            AddProviderStage.initStyle(StageStyle.TRANSPARENT);

            AddProviderStage.setScene(scene);
            AddProviderStage.show();
        }

    }


    public void ModifyContextList(ActionEvent event) throws IOException {
        LoadModifyProviderScene();
    }

    public void DeleteContextList(ActionEvent event) throws SQLException, IOException {
        ShowAllDialogs.initDialogWithShow(ProvidersRootStackPane.getScene().getWindow(), ShowAllDialogs.AlertTypeDialog.DELETE);
        if (ShowAllDialogs.DELETEBUTTON.get()) {
            Provider provider = new Provider();

            provider = AllProvidersTable.getItems().get(AllProvidersTable.getSelectionModel().getSelectedIndex());
            ModifyProviderController.DeleteProviders(provider.getId());
        }
    }

    public static Provider SetInfo(int id, String FirstName, String LastName, String PhoneNumber, String Email, String Address, float TotalFigure) {
        Provider ProviderForList = new Provider();
        ProviderForList.setId(id);
        ProviderForList.setFirstName(FirstName);
        ProviderForList.setLastName(LastName);
        ProviderForList.setPhoneNumber(PhoneNumber);
        ProviderForList.setEmail(Email);
        ProviderForList.setAddress(Address);
        ProviderForList.setTotalFigure(TotalFigure);
        return ProviderForList;

    }

    public void DeleteSelected(ActionEvent event) throws SQLException, IOException {
        ShowAllDialogs.initDialogWithShow(ProvidersRootStackPane.getScene().getWindow(), ShowAllDialogs.AlertTypeDialog.DELETE);
        Iterator<Provider> iterator = SelectedProvider.iterator();
        if (ShowAllDialogs.DELETEBUTTON.get()) {
            while (iterator.hasNext()) {
                Provider pro = iterator.next();
                System.out.println(" " + pro.getId());
                ModifyProviderController.DeleteProviders(pro.getId());
                iterator.remove();
            }

        }
    }

    public void SendEmail(ActionEvent actionEvent) throws IOException {


        //  ProviderList.removeAll(SelectedProvider);
        //   SendEmailMessageController.TempProvidersList.addAll(ProviderList);
        // SelectedProvider.clear();
        SendEmailController.IfTabPaneIsOpen = true;
        AnchorPane root = FXMLLoader.load(getClass().getResource("SendEmail.fxml"));
        ProvidersRootContainerTemp.getChildren().setAll(root);


    }

    public abstract class JFXCheckboxCell<T> extends TableCell<T, Boolean> {
        protected JFXCheckBox isChecked = new JFXCheckBox();

        public JFXCheckboxCell() {
            HBox hbox = new HBox();
            hbox.getChildren().addAll(this.isChecked);
            hbox.setAlignment(Pos.CENTER);
            hbox.setSpacing(5.0);

            this.initCheckBoxes();
        }

        private void initCheckBoxes() {
            this.isChecked.setOnAction(event -> {
                boolean newValue = this.isChecked.isSelected();
                T row = (T) this.getTableView().getItems().get(this.getIndex());
                this.onBeforeUpdateRow(row, newValue);
                this.onUpdateRow(row, newValue);
                (new Thread(() -> {
                    this.onUpdateEntity(row);
                })).start();
            });

        }

        @Override
        public void updateItem(Boolean item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || empty) {
                setGraphic(null);
                setText("");
            } else {
                this.isChecked.setSelected(this.getItem());
                this.setGraphic(this.isChecked.getParent());
                this.setText("");
            }
        }

        public abstract void onUpdateRow(T row, Boolean newValue);

        public abstract void onUpdateEntity(T entity);

        public void onBeforeUpdateRow(T row, Boolean newValue) {

        }
    }


    public class CustomButtonCell<T, S> extends TableCell<T, S> {
        private Button ModifyButton = new Button("Modify");


        @Override
        protected void updateItem(S item, boolean empty) {

            super.updateItem(item, empty);
            if (empty || item == null) {
                this.setText("");
                this.setGraphic(null);
            } else {
                //    if( !SendEmailMessageController.ForDisableModifyButton.get())
                ModifyButton.setId("EditButton");
                ModifyButton.setOnAction(event -> {

                    provider = (Provider) this.getTableView().getItems().get(getIndex());
                    // if( SendEmailMessageController.ForDisableModifyButton.get()){
                    //   SendEmailMessageController.ProvidersList.add(provider);
                    //   ProviderList.remove(provider);

                    // else {

                    try {
                        LoadModifyProviderScene();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                });
                //   if (SendEmailController.IfTabPaneIsOpen)
                //     ModifyButton.setText("Add Provider");
                this.setGraphic(ModifyButton);
            }
        }


    }

    public static void InitTable() throws SQLException {
        //     SelectedProvidersForTheListOfEmail.clear();
        SelectedProvidersListForEmail.clear();
        ProviderList.clear();
        if (SendEmailController.IfTabPaneIsOpen) {
            ProviderList.addAll(SendEmailMessageController.TempProvidersList);
        } else {
            String sql = "SELECT  * from ProvidersInfo where userID="+user.getUserID();
            Connection connection = Connector.ConnectionClass.getConnection();
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()) {

                ProviderList.add(SetInfo(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getFloat(7))
                );
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (SendEmailController.IfTabPaneIsOpen) {
            SendEmailButton.setVisible(false);
            DeleteSelectedButton.setVisible(false);


        }
        ProvidersRootContainerTemp = ProvidersRootStackPane;
   //     if (!SendEmailController.IfTabPaneIsOpen)
     //       SendEmailMessageController.TableProviderContainer = TableRoot;
        DeleteSelectedContext.setDisable(true);
        DeleteSelectedButton.setDisable(true);
        ForDisableButtons.set(false);
    /*
        ForDisableButtons.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                SendEmailButton.setDisable(ForDisableButtons.get());

            }
        });

     */
        ShowAllDialogs.DialogParent.getStyleClass().setAll("DialogsRoot");
        ModifyCol.setCellValueFactory(call -> new SimpleBooleanProperty(true).asObject());
        ModifyCol.setCellFactory(call -> {
            return new CustomButtonCell<>();
        });

        SelectedItem.setCellValueFactory(call -> new SimpleBooleanProperty(false).asObject());
        SelectedItem.setCellFactory(call -> {
            return new JFXCheckboxCell() {
                @Override
                public void onUpdateRow(Object row, Boolean newValue) {

                    if (newValue) {

                        provider = (Provider) this.getTableView().getItems().get(getIndex());
                        //     if (!SelectedProvider.contains(provider))
                        //       if( SendEmailMessageController.ForDisableModifyButton.get())
                        //        SelectedProvidersForTheListOfEmail.add(provider);
                        // else{
                        SelectedProvider.add(provider);
                        System.out.println("Teeessst");
                    }
                    //   SelectedProvidersLoadEmail.add(provider);
                    else {

                        provider = (Provider) this.getTableView().getItems().get(getIndex());
                        //     if( SendEmailMessageController.ForDisableModifyButton.get())
                        //      SelectedProvidersForTheListOfEmail.remove(provider);

                        //    else{
                        SelectedProvider.remove(provider);
                        System.out.println("Teeessst");
                    }
                    //  SelectedProvidersLoadEmail.remove(provider);

                    //   if( !SendEmailMessageController.ForDisableModifyButton.get()) {
                    DeleteSelectedButton.setDisable(SelectedProvider.isEmpty());
                    DeleteSelectedContext.setDisable(SelectedProvider.isEmpty());


                }

                @Override
                public void onUpdateEntity(Object entity) {

                }
            };

        });
        FirstCol.setCellValueFactory(new PropertyValueFactory<Provider, String>("FirstName"));
        LastCol.setCellValueFactory(new PropertyValueFactory<Provider, String>("LastName"));
        AddrCol.setCellValueFactory(new PropertyValueFactory<Provider, String>("Address"));
        PhoneCol.setCellValueFactory(new PropertyValueFactory<Provider, String>("PhoneNumber"));
        EmailCol.setCellValueFactory(new PropertyValueFactory<Provider, String>("Email"));
        TotalCol.setCellValueFactory(new PropertyValueFactory<Provider, Float>("TotalFigure"));
        try {
            InitTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        FilteredList<Provider> Results = new FilteredList<>(ProviderList, b -> true);
        SearchBox.textProperty().addListener(((observable, oldValue, newValue) -> {
            Results.setPredicate(provider -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                String lowerCaseFilter = newValue.toLowerCase();
                if (provider.getFirstName().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (provider.getLastName().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (String.valueOf(provider.getTotalFigure()).toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (provider.getAddress().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (provider.getEmail().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (provider.getPhoneNumber().toLowerCase().contains(lowerCaseFilter))
                    return true;


                else
                    return false;
            });
        }));

        SortedList<Provider> SortedResult = new SortedList<>(Results);
        SortedResult.comparatorProperty().bind(AllProvidersTable.comparatorProperty());
        AllProvidersTable.setItems(SortedResult);
        /*
        SendEmailMessageController.ForDisableModifyButton.addListener((observable, oldValue, newValue) -> {
            DeleteSelectedButton.setDisable(newValue);
            SendEmailButton.setDisable(newValue);
            AddProvidersButton.setText("Add Providers To List");
            if (newValue) {
                AllProvidersTable.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
            }
        });

         */
    }
}

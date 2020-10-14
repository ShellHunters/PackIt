package interfaceMagazinier.providers;

import Connector.ConnectionClass;
import Dialogs.Resources.Controllers.ShowAllDialogs;
import basicClasses.*;
import com.jfoenix.controls.*;
import javafx.beans.InvalidationListener;
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
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import sun.invoke.empty.Empty;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;



public class SendEmailMessageController implements Initializable {

    @FXML
    private StackPane SendEmailMessageRoot;
    /*
        @FXML

        public TableView<Provider> EmailProvidersTable;


        @FXML
        public TableColumn<Provider, Boolean> SelectedItem;


        @FXML
        public TableColumn<Provider, String> FirstCol;

        @FXML
        public TableColumn<Provider, String> LastCol;
        @FXML
        public TableColumn<Provider, Boolean> DeleteCol;


        @FXML
        public TableColumn<Provider, String> PhoneCol;
        @FXML
        public TableColumn<Provider, String> EmailCol;


     */
    @FXML
    private JFXComboBox<Provider> ProviderCombobox;


    @FXML
    private TableView<product> ProductTable;

    @FXML
    private TableColumn<product, Boolean> SelectedProductCol;
    @FXML
    public TableColumn<product, Integer> BarcodeCol;

    @FXML
    private TableColumn<product, String> ProductNameCol;

    @FXML
    private TableColumn<product, Integer> QuantityCol;
    @FXML
    private TableColumn<product, Integer> OriginalStockCol;
    @FXML
    private TableColumn<product, Integer> NeededQuantityCol;

    @FXML
    private TableColumn<product, Boolean> DeleteProductCol;
    @FXML
    private TableColumn<product, Boolean> ModifyProductCol;

    @FXML
    private JFXTextField SearchForProducts;

    @FXML
    private JFXButton DeleteSelectedProductsButton;
    @FXML
    private MenuItem DeleteContextProductButton;


    @FXML
    private MenuItem DeleteSelectedProductContext;
    @FXML
    private ContextMenu ContextList;

    @FXML
    private MenuItem ModifyContextButton;

    @FXML
    private MenuItem DeleteContextButton;

    @FXML
    private MenuItem DeleteSelectedContext;
    @FXML
    private JFXButton ModifySelectedProductsButton;
    @FXML
    private JFXTextField SearchForProviders;
    @FXML
    private MenuItem ModifyContextProductButton;

    @FXML
    private MenuItem ModifySelectedProductContext;
    public static StackPane TableProviderContainer = new StackPane();
    @FXML
    private JFXButton DeleteSelectedProvidersButton;

    @FXML
    private JFXTextField SubjectMessageTextField;
    @FXML
    private TextArea ProductArea;

    @FXML
    private TextArea MessageArea;



    @FXML
    private JFXButton SendEmailMessageButton;
    public static Provider providers;
    public static product infoProducts;
    public ArrayList<Provider> DeletedProvidersList = new ArrayList<Provider>();
    public static ArrayList<product> SelectedProductList = new ArrayList<product>();
    public static ArrayList<product> TotalProducts = new ArrayList<product>();

    public static ObservableList<Provider> ProvidersList = FXCollections.observableArrayList();
    public static ObservableList<product> ProductList = FXCollections.observableArrayList();
    FilteredList<product> ProductsResults = new FilteredList<>(ProductList, b -> true);
    SortedList<product> SortedProductsResult = new SortedList<>(ProductsResults);

    public static ArrayList<Provider> TempProvidersList = new ArrayList<Provider>();
    public static boolean IfEmailMessageIsOpen, IfStageIsLoaded, IfNeededProductBoxIsChecked, IfSendEmailMessageIsLoaded, IfModifyIsClicled;
    public static SimpleBooleanProperty ForDisableModifyButton = new SimpleBooleanProperty();
    public static SimpleBooleanProperty AddProviderButtonClicked = new SimpleBooleanProperty();
    public static SimpleBooleanProperty AddProductButtonClicked = new SimpleBooleanProperty();
    public static SimpleBooleanProperty IfMultipleProductSelect = new SimpleBooleanProperty();
    public static String msgRecipients;
    public static Provider tempoProvider;
public static JFXDialog settingInfoDialog;
public static SimpleBooleanProperty forDisablingTextArea = new SimpleBooleanProperty();
    public static String Quantity, msgSubject, msgContent;
public static JFXDialog sendProductNotInListDialog;
public static Integer idOfTheProvider;
//




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

    /*
        public class CustomButtonCell<T, S> extends TableCell<T, S> {
            private JFXButton DeleteButton = new JFXButton("Delete");


            @Override
            protected void updateItem(S item, boolean empty) {

                super.updateItem(item, empty);
                if (empty || item == null) {
                    this.setText("");
                    this.setGraphic(null);
                } else {
                    DeleteButton.setId("DeleteButton");
                    DeleteButton.setOnAction(event -> {

                        providers = (Provider) this.getTableView().getItems().get(getIndex());
                        imProviderController.ProviderList.add(providers);
                        TempProvidersList.add(providers);

                        ProvidersList.remove(providers);
                        DeletedProvidersList.clear();
                        DeleteSelectedProvidersButton.setDisable(true);
                    });
                    this.DeleteButton.setText("Delete");
                    this.setGraphic(DeleteButton);
                }
            }


        }

     */
    public class CustomButtonDeleteCell<T, S> extends TableCell<T, S> {
        private JFXButton DeleteButton = new JFXButton("Delete");


        @Override
        protected void updateItem(S item, boolean empty) {

            super.updateItem(item, empty);
            if (empty || item == null) {
                this.setText("");
                this.setGraphic(null);
            } else {
                DeleteButton.setId("DeleteButton");
                DeleteButton.setOnAction(event -> {
                    if (ProductList.size() == 1)
                        infoProducts = ProductList.get(0);
                    else infoProducts = (product) this.getTableView().getItems().get(getIndex());
                    DeleteIndividualProduct(infoProducts);


                    ForDisableButtons(true);


                });
                this.DeleteButton.setText("Delete");
                this.setGraphic(DeleteButton);
            }
        }


    }

    public class CustomButtonModifyCell<T, S> extends TableCell<T, S> {
        private JFXButton ModifyButton = new JFXButton("Modify");


        @Override
        protected void updateItem(S item, boolean empty) {

            super.updateItem(item, empty);
            if (empty || item == null) {
                this.setText("");
                this.setGraphic(null);
            } else {
                ModifyButton.setOnAction(event -> {
                    if (ProductList.size() == 1)
                        infoProducts = ProductList.get(0);
                    else infoProducts = (product) this.getTableView().getItems().get(getIndex());


                    ModifyProductMethod(infoProducts);

                });
                this.ModifyButton.setText("Modify");
                this.setGraphic(ModifyButton);
            }
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ProductArea.setDisable(true);
        forDisablingTextArea.set(true);
        SendEmailMessageButton.setDisable(true);
        ProviderCombobox.setItems(imProviderController.ProviderList);
        IfModifyIsClicled = false;
        IfSendEmailMessageIsLoaded = false;
        IfStageIsLoaded = true;
        //  AddProviderButtonClicked.set(false);
//        DeleteSelectedContext.setDisable(true);
        ForDisableButtons(true);
        IfNeededProductBoxIsChecked = false;
        TotalProducts.clear();
        SelectedProductList.clear();
        IfEmailMessageIsOpen = false;
        SendEmailMessageController.ProductList.clear();
        IfMultipleProductSelect.set(false);
        infoProducts = new product();
        //ForDisableModifyButton.set(false);
        ProvidersList.clear();
//        DeleteSelectedProvidersButton.setDisable(true);
/*
        DeleteCol.setCellValueFactory(call -> new SimpleBooleanProperty(true).asObject());
        DeleteCol.setCellFactory(call -> {
            return new CustomButtonCell<>();
        });
        SelectedItem.setCellValueFactory(call -> new SimpleBooleanProperty(false).asObject());
        SelectedItem.setCellFactory(call -> {
            return new JFXCheckboxCell() {
                @Override
                public void onUpdateRow(Object row, Boolean newValue) {

                    if (newValue) {

                        providers = (Provider) this.getTableView().getItems().get(getIndex());
                        DeletedProvidersList.add(providers);
                    } else {

                        providers = (Provider) this.getTableView().getItems().get(getIndex());
                        DeletedProvidersList.remove(providers);



                    }
                    DeleteSelectedContext.setDisable(DeletedProvidersList.isEmpty());
                    DeleteSelectedProvidersButton.setDisable(DeletedProvidersList.isEmpty());

                }


                @Override
                public void onUpdateEntity(Object entity) {

                }
            };
        });

 */
        DeleteProductCol.setCellValueFactory(call -> new SimpleBooleanProperty(true).asObject());
        DeleteProductCol.setCellFactory(call -> {
            return new CustomButtonDeleteCell<>();
        });
        ModifyProductCol.setCellValueFactory(call -> new SimpleBooleanProperty(true).asObject());
        ModifyProductCol.setCellFactory(call -> {
            return new CustomButtonModifyCell<>();
        });
        SelectedProductCol.setCellValueFactory(call -> new SimpleBooleanProperty(false).asObject());
        SelectedProductCol.setCellFactory(call -> {
            return new JFXCheckboxCell() {
                @Override
                public void onUpdateRow(Object row, Boolean newValue) {

                    if (newValue) {

                        infoProducts = (product) this.getTableView().getItems().get(getIndex());
                        SelectedProductList.add(infoProducts);
                    } else {

                        infoProducts = (product) this.getTableView().getItems().get(getIndex());
                        SelectedProductList.remove(infoProducts);


                    }
                    ForDisableButtons(SelectedProductList.isEmpty());
                }


                @Override
                public void onUpdateEntity(Object entity) {

                }
            };
        });
        /*
        FirstCol.setCellValueFactory(new PropertyValueFactory<Provider, String>("FirstName"));
        LastCol.setCellValueFactory(new PropertyValueFactory<Provider, String>("LastName"));
        PhoneCol.setCellValueFactory(new PropertyValueFactory<Provider, String>("PhoneNumber"));
        EmailCol.setCellValueFactory(new PropertyValueFactory<Provider, String>("Email"));


        ProvidersList.addAll(imProviderController.SelectedProvidersLoadEmail);
        imProviderController.SelectedProvidersLoadEmail.clear();
        FilteredList<Provider> ProviderResults = new FilteredList<>(ProvidersList, b -> true);
        SearchForProviders.textProperty().addListener(((observable, oldValue, newValue) -> {
            ProviderResults.setPredicate(provider -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                String lowerCaseFilter = newValue.toLowerCase();
                if (provider.getFirstName().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (provider.getLastName().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (provider.getEmail().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (provider.getPhoneNumber().toLowerCase().contains(lowerCaseFilter))
                    return true;


                else
                    return false;
            });
        }));
        SortedList<Provider> SortedProvidersResult = new SortedList<>(ProviderResults);
        SortedProvidersResult.comparatorProperty().bind(EmailProvidersTable.comparatorProperty());
        EmailProvidersTable.setItems(SortedProvidersResult);


         */

        BarcodeCol.setCellValueFactory(new PropertyValueFactory<product, Integer>("barcode"));
        ProductNameCol.setCellValueFactory(new PropertyValueFactory<product, String>("productName"));
        NeededQuantityCol.setCellValueFactory(new PropertyValueFactory<product, Integer>("NeededQuantity"));
        QuantityCol.setCellValueFactory(new PropertyValueFactory<product, Integer>("quantity"));
        OriginalStockCol.setCellValueFactory(new PropertyValueFactory<product, Integer>("initialQuantity"));


        SearchForProducts.textProperty().addListener(((observable, oldValue, newValue) -> {
            ProductsResults.setPredicate(infoProduct -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                String lowerCaseFilter = newValue.toLowerCase();
                if (infoProduct.getProductName().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (infoProduct.getBarcode().toString().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (String.valueOf(infoProduct.getNeededQuantity()).toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (infoProduct.getQuantity().toString().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (infoProduct.getInitialQuantity().toString().toLowerCase().contains(lowerCaseFilter))
                    return true;


                else
                    return false;
            });
        }));
        SortedProductsResult.comparatorProperty().bind(ProductTable.comparatorProperty());
        ProductTable.setItems(SortedProductsResult);

        MessageArea.textProperty().addListener((observable, oldValue, newValue) -> {
        });
        ProductList.addListener((InvalidationListener) c -> {

            RefreshTextArea();
            SendEmailMessageButton.setDisable(ProductList.isEmpty());
        });

        IfMultipleProductSelect.addListener((observable, oldValue, newValue) -> {
            ForDisableButtons(SelectedProductList.isEmpty());

        });
    }


/*

    public void DeleteContextList(ActionEvent actionEvent) {
        providers = EmailProvidersTable.getItems().get(EmailProvidersTable.getSelectionModel().getSelectedIndex());
        TempProvidersList.add(providers);
        ProvidersList.remove(providers);
    }

    public void DeleteSelected(ActionEvent actionEvent) throws IOException {
        DeleteSelectedProviders();
    }

 */


/*
    public void DeleteSelectedProviders() throws IOException {
        ShowAllDialogs.initDialogWithShow(SendEmailMessageRoot.getScene().getWindow(), ShowAllDialogs.AlertTypeDialog.DELETE);
        if (ShowAllDialogs.DELETEBUTTON.get()) {
            imProviderController.ProviderList.addAll(DeletedProvidersList);
            TempProvidersList.addAll(DeletedProvidersList);
            ProvidersList.removeAll(DeletedProvidersList);
            DeleteSelectedProvidersButton.setDisable(true);
            DeletedProvidersList.clear();
        }

    }

 */

    public void DeleteContextProductList() {
        infoProducts = ProductTable.getItems().get(ProductTable.getSelectionModel().getSelectedIndex());

        DeleteIndividualProduct(infoProducts);
    }

    public void DeleteSelectedProductContext() {
        DeleteSelectedProduct();
    }

    public void AddProductOnList(ActionEvent actionEvent) throws IOException {
/*
        AddProductButtonClicked.set(false);
        AddProductButtonClicked.set(true);


 */
System.out.println("abdelkader boussaid dahmouni");

     //   loader.setController(SendEmailMessageController);
imProviderController.controllerOfSendEmail.setSendEmailProductsTab();


    }

    public void DeleteSelectedProduct() {
        ProductList.removeAll(SelectedProductList);
        Iterator<product> infoProductIterator;
        infoProductIterator = SelectedProductList.iterator();
        //  System.out.println(SelectedProductList.size());
        while (infoProductIterator.hasNext()) {
            product infoProduct = infoProductIterator.next();
            DeleteIndividualProduct(infoProduct);

        }

        ForDisableButtons(true);
    }


    public void ModifySelectedProducts() {
        IfMultipleProductSelect.set(true);

        SendEmailController.ProductName = SelectedProductList.size() + " " + "Products";
        IfModifyIsClicled = true;
        SendEmailController.InitSetQuantity(SendEmailMessageRoot.getScene().getWindow());

    }

    public void ModifyContextProductList(ActionEvent actionEvent) {
        infoProducts = ProductTable.getItems().get(ProductTable.getSelectionModel().getSelectedIndex());

    }

    public void ModifySelectedProductContext(ActionEvent actionEvent) {
        ModifySelectedProducts();
    }


    public void RefreshTextArea() {
        String string = "The Needed  Products :";
        ProductArea.setText(string + "\n");
        for (product infoProduct : ProductList) {
            ProductArea.appendText("\n  " + "The Product Name :  " + infoProduct.getProductName() + "   " + " , The Needed Quantity : " + infoProduct.getNeededQuantity() + "\n");
        }
    }

    public void SendEmailMessage(ActionEvent actionEvent) throws SQLException, IOException {
        if (ProviderCombobox.getValue() == null) {

            ShowAllDialogs.initDialogWithShow(SendEmailMessageRoot.getScene().getWindow(), ShowAllDialogs.AlertTypeDialog.ONEBUTTON);
        }
        else {
            msgContent = "\n " + MessageArea.getText() + "\n \n \n" + ProductArea.getText();
            msgSubject = SubjectMessageTextField.getText();

/*
        for (Provider provider : ProvidersList){
            msgRecipients.add(provider.getEmail());

        }

 */
            msgRecipients = ProviderCombobox.getValue().getEmail();
            MessageArea.setDisable(true);

            tempoProvider= ProviderCombobox.getValue();
            idOfTheProvider=ProviderCombobox.getValue().getId();
            ShowSettingInformation ();
System.out.println("after it this is the value "+Email.ItSent);

            //System.out.println("the date test   "+dateformat.format(System.currentTimeMillis()));
            forDisablingTextArea.addListener((observable, oldValue, newValue) -> {
if (!forDisablingTextArea.get()) {
    MessageArea.setDisable(forDisablingTextArea.get());
}
            });

            forDisablingTextArea.set(true);
        }
/*
        String path = "C:\\Users\\Nassim\\Desktop\\PackItIn\\src\\resource\\File\\Blank_A4.jasper";

        try {
            // Path documentPath
            // HashMap<String, Object> params
            // JRDataSource jasperDataSource/
            // Indentation CTRL + ALT + L
            Path documentPath = Paths.get(path);
            Map<String, Object> params = new HashMap<>();
            params.put("ProviderName", "Hamouda"); // get it from login
            JREmptyDataSource emptyDatasource = new JREmptyDataSource();
            JRBeanCollectionDataSource jasperDataSource = new JRBeanCollectionDataSource(ProductList);
            params.put("DataSource", jasperDataSource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(documentPath.toAbsolutePath().toString(), params, emptyDatasource);
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            e.printStackTrace();
        }

 */
    }

    void ForDisableButtons(boolean state) {
        DeleteSelectedProductsButton.setDisable(state);
        ModifySelectedProductsButton.setDisable(state);
        DeleteSelectedProductContext.setDisable(state);
        ModifySelectedProductContext.setDisable(state);

    }
    public void sendProductNotInList (ActionEvent event) throws IOException {
        StackPane stackPane = FXMLLoader.load(getClass().getResource("settingProductNotInList.fxml"));
        sendProductNotInListDialog= new JFXDialog(SendEmailMessageRoot,stackPane,JFXDialog.DialogTransition.RIGHT);
        sendProductNotInListDialog.show();
    }

    public void ModifyProductMethod(product infoProducts) {
        IfEmailMessageIsOpen = true;
        IfModifyIsClicled = true;
        Quantity = String.valueOf(infoProducts.getNeededQuantity());
        SendEmailController.ProductName = infoProducts.getProductName();
        SendEmailController.InitSetQuantity(SendEmailMessageRoot.getScene().getWindow());
        if (SetQuantityController.IfExitToModifySendEmailMessage) {
            // System.out.println("In The Condition Iffff");

        } else {
            //   System.out.println("The Enter COndition Elssse");

            ForDisableButtons(true);
            SelectedProductList.clear();
        }
    }

    void EmailWasSent(Integer barcode) {
        Connection connection = ConnectionClass.getConnection();
        String Sql = "UPDATE stock set IfWasSent=? WHERE barcode=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(Sql);
            preparedStatement.setInt(2, barcode);
            preparedStatement.setBoolean(1, true);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    void DeleteIndividualProduct(product infoProducts) {

       if(infoProducts.getBarcode()!=-1) {
           infoProducts.setNeededQuantity(0);

           SendEmailController.ProductList.add(infoProducts);
           SendEmailController.TempoListOfProducts.add(infoProducts);
           if (infoProducts.getStockPercentage() <= 20)
               SendEmailController.NeededProduct.add(infoProducts);

       }
        ProductList.remove(infoProducts);
        SelectedProductList.clear();
    }
    void showApplyCommandTabWithNoSendEmail(){



    }
public  void ShowSettingInformation () throws IOException {
        StackPane stackPane = FXMLLoader.load(getClass().getResource("settingSenderInformation.fxml"));
    settingInfoDialog= new JFXDialog(SendEmailMessageRoot,stackPane,JFXDialog.DialogTransition.RIGHT);
    settingInfoDialog.show();

    settingInfoDialog.setOnDialogClosed(e->forDisablingTextArea.set(false));


}
  public static  void InsertProductToCommand(ObservableList<product> ProductList, Provider provider, String Date) throws SQLException {

        Connection connection = ConnectionClass.getConnection();
        //  Connection connection2 =ConnectionClass.getConnection();

        String Sql = "INSERT INTO ProductsCommand (id,ProductName,DateOfCommand,RequiredQuantity,barcode,userID,idOfProvider) values (?,?,?,?,?,?,?)";

        String Sql3 = "SELECT * FROM ProductsCommand WHERE id = (SELECT MAX(id) FROM ProductsCommand) and userID=?";
        int id = 1;

        PreparedStatement preparedStatement = connection.prepareStatement(Sql3);
        preparedStatement.setInt(1,user.getUserID());
        // PreparedStatement preparedStatement2 = connection2.prepareStatement(Sql2);
        //   PreparedStatement preparedStatement3 = connectio
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
            id += resultSet.getInt(1);
        //System.out.println("lwelwelel test  "+id);
        preparedStatement = connection.prepareStatement(Sql);
        Command command = new Command(id, Date, provider.getId(), provider, ProductList, ProductList.size());
        CommandHistoryController.CommandList.add(command);

        CommandHistoryController.CommandHashMap.put(id, command);
        for (product Product : ProductList) {
            // System.out.println("lwelwelel test  "+id +"   "+Product.getProductName()+"    "+Product.getNeededQuantity()+"   " +Product.getBarcode() + "    "+Date) ;


            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, Product.getProductName());
            preparedStatement.setString(3, Date);
            preparedStatement.setInt(4, Product.getNeededQuantity());
            preparedStatement.setInt(5, Product.getBarcode());
            preparedStatement.setInt(6, user.getUserID());
            preparedStatement.setInt(7,idOfTheProvider );
            preparedStatement.executeUpdate();
        }


    }




}

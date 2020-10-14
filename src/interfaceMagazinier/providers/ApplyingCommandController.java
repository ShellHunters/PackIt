package interfaceMagazinier.providers;

import Connector.ConnectionClass;
import basicClasses.Provider;
import basicClasses.product;
import basicClasses.user;
import com.jfoenix.controls.*;
import interfaceMagazinier.stock.update.updateController;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class ApplyingCommandController implements Initializable {


    @FXML
    private StackPane applyingCommandContainer;
    /*
        @FXML
        private JFXTextField SearchProviderTextField;

        @FXML
        private JFXDatePicker CommandDatePicker;

        @FXML
        public TableView<Provider >  ProvidersTableView;

        @FXML
        public TableColumn<Provider, Boolean> SelectedItem;

        @FXML
        public TableColumn<Provider, String> FirstCol;

        @FXML
        public TableColumn<Provider, String> LastCol;
        @FXML
        public TableColumn<Provider, String> PhoneCol;
        @FXML
        public TableColumn<Provider, String> EmailCol;

     */
    @FXML
    private TableView<product> ProductTableView;
    @FXML
    private TableColumn<product, Boolean> selectedCommandCol;

    @FXML
    private TableColumn<product, String> ProductNameCol;

    @FXML
    private TableColumn<product, Integer> RequiredQuantityCol;

    @FXML
    private TableColumn<product, Boolean> WasAddedToStockCol;

    @FXML
    private TableColumn<product, Boolean> SetRequiredButtonCol;

    @FXML
    private JFXButton applySelectedProductsButton;

    @FXML
    private JFXButton SetRequiredQuantityButton;

    @FXML
    private JFXTextField searchCommandTextField;

    @FXML
    private Label ProviderNameLabel;

    @FXML
    private JFXButton applyingAllProductsButton;

    @FXML
    private JFXCheckBox NotAddedCheckBox;

    public static ObservableList<product> ProductList = FXCollections.observableArrayList();

    public static boolean IfApplyingCommandIsOpen, IfApplyingSceneIsOpen;
    CursorPosition dragPosition = new CursorPosition();
    static public Provider TheProvider;
    static public product TheProduct;
    public static Integer RequireQuantity, IndexOfProduct;
    public static ArrayList<Provider> SelectedProvidersList = new ArrayList<Provider>();
    public static ArrayList<product> SelectedProductList = new ArrayList<product>();
    public static ArrayList<product> TempoProductList = new ArrayList<product>();
    public static ObservableList<product> NotAddedList = FXCollections.observableArrayList();
    static ArrayList<product> TempoList = new ArrayList<product>();

    public static String ProviderName;

    @FXML
    void applySelectedProducts(ActionEvent event) throws IOException, SQLException {

        System.out.println("the Size  " + SelectedProductList.size());
        LookingList(SelectedProductList);


    }

    @FXML
    void applyingAllProducts(ActionEvent event) throws IOException, SQLException {
        try {
            LookingList(ProductList);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    class CursorPosition {
        double x, y;
    }

    public void Exit() {
       // Stage stage = (Stage) applyingCommandContainer.getScene().getWindow();

        IfApplyingSceneIsOpen = false;
        CommandHistoryController.applyingCommandDialog.close();
        //stage.close();
    }

    public class CustomButtonCell<T, S> extends TableCell<T, S> {
        private Button AddProductToStock = new Button("Add Product To Stock");


        @Override
        protected void updateItem(S item, boolean empty) {

            super.updateItem(item, empty);
            if (empty || item == null) {
                this.setText("");
                this.setGraphic(null);
            } else {
                product MyProduct = (product) this.getTableView().getItems().get(getIndex());
                AddProductToStock.setOnAction(e -> {
                    TheProduct = (product) this.getTableView().getItems().get(getIndex());
                    System.out.println(TheProduct.getProductName());

                    IfApplyingCommandIsOpen = true;
                    try {
                        FindTheWay(TheProduct);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                    IfApplyingCommandIsOpen = false;
                });


                if (!MyProduct.getIfWasAdded()&&MyProduct.getConfirmedProduct() ) {
                    this.setGraphic(AddProductToStock);
                } else {
                    this.setText("");
                    this.setGraphic(null);
                }

            }

        }
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
                product MyProduct = (product) this.getTableView().getItems().get(getIndex());
                if (!MyProduct.getIfWasAdded()&&MyProduct.getConfirmedProduct() ) {
                    this.isChecked.setSelected(this.getItem());
                    this.setGraphic(this.isChecked.getParent());
                } else {
                    this.setGraphic(null);
                }
                this.setText("");
            }
        }

        public abstract void onUpdateRow(T row, Boolean newValue);

        public abstract void onUpdateEntity(T entity);

        public void onBeforeUpdateRow(T row, Boolean newValue) {

        }
    }

    boolean IfInTable(Integer Barcode) throws SQLException {

        Connection connection = ConnectionClass.getConnection();
        String Sql = "SELECT  * FROM stock WHERE barcode=?";
        PreparedStatement preparedStatement = connection.prepareStatement(Sql);
        preparedStatement.setInt(1, Barcode);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            System.out.println("we find the barcode  " + Barcode);
            RequireQuantity = TheProduct.getNeededQuantity();
            try {
                TheProduct = new product(resultSet.getString("name"), resultSet.getInt("barcode"), resultSet.getFloat("buyprice"), resultSet.getFloat("sellprice"), resultSet.getInt("quantity"), resultSet.getDate("expirationdate").toString());
            } catch (Exception e) {
                TheProduct = new product(resultSet.getString("name"), resultSet.getInt("barcode"), resultSet.getFloat("buyprice"), resultSet.getFloat("sellprice"), resultSet.getInt("quantity"), "");
            }

            return true;
        }
        return false;

    }

    public static void InitTable() {
        TempoProductList.clear();
        NotAddedList.clear();
        SelectedProductList.clear();
        ProductList.clear();
        for (product myproduct : CommandHistoryController.command.getListOfProducts())
            if (!myproduct.getIfWasAdded()) {
                NotAddedList.add(myproduct);
            }
        ProductList.addAll(CommandHistoryController.command.getListOfProducts());

    }


    public void SceneDraggedMouse(MouseEvent mouseEvent) {
        Stage stage = (Stage) applyingCommandContainer.getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() + dragPosition.x);
        stage.setY(mouseEvent.getScreenY() + dragPosition.y);
    }

    public void ScenePressedMouse(MouseEvent mouseEvent) {
        Stage stage = (Stage) applyingCommandContainer.getScene().getWindow();
        dragPosition.x = stage.getX() - mouseEvent.getScreenX();
        dragPosition.y = stage.getY() - mouseEvent.getScreenY();
    }

    void LookingList(Collection<product> TheCollection) throws SQLException, IOException {
        IfApplyingCommandIsOpen = true;

        for (product myProduct : TheCollection) {
            if (myProduct.getIfWasAdded())
                continue;
            TheProduct = myProduct;

            FindTheWay(TheProduct);
        }
        IfApplyingCommandIsOpen = false;
    }

    void FindTheWay(product theProduct) throws SQLException, IOException {
        JFXDialog dialog;
        Region root1;
        IndexOfProduct = ProductList.indexOf(theProduct);
        System.out.println("the index is:  " + IndexOfProduct);
                System.out.println(TheProduct);
        root1 = FXMLLoader.load(getClass().getResource("/interfaceMagazinier/stock/add/addProduct.fxml"));
        dialog = new JFXDialog(applyingCommandContainer, root1, JFXDialog.DialogTransition.RIGHT);
        dialog.show();
    }

    Provider getTheProvider(Integer id) throws SQLException {
        Provider provider = null;
        String Sql = "SELECT  * from ProvidersInfo where id=? and userID=?";
        Connection connection = ConnectionClass.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(Sql);
        preparedStatement.setInt(1, id);
        preparedStatement.setInt(2, user.getUserID());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next())
            provider = new Provider(id, resultSet.getString(2), resultSet.getString(3), resultSet.getFloat(7),resultSet.getString(5));
        return provider;

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("test if init ");
ProductList.clear();
        NotAddedCheckBox.setSelected(false);
        System.out.println("hada houwa test aaa " + CommandHistoryController.command.getIdOfProvider());
        try {
            TheProvider = getTheProvider(CommandHistoryController.command.getIdOfProvider());
            if (TheProvider != null)
                ProviderNameLabel.setText("The Command Provider :  " + TheProvider.getLastName() + " " + TheProvider.getFirstName());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        IfApplyingSceneIsOpen = true;
        IfApplyingCommandIsOpen = false;
        InitTable();

        NotAddedCheckBox.setDisable(NotAddedList.isEmpty());
        applySelectedProductsButton.setDisable(true);
        applyingAllProductsButton.setDisable(NotAddedList.isEmpty());
        RequiredQuantityCol.setCellValueFactory(new PropertyValueFactory<product, Integer>("NeededQuantity"));
        ProductNameCol.setCellValueFactory(new PropertyValueFactory<product, String>("productName"));
        //   NumberOfProductsCol.setCellValueFactory(new PropertyValueFactory<Command, Integer>("SizeOfProduct"));
        SetRequiredButtonCol.setCellValueFactory(call -> new SimpleBooleanProperty(true).asObject());

        SetRequiredButtonCol.setCellFactory(call -> {
            return new CustomButtonCell<>();
        });

        selectedCommandCol.setCellValueFactory(call -> new SimpleBooleanProperty(false).asObject());

        selectedCommandCol.setCellFactory(call -> {
            return new JFXCheckboxCell() {

                @Override
                public void onUpdateRow(Object row, Boolean newValue) {
                    product theProduct = (product) this.getTableView().getItems().get(getIndex());
                    if (newValue)
                        SelectedProductList.add(theProduct);
                    else SelectedProductList.remove(theProduct);
                    applySelectedProductsButton.setDisable(SelectedProductList.isEmpty());
                }

                @Override
                public void onUpdateEntity(Object entity) {

                }
            };
        });

        NotAddedCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {


            if (newValue) {
                TempoList.clear();
                TempoList.addAll(ProductList);
                System.out.println("hada test li kount n7awess 3lih  " + TempoList.size());
                ProductList.clear();
                ProductList.addAll(NotAddedList);

            } else {
                System.out.println("hada test li kount n7awess 3lih 2   " + TempoList.size());

                ProductList.clear();
                ProductList.addAll(TempoList);
            }

        });
        NotAddedList.addListener((InvalidationListener) c -> {
            NotAddedCheckBox.setDisable(NotAddedList.isEmpty());
            applyingAllProductsButton.setDisable(NotAddedList.isEmpty());

        });

        FilteredList<product> ProductResults = new FilteredList<>(ProductList, b -> true);
        searchCommandTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            ProductResults.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                String lowerCaseFilter = newValue.toLowerCase();
                //else if (product.getProductType().toLowerCase().indexOf(lowerCaseFilter) != -1)
                //  return true;
                if (product.getProductName().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else return String.valueOf(product.getNeededQuantity()).toLowerCase().contains(lowerCaseFilter);
            });
        }));

        SortedList<product> SortedResult = new SortedList<>(ProductResults);
        SortedResult.comparatorProperty().bind(ProductTableView.comparatorProperty());
        ProductTableView.setItems(SortedResult);








    }
}

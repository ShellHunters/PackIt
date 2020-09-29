package interfaceMagazinier.providers;

import basicClasses.Provider;
import basicClasses.product;
import basicClasses.user;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.IOException;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SendEmailController implements Initializable {



    @FXML
    AnchorPane TheSendEmailRoot;


    @FXML
    private TabPane TheTabPaneRoot;
    @FXML
    private StackPane AllSendEmailRoot;
    @FXML
    private JFXTextField SearchBox;

    @FXML
    private JFXButton SetMultipleQuantityButton;
    @FXML
    private JFXCheckBox CheckboxNeededProduct;
    @FXML
    private JFXCheckBox MultipleSelectCheckBox;
    @FXML
    private JFXButton SendEmailButton;
    @FXML
    private TableView<product> TableOfEmail;

    @FXML
    private TableColumn<product, Boolean> SelectedItem;
    @FXML
    private TableColumn<product, Integer> BarcodeCol;

    @FXML
    private TableColumn<product, String> ProductNameCol;

    @FXML
    private TableColumn<product, Double> LastPriceCol;

    @FXML
    private TableColumn<product, Integer> QuantityCol;

    @FXML
    private TableColumn<product, Integer> OriginalStockCol;

    @FXML
    private TableColumn<product, Double> StockPercentageCol;

    @FXML
    private TableColumn<product, String> WasSentCol;
    @FXML
    private Tab SendEmailProductsTab;
    @FXML
    private Tab SendEmailMessageTab;
    @FXML
    private Tab ApplyingCommandTab;

    @FXML
    private JFXButton DeleteSelectedProvidersButton;
    public static Provider providers;
    public static product theProduct = new product();
    public static ObservableList<Provider> ProvidersList = FXCollections.observableArrayList();
    public static boolean IfMultipleSelected, TheOldValue;
    public static String ProductName;
    public static ObservableList<product> ProductList = FXCollections.observableArrayList();
    public static ArrayList<product> NeededProduct = new ArrayList<product>();
    public static ArrayList<product> TempoListOfProducts = new ArrayList<product>();
    public static ArrayList<product> MultipleSelectionList = new ArrayList<product>();
    public static SimpleBooleanProperty ForceCheck = new SimpleBooleanProperty(true);
    public static SimpleBooleanProperty ForceCheckForMultipleSelectCheckbox = new SimpleBooleanProperty(true);
    public static SimpleBooleanProperty ForDisableSetQuantityButton = new SimpleBooleanProperty(true);
    public static SimpleBooleanProperty ForDisableMultipleCheck = new SimpleBooleanProperty(true);
    public static boolean IfTabPaneIsOpen;
 public  static DecimalFormat df = new DecimalFormat("0.00");
    public void ApplyingCommand() {
        System.out.println("the size of the tab pane " + TheTabPaneRoot.getTabs().size());
        //System.out.println("the tab 1  "+TheTabPaneRoot.getTabs().get(1).getGraphic());
        // System.out.println("the tab 2  "+TheTabPaneRoot.getTabs().get(2).getGraphic());
        //System.out.println("the tab 3  "+TheTabPaneRoot.getTabs().get(3).getGraphic());

        if (!CommandHistoryController.IfApplyingCommandIsOpen) {

            System.out.println("I will there " + ApplyingCommandTab);

            TheTabPaneRoot.getTabs().add(ApplyingCommandTab);
            System.out.println("the size of the tab pane " + TheTabPaneRoot.getTabs().size());
            CommandHistoryController.IfApplyingCommandIsOpen = true;

        }

        TheTabPaneRoot.getSelectionModel().select(ApplyingCommandTab);

    }

    public abstract class JFXCheckboxProductCell<T> extends TableCell<T, Boolean> {
        protected JFXCheckBox isChecked = new JFXCheckBox();

        public JFXCheckboxProductCell() {
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
            ForceCheck.addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    isChecked.setSelected(false);
                }

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


    public void SendEmail(ActionEvent actionEvent) throws IOException {


        if (!SendEmailMessageController.IfSendEmailMessageIsLoaded) {
            System.out.println("the size of the tab pane " + TheTabPaneRoot.getTabs().size());
            TheTabPaneRoot.getTabs().add(SendEmailMessageTab);
            SendEmailMessageController.IfSendEmailMessageIsLoaded = true;
        }
        TheTabPaneRoot.getSelectionModel().select(SendEmailMessageTab);
    }

    public void SetMultipleQuantity(ActionEvent actionEvent) {
        Integer size = MultipleSelectionList.size();
        ProductName = size.toString() + "  Products";

        InitSetQuantity(TheSendEmailRoot.getScene().getWindow());
        if (!SetQuantityController.IfExit) {
            if (!SendEmailMessageController.IfSendEmailMessageIsLoaded) {


                TheTabPaneRoot.getTabs().add(SendEmailMessageTab);
                SendEmailMessageController.IfSendEmailMessageIsLoaded = true;
                TheTabPaneRoot.getSelectionModel().select(SendEmailMessageTab);
            }
        }


    }

    public static void InitSetQuantity(Window owner) {
        StackPane root = null;
        try {
            root = FXMLLoader.load(SendEmailController.class.getResource("SetQuantity.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.initModality(Modality.WINDOW_MODAL);

        stage.setScene(scene);
        stage.initOwner(owner);
        stage.showAndWait();
    }

    public void InitTable() throws SQLException {
        TempoListOfProducts.clear();
        MultipleSelectionList.clear();
        NeededProduct.clear();
        ProductList.clear();
        String sql = "SELECT * FROM stock where userID=?";
        Connection connection = Connector.ConnectionClass.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, user.getUserID());
        ResultSet resultSet=preparedStatement.executeQuery();
        while (resultSet.next()) {
            df.setRoundingMode(RoundingMode.UP);
            float Percentage = Float.parseFloat( df.format( (float) (resultSet.getInt(5) * 100) / resultSet.getInt(8)));
            // System.out.println("this is hbal test  "+resultSet.getInt(8) + "ber ber "+Percentage);
            if (resultSet.getBoolean(9))
                WasSentCol.getStyleClass().add("ItWasSent");
            else WasSentCol.getStyleClass().add("ItWasntSent");
//            String theDate=resultSet.getDate(3).toString();
            //  System.out.println("tajrouba   "+theDate);
            //   System.out.println("this is 3333333333hbal test  "+resultSet.getInt(5));
            //  System.out.println("hada hballllllll fga3333333  "+ resultSet.getInt(1) + "   "+ resultSet.getString(4)+ "   "+resultSet.getFloat(2) + "   "+ resultSet.getInt(5)+ "   "+resultSet.getInt(8)+ "   "+Percentage + "   "+resultSet.getBoolean(9));
            if (Percentage <= 20)
                NeededProduct.add(new product(resultSet.getString(4), resultSet.getInt(1), resultSet.getFloat(2), resultSet.getInt(5), Percentage, resultSet.getBoolean(9), resultSet.getInt(8)));

            ProductList.add(new product(resultSet.getString(4), resultSet.getInt(1), resultSet.getFloat(2), resultSet.getInt(5), Percentage, resultSet.getBoolean(9), resultSet.getInt(8)));
            TempoListOfProducts.add(new product(resultSet.getString(4), resultSet.getInt(1), resultSet.getFloat(2), resultSet.getInt(5), Percentage, resultSet.getBoolean(9), resultSet.getInt(8)));

        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InitValues();


        CommandHistoryController.IfApplyingCommandIsOpen = false;
        SetMultipleQuantityButton.setDisable(true);
        MultipleSelectCheckBox.setSelected(false);
        TheTabPaneRoot.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        TheTabPaneRoot.getTabs().removeAll(SendEmailMessageTab, ApplyingCommandTab);

        System.out.println(TheTabPaneRoot.getTabs().size());
        /*
        SendEmailMessageController.AddProviderButtonClicked.addListener((observable, oldValue, newValue) -> {
            if (newValue) {

                if (SendEmailMessageController.IfStageIsLoaded) {
                    TheTabPaneRoot.getTabs().add(AddProviderToListTab);

                    SendEmailMessageController.IfStageIsLoaded=false;
                }


                TheTabPaneRoot.getSelectionModel().select(AddProviderToListTab);

            }


        });

         */
        SendEmailMessageController.AddProductButtonClicked.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                TheTabPaneRoot.getSelectionModel().select(SendEmailProductsTab);
            }
        });
        SendEmailMessageTab.setOnCloseRequest(e -> {
            SendEmailMessageController.IfEmailMessageIsOpen = false;
            System.out.println("Hellllooooo");
        });
        /*
        AddProviderToListTab.setOnCloseRequest(event ->{SendEmailMessageController.ForDisableModifyButton.set(false) ;
            SendEmailMessageController.IfStageIsLoaded=true; });

         */

        System.out.println("hadi tab pane " + TheTabPaneRoot);
        SendEmailProductsTab.setOnCloseRequest(e -> {
            SendEmailMessageController.IfSendEmailMessageIsLoaded = false;
            try {
                ReturnToProvidersInterface();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
        ApplyingCommandTab.setOnCloseRequest(e -> {
                    CommandHistoryController.IfApplyingCommandIsOpen = false;
                    System.out.println("this is the second place");


                }
        );

        try {
            InitTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        MultipleSelectCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                ForceCheckForMultipleSelectCheckbox.set(MultipleSelectCheckBox.isSelected());
                IfMultipleSelected = newValue;
                if (!newValue) {
                    ForceCheck.set(false);
                    MultipleSelectionList.clear();
                }


            }

        });
        SelectedItem.setCellValueFactory(call -> new SimpleBooleanProperty(false).asObject());
        SelectedItem.setCellFactory(call -> {
            return new JFXCheckboxProductCell<product>() {
                @Override
                public void onUpdateRow(product row, Boolean newValue) {
                    if (newValue) {
                        theProduct = (product) this.getTableView().getItems().get(getIndex());
                        if (IfMultipleSelected) {
                            SendEmailController.MultipleSelectionList.add(theProduct);
                            ProductName = String.valueOf(MultipleSelectionList.size());

                        } else {

                            ProductName = theProduct.getProductName();

                            InitSetQuantity(TheSendEmailRoot.getScene().getWindow());

                        }

                    } else {
                        if (IfMultipleSelected) {
                            MultipleSelectionList.remove(theProduct);
                        }


                    }
                    SetMultipleQuantityButton.setDisable(MultipleSelectionList.isEmpty());

                }

                @Override
                public void onUpdateEntity(product entity) {

                }

            };
        });

        BarcodeCol.setCellValueFactory(new PropertyValueFactory<product, Integer>("barcode"));
        ProductNameCol.setCellValueFactory(new PropertyValueFactory<product, String>("productName"));
        LastPriceCol.setCellValueFactory(new PropertyValueFactory<product, Double>("buyPrice"));
        QuantityCol.setCellValueFactory(new PropertyValueFactory<product, Integer>("quantity"));
        OriginalStockCol.setCellValueFactory(new PropertyValueFactory<product, Integer>("initialQuantity"));
        StockPercentageCol.setCellValueFactory(new PropertyValueFactory<product, Double>("StockPercentage"));
        WasSentCol.setCellValueFactory(new PropertyValueFactory<product, String>("ifWasSentString"));
        FilteredList<product> Results = new FilteredList<>(ProductList, b -> true);
        SearchBox.textProperty().addListener(((observable, oldValue, newValue) -> {
            Results.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty())
                    return true;
                String lowerCaseFilter = newValue.toLowerCase();
                if (product.getProductName().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (String.valueOf(product.getBarcode()).toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (String.valueOf(product.getBuyPrice()).toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (String.valueOf(product.getInitialQuantity()).toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (String.valueOf(product.getQuantity()).toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                else if (String.valueOf(product.getStockPercentage()).toLowerCase().indexOf(lowerCaseFilter) != -1)
                    return true;
                    //else if (product.getProductType().toLowerCase().indexOf(lowerCaseFilter) != -1)
                    //  return true;
                else
                    return false;
            });
        }));

        SortedList<product> SortedResult = new SortedList<>(Results);
        SortedResult.comparatorProperty().bind(TableOfEmail.comparatorProperty());
        TableOfEmail.setItems(SortedResult);
        CheckboxNeededProduct.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                SendEmailMessageController.IfNeededProductBoxIsChecked = CheckboxNeededProduct.isSelected();
                if (newValue) {


                    ProductList.clear();

                    ProductList.addAll(NeededProduct);
                } else {
                    ProductList.clear();
                    ProductList.addAll(TempoListOfProducts);
                }
            }
        });

        ForDisableSetQuantityButton.addListener((observable, oldValue, newValue) -> {
            SetMultipleQuantityButton.setDisable(ForDisableSetQuantityButton.get());
        });
        ForDisableMultipleCheck.addListener((observable, oldValue, newValue) -> {
            MultipleSelectCheckBox.setDisable(newValue);

        });
        ForceCheckForMultipleSelectCheckbox.addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (IfMultipleSelected)
                    MultipleSelectCheckBox.setSelected(false);
            }

        });
    }


    public void ReturnToProvidersInterface() throws IOException {
        SendEmailMessageController.ForDisableModifyButton.set(false);
        IfTabPaneIsOpen = false;
        SendEmailMessageController.TempProvidersList.clear();
        StackPane Container = FXMLLoader.load(getClass().getResource("imProviders.fxml"));

        TheSendEmailRoot.getChildren().setAll(Container);

    }

    public static void InitValues() {
        ForDisableMultipleCheck.set(false);
        ForDisableSetQuantityButton.set(true);
        ForceCheckForMultipleSelectCheckbox.set(false);
        ForceCheck.set(true);
        TheOldValue = false;
    }


}

package interfaceMagazinier.providers;

import Connector.ConnectionClass;
import basicClasses.Command;
import basicClasses.Provider;
import basicClasses.product;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialog;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.net.URL;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class CommandHistoryController implements Initializable {
    @FXML
    private StackPane CommandHistoryContainer;

    @FXML
    private TableView<Command> CommandTableView;

    @FXML
    private TableColumn<Command, Integer> IdCol;

    @FXML
    private TableColumn<Command, String> DateOfCommandCol;

    @FXML
    private TableColumn<Command, Integer> NumberOfProductsCol;

    @FXML
    private TableColumn<Command, String> AllWasAddedCol;

    @FXML
    private TableColumn<Command, Boolean> ExploreButtonCol;

    @FXML
    private JFXDatePicker CommandDatePicker;

    ObservableList <Command> CommandList = FXCollections.observableArrayList();
    ArrayList<product> ProductList=new ArrayList<product>();
    HashMap<Integer,Command>CommandHashMap= new HashMap<Integer,Command >();
   public static JFXDialog ApplyingCommandDialog;
   public StackPane ApplyingCommandContainer;
    public static Command command;
    Stage CommandHistoryStage;
public static boolean IfApplyingCommandIsOpen;




    public class CustomButtonCell<T, S> extends TableCell<T, S> {
        private Button ExploreButton = new Button("Explore Command");


        @Override
        protected void updateItem (S item, boolean empty) {

            super.updateItem(item, empty);
            if (empty || item == null) {
                this.setText("");
                this.setGraphic(null);
            }


          else   {
              ExploreButton.setOnAction(e->{
 command =(Command) this.getTableView().getItems().get(getIndex());
                  try {
                      LoadApplyingCommandScene();
                  } catch (IOException ioException) {
                      ioException.printStackTrace();
                  }

              });



              this.setGraphic(ExploreButton);}

        }
    }
    void LoadApplyingCommandScene() throws IOException {
        ApplyingCommandContainer= FXMLLoader.load(getClass().getResource("ApplyingCommand.fxml"));

        Scene scene = new Scene(ApplyingCommandContainer);
        Stage stage= new Stage();
        stage.setScene(scene);
        stage.initOwner(CommandHistoryContainer.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();

        //ApplyingCommandDialog=new JFXDialog(CommandHistoryContainer,ApplyingCommandContainer,JFXDialog.DialogTransition.CENTER);
       // ApplyingCommandDialog.setOverlayClose(false);
     //   ApplyingCommandDialog.show();

    }

    void InitTable() throws SQLException {
        Integer ID;
        Connection connection = ConnectionClass.getConnection();
        Connection connection2 = ConnectionClass.getConnection();
        String Sql2="SELECT * FROM  ProvidersCommand";
        String Sql = "SELECT * FROM  ProductsCommand";
        //String Sql3="SELECT IdOFTheProvider FROM ProvidersCommand WHERE id=?";
        PreparedStatement preparedStatement2 = connection2.prepareStatement(Sql2);
        PreparedStatement preparedStatement = connection.prepareStatement(Sql);
        ResultSet resultSet2=preparedStatement2.executeQuery();
        /*
        (new Thread(() -> {
            while (true) {
                try {
                    if (!resultSet2.next()) break;

         */
        while (resultSet2.next()){
             ID = resultSet2.getInt(1);
            //     System.out.println("this is command test  " +ID);
            CommandHashMap.put(ID, new Command(ID, resultSet2.getString(6), resultSet2.getInt(7),new Provider(resultSet2.getString(2), resultSet2.getString(3), resultSet2.getString(4), resultSet2.getString(5))));


        }


                    /*
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        })).start();


 */
        ResultSet resultSet=preparedStatement.executeQuery();
        /*
        (new Thread(() -> {
            while (true) {
                try {
                    if (!resultSet.next()) break;

         */
        while (resultSet.next()) {
            ID = resultSet.getInt(1);
         //   System.out.println("this is  second command test  " +ID);

            // if (!CommandHashMap.containsKey(ID))
            //CommandHashMap.put(ID,  new Command(ID, new  product(resultSet.getString(2),resultSet.getInt(6),resultSet.getInt(5),resultSet.getBoolean(3))));

            CommandHashMap.get(ID).addListProducts(new product(resultSet.getString(2), resultSet.getInt(6), resultSet.getInt(5), resultSet.getBoolean(3)));

        }
/*
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        })).start();

 */
        for (Command command : CommandHashMap.values()){
            command.setSizeOfProduct(command.getListOfProducts().size());
//System.out.println("this is loop test   "+ command.getDateOfCommand().substring(0,10));
        }
        CommandList.addAll(CommandHashMap.values());

    }







    @Override
    public void initialize (URL location, ResourceBundle resources) {

        System.out.println( LocalDate.now());
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        Date date = new Date();
        CommandDatePicker.setValue(LocalDate.now());


    //CommandDatePicker.setValue(LocalDate.now());
    //    CommandDatePicker.setValue(LocalDate.of(2016, 7, 25));

//        System.out.println("instializer command test  "+CommandDatePicker.getValue().toString());
        try {
            InitTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        IdCol.setCellValueFactory(new PropertyValueFactory<Command, Integer>("Id"));
        DateOfCommandCol.setCellValueFactory(new PropertyValueFactory<Command, String>("DateOfCommand"));
        NumberOfProductsCol.setCellValueFactory(new PropertyValueFactory<Command, Integer>("SizeOfProduct"));
        ExploreButtonCol.setCellValueFactory(call -> new SimpleBooleanProperty(true).asObject());
        ExploreButtonCol.setCellFactory(call -> {
            return new CustomButtonCell<>();
        });

        FilteredList<Command> Results = new FilteredList<>(CommandList, b -> true);


        CommandDatePicker.valueProperty().addListener((observable, oldValue, newValue) ->{
            String theDate =CommandDatePicker.getValue().toString();
            System.out.println("now    "+theDate);
            System.out.println("under it  "+LocalDate.now().toString());
            System.out.println("the test   "+theDate.equals(LocalDate.now().toString()));
            System.out.println("the final inchaalahn    "  +CommandHashMap.get(1).getDateOfCommand().substring(0, 10));
            System.out.println("the final system  "+CommandHashMap.get(1).getDateOfCommand().substring(0, 10).equals(theDate));
        Results.setPredicate(command -> {
            if (newValue == null || theDate.isEmpty())
                return true;

            return command.getDateOfCommand().substring(0, 10).equals(theDate);

        });

        });
        SortedList<Command> SortedResult = new SortedList<>(Results);
        SortedResult.comparatorProperty().bind(CommandTableView.comparatorProperty());
        CommandTableView.setItems(SortedResult);


    }
}

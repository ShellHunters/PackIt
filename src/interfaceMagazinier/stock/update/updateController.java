package interfaceMagazinier.stock.update;

import Connector.ConnectionClass;
import basicClasses.Provider;
import basicClasses.product;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import interfaceMagazinier.providers.ApplyingCommandController;
import interfaceMagazinier.providers.SendEmailController;
import interfaceMagazinier.providers.SendEmailMessageController;
import interfaceMagazinier.providers.imProviderController;
import interfaceMagazinier.stock.add.addController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.ResourceBundle;

public class updateController implements Initializable {
@FXML
    private JFXComboBox <Provider> ProvidersComboBox;
    @FXML private  JFXTextField productname;
    @FXML private  JFXTextField barcode;
    @FXML private  JFXTextField sellprice;
    @FXML private  JFXTextField buyprice;
    @FXML private  JFXTextField quantity;
    @FXML private  JFXDatePicker expirationdate;
    @FXML private  Label errorLabel;
    public static product productSelected;
    private   Integer RequiredQuantity;
public  product NewProduct,OldProduct;
    public boolean IfFromApplyingCommand;
ObservableList<Provider > ProviderList= FXCollections.observableArrayList();
    public static product getProductSelected() {
        return productSelected;
    }
    public static void UpdateLIst(product OldProduct , product productSelected) {

        //System.out.println("this is for the old  "+OldProduct .getBarcode() +"   "+ OldProduct .getProductName());
       // System.out.println("this is for the new  "+productSelected.getBarcode() +"   "+ productSelected.getProductName());

       // System.out.println("hello this is beforre");
        if (SendEmailController.IfTabPaneIsOpen){
         //   System.out.println("man    this is after");

            float percentage = (float)  (productSelected.getQuantity()*100) / productSelected.getInitialQuantity();
            ArrayList<product> productArrayList = new ArrayList<product>(SendEmailController.ProductList);
            Integer index = ListContainsElement(productArrayList,OldProduct );
            System.out.println("the index  "+index);

            if (index!=-1) {
                ModifyIfListContainsElement(productArrayList, index, productSelected);
                SendEmailController.ProductList.clear();
                SendEmailController.ProductList.addAll(productArrayList);
                SendEmailController.TempoListOfProducts.clear();
                SendEmailController.TempoListOfProducts.addAll(productArrayList);
                productArrayList.clear();
                index = ListContainsElement(SendEmailController.NeededProduct, OldProduct);
                if (index != -1)
                    if (percentage <= 20) {
                        productArrayList.addAll(SendEmailController.ProductList);
                        if (SendEmailMessageController.IfNeededProductBoxIsChecked) {
                            ModifyIfListContainsElement(productArrayList, index, productSelected);
                            ModifyIfListContainsElement(SendEmailController.NeededProduct, index, productSelected);
                        } else ModifyIfListContainsElement(SendEmailController.NeededProduct, index, productSelected);
                    }
            }
            productArrayList.clear();
productArrayList.addAll(SendEmailMessageController.ProductList);
                index = ListContainsElement(productArrayList,OldProduct );
                if (index!=-1)
                ModifyIfListContainsElement(productArrayList, index, productSelected);
                SendEmailMessageController.ProductList.clear();
            SendEmailMessageController.ProductList.addAll(productArrayList);
        }

    }
    public static void setProductSelected(product productSelected) {

        updateController.productSelected = productSelected;
    }
   static Integer ListContainsElement(ArrayList<product> theList , product TheProduct){
        if (theList.isEmpty())
            return -1;
        int i=0;
        for (product Product : theList){
            System.out.println(Product.getProductName());
            System.out.println(Product.getBarcode());
            if (Product.getBarcode().equals(TheProduct.getBarcode()))
                return i;
            else i++;
        }
        return -1;
    }
   static void ModifyIfListContainsElement(ArrayList<product> theList  , Integer index , product NewProduct){
        // int index =ListContainsElement(theList,Product);

        product Product = theList.get(index);
float percentage =  (float) (NewProduct.getQuantity()*100) / NewProduct.getInitialQuantity();
        int NeededProduct = Product.getNeededQuantity();

        NewProduct.setNeededQuantity(NeededProduct);
        NewProduct.setStockPercentage(percentage);
        theList.set(index,NewProduct);


    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        IfFromApplyingCommand=ApplyingCommandController.IfApplyingCommandIsOpen;
        if (ApplyingCommandController.IfApplyingCommandIsOpen) {
RequiredQuantity=ApplyingCommandController.RequireQuantity;
            ProvidersComboBox.setVisible(false);

        }
        else {

            ProvidersComboBox.setVisible(true);
            ProvidersComboBox.setItems(imProviderController.ProviderList);


        }

        productname.setText(productSelected.getProductName());
        barcode.setText((String.valueOf(productSelected.getBarcode())));
        sellprice.setText(String.valueOf(productSelected.getSellPrice()));
        buyprice.setText(String.valueOf(productSelected.getBuyPrice()));
        quantity.setText(String.valueOf(productSelected.getQuantity()));
        if (productSelected.getExpirationDate() == "") expirationdate.setValue(null); else expirationdate.setValue(LocalDate.parse(productSelected.getExpirationDate()));
OldProduct=productSelected;
System.out.println("1 test   "+OldProduct.getBarcode());
    }

    @FXML private void updateProduct(ActionEvent event) throws SQLException {
        if (errorCheck()) return;
        float totalfigure;
        //Need this later
        boolean barcodeChanged = Integer.parseInt(barcode.getText()) != productSelected.getBarcode();


        productSelected.setBarcode(Integer.parseInt(barcode.getText()));
        productSelected.setProductName(productname.getText());
        productSelected.setBuyPrice(Float.parseFloat(buyprice.getText()));
        productSelected.setSellPrice(Float.parseFloat(sellprice.getText()));
        productSelected.setQuantity(Integer.parseInt(quantity.getText()));
        if (expirationdate.getValue() == null) productSelected.setExpirationDate(""); else productSelected.setExpirationDate(expirationdate.getValue().toString());

NewProduct=productSelected;
System.out.println("2 test  "+NewProduct.getBarcode());
        //update product
        UpdateLIst(OldProduct,NewProduct);

        Connection connection = ConnectionClass.getConnection();
        String query = "Update stock set name=?, barcode=? , buyprice=? , sellprice=? , quantity=? ,expirationdate=? where barcode='"+productSelected.getBarcode()+"'" ;
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1,productname.getText());
        pst.setString(2,barcode.getText());
        pst.setString(3,buyprice.getText());
        pst.setString(4,String.valueOf(sellprice.getText()));
        pst.setString(5,String.valueOf(quantity.getText()));
        if (expirationdate.getValue() == null) {pst.setNull(6, Types.DATE);}
        else { pst.setString(6,expirationdate.getValue().toString());}
        pst.execute() ;

        if (barcodeChanged) {
            //Kayna erreur hnaya
            String sql = "Update stock set  barcode=? where name=? and sellprice=? and buyprice=? and quantity=? and expirationdate=?";
            PreparedStatement Pst = connection.prepareStatement(sql);
            Pst.setString(1, barcode.getText());
            Pst.setString(2, productname.getText());
            Pst.setString(3, sellprice.getText());
            Pst.setString(4, buyprice.getText());
            Pst.setString(5, quantity.getText());
            if (expirationdate.getValue() == null) { pst.setNull(6, Types.DATE); }
            else { pst.setString(6, expirationdate.getValue().toString()); }
            Pst.execute();
        }
        if (IfFromApplyingCommand){
            System.out.println("heloooooooooo apllllyyyiiinng");
            for (Provider provider : ApplyingCommandController.SelectedProvidersList){
                totalfigure   = ( ( provider.getTotalFigure() + (float) RequiredQuantity * Float.parseFloat( buyprice.getText()) ));
                System.out.println("in add scene  "+totalfigure);
                ApplyingCommandController.TheProvider.setTotalFigure(totalfigure);
               addController. UpdateProvider(ApplyingCommandController.TheProvider.getId(),totalfigure);

            }
            ApplyingCommandController.InitTable();
        }
        else{
            totalfigure   = ( ( ProvidersComboBox.getValue().getTotalFigure() +RequiredQuantity * Float.parseFloat( buyprice.getText()) ));
            ProvidersComboBox.getValue().setTotalFigure(totalfigure);
           addController. UpdateProvider(ProvidersComboBox.getValue().getId(), totalfigure);

        }

        imProviderController.InitTable();
    }




    private boolean errorCheck() throws SQLException {
//        String query = "SELECT * FROM stock where barcode= ?";
//        Connector connection = ConnectionClass.getConnection();
//        PreparedStatement preparedStatement = connection.prepareStatement(query);
//        preparedStatement.setString(1, barcode.getText());
//        ResultSet resultSet = preparedStatement.executeQuery();

        if (productname.getText().isEmpty() || barcode.getText().isEmpty() || sellprice.getText().isEmpty() || buyprice.getText().isEmpty() || quantity.getText().isEmpty() || (ProvidersComboBox.isVisible() && ProvidersComboBox.getValue()==null) ){
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText("Fill all the text fields");
            return true;
        }
        else if (!barcode.getText().matches("[0-9]*") || !quantity.getText().matches("[0-9]*") || !sellprice.getText().matches("[0-9]*\\.?[0-9]+") || !buyprice.getText().matches("[0-9]*\\.?[0-9]+")){
            errorLabel.setTextFill(Paint.valueOf("red"));
            errorLabel.setText("Some text fields must be numbers only");
            return true;
        }
//      else if (resultSet.next()){errorLabel.setText("The product "+productname.getText()+" already exists");
//            resetFields();
//            return false;}
        else {
            errorLabel.setTextFill(Paint.valueOf("green"));
            errorLabel.setText("product updated succefully");
            return false;
        }
    }
}
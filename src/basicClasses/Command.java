package basicClasses;

import java.util.ArrayList;
import java.util.Collection;

public class Command {
    Provider CommandProvider;
    ArrayList<product> ListOfProducts;
    String DateOfCommand;
    Integer SizeOfProduct;
    Integer Id,IdOfProvider;

    public Command(){

        ListOfProducts = new ArrayList<product>();
        DateOfCommand= null;
        Id = SizeOfProduct = null;
    }
    public Command (  product Product){
        //   ListOfProviders =new ArrayList<Provider>();
        ListOfProducts.add(Product);

    }
    public Command (Collection<product> theProductList){
        ListOfProducts.addAll(theProductList);

    }
    public  Command (Integer ID, String dateOfCommand ,Integer idOfProvider,Provider provider,Collection<product> theProductList ,Integer size){
        CommandProvider=provider;
        ListOfProducts = new ArrayList<product>();
        Id=ID;
        IdOfProvider=idOfProvider;
        DateOfCommand = dateOfCommand;
        ListOfProducts.addAll(theProductList);
        SizeOfProduct=size;

    }
    public  Command (Integer ID, String dateOfCommand ,Integer idOfProvider,Provider provider ,Integer size ){
        CommandProvider=provider;
        ListOfProducts = new ArrayList<product>();
        Id=ID;
        IdOfProvider=idOfProvider;
        DateOfCommand = dateOfCommand;
        SizeOfProduct=size;

    }

    public  Command (Integer ID, String dateOfCommand ,Integer idOfProvider,Provider provider ){
        CommandProvider=provider;
        ListOfProducts = new ArrayList<product>();
        Id=ID;
        IdOfProvider=idOfProvider;
        DateOfCommand = dateOfCommand;
    }
    public void addListProducts(product Product){
        ListOfProducts.add(Product);
    }



    public ArrayList<product> getListOfProducts () {
        return ListOfProducts;
    }

    public void setListOfProducts (ArrayList<product> listOfProducts) {
        ListOfProducts = listOfProducts;
    }

    public String getDateOfCommand () {
        return DateOfCommand;
    }

    public void setDateOfCommand (String dateOfCommand) {
        DateOfCommand = dateOfCommand;
    }

    public Integer getSizeOfProduct () {
        return SizeOfProduct;
    }

    public void setSizeOfProduct (Integer sizeOfProduct) {
        SizeOfProduct = sizeOfProduct;
    }

    public Integer getId () {
        return Id;
    }

    public void setId (Integer id) {
        Id = id;
    }

    public Provider getCommandProvider () {
        return CommandProvider;
    }

    public void setCommandProvider (Provider commandProvider) {
        CommandProvider = commandProvider;
    }

    public Integer getIdOfProvider () {
        return IdOfProvider;
    }

    public void setIdOfProvider (Integer idOfProvider) {
        IdOfProvider = idOfProvider;
    }
}


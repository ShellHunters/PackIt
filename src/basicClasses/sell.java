package basicClasses;

import java.sql.*;

import Connector.ConnectionClass;
import interfaceMagazinier.stock.imStockController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class sell {
    private List<product> soldProducts;
    private int totalPrice = 0;
    private LocalDateTime sellTime;

     List<product> getSoldProducts() { return soldProducts; }

    public void setSoldProducts(List<product> soldProducts) { this.soldProducts = soldProducts; }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getSellTime() {
        return sellTime;
    }

    public void setSellTime(LocalDateTime sellTime) {
        this.sellTime = sellTime;
    }

    public sell(List<product> soldProducts){
        this.soldProducts = soldProducts;
        soldProducts.forEach(product -> {
            totalPrice += product.getSellPrice() * product.getQuantity();
        });
        sellTime  = LocalDateTime.now();
    }

     public void pushSell() throws SQLException {
         Connection connection = ConnectionClass.getConnection();

         //Incrementation of NumberOfSells in database of each product and decrementing quantity
         this.soldProducts.forEach(product -> {

             //Get the original quantity
             //REMARQUE : We had to get the new quantity because in sells the quantity field is not the same as the quantity field in stock (one is quantity in stock and the other is the quantity of sold products)
             //reminder nassim : if u have time add a soldQUantity field in the product class and adjust this function
             AtomicInteger newQuantity = new AtomicInteger();
             imStockController.products.forEach(originalProduct -> {
                 if (originalProduct.getBarcode() == product.getBarcode()){
                     newQuantity.set(originalProduct.getQuantity() - product.getQuantity());
                     return;
                 }
             });

            //changing the quantity and the number of sells
             //REMARQUE : Number of sells is correct because we used the second constructor (which includes the number of sells) to add the products to the list.
            String query = "UPDATE stock SET numberOfSells=?, quantity=? WHERE barcode=" + product.getBarcode();
            PreparedStatement numberOfSellsStatement;
            try {
                numberOfSellsStatement = connection.prepareStatement(query);
                numberOfSellsStatement.setInt(1, product.getNumberOfSells() + product.getQuantity());
                numberOfSellsStatement.setInt(2, newQuantity.get());
                numberOfSellsStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        //GetLast ID Sell
        String idQuery = "SELECT * FROM sells WHERE id = ( SELECT MAX( id ) FROM sells )";
        Statement idStatement = connection.createStatement();
        ResultSet rs = idStatement.executeQuery(idQuery);

        int id = 0;
        if (rs.next()){
            id = rs.getInt("id") + 1;
        }

        System.out.println(id);

        String query = "INSERT INTO sells(id, productCode, quantity, sellTime, sellPrice, profit) VALUES (?, ?, ?, ?, ?, ?)";
        int finalId = id;
        soldProducts.forEach(product -> {
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, String.valueOf(finalId));
                ps.setString(2, String.valueOf(product.getBarcode()));
                ps.setString(3, String.valueOf(product.getQuantity()));
                ps.setString(4, LocalDateTime.now().toString());
                ps.setString(5, String.valueOf(product.getSellPrice()));
                ps.setString(6, String.valueOf(product.getSellPrice() - product.getBuyPrice()));
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        connection.close();
    }
}
package basicClasses;

import java.sql.*;

import Connector.ConnectionClass;
import interfaceMagazinier.stock.imStockController;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class sell {
    private List<product> soldProducts;
    private SimpleFloatProperty totalPrice;
    private SimpleFloatProperty totalProfit;
    private LocalDateTime sellTime;
    private SimpleIntegerProperty id;
    private float discountAmount;
    private int clientID;

    public List<product> getSoldProducts() { return soldProducts; }

    public void setSoldProducts(List<product> soldProducts) { this.soldProducts = soldProducts; }


    public LocalDateTime getSellTime() {
        return sellTime;
    }

    public void setSellTime(LocalDateTime sellTime) {
        this.sellTime = sellTime;
    }
    public sell () {
        this.totalPrice= new SimpleFloatProperty(0);
        this.totalProfit =new SimpleFloatProperty(0);
        soldProducts  = FXCollections.observableArrayList();
    }

    public sell (int id){
        this.id = new SimpleIntegerProperty(id);
        this.soldProducts = FXCollections.observableArrayList();
        this.totalPrice = new SimpleFloatProperty(0);
        this.totalProfit = new SimpleFloatProperty(0);
    }

    public sell(List<product> soldProducts){
        AtomicReference<Float> total = new AtomicReference<>((float) 0);
        AtomicReference<Float> profit = new AtomicReference<>((float) 0);
        this.soldProducts = soldProducts;
        soldProducts.forEach(product -> {
            total.updateAndGet(v -> new Float((float) (v + product.getSellPrice() * product.getQuantity())));
            profit.updateAndGet(v -> new Float((float) (v + (product.getSellPrice() - product.getBuyPrice()) * product.getQuantity())));
        });

        this.totalProfit = new SimpleFloatProperty(total.get());
        this.totalProfit = new SimpleFloatProperty(profit.get());

        sellTime  = LocalDateTime.now();
    }

    public void addProduct(product product) {
        AtomicBoolean b = new AtomicBoolean(true);
        this.totalPrice = new SimpleFloatProperty(this.totalPrice.get() + product.getSellPrice());
        this.totalProfit = new SimpleFloatProperty(this.totalProfit.get() + product.getSellPrice() - product.getBuyPrice());
        this.soldProducts.forEach(product1 -> {
            if (product1.getBarcode() == product.getBarcode()){
                product1.setQuantity(product1.getQuantity() + 1);
                b.set(false);
                return;
            }
        });
        if (b.get()) this.soldProducts.add(product);
    }



    public void removeProduct(Collection<product> products) {
        products.forEach(product -> {
            this.totalPrice = new SimpleFloatProperty(this.totalPrice.get() - product.getSellPrice() * product.getQuantity());
            this.totalProfit = new SimpleFloatProperty(this.totalProfit.get() - ( product.getSellPrice() - product.getBuyPrice()) * product.getQuantity());
        });
        this.soldProducts.removeAll(products);
    }

     public void pushSell() throws SQLException {
        if (discountAmount != 0) {
            product discount = new product();

            discount.setProductName("discount");
            discount.setQuantity(1);
            discount.setSellPrice(-1 * discountAmount);
            discount.setBuyPrice(0);
            this.soldProducts.add(discount);
        }

         Connection connection = ConnectionClass.getConnection();

         //Incrementation of NumberOfSells in database of each product and decrementing quantity
         this.soldProducts.forEach(product -> {
            if (product.getProductName().equals("discount")) return;

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
            // REMARQUE : Number of sells is correct because we used the second constructor (which includes the number of sells) to add the products to the list.
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

        String query = "INSERT INTO sells(id, productCode, quantity, sellTime, sellPrice, profit, clientid) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int finalId = id;
        String sellTime = LocalDateTime.now().toString();
        soldProducts.forEach(product -> {
            try {
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, String.valueOf(finalId));
                ps.setString(2, String.valueOf(product.getBarcode()));
                ps.setString(3, String.valueOf(product.getQuantity()));
                ps.setString(4, sellTime);
                ps.setString(5, String.valueOf(product.getSellPrice()));
                ps.setString(6, String.valueOf((product.getSellPrice() - product.getBuyPrice()) * product.getQuantity()));
                ps.setString(7, String.valueOf(clientID));
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        connection.close();
    }

    public float getTotalPrice() {
        return totalPrice.get();
    }

    public SimpleFloatProperty totalPriceProperty() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice.set(totalPrice);
    }

    public float getTotalProfit() {
        return totalProfit.get();
    }

    public SimpleFloatProperty totalProfitProperty() {
        return totalProfit;
    }

    public void setTotalProfit(float totalProfit) {
        this.totalProfit.set(totalProfit);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setDiscountAmount(float discountAmount) {
        this.discountAmount = discountAmount;
    }

    public float getDiscountAmount(){
        return this.discountAmount;
    }


    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }
}
package basicClasses;

import java.sql.Connection;
import Connection.ConnectionClass;
import javafx.collections.transformation.SortedList;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        soldProducts.forEach(product -> totalPrice += product.getSellPrice() * product.getQuantity());
        sellTime  = LocalDateTime.now();
    }

    public static void pushSell(sell newSell) throws SQLException {
        Connection connection = ConnectionClass.getConnection();
        Statement statement = connection.createStatement();
        String query = "INSERT INTO sells(sellDate, amount) VALUES ('" + newSell.getSellTime() + "', '" + newSell.getTotalPrice() + "')";
        //ADD THE PART WHERE U CHANgE THE PRODUCT STATS
        statement.execute(query);
        connection.close();
    }
}

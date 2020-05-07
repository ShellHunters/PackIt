package basicClasses;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;

public class sell {
    class soldProduct{
        product product;
        int quantitySold;

        public soldProduct(product product, int quantitySold){
            this.product = product;
            this.quantitySold = quantitySold;
        }
    }

    private ArrayList<soldProduct> soldProducts;
    private int totalPrice = 0;
    private LocalDateTime sellTime;

    public ArrayList<soldProduct> getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(ArrayList<soldProduct> soldProducts) {
        this.soldProducts = soldProducts;
    }

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

    public sell(ArrayList<soldProduct> soldProducts){
        this.soldProducts = soldProducts;
        soldProducts.forEach(soldProduct -> totalPrice += soldProduct.product.getSellPrice() * soldProduct.quantitySold); //Calculate final price
        sellTime = LocalDateTime.now(); //When did the sell created
    }
}

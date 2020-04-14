package basicClasses;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;
import sun.java2d.pipe.SpanShapeRenderer;

import java.time.LocalDate;
import java.util.Date;

public class product extends RecursiveTreeObject<product> {
    private SimpleStringProperty productName;
    private SimpleIntegerProperty barcode;
    private SimpleFloatProperty buyPrice, sellPrice;
    private SimpleIntegerProperty quantity;
    private ObjectProperty<LocalDate> expirationDate;

    public product(String productName, int barcode, float buyPrice, float sellPrice, int quantity, LocalDate expirationDate) {
        this.productName = new SimpleStringProperty(productName);
        this.barcode = new SimpleIntegerProperty(barcode);
        this.buyPrice = new SimpleFloatProperty(buyPrice);
        this.sellPrice = new SimpleFloatProperty(sellPrice);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.expirationDate = new ObjectPropertyBase<LocalDate>() { //i don t know wtf is this just testing
            @Override
            public Object getBean() {
                return expirationDate;
            }

            @Override
            public String getName() {
                return null;
            }
        };
    }

    public String getProductName() {
        return productName.get();
    }

    public SimpleStringProperty productNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public int getBarcode() {
        return barcode.get();
    }

    public SimpleIntegerProperty barcodeProperty() {
        return barcode;
    }

    public void setBarcode(int barcode) {
        this.barcode.set(barcode);
    }

    public float getBuyPrice() {
        return buyPrice.get();
    }

    public SimpleFloatProperty buyPriceProperty() {
        return buyPrice;
    }

    public void setBuyPrice(float buyPrice) {
        this.buyPrice.set(buyPrice);
    }

    public float getSellPrice() {
        return sellPrice.get();
    }

    public SimpleFloatProperty sellPriceProperty() {
        return sellPrice;
    }

    public void setSellPrice(float sellPrice) {
        this.sellPrice.set(sellPrice);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public LocalDate getExpirationDate() {
        return expirationDate.get();
    }

    public ObjectProperty<LocalDate> expirationDateProperty() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate.set(expirationDate);
    }
}
package interfaceFournisseur;

public class tableItem {
    private String product;
    private int quantity;
    private boolean getit;
    private Integer idOfCommand;
    public tableItem(String product,int quantity ){
        this.product=product;
        this.quantity=quantity;
    }

    public tableItem(String product,int quantity ,Integer IdOfCommand){
        this.product=product;
        this.quantity=quantity;
        this.idOfCommand=IdOfCommand;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isGetit () {
        return getit;
    }

    public void setGetit (boolean getit) {
        this.getit = getit;
    }

    public Integer getIdOfCommand () {
        return idOfCommand;
    }

    public void setIdOfCommand (Integer idOfCommand) {
        this.idOfCommand = idOfCommand;
    }
}

package helpfulClasses.models;

public class ProductInTable {

    Integer product_id;
    String name;
    int quantity;
    Integer price;
    Integer totalPrice;

    public ProductInTable(String name, int quantity, Integer price){
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
    public ProductInTable(String name, Integer price){
        this.name = name;
        this.quantity = 1;
        this.price = price;
    }
    public ProductInTable(){
        this.quantity = 1;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }
}

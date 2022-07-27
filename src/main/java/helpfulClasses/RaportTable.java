package helpfulClasses;

public class RaportTable {
    private String name;
    private String price;
    private int quantity;
    private String totPrice;

    public RaportTable(String name, String price, int quantity, String totPrice) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.totPrice = totPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTotPrice() {
        return totPrice;
    }

    public void setTotPrice(String totPrice) {
        this.totPrice = totPrice;
    }
}

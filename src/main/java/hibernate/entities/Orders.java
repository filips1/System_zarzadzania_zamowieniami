package hibernate.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Entity
public class Orders {

    public enum Status{
        TODO, COOKING, READY
    }
    public Orders(){}
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer order_id;
    private Integer total_price;
    private Date date;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "Order_Product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Products> products = new HashSet<>();

    public Orders(Integer total_price, Date date, Status status, Users user) {
        this.total_price = total_price;
        this.date = date;
        this.status = status.name();
        this.user = user;
    }

    public Orders(Integer total_price, Date date, Status status, Users user, Set<Products> products) {
        this.total_price = total_price;
        this.date = date;
        this.status = status.name();
        this.user = user;
        this.products = products;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public Integer getTotal_price() {
        return total_price;
    }

    public void setTotal_price(Integer total_price) {
        this.total_price = total_price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status.name();
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Set<Products> getProducts() {
        return products;
    }

    public void setProducts(Set<Products> products) {
        this.products = products;
    }
}

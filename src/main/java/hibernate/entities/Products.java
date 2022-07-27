package hibernate.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@Entity
public class Products {
    public Products(){

    }
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer product_id;
    private String name;
    private Integer price;
    private Boolean status = true;
    @ManyToMany(mappedBy = "products")
    private Set<Orders> products = new HashSet<>();
    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "Product_Ingredient",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private Set<Ingredients> ingredients = new HashSet<>();

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Set<Orders> getProducts() {
        return products;
    }

    public void setProducts(Set<Orders> products) {
        this.products = products;
    }

    public Set<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public Products(String name, Integer price) {
    this.name = name;
    this.price = price;
    }

}

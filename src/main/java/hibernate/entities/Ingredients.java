package hibernate.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@Entity
public class Ingredients {

    public Ingredients(){}
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer ingredient_id;
    private String name;
    private String availability;

    @ManyToMany(mappedBy = "ingredients")
    private Set<Products> posts = new HashSet<>();


    public Ingredients(String name, String availability) {
        this.name = name;
        this.availability = availability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public Integer getIngredient_id() {
        return ingredient_id;
    }

    public void setIngredient_id(Integer ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public Set<Products> getPosts() {
        return posts;
    }

    public void setPosts(Set<Products> posts) {
        this.posts = posts;
    }
}

package helpfulClasses;

import java.util.Set;

public class IngredientstoString {
    private String name;
    private int price;
    private String ingredients;
    private String status;

    public IngredientstoString(String name, int price, String ingredients, Boolean status) {
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        if (status) {
            this.status = "Available";
        } else {
            this.status = "Unavailable";
        }

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}

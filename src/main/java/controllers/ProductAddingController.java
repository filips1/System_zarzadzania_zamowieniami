package controllers;

import hibernate.dao.CrudOperations;
import hibernate.entities.Ingredients;
import hibernate.entities.Products;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.*;

public class ProductAddingController {

    String name;

    @FXML
    private ListView<String> ingredients;
    @FXML
    private ListView<String> anothertable;
    @FXML
    private TextField productName;
    @FXML
    private TextField productPrice;
    @FXML
    private Button closebutton;
    @FXML
    private Button add_button;
    @FXML
    private Button update_button;
    @FXML
    private ComboBox<String> statusc;

    @FXML
    public void initialize() {

        ObservableList<String> productStatus = FXCollections.observableArrayList("Available", "Unavailable");
        statusc.setItems(productStatus);
        ingredients.setStyle("-fx-font-size: 1.5em ;");
        CrudOperations<Ingredients> ingredient = new CrudOperations<>();
        List a=ingredient.get("Ingredients");
        for (Iterator iterator = a.iterator(); iterator.hasNext(); ) {

            Ingredients addons = (Ingredients) iterator.next();
            ingredients.getItems().add(addons.getName());
        }
        ingredients.setCellFactory(param -> new ListCell<String>() {
            private ImageView imageView = new ImageView();
            @Override
            public void updateItem(String name, boolean empty) {
                super.updateItem(name, empty);

                setText(name);
                setGraphic(imageView);

            }
        });
        ingredient.stop();
    }
    public void myFunction(String name, String price) {
        productName.setText(name);
        productPrice.setText(price);
        this.name = name;
    }

    public void visiblebuttons(boolean state) {
        if (state) {
            add_button.setVisible(false);
        } else {
            update_button.setVisible(false);
        }
    }
    public void setIng(String ing, String status) {
        ing = ing.replaceAll("\\s+","");
        String napis[] = ing.split("-");
        CrudOperations<Ingredients> ingredientsCrudOperations = new CrudOperations<>();
        List in = ingredientsCrudOperations.get("Ingredients");
        for (Iterator iterator = in.iterator();iterator.hasNext();) {
            Ingredients ingredients2 = (Ingredients) iterator.next();
            for (int i=1;i<napis.length;i++) {
                if (ingredients2.getName().equals(napis[i])) {
                    anothertable.getItems().add(ingredients2.getName());
                    ingredients.getItems().remove(napis[i]);
                }
            }
        }
        statusc.setPromptText(status);
        ingredientsCrudOperations.stop();
    }
    @FXML
    public void updateproduct() {
        CrudOperations<Products> products = new CrudOperations<>();
        CrudOperations<Ingredients> i = new CrudOperations<>();
        List a = products.get("Products");
        List<String> ingredients_table = anothertable.getItems();
        Set<Ingredients> ingr = new HashSet<>();
        for (Iterator iterator = a.iterator(); iterator.hasNext(); ) {

            Products prod = (Products) iterator.next();
            if (prod.getName().equals(name)){
                prod.setName(productName.getText());
                prod.setPrice(Integer.parseInt(productPrice.getText()));
                List b = i.get("Ingredients");
                for (String it : ingredients_table) {
                    for (Iterator iterator1 = b.iterator();iterator1.hasNext();) {
                        Ingredients ing = (Ingredients) iterator1.next();
                        if (ing.getName().equals(it)) {
                            ingr.add(ing);
                        }
                    }
                }
                if (statusc.getSelectionModel().equals("Available")) {
                    prod.setStatus(true);
                } else {
                    prod.setStatus(false);
                }
                prod.setIngredients(ingr);
                products.update(prod);
            }
        }
        products.stop();
        i.stop();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Product " + name + " was updated");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        alert.showAndWait();

        Stage stage = (Stage) closebutton.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void addIngredient() {
        String a = ingredients.getSelectionModel().getSelectedItem();
        CrudOperations<Ingredients> i = new CrudOperations<>();
        List b=i.get("Ingredients");
        for (Iterator iterator = b.iterator(); iterator.hasNext(); ) {
            Ingredients ing = (Ingredients) iterator.next();
            if(ing.getName().equals(a)){
                //TO sie usuwa z listy i dodac do nowej
                ingredients.getItems().remove(a);
                anothertable.getItems().add(a);
            }
        }
        i.stop();
    }
    @FXML
    public void removeingredient() {
        String a = anothertable.getSelectionModel().getSelectedItem();
        anothertable.getItems().remove(a);
        ingredients.getItems().add(a);
    }
    @FXML
    public void createproduct() {
        String name = productName.getText();
        String price = productPrice.getText();
        List<String> ingredients_table = anothertable.getItems();
        CrudOperations<Products> i = new CrudOperations<>();
        Products product = new Products(name,Integer.parseInt(price));
        Set<Ingredients> ingr = new HashSet<>();
        List b = i.get("Ingredients");
        for (String it : ingredients_table) {
            for (Iterator iterator = b.iterator();iterator.hasNext();) {
                Ingredients ing = (Ingredients) iterator.next();
                if (ing.getName().equals(it)) {
                    ingr.add(ing);
                }
            }
        }
        product.setIngredients(ingr);
        i.create(product);
        i.stop();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Product " + name + " was added");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        alert.showAndWait();

        productName.setText("");
        productPrice.setText("");
        anothertable.getItems().clear();
        ingredients.getItems().clear();
        initialize();
    }
    @FXML
    public void leave(ActionEvent actionEvent) {
        Stage stage = (Stage) closebutton.getScene().getWindow();
        stage.close();
    }
}

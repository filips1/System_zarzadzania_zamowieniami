package controllers;


import com.itextpdf.text.Anchor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import helpfulClasses.helpfulMethods;
import hibernate.dao.CrudOperations;
import hibernate.entities.Ingredients;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;


public class IngredientAddingController {

    @FXML
    private Label resultLabel;
    @FXML
    private TextField ingredientName;
    @FXML
    private javafx.scene.control.Button leaveButton;
    @FXML
    private ComboBox<String> ingredientAvailability;

    ObservableList<String> ingObservableList = FXCollections.observableArrayList("Available", "Unavailable");

    @FXML
    AnchorPane anchorPane;

    @FXML
    public void initialize() {
        ingredientAvailability.setItems(ingObservableList);
    }

    @FXML
    public void leaveIngAdding() {
        Stage stage = (Stage) leaveButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void addIngredient() {
        if (!ingredientAvailability.getSelectionModel().isEmpty() && !ingredientName.getText().equals("") && ingredientName.getText() != null) {
            String name = ingredientName.getText();
            CrudOperations<Ingredients> ingredients = new CrudOperations<>();
            List iList = ingredients.get("Ingredients");
            boolean exists = false;
            for (Iterator iterator = iList.iterator(); iterator.hasNext(); ) {
                Ingredients ingredient = (Ingredients) iterator.next();

                if (ingredient.getName().equals(name)) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                resultLabel.setText("Ingredient with that name already exists!");
                resultLabel.setTextFill(Color.RED);
            } else {
                Ingredients ingr = new Ingredients(name, ingredientAvailability.getSelectionModel().getSelectedItem());
                ingredients.create(ingr);
                resultLabel.setText("Added successfully!");
                resultLabel.setTextFill(Color.GREEN);
            }
            ingredients.stop();
        } else {
            resultLabel.setText("One of the fields was not filled!");
            resultLabel.setTextFill(Color.RED);
        }
    }

    @FXML
    public void clickOnCloseButton(){
        Stage stage = (Stage)anchorPane.getScene().getWindow();
        stage.close();
    }
}

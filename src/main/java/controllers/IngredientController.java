package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class IngredientController {

    @FXML
    AnchorPane anchorPane;

    @FXML
    public void clickOnCloseButton() throws IOException {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }
}

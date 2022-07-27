package controllers;

import helpfulClasses.helpfulMethods;
import hibernate.dao.CrudOperations;
import hibernate.entities.Roles;
import hibernate.entities.Users;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.InputEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/*
    @author Daniel Partyka
    @author Rafał Sudoł
    @author Dariusz Słabicki
    @author Krystian Wolański
    @author Filip Szpunar
*/
public class LoginController {

    public static String CURRENT_USER_LOGIN;
    public static Users CURRENT_USER;

    @FXML
    TextField logowanie;

    @FXML
    PasswordField haslo;

    @FXML
    Label checkdata;


    //Metoda, ktora wylacza aplikacje
    @FXML
    void clickOnCloseButton(InputEvent event) {
        Platform.exit();

    }
    //Metoda, ktora odpowiada za panel logowania i autoryzacje
    @FXML
    void login() {
        Parent root = null;
        String id_login = logowanie.getText();
        String password = haslo.getText();

        CrudOperations<Users> usersCrudOperations = new CrudOperations<>();
        List<Users> users = usersCrudOperations.getItems(Users.class);
        usersCrudOperations.stop();

        Users loggedUser = null;

        try {
            for (Users user : users) {
                if (id_login.equals(user.getLogin()) && password.equals(user.getPassword())) {
                    loggedUser = user;
                    CURRENT_USER = loggedUser;
                    String role = user.getRole().getName();
                    if (role.equals("Kucharz")) {
                        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/CookPanel.fxml"));
                    } else if (role.equals("Kierownik")) {
                        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/AdminProductPanel.fxml"));
                    } else {
                        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/OrderPanel.fxml"));
                    }
                    loggedUser = new Users();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (loggedUser != null) {

                Stage stage = new Stage();
                Scene scene = new Scene(root);
                helpfulMethods.addDragging(stage, scene);
                helpfulMethods.setStageUndecorated(stage);
                stage.setTitle("New stage");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username of password");
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(
                        getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
                dialogPane.getStyleClass().add("myDialog");
                alert.showAndWait();
                loggedUser = null;
                logowanie.setText("");
                haslo.setText("");
            }
        }
    }



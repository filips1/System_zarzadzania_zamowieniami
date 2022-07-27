package controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import helpfulClasses.IngredientstoString;
import helpfulClasses.RaportTable;
import helpfulClasses.helpfulMethods;
import hibernate.dao.CrudOperations;

import hibernate.entities.Ingredients;
import hibernate.entities.Products;
import hibernate.entities.Roles;
import hibernate.entities.Users;

import hibernate.entities.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.AnchorPane;

import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class ProductController {

    @FXML
    private TableView<IngredientstoString> productable;
    @FXML
    private TableColumn<IngredientstoString, String> column1;
    @FXML
    private TableColumn<IngredientstoString, Integer> column2;
    @FXML
    private TableColumn<IngredientstoString, String> column3;
    @FXML
    private TableColumn<IngredientstoString, String> column4;
    @FXML
    private Label productName;
    @FXML
    private Label productPrice;
    @FXML
    private Label ing;
    @FXML
    private Label status;


    @FXML
    private Button addUser;
    @FXML
    private Button editUser;
    @FXML
    private Button returnUser;
    @FXML
    private TextField AccName;
    @FXML
    private TextField AccPESEL;
    @FXML
    private TextField ACCPhone;
    @FXML
    private TextField AccEmail;
    @FXML
    private TextField AccLog;
    @FXML
    private TextField AccPass;
    @FXML
    private ComboBox<String> AccPos;
    @FXML
    private ListView<String> AccountsList;
    private Integer AccID;


    @FXML
    private TableView<RaportTable> raportTable;
    @FXML
    private TableColumn<String, RaportTable> prodName;
    @FXML
    private TableColumn<String, RaportTable> prodPrice;
    @FXML
    private TableColumn<String, RaportTable> prodQuant;
    @FXML
    private TableColumn<String, RaportTable> prodTotPrice;

    @FXML
    private ListView<String> ordersList;
    @FXML
    private DatePicker dataPicker;

    @FXML AnchorPane anchorPane;
    @FXML Label userlabel;


    @FXML
    void updateproduct() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxmls/AdminProductAdding.fxml"));
        Parent root = (Parent) loader.load();
        ProductAddingController productAddingController = loader.getController();
        productAddingController.myFunction(productName.getText(), productPrice.getText());
        productAddingController.setIng(ing.getText(),status.getText());
        productAddingController.visiblebuttons(true);
        Stage stage = new Stage();
        stage.setTitle("Product Update");
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        helpfulMethods.setStageUndecorated(stage);
        helpfulMethods.addDragging(stage,scene);
        stage.showAndWait();
        refresh();
    }

    @FXML
    void deleteproduct() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to remove this product?");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        Optional<ButtonType> result = alert.showAndWait();
        String a = productName.getText();
        if(result.get() == ButtonType.OK) {
            CrudOperations<Products> products = new CrudOperations<>();
            List p = products.get("Products");
            for (Iterator iterator = p.iterator(); iterator.hasNext(); ) {
                Products prod = (Products) iterator.next();
                if (prod.getName().equals(a)) {
                    prod.setStatus(false);
                    products.update(prod);
                }
            }
            refresh();
            products.stop();
        }
    }

    @FXML
    void addProductOnClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxmls/AdminProductAdding.fxml"));
        Parent root = (Parent) loader.load();
        ProductAddingController productAddingController = loader.getController();
        productAddingController.visiblebuttons(false);
        Stage stage = new Stage();
        stage.setTitle("Product Adding");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.showAndWait();
        ObservableList<IngredientstoString> olist = FXCollections.observableArrayList();
        productable.setItems(olist);
        refresh();
    }

    public void refresh() {

        CrudOperations<Products> product = new CrudOperations<>();
        ObservableList<IngredientstoString> olist = FXCollections.observableArrayList();
        List prod = product.get("Products");
        column1.setCellValueFactory(new PropertyValueFactory<>("name"));
        column2.setCellValueFactory(new PropertyValueFactory<>("price"));
        column3.setCellValueFactory(new PropertyValueFactory<>("ingredients"));

        prodName.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        prodQuant.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        prodTotPrice.setCellValueFactory(new PropertyValueFactory<>("totPrice"));


        for (Iterator iterator = prod.iterator(); iterator.hasNext(); ) {

            Products products = (Products) iterator.next();
            Set<Ingredients> ingredients = products.getIngredients();
            String ingredientsstring = "";
            for (Iterator iterator1 = ingredients.iterator(); iterator1.hasNext(); ) {
                Ingredients ingredients1 = (Ingredients) iterator1.next();
                ingredientsstring += "-" + ingredients1.getName() + " ";
            }
            IngredientstoString its = new IngredientstoString(products.getName(), products.getPrice(), ingredientsstring, products.getStatus());
            olist.add(its);
        }
        productable.setItems(olist);

        productable.getSelectionModel().selectedItemProperty().addListener((observableValue, products, selectedRow) -> {
            if (selectedRow != null) {
                productName.setText(selectedRow.getName());
                productPrice.setText(Integer.toString(selectedRow.getPrice()));
                ing.setText(selectedRow.getIngredients());
                status.setText(selectedRow.getStatus());
            }
        });
        product.stop();

    }

    @FXML
    public void initialize() {
        editUser.setVisible(false);
        returnUser.setVisible(false);
        addUser.setVisible(true);

        userlabel.setText(LoginController.CURRENT_USER.login.toString());
        CrudOperations<Products> product = new CrudOperations<>();
        CrudOperations<Orders> orders_product = new CrudOperations<>();
        ObservableList<IngredientstoString> olist = FXCollections.observableArrayList();
        List prod = product.get("Products");

        column1.setCellValueFactory(new PropertyValueFactory<>("name"));
        column2.setCellValueFactory(new PropertyValueFactory<>("price"));
        column3.setCellValueFactory(new PropertyValueFactory<>("ingredients"));
        column4.setCellValueFactory(new PropertyValueFactory<>("status"));

        prodName.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        prodQuant.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        prodTotPrice.setCellValueFactory(new PropertyValueFactory<>("totPrice"));


        for (Iterator iterator = prod.iterator(); iterator.hasNext(); ) {

            Products products = (Products) iterator.next();
            Set<Ingredients> ingredients = products.getIngredients();
            String ingredientsstring = "";
            for (Iterator iterator1 = ingredients.iterator(); iterator1.hasNext(); ) {
                Ingredients ingredients1 = (Ingredients) iterator1.next();
                ingredientsstring += "-" + ingredients1.getName() + " ";
            }

            IngredientstoString its = new IngredientstoString(products.getName(), products.getPrice(), ingredientsstring, products.getStatus());
            olist.add(its);
        }
        productable.setItems(olist);

        productable.getSelectionModel().selectedItemProperty().addListener((observableValue, products, selectedRow) -> {
            if (selectedRow != null) {
                productName.setText(selectedRow.getName());
                productPrice.setText(Integer.toString(selectedRow.getPrice()));
                ing.setText(selectedRow.getIngredients());
                status.setText(selectedRow.getStatus());
            }
        });

        ordersList.getSelectionModel().selectedItemProperty().addListener((observableValue, products, selected) -> {
            ObservableList<RaportTable> oList = FXCollections.observableArrayList();
            char[] charArray = selected.toCharArray();
            CrudOperations<Orders> orders = new CrudOperations<>();
            Orders order = orders.getItem(Orders.class, Integer.parseInt(String.valueOf(charArray[charArray.length - 1])));
            Products[] productsTab = new Products[1];
            int[] numberOfProductsTab = new int[1];
            boolean go = true;
            Set<Products> setOfProducts = order.getProducts();
            for (Iterator iterator = setOfProducts.iterator(); iterator.hasNext(); ) {
                boolean notexists = true;
                Products products1 = (Products) iterator.next();
                if (go) {
                    productsTab[0] = products1;
                    numberOfProductsTab[0] = 1;
                    go = false;
                } else {
                    for (int i = 0; i < productsTab.length; i++) {
                        if (products1.getName().equals(productsTab[i].getName())) {
                            numberOfProductsTab[i]++;
                            notexists = false;
                        }
                    }
                    if (notexists) {
                        Products[] tempProductsTab = new Products[productsTab.length + 1];
                        int[] tempIngredientsNumberTab = new int[numberOfProductsTab.length + 1];

                        System.arraycopy(productsTab, 0, tempProductsTab, 0, productsTab.length);
                        System.arraycopy(numberOfProductsTab, 0, tempIngredientsNumberTab, 0, productsTab.length);

                        tempIngredientsNumberTab[tempIngredientsNumberTab.length - 1] = 1;
                        tempProductsTab[tempProductsTab.length - 1] = products1;

                        productsTab = tempProductsTab;
                        numberOfProductsTab = tempIngredientsNumberTab;
                    }
                }

            }
            for (int i = 0; i < productsTab.length; i++) {
                RaportTable tableElem = new RaportTable(productsTab[i].getName(), productsTab[i].getPrice() + " zł",
                        numberOfProductsTab[i], numberOfProductsTab[i] * productsTab[i].getPrice() + " zł");
                oList.add(tableElem);
            }
            orders.stop();
            raportTable.setItems(oList);
        });

        product.stop();
        InitializeLogin();
    }


    public void InitializeLogin() {


        AccountsList.setStyle("-fx-font-size: 1.5em ;");

        CrudOperations<Users> user = new CrudOperations<>();
        List a = user.get("Users");
        for (Iterator iterator = a.iterator(); iterator.hasNext(); ) {

            Users users = (Users) iterator.next();
            AccountsList.getItems().add(users.getName()+" ("+users.getRole().getName()+" )");

        }

        AccountsList.setCellFactory(param -> new ListCell<String>() {
            private ImageView imageView = new ImageView();

            @Override
            public void updateItem(String name, boolean empty){
                super.updateItem(name, empty);
                setText(name);
                setGraphic(imageView);
            }
        });
        List b = user.get("Roles");
        for (Iterator iterator = b.iterator(); iterator.hasNext(); ) {
            Roles roles = (Roles) iterator.next();
            AccPos.getItems().add(roles.getName());

        }
        user.stop();

    }


    @FXML
    public void remove() {
        if (AccountsList.getSelectionModel().isEmpty()){
            return;
        }
        else {
            String a = AccountsList.getSelectionModel().getSelectedItem().substring(0, AccountsList.getSelectionModel().getSelectedItem().indexOf("(") - 1);
            System.out.println(AccountsList.getSelectionModel().getSelectedItem().substring(0, AccountsList.getSelectionModel().getSelectedItem().indexOf("(") - 1));
            CrudOperations<Users> user = new CrudOperations<>();

            List b = user.get("Users");
            for (Iterator iterator = b.iterator(); iterator.hasNext(); ) {
                Users users = (Users) iterator.next();
                if (users.getName().equals(a)) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
                    dialogPane.getStyleClass().add("myDialog");
                    alert.setTitle("Confirmation Dialog");
                    alert.setHeaderText("Are you sure?");
                    alert.setContentText("Are you really ok with this?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        user.delete(users);
                        // ... user chose OK
                        Alert news = new Alert(Alert.AlertType.INFORMATION);
                        DialogPane dialogPane1 = news.getDialogPane();
                        dialogPane1.getStylesheets().add(
                                getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
                        dialogPane1.getStyleClass().add("myDialog");
                        news.setTitle("Success");
                        news.setHeaderText(a + " has been removed");
                        news.setContentText("Congratulations user " + a + " has been removed");
                        news.showAndWait();

                    } else {
                        return;
                        // ... user chose CANCEL or closed the dialog
                    }

                }
            }

            leavelogin();
            InitializeLogin();
            user.stop();
        }
    }
    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
    public Boolean validate() {
        if (isValid(AccEmail.getText())) {
            if (ACCPhone.getText().matches("^[0-9]*$")) {

                if (AccName.getText().length() >= 3 && !AccName.getText().contains("(") && !AccName.getText().contains(")")) {
                    if (AccPESEL.getText().length() == 11 && AccPESEL.getText().matches("[0-9]+")) {
                        if (AccPass.getText().length() >= 8) {
                            return true;
                        } else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            DialogPane dialogPane = alert.getDialogPane();
                            dialogPane.getStylesheets().add(
                                    getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
                            dialogPane.getStyleClass().add("myDialog");
                            alert.setTitle("Information Dialog");
                            alert.setHeaderText("Wrong");
                            alert.setContentText("Password is to short. Must contain 8 letters");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        DialogPane dialogPane = alert.getDialogPane();
                        dialogPane.getStylesheets().add(
                                getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
                        dialogPane.getStyleClass().add("myDialog");
                        alert.setTitle("Information Dialog");
                        alert.setHeaderText("Wrong");
                        alert.setContentText("Invalid PESEL. Must contain 11 numbers");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
                    dialogPane.getStyleClass().add("myDialog");
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Wrong");
                    alert.setContentText("Name is to short.Must contain 3 letters or more. '(',')' cannot be used.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(
                        getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
                dialogPane.getStyleClass().add("myDialog");
                alert.setTitle("Information Dialog");
                alert.setHeaderText("Wrong");
                alert.setContentText("Telephone number must only contain numbers");
                alert.showAndWait();
            }
        }
        else {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Wrong");
        alert.setContentText("Invalid Email. Please fix it");
        alert.showAndWait();
    }
    return false;
    }
    @FXML
    public void add_account() {
        if (!AccName.getText().isEmpty() && !AccPESEL.getText().isEmpty() && !ACCPhone.getText().isEmpty() && !AccEmail.getText().isEmpty() && !AccLog.getText().isEmpty() && !AccPass.getText().isEmpty()) {

            if (validate()) {
                CrudOperations<Users> user = new CrudOperations<>();


                List b = user.get("Roles");
                String a = AccPos.getSelectionModel().getSelectedItem();
                if (a == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
                    dialogPane.getStyleClass().add("myDialog");
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Missing Information");
                    alert.setContentText("Role is missing. Please choose user role and try again");
                    alert.showAndWait();
                    return;
                }
                for (Iterator iterator = b.iterator(); iterator.hasNext(); ) {
                    Roles role = (Roles) iterator.next();
                    if (a.equals(role.getName())) {
                        Users u = new Users(AccName.getText(), AccPESEL.getText(), ACCPhone.getText(), AccEmail.getText(), AccLog.getText(), AccPass.getText(), role);
                        System.out.println(role);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        DialogPane dialogPane = alert.getDialogPane();
                        dialogPane.getStylesheets().add(
                                getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
                        dialogPane.getStyleClass().add("myDialog");
                        alert.setTitle("Infornmation Dialog");
                        alert.setHeaderText("New Account is created");
                        alert.setContentText("Congratulations");

                        alert.showAndWait();
                        user.create(u);
                        cleartext();
                        break;
                    }
                }

            }
            leavelogin();
            InitializeLogin();

        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Some informations are missing");
            alert.setContentText("Fill the missing brackets and confirm again");
            alert.showAndWait();

        }

    }

    public void choose_update() {
        if (AccountsList.getSelectionModel().isEmpty()){
            return;
        }
        else {
            editUser.setVisible(true);
            returnUser.setVisible(true);
            addUser.setVisible(false);

            String a = AccountsList.getSelectionModel().getSelectedItem().substring(0, AccountsList.getSelectionModel().getSelectedItem().indexOf("(") - 1);

            CrudOperations<Users> user = new CrudOperations<>();
            List c = user.get("Roles");
            List b = user.get("Users");
            for (Iterator iterator = b.iterator(); iterator.hasNext(); ) {
                Users users = (Users) iterator.next();
                if (users.getName().equals(a)) {

                    AccName.setText(users.getName());
                    AccPESEL.setText(users.getPid());
                    ACCPhone.setText(users.getPhone_number());
                    AccEmail.setText(users.getEmail());
                    AccLog.setText(users.getLogin());
                    AccPass.setText(users.getPassword());
                    AccID = users.getUser_id();

                    for (iterator = c.iterator(); iterator.hasNext(); ) {
                        Roles role = (Roles) iterator.next();
                        if (role.equals(users.getRole())) {
                            AccPos.setValue(role.getName());

                        }
                    }
                }

            }
            user.stop();
        }
    }

    public void update() {
        if (!AccName.getText().isEmpty() && !AccPESEL.getText().isEmpty() && !ACCPhone.getText().isEmpty() && !AccEmail.getText().isEmpty() && AccPos != null && !AccLog.getText().isEmpty() && !AccPass.getText().isEmpty()) {
            if (validate()) {
                CrudOperations<Users> user = new CrudOperations<>();
                List b = user.get("Roles");
                List c = user.get("Users");
                String a = AccPos.getSelectionModel().getSelectedItem();
                if (a == null) {
                    return;
                }

                for (Iterator iterator = c.iterator(); iterator.hasNext(); ) {

                    Users users = (Users) iterator.next();

                    if (AccID == users.getUser_id()) {

                        users.setName(AccName.getText());
                        users.setPid(AccPESEL.getText());
                        users.setPhone_number(ACCPhone.getText());
                        users.setEmail(AccEmail.getText());
                        users.setLogin(AccLog.getText());
                        users.setPassword(AccPass.getText());

                        for (iterator = b.iterator(); iterator.hasNext(); ) {
                            Roles role = (Roles) iterator.next();

                            if (role.getName().equals(a)) {
                                users.setRole(role);
                            }
                        }

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        DialogPane dialogPane = alert.getDialogPane();
                        dialogPane.getStylesheets().add(
                                getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
                        dialogPane.getStyleClass().add("myDialog");
                        alert.setTitle("Confirmation Dialog");
                        alert.setHeaderText("Are you sure?");
                        alert.setContentText("Are you really ok with this?");

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            user.update(users);
                            Alert news = new Alert(Alert.AlertType.INFORMATION);
                            DialogPane dialogPane1 = news.getDialogPane();
                            dialogPane1.getStylesheets().add(
                                    getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
                            dialogPane1.getStyleClass().add("myDialog");
                            news.setTitle("Success");
                            news.setHeaderText(AccName.getText() + " has been updated");
                            news.setContentText("Congratulations user " + AccName.getText() + " has been updated");
                            news.showAndWait();

                            editUser.setVisible(false);
                            returnUser.setVisible(false);
                            addUser.setVisible(true);

                            // ... user chose OK
                        } else {
                            // ... user chose CANCEL or closed the dialog
                        }


                        break;


                    }
                }

                user.stop();
                leavelogin();
                InitializeLogin();
                cleartext();
                AccID = 0;
            }
            }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Some informations are missing");
            alert.setContentText("Fill the missing brackets and confirm again");
            alert.showAndWait();

        }
    }


    public void returning() {
        cleartext();
        AccID = 0;
        leavelogin();
        InitializeLogin();
        editUser.setVisible(false);
        returnUser.setVisible(false);
        addUser.setVisible(true);
    }

    public void cleartext() {
        AccName.clear();
        AccPESEL.clear();
        ACCPhone.clear();
        AccEmail.clear();
        AccLog.clear();
        AccPass.clear();
        ;
    }

    public void leavelogin() {
        AccountsList.getItems().clear();
        AccPos.getItems().clear();
    }

    @FXML
    public void addIngredient() throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/AdminIngredientAdding.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Ingredient Adding");
        Scene scene = new Scene(root);
        stage.setScene(scene);

        helpfulMethods.setStageUndecorated(stage);
        helpfulMethods.addDragging(stage,scene);
        stage.show();
    }
    @FXML
    public void clickOnCloseButton() {
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }


    public void findByDate() {
        LocalDate localDate = dataPicker.getValue();
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        DialogPane dialogPane = a.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        a.setHeaderText(null);
        if (localDate != null) {
            ordersList.getItems().clear();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date date = Date.from(instant);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String dateAfterFormat = formatter.format(date);
            CrudOperations<Orders> orders = new CrudOperations<>();
            List<Orders> ordersList1 = orders.getItems(Orders.class);

            for (Orders elem : ordersList1) {
                if (dateAfterFormat.equals(formatter.format(elem.getDate()))) {
                    ordersList.getItems().add("Order: " + elem.getOrder_id());
                }
            }
            if(ordersList.getItems().isEmpty()){
                a.setContentText("No orders found from the chosen day!");
                a.setTitle("No orders found from the chosen day!");
                a.show();
            }

        }else{
           a.setContentText("Date was not chosen!");
           a.setTitle("Date was not chosen!");
           a.show();
        }
    }


    @FXML
    public void generateInvoiceOrders() throws FileNotFoundException, DocumentException {
        LocalDate localDate = dataPicker.getValue();
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        DialogPane dialogPane = a.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        a.setHeaderText(null);
        if (localDate != null) {
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            Date date = Date.from(instant);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            CrudOperations<Orders> orders = new CrudOperations<>();
            List<Orders> ordersList = orders.getItems(Orders.class);

            
            boolean foundOne = false;

            CrudOperations<Products> products = new CrudOperations<>();
            List<Products> productsList = products.getItems(Products.class);
            products.stop();
            String productsTable[][] = new String[productsList.size()][4];
            int i = 0;
            for (Products product : productsList){
                productsTable[i][0] = product.getName();
                productsTable[i][1] = product.getPrice().toString();
                productsTable[i][2] = "0";
                productsTable[i][3] = product.getStatus().toString();
                i++;
            }
            System.out.println(productsTable[0][3]);
            for (Orders elem : ordersList) {
                if (formatter.format(date).equals(formatter.format(elem.getDate()))) {
                    foundOne = true;
                    Set<Products> setOfProducts = elem.getProducts();
                    for (Iterator iterator = setOfProducts.iterator(); iterator.hasNext(); ) {
                        Products product = (Products) iterator.next();
                        for (int x = 0; x < productsTable.length; x++){
                            if (product.getName().equals(productsTable[x][0])){
                                int quant = Integer.parseInt(productsTable[x][2]) + 1;
                                productsTable[x][2] = String.valueOf(quant);
                                break;
                            }
                        }
                    }
                }
            }

            if(foundOne) {
                Document document = new Document(PageSize.A4.rotate());
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("daily_invoice\\Raport_Sprzedazy_Z_Dnia.pdf"));
                PdfPTable table = new PdfPTable(4);
                Chunk glue = new Chunk(new VerticalPositionMark());
                Paragraph p = new Paragraph("Invoice from day: " + formatter.format(date));
                Paragraph paragraph = new Paragraph("");
                document.open();
                p.add(new Chunk(glue));
                paragraph.add(new Chunk(glue));
                p.setAlignment(Element.ALIGN_CENTER);
                document.add(p);
                document.add(paragraph);
                table.addCell("Name");
                PdfPCell cell1 = new PdfPCell(new Phrase("Price"));
                PdfPCell cell2 = new PdfPCell(new Phrase("Quantity"));
                PdfPCell cell3 = new PdfPCell(new Phrase("Total Price"));
                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);

                for (int x = 0; x < productsTable.length; x++) {
                    if(productsTable[x][3].equals("false") && productsTable[x][2].equals("0")){}
                    else {
                        table.addCell(productsTable[x][0]);
                        table.addCell(productsTable[x][1]);
                        table.addCell(productsTable[x][2]);
                        table.addCell(Integer.toString(Integer.parseInt(productsTable[x][1]) * Integer.parseInt(productsTable[x][2])));
                    }
                }
                document.add(table);
                document.addTitle("Invoice of daily sales");
                document.close();

                a.setContentText("Invoice generated!");
                a.setTitle("Invoice generated!");
                a.show();
            }else {
                a.setContentText("No orders found from the chosen day!");
                a.setTitle("No orders found from the chosen day!");
                a.show();
            }
            orders.stop();
        } else {
            a.setTitle("Date was not chosen!");
            a.setContentText("Date was not chosen!");
            a.show();
        }
    }


}




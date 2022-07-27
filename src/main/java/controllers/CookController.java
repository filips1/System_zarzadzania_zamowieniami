package controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import helpfulClasses.helpfulMethods;
import hibernate.dao.CrudOperations;
import hibernate.entities.Ingredients;
import hibernate.entities.Orders;
import hibernate.entities.Products;
import hibernate.entities.Ingredients;
import hibernate.entities.Orders;
import hibernate.entities.Products;
import hibernate.entities.Users;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.persistence.criteria.Root;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/*
    @author Daniel Partyka
    @author Rafał Sudoł
    @author Dariusz Słabicki
    @author Krystian Wolański
    @author Filip Szpunar
*/
public class CookController {

    @FXML
    HBox hbox_orders;

    @FXML
    AnchorPane anchorPane;

    @FXML
    Label userLabel;


    //Metoda inicjalizujaca panel kucharza
    @FXML
    public void initialize() {

        userLabel.setText(LoginController.CURRENT_USER.login.toString());

        try {
            hbox_orders.setSpacing(255);
            CrudOperations<Orders> orderservice = new CrudOperations<>();
            List<Orders> orders = orderservice.getItems(Orders.class);
            for (Orders order : orders) {
                if (order.getStatus().equals(Orders.Status.COOKING.name())||order.getStatus().equals(Orders.Status.TODO.toString())) {
                    Pane panel = FXMLLoader.load(getClass().getResource("/fxmls/CookPanelOrderPane.fxml"));
                    Label label = (Label) panel.lookup("#label_order");
                    label.setText("Order nr " + order.getOrder_id());
                    Label label1 = (Label) panel.lookup("#label_status");
                    label1.setText("Status: "+order.getStatus());
                    ScrollPane scrollPane = (ScrollPane) panel.lookup("#scrollpane1");
                    VBox vboxen = (VBox) scrollPane.getContent().lookup("#products_vbox");
                    vboxen.setSpacing(100);
                    List<Products> listproducts = new ArrayList<Products>(order.getProducts());
                    for (Products product:listproducts) {

                        Pane panelproduct = FXMLLoader.load(getClass().getResource("/fxmls/CookPanelProductPane.fxml"));
                        Label productlabel = (Label) panelproduct.lookup("#label_product");
                        VBox ingredientvbox = (VBox) panelproduct.lookup("#vbox_ingredients");
                        productlabel.setText(product.getName());
                        List<Ingredients> listingredients = new ArrayList<Ingredients>(product.getIngredients());
                        System.out.println(listingredients.size());
                        for (Ingredients ing : listingredients) {
                            ingredientvbox.getChildren().add(new Label("- " + ing.getName()));
                        }

                        vboxen.getChildren().add(panelproduct);


                    }

                    Pane panelproduct = new Pane();
                    panelproduct.setPrefHeight(200);
                    vboxen.getChildren().add(panelproduct);
                    Button button =  (Button) panel.lookup("#ready_btn");
                    if(order.getStatus().equals(Orders.Status.TODO.toString().toUpperCase())) {
                        button.setText("Cooking");
                        button.setOnAction(event -> {
                            try {
                                cooking(order.getOrder_id());
                            } catch (IOException | DocumentException e) {
                                e.printStackTrace();
                            }
                        });
                    }else if(order.getStatus().equals(Orders.Status.COOKING.toString().toUpperCase())){
                        button.setText("Ready");
                        button.setOnAction(event -> {
                            try {
                                ready(order.getOrder_id());
                            } catch (IOException | DocumentException e) {
                                e.printStackTrace();
                            }
                        });

                    }
                    hbox_orders.getChildren().add(panel);

                    Button button2 = (Button) panel.lookup("#rlbutton");
                    button2.setText("Ingredients Status");
                    button2.setOnAction(event -> {
                        try {
                            runningLow(order);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
            Pane panel = new Pane();
            panel.setPrefWidth(255);
            hbox_orders.getChildren().add(panel);
            orderservice.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void runningLow(Orders order) throws IOException {

        AnchorPane apanel = FXMLLoader.load(getClass().getResource("/fxmls/IngredientPanel.fxml"));
        VBox vbox = (VBox) apanel.lookup("#ingriedientsvbox");


        Set<Ingredients> ingredientsSet = new HashSet<>();
        for (Products prod:order.getProducts()) {
            for (Ingredients ing:prod.getIngredients()) {
                ingredientsSet.add(ing);
            }
        }


        for (Ingredients ingredient: ingredientsSet) {
            Pane panel = FXMLLoader.load(getClass().getResource("/fxmls/IngredientPanelPane.fxml"));
            Label nameLabel =(Label) panel.lookup("#namelabel");
            Label aviabliltyLabel =(Label) panel.lookup("#aviabilitylabel");
            Button rlbutton = (Button) panel.lookup("#rlbutton");
            Button unavailablebutton = (Button) panel.lookup("#unaviablebutton");
            nameLabel.setText(ingredient.getName());
            aviabliltyLabel.setText(ingredient.getAvailability());
            rlbutton.setOnAction(event -> {
                    runningLowStatus(ingredient.getIngredient_id());
                    aviabliltyLabel.setText("Running Low");
            });
            unavailablebutton.setOnAction(event -> {
                unavailableStatus(ingredient.getIngredient_id());
                aviabliltyLabel.setText("Unaviable");
            });
            vbox.getChildren().add(panel);
        }
        Stage stage = new Stage();
        Scene scene = new Scene(apanel);
        helpfulMethods.addDragging(stage, scene);
        helpfulMethods.setStageUndecorated(stage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("New stage");
        stage.setScene(scene);
        stage.showAndWait();
        Parent root;
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/CookPanel.fxml"));
        stage = (Stage) hbox_orders.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public void ready(int id) throws IOException, DocumentException {

        CrudOperations<Orders> orderservice = new CrudOperations<>();
        Orders order = orderservice.getItem(Orders.class, id);
        order.setStatus(Orders.Status.READY);
        generateinvoice(order);
        orderservice.update(order);
        orderservice.stop();
        Parent root;
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/CookPanel.fxml"));
        Stage stage = (Stage) hbox_orders.getScene().getWindow();
        stage.setScene(new Scene(root));
        Alert news = new Alert(Alert.AlertType.INFORMATION);
        news.setTitle("Success");
        news.setHeaderText(null);
        news.setContentText("Order nr "+order.getOrder_id() + " is now ready");
        DialogPane dialogPane = news.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        Optional<ButtonType> result = news.showAndWait();

    }

    public void cooking(int id) throws IOException, DocumentException {

        CrudOperations<Orders> orderservice = new CrudOperations<>();
        Orders order = orderservice.getItem(Orders.class, id);
        order.setStatus(Orders.Status.COOKING);
        orderservice.update(order);
        orderservice.stop();
        Parent root;
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/CookPanel.fxml"));
        Stage stage = (Stage) hbox_orders.getScene().getWindow();
        stage.setScene(new Scene(root));
        Alert news = new Alert(Alert.AlertType.INFORMATION);
        news.setTitle("Success");
        news.setHeaderText(null);
        news.setContentText("Order nr "+order.getOrder_id() + " is now being cooked");
        DialogPane dialogPane = news.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        Optional<ButtonType> result = news.showAndWait();

    }

    public void generateinvoice(Orders order) throws FileNotFoundException, DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("order_invoice\\rachunekzamowienianr" + order.getOrder_id() + ".pdf"));
        PdfPTable table = new PdfPTable(2);
        Chunk glue = new Chunk(new VerticalPositionMark());
        Paragraph p = new Paragraph("Rachunek z dnia: " + formatter.format(date));
        Paragraph paragraph = new Paragraph("");
        document.open();
        p.add(new Chunk(glue));
        paragraph.add(new Chunk(glue));
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        document.add(paragraph);
        table.addCell("Numer zamowienia");
        table.addCell(order.getOrder_id().toString());
        table.addCell("zwartosc zamowienia");
        String nazwa="";
        for (Products prod: order.getProducts()) {
            nazwa = nazwa + prod.getName()+" ";
        }
        table.addCell(nazwa);
        table.addCell("Cena zamowienia");
        table.addCell(order.getTotal_price().toString());
        table.addCell("Kucharz");
        table.addCell(LoginController.CURRENT_USER.getName());
        document.add(table);
        document.addTitle("rachunek");
        document.close();
    }

    @FXML
    public void clickOnCloseButton(){
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }
    public void runningLowStatus(int id){
        CrudOperations<Ingredients> ingredientservice = new CrudOperations<>();
        Ingredients ingredient = ingredientservice.getItem(Ingredients.class, id);
        ingredient.setAvailability("Running Low");
        ingredientservice.update(ingredient);
        ingredientservice.stop();
        Alert news = new Alert(Alert.AlertType.INFORMATION);
        news.setTitle("Success");
        news.setHeaderText(null);
        news.setContentText(ingredient.getName() + " is running low");
        DialogPane dialogPane = news.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        Optional<ButtonType> result = news.showAndWait();
    }
    public void unavailableStatus(int id){
        CrudOperations<Ingredients> ingredientservice = new CrudOperations<>();
        Ingredients ingredient = ingredientservice.getItem(Ingredients.class, id);
        ingredient.setAvailability("Unavailable");
        ingredientservice.update(ingredient);
        ingredientservice.stop();
        Alert news = new Alert(Alert.AlertType.INFORMATION);
        news.setTitle("Success");
        news.setHeaderText(null);
        news.setContentText(ingredient.getName() + " is unavailable");
        DialogPane dialogPane = news.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        Optional<ButtonType> result = news.showAndWait();
    }

}

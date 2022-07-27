package controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import helpfulClasses.helpfulMethods;
import helpfulClasses.models.ProductInTable;
import hibernate.dao.CrudOperations;
import hibernate.entities.Ingredients;
import hibernate.entities.Orders;
import hibernate.entities.Products;
import hibernate.entities.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.modelmapper.ModelMapper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class OrderController {

    @FXML
    AnchorPane anchorPane;



    @FXML TableView<ProductInTable> tableView;
    @FXML TableColumn<ProductInTable, String> tableName;
    @FXML TableColumn<ProductInTable, Integer> tableQuantity;
    @FXML TableColumn<ProductInTable, Integer> tablePrice;


    private ModelMapper modelMapper;
    private ObservableList<ProductInTable> orderObservableList;

    @FXML
    private TableColumn<Ingredients, String> ingredientName;
    @FXML
    private TableColumn<Ingredients, String> ingredientAvailability;
    @FXML
    private TableView<Ingredients> ingredientsTable;
    @FXML Label userlabel;

    @FXML VBox order_vbox;

    @FXML
    public void initialize(){

        userlabel.setText(LoginController.CURRENT_USER.login.toString());
        modelMapper = new ModelMapper();

        tableName.setCellValueFactory(new PropertyValueFactory<ProductInTable, String>("name"));
        tableQuantity.setCellValueFactory(new PropertyValueFactory<ProductInTable, Integer>("quantity"));
        tablePrice.setCellValueFactory(new PropertyValueFactory<ProductInTable, Integer>("totalPrice"));
        orderObservableList = FXCollections.observableArrayList();


        CrudOperations<Products> productService = new CrudOperations<>();

        List<Products> listOfProducts = productService.getItems(Products.class);
        productService.stop();
        try {
            HBox hBox = new HBox();
            hBox.setSpacing(65);
            order_vbox.getChildren().add(hBox);
            order_vbox.setSpacing(35);
            for(Products x : listOfProducts){
                if(x.getStatus().equals(true)) {
                    Pane panel = FXMLLoader.load(getClass().getResource("/fxmls/OrderPanelOrderPane.fxml"));
                    Pane panel2 = (Pane) panel.getChildren().get(0);
                    Label label = (Label) panel.lookup("#label");
                    label.setText(x.getName());

                    panel2.setOnMouseClicked(mouseEvent -> cardOnClick(x));

                    if(hBox.getChildren().size() < 3) {
                        hBox.getChildren().add(panel);
                    } else{
                        hBox = new HBox();

                        hBox.setSpacing(65);
                        hBox.getChildren().add(panel);

                        order_vbox.getChildren().add(hBox);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setIngredientTable();

        ingredientsTable.getSelectionModel().selectedItemProperty().addListener((observableValue, products, selectedRow) -> {
            if (selectedRow != null) {
                CrudOperations<Ingredients> ingredients = new CrudOperations<>();
                List iList = ingredients.get("Ingredients");
                for (Iterator iterator = iList.iterator(); iterator.hasNext(); ) {
                    Ingredients ingredient = (Ingredients) iterator.next();
                    if (ingredient.getName().equals(selectedRow.getName())) {
                        ingredient.setAvailability("Available");
                        ingredients.update(ingredient);
                    }
                }
                ingredients.stop();
            }
        });
    }

    private void cardOnClick(Products product) {
        ProductInTable o = modelMapper.map(product, ProductInTable.class);
        o.setTotalPrice(o.getPrice());

        for(ProductInTable productInTable :orderObservableList) {
            if(productInTable.getProduct_id().equals(o.getProduct_id())){
                int quantity = productInTable.getQuantity() + 1;
                productInTable.setQuantity(quantity);
                int totalPrice = productInTable.getPrice() * quantity;
                productInTable.setTotalPrice(totalPrice);

                o = productInTable;
                orderObservableList.remove(o);

                break;
            }
        }
        orderObservableList.add(o);

        tableView.setItems(orderObservableList);
        tableView.refresh();
    }

    @FXML
    public void sendOrder(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to send the order?");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.get() == ButtonType.OK) {
            ObservableList<ProductInTable> productsInTable;
            productsInTable = tableView.getItems();

            Set<Products> products = new HashSet<>();
            Integer totalPrice = 0;

            for(ProductInTable productInTable:productsInTable) {
                totalPrice+=productInTable.getTotalPrice();
                Products product = modelMapper.map(productInTable, Products.class);
                products.add(product);

            }
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("The order has been sent");
            dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.showAndWait();

            CrudOperations<Orders> orders = new CrudOperations<>();

            Orders order = new Orders(totalPrice,new Date(), Orders.Status.TODO,LoginController.CURRENT_USER,products);
            orders.create(order);
            orders.stop();

            clearTable();
        }
    }

    @FXML
    public void clearTable(){
        orderObservableList.clear();
        tableView.setItems(orderObservableList);
        tableView.refresh();
    }
    @FXML
    public void clickOnCloseButton(){
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void setAvailable() {
        setIngredientTable();
    }

    public void setIngredientTable() {

        ingredientName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ingredientAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));

        CrudOperations<Ingredients> ingredients = new CrudOperations<>();
        ObservableList<Ingredients> iObsList = FXCollections.observableArrayList();
        List iList = ingredients.getItems(Ingredients.class);

        for (Iterator iterator = iList.iterator(); iterator.hasNext(); ) {
            Ingredients ingredient = (Ingredients) iterator.next();
            iObsList.add(ingredient);
        }

        ingredientsTable.setItems(iObsList);
        ingredients.stop();
    }

    @FXML
    public void addIngredient() throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getClassLoader().getResource("fxmls/AdminIngredientAdding.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Ingredient Adding");
        Scene scene = new Scene(root);
        stage.setScene(scene);

        helpfulMethods.setStageUndecorated(stage);
        helpfulMethods.addDragging(stage,scene);
        stage.showAndWait();
        setIngredientTable();
    }

    @FXML
    public void generateinvoice(ActionEvent actionEvent) throws FileNotFoundException, DocumentException {
        Document document = new Document(PageSize.A4.rotate());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        Date date = new Date();
        String inputString = formatter.format(date).toString();
        String stringWithoutSpaces = inputString.replaceAll("\\s+","");
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("ingredients_invoice\\raportdotyczacyskladnikowzdnia" + stringWithoutSpaces + ".pdf"));
        PdfPTable table = new PdfPTable(2);
        Chunk glue = new Chunk(new VerticalPositionMark());
        Paragraph p = new Paragraph("Raport z dnia: " + formatter.format(date));
        Paragraph paragraph = new Paragraph("");
        document.open();
        p.add(new Chunk(glue));
        paragraph.add(new Chunk(glue));
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        document.add(paragraph);
        table.addCell("Nazwa skladnika");
        PdfPCell cell = new PdfPCell(new Phrase("Dostepnosc"));
        table.addCell(cell);
        ObservableList<Ingredients> ingredients_list = ingredientsTable.getItems();
        for (Ingredients i : ingredients_list) {
            table.addCell(i.getName());
            table.addCell(i.getAvailability());
        }
        document.add(table);
        document.addTitle("Raport dotyczący składników do pizzy");
        document.close();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Ingredients invoice has been generated");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getClassLoader().getResource("fxmls/style.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialog");
        alert.showAndWait();
    }

}

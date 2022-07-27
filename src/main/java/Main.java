import helpfulClasses.helpfulMethods;
import hibernate.dao.CrudOperations;
import hibernate.entities.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

/*
    @author Daniel Partyka
    @author Rafał Sudoł
    @author Dariósz Słabicki
    @author Krystian Wolański
    @author Filip Szpunar
*/
public class Main extends Application {

    public static void main(String[] args){

        //Operacje sluzace do "manualnych testow aplikacji"
        try{

            CrudOperations<Roles> roles = new CrudOperations<>();
            Roles r1 = new Roles("Kucharz");
            roles.create(r1);
            Roles r2 = new Roles("Specjalista do spraw zamówień");
            roles.create(r2);
            Roles r3 = new Roles("Kierownik");
            roles.create(r3);
            System.out.println(r3);

            CrudOperations<Users> users = new CrudOperations<>();
            Users u1 = new Users("Rafał", "1234566545", "99123", "krfds2@gmail.com", "rafineria", "123", r1);
            users.create(u1);
            Users u2 = new Users("Paweł", "2334566545", "89123", "pablo12@gmail.com", "pablo", "321", r3);
            users.create(u2);
            Users u3 = new Users("Michał", "3234566545", "79123", "miki32@gmail.com", "michael", "213", r2);
            users.create(u3);
            Users u4 = new Users("Tytus", "9934566545", "79123", "miki32@gmail.com", "michael", "213", r2);
            users.create(u4);
            users.delete(u3);

            CrudOperations<Ingredients> ingredients = new CrudOperations<>();
            Ingredients i1 = new Ingredients("Cheese", "Available");
            ingredients.create(i1);
            Ingredients i2 = new Ingredients("Bacon", "Available");
            ingredients.create(i2);
            Ingredients i3 = new Ingredients("Pineapple", "Unavailable");
            ingredients.create(i3);

            CrudOperations<Products> products = new CrudOperations<>();
            Products p1 = new Products("Hawaian", 25);
            Products p2 = new Products("4 Cheese",39);
            Products p3 = new Products("Neapolitan",35);

            p1.getIngredients().add(i3);
            p1.getIngredients().add(i2);
            p1.getIngredients().add(i1);
            p2.getIngredients().add(i1);
            p2.getIngredients().add(i2);
            p2.getIngredients().add(i3);
            p3.getIngredients().add(i1);
            p3.getIngredients().add(i2);
            p3.getIngredients().add(i3);
            products.create(p1);
            products.create(p2);
            products.create(p3);


            CrudOperations<Orders> orders = new CrudOperations<>();

            Date date = new Date(System.currentTimeMillis());
            Orders o1 = new Orders(50,date, Orders.Status.COOKING,u3);
            o1.getProducts().add(p1);
            o1.getProducts().add(p2);
            o1.getProducts().add(p3);
            orders.create(o1);

            date = new Date(System.currentTimeMillis());
            Orders o2 = new Orders(70,date, Orders.Status.COOKING,u3);
            o2.getProducts().add(p2);
            orders.create(o2);

            date = new Date(System.currentTimeMillis());
            Orders o3 = new Orders(70,date, Orders.Status.COOKING,u3);
            o3.getProducts().add(p2);
            orders.create(o3);

            date = new Date(System.currentTimeMillis());
            Orders o4 = new Orders(70,date, Orders.Status.COOKING,u3);
            o4.getProducts().add(p2);
            orders.create(o4);

            date = new Date(System.currentTimeMillis());
            Orders o5 = new Orders(70,date, Orders.Status.COOKING,u3);
            o5.getProducts().add(p2);
            orders.create(o5);

            date = new Date(System.currentTimeMillis());
            Orders o6 = new Orders(70,date, Orders.Status.COOKING,u3);
            o6.getProducts().add(p2);
            orders.create(o6);


        } catch (Exception e){
            e.getStackTrace();
        }

        launch(args);
    }
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxmls/LoginPanel.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("fxmls/style.css");

        helpfulMethods.addDragging(stage, scene);
        helpfulMethods.setStageUndecorated(stage);

        stage.setScene(scene);

        stage.setResizable(true);
        stage.setTitle("Moja");
        stage.show();

    }


}

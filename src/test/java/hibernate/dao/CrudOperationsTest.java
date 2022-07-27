package hibernate.dao;

import hibernate.entities.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CrudOperationsTest {

    @Test
    @Order(1)
    void createRole() {

        CrudOperations<Roles> roles = new CrudOperations<>();
        Roles r1 = new Roles("Kucharz");
        roles.create(r1);
        assertEquals(roles.get("Roles").get(0), r1);

    }

    @Test
    @Order(2)
    void createUser() {

        CrudOperations<Users> users = new CrudOperations<>();
        CrudOperations<Roles> roles = new CrudOperations<>();
        Users u1 = new Users("Rafał", "1234566545", "99123", "krfds2@gmail.com", "rafineria", "123", (Roles)roles.get("Roles").get(0));
        users.create(u1);
        assertEquals(users.get("Users").get(0), u1);

    }

    @Test
    @Order(3)
    void createIngredient() {

        CrudOperations<Ingredients> ingredients = new CrudOperations<>();
        Ingredients i1 = new Ingredients("Cheese", "aviable");
        ingredients.create(i1);
        assertEquals(ingredients.get("Ingredients").get(0), i1);

    }

    @Test
    @Order(4)
    void createProduct() {

        CrudOperations<Products> products = new CrudOperations<>();
        CrudOperations<Ingredients> ingredients = new CrudOperations<>();
        Products p1 = new Products("Hawaian", 25);
        p1.getIngredients().add((Ingredients)ingredients.get("Ingredients").get(0));
        products.create(p1);
        assertEquals(products.get("Products").get(0), p1);

    }

    @Test
    @Order(5)
    void createOrder() {

        CrudOperations<Users> users = new CrudOperations<>();
        CrudOperations<Products> products = new CrudOperations<>();
        CrudOperations<Orders> orders = new CrudOperations<>();
        System.out.println(users.get("Users").size());
        Date date = new Date(System.currentTimeMillis());
        Orders o1 = new Orders(50,date,Orders.Status.COOKING,(Users)users.get("Users").get(0));
        o1.getProducts().add((Products)products.get("Products").get(0));
        orders.create(o1);
        assertEquals(orders.get("Orders").get(0), o1);

    }



    @Test
    @Order(6)
    void updateIngredients() {

        CrudOperations<Ingredients> ingredients = new CrudOperations<>();
        Ingredients ingredient = (Ingredients)ingredients.get("Ingredients").get(0);
        ingredient.setName("asd");
        ingredients.update(ingredient);
        assertEquals(ingredient, ingredients.get("Ingredients").get(0));

    }

    @Test
    @Order(7)
    void updateOrders() {

        CrudOperations<Orders> orders = new CrudOperations<>();
        Orders order = (Orders)orders.get("Orders").get(0);
        order.setStatus(Orders.Status.COOKING);
        orders.update(order);
        assertEquals(order, orders.get("Orders").get(0));

    }

    @Test
    @Order(8)
    void updateUsers() {

        CrudOperations<Users> users = new CrudOperations<>();
        Users user = (Users)users.get("Users").get(0);
        user.setEmail("asd");
        users.update(user);
        assertEquals(user, users.get("Users").get(0));

    }

    @Test
    @Order(9)
    void updateProducts() {

        CrudOperations<Products> products = new CrudOperations<>();
        Products product = (Products)products.get("Products").get(0);
        product.setName("asd");
        products.update(product);
        assertEquals(product, products.get("Products").get(0));

    }

    @Test
    @Order(10)
    void updateRoles() {

        CrudOperations<Roles> roles = new CrudOperations<>();
        Roles role = (Roles)roles.get("Roles").get(0);
        role.setName("asd");
        roles.update(role);
        assertEquals(role, roles.get("Roles").get(0));

    }

    @Test
    @Order(11)
    void getIngredients() {

        CrudOperations<Ingredients> ingredients = new CrudOperations<>();
        List<Ingredients> a=ingredients.get("Ingredients");
        assertEquals(a.size(), 1);

    }

    @Test
    @Order(12)
    void getOrders() {

        CrudOperations<Orders> orders = new CrudOperations<>();
        List<Orders> a=orders.get("Orders");
        assertEquals(a.size(), 1);

    }

    @Test
    @Order(13)
    void getProducts() {

        CrudOperations<Products> products = new CrudOperations<>();
        List<Products> a=products.get("Products");
        assertEquals(a.size(), 1);

    }
    @Test
    @Order(14)
    void getRoles() {

        CrudOperations<Roles> roles = new CrudOperations<>();
        List<Roles> a=roles.get("Roles");
        assertEquals(a.size(), 1);

    }

    @Test
    @Order(15)
    void getUsers() {

        CrudOperations<Users> users = new CrudOperations<>();
        List<Users> a=users.get("Users");
        assertEquals(a.size(), 1);

    }

    @Test
    @Order(16)
    void deleteOrder() {

        CrudOperations<Orders> orders = new CrudOperations<>();
        List<Orders> a=orders.get("Orders");
        orders.delete(a.get(0));
        a=orders.get("Orders");
        assertEquals(a.size(), 0);

    }

    @Test
    @Order(17)
    void deleteProduct() {

        CrudOperations<Products> products = new CrudOperations<>();
        List<Products> a=products.get("Products");
        products.delete(a.get(0));
        a=products.get("Products");
        assertEquals(a.size(), 0);

    }

    @Test
    @Order(18)
    void deleteIngredient() {

        CrudOperations<Ingredients> ingredient = new CrudOperations<>();
        List<Ingredients> a=ingredient.get("Ingredients");
        ingredient.delete(a.get(0));
        a=ingredient.get("Ingredients");
        assertEquals(a.size(), 0);

    }

    @Test
    @Order(19)
    void deleteUser() {

        CrudOperations<Users> users = new CrudOperations<>();
        List<Users> a=users.get("Users");
        users.delete(a.get(0));
        a=users.get("Users");
        assertEquals(a.size(), 0);

    }

    @Test
    @Order(20)
    void deleteRole() {

        CrudOperations<Roles> roles = new CrudOperations<>();
        List<Roles> a=roles.get("Roles");
        roles.delete(a.get(0));
        a=roles.get("Roles");
        assertEquals(a.size(), 0);

    }


}
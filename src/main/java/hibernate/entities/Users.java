package hibernate.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Users {

    public Users(String name, String pid, String phone_number, String email, String login, String password,Roles role){
        this.name = name;
        this.pid = pid;
        this.phone_number = phone_number;
        this.email = email;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public Users(){}
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Integer user_id;
    public String name;
    public String pid;
    public String phone_number;
    public String email;
    public String login;
    public String password;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    public Roles role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    List<Orders> orders = new ArrayList<Orders>();

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }
}

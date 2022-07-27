package hibernate.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Roles {
    public Roles(){

    }
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer role_id;
    private String name;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    private List<Users> users = new ArrayList<Users>();

    public Integer getRole_id() {
        return role_id;
    }

    public Roles(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }
}

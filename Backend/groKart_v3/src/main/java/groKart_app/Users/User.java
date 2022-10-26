package groKart_app.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import groKart_app.Karts.Kart;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

//import org.hibernate.annotations.Table;

@Entity
@Table(name = "AppUser")
public class User {

     /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany
    @JoinColumn(name = "kart_id")
    @JsonIgnore
    private List<Kart> karts;

    private String userName;
    private String emailAdd;
    private String password;
    private String displayName;
    private int privilege;

    public User(String userName, String emailAdd, String password, String displayName, int privilege) {
        this.userName = userName;
        this.emailAdd = emailAdd;
        this.password = password;
        this.displayName = displayName;
        this.privilege = privilege;
        karts = new ArrayList<Kart>();
    }

    public User() {
        karts = new ArrayList<Kart>();
    }

    // =============================== Getters and Setters for each field ================================== //

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getEmailAdd(){
        return emailAdd;
    }

    public void setEmailAdd(String emailAdd){
        this.emailAdd = emailAdd;
    }

    public String getPassword() { return this.password; }

    public void setPassword(String password) { this.password = password; }

    public String getDisplayName() {return displayName;}

    public void setDisplayName(String displayName) {this.displayName = displayName;}

    public void setPrivilege(int privilege) { this.privilege = privilege; }

    public int getPrivilege() { return this.privilege; }

    public void addKart(Kart kart) {
        karts.add(kart);
    }

    public void removeKart(Kart kart) {
        karts.remove(kart);
    }
}

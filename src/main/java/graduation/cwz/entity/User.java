package graduation.cwz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "username")
    private String uername;
    @Column(name = "password")
    private String password;

    public User() {}
    public User(String uername) {
        this.uername = uername;
    }
    public User(String uername, String password) {
        this.uername = uername;
        this.password = password;
    }

    public String getUername() {
        return uername;
    }

    public void setUername(String uername) {
        this.uername = uername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

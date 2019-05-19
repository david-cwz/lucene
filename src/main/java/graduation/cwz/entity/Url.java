package graduation.cwz.entity;
import javax.persistence.*;

@Entity
@Table(name = "url")
public class Url {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "urlId")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    public Url() {}

    public Url(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

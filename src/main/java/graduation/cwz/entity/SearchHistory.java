package graduation.cwz.entity;

import javax.persistence.*;

@Entity
@Table(name = "search_history")
public class SearchHistory {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "records")
    private String record;

    @ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)//cascade规定了级联操作，ALL包括添加、修改、删除操作
    @JoinColumn(name="userName")
    private User user;

    @Column(name = "date")
    private String date;

    @Column(name = "isPreEmbedded")
    private boolean isPreEmbedded = false;

    @Column(name = "haveNewResult")
    private String haveNewResult;

    public SearchHistory() {}

    public SearchHistory(String record, User user, String date) {
        this.record = record;
        this.user = user;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPreEmbedded() {
        return isPreEmbedded;
    }

    public void setPreEmbedded(boolean preEmbedded) {
        isPreEmbedded = preEmbedded;
    }

    public String getHaveNewResult() {
        return haveNewResult;
    }

    public void setHaveNewResult(String haveNewResult) {
        this.haveNewResult = haveNewResult;
    }
}

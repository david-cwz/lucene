package graduation.cwz.entity;

import javax.persistence.*;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "messageId")
    private int id;

    // 消息类型
    @Column(name = "intro")
    private String intro;

    @Lob
    @Column(name="content", columnDefinition="TEXT", nullable=true)
    private String content;

    @ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)//cascade规定了级联操作，ALL包括添加、修改、删除操作
    @JoinColumn(name="userName")
    private User user;

    @Column(name = "email")
    private String email;

    @Column(name = "date")
    private String date;

    public Message() {}

    public Message(String intro, String content) {
        this.intro = intro;
        this.content = content;
    }

    public Message(String intro, String content, User user, String email, String date) {
        this.intro = intro;
        this.content = content;
        this.user = user;
        this.email = email;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

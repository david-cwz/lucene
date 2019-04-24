package graduation.cwz.entity;

import javax.persistence.*;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    // 消息类型
    @Column(name = "intro")
    private String intro;

    @Column(name="content", columnDefinition="TEXT", nullable=true)
    private String content;

    public Message() {}
    public Message(String intro, String content) {
        this.intro = intro;
        this.content = content;
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
}

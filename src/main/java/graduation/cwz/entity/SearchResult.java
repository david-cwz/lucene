package graduation.cwz.entity;
import javax.persistence.*;

@Entity
@Table(name = "searchResult")
public class SearchResult {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "resultId")
    private int id;

    @ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)//cascade规定了级联操作，ALL包括添加、修改、删除操作
    @JoinColumn(name="recordId")
    private SearchHistory record;

    @OneToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)//cascade规定了级联操作，ALL包括添加、修改、删除操作
    @JoinColumn(name="messageId")
    private Message message;

    @Column(name = "intro")
    private String intro;

    @Lob
    @Column(name = "content", columnDefinition="TEXT", nullable=true)
    private String content;

    public SearchResult() {}

    public SearchResult(SearchHistory record, Message message, String intro, String content) {
        this.record = record;
        this.message = message;
        this.intro = intro;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SearchHistory getRecord() {
        return record;
    }

    public void setRecord(SearchHistory record) {
        this.record = record;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
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

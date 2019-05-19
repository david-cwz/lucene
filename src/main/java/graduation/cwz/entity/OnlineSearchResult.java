package graduation.cwz.entity;
import javax.persistence.*;

@Entity
@Table(name = "searchResult")
public class OnlineSearchResult {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "onlineResultId")
    private int id;

    @ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)//cascade规定了级联操作，ALL包括添加、修改、删除操作
    @JoinColumn(name="recordId")
    private SearchHistory record;

    @Lob
    @Column(name = "content", columnDefinition="TEXT", nullable=true)
    private String content;

    @Column(name = "url")
    private String url;

    public OnlineSearchResult() {}

    public OnlineSearchResult(SearchHistory record, String content, String url) {
        this.record = record;
        this.content = content;
        this.url = url;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

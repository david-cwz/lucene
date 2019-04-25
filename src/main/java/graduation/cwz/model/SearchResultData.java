package graduation.cwz.model;

public class SearchResultData {
    private int id;
    private String intro;
    private String content;

    public SearchResultData() {}
    public SearchResultData(int id, String intro, String content) {
        this.id = id;
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

package graduation.cwz.model;

public class OnlineSearchResultData {
    private String content;
    private String url;
    private int recordId;

    public OnlineSearchResultData() {}

    public OnlineSearchResultData(String content, String url, int recordId) {
        this.content = content;
        this.url = url;
        this.recordId = recordId;
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

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }
}

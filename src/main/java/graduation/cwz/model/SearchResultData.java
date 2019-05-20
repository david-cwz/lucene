package graduation.cwz.model;

public class SearchResultData {
    private String intro;
    private String content;
    private int messageId;
    private int recordId;

    public SearchResultData() {}

    public SearchResultData(String intro, String content, int messageId, int recordId) {
        this.intro = intro;
        this.content = content;
        this.messageId = messageId;
        this.recordId = recordId;
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

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }
}

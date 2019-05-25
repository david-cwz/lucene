package graduation.cwz.model;

public class SearchResultData {
    private String intro;
    private String content;
    private int messageId;
    private int recordId;
    private String userName;
    private String email;
    private String date;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

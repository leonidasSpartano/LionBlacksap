package model;

public class Chat {
    private String id;
    private String senderId;
    private String text;
    private String recipientId;
    private long timestamp;

    public Chat() {}

    public Chat(String id, String senderId, String text, String recipientId, long timestamp) {
        this.id = id;
        this.senderId = senderId;
        this.text = text;
        this.recipientId = recipientId;
        this.timestamp = timestamp;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

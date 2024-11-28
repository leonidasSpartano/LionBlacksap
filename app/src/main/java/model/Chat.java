package model;

public class Chat {
    private String senderId;
    private String senderName;
    private String recipientId;
    private String recipientEmail;  // Añadir este campo si lo usas
    private String messageText;
    private long timestamp;
    private String id;

    // Constructor sin argumentos (requerido por Firebase)
    public Chat() {
        // Este constructor es necesario para deserializar datos de Firebase
    }

    // Constructor con parámetros
    public Chat(String senderId, String senderName, String recipientId, String recipientEmail, String messageText, long timestamp) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.recipientId = recipientId;
        this.recipientEmail = recipientEmail;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    public Chat(String senderId, String senderName, String recipientId, String messageText, long timestamp) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.recipientId = recipientId;
        this.messageText = messageText;
        this.timestamp = timestamp;
    }

    // Getters y Setters
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

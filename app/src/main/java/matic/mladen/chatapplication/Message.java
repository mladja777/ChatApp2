package matic.mladen.chatapplication;

/**
 * Created by Mladen on 4/5/2018.
 */

public class Message {
    private String message;
    private int mMessageId;
    private int mSenderId;
    private int mReceiverId;

    public Message(String message, int messageId, int senderId, int receiverId) {
        this.message = message;
        this.mMessageId = messageId;
        this.mReceiverId = receiverId;
        this.mSenderId = senderId;
    }

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getMessageId() {
        return this.mMessageId;
    }

    public int getSenderId() {
        return this.mSenderId;
    }

    public int getReceiverId() {
        return this.mReceiverId;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

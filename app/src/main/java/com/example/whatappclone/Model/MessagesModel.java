package com.example.whatappclone.Model;

public class MessagesModel {

    String uId,messages,messageId;
    Long timeStamp;

    public MessagesModel(String uId, String messages, Long timeStamp) {
        this.uId = uId;
        this.messages = messages;
        this.timeStamp = timeStamp;
    }

    public MessagesModel(String uId, String messages) {
        this.uId = uId;
        this.messages = messages;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    private MessagesModel(){}

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}

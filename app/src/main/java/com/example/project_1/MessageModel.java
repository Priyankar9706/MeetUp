package com.example.project_1;

public class MessageModel {
    String myid,msg,messageId;
    String  time;

    public MessageModel(String myid, String msg, String time) {
        this.myid = myid;
        this.msg = msg;
        this.time = time;
    }

    public MessageModel(String myid, String msg) {
        this.myid = myid;
        this.msg = msg;
    }
    public MessageModel(){

    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMyid() {
        return myid;
    }

    public void setMyid(String myid) {
        this.myid = myid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

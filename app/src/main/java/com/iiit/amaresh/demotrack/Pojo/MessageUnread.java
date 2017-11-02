package com.iiit.amaresh.demotrack.Pojo;

/**
 * Created by LIPL on 25/01/17.
 */
public class MessageUnread {
    String id,empid,message,send_by,created_date,msg_status;


    public MessageUnread(String id, String empid, String message, String send_by, String created_date, String msg_status) {
        this.id=id;
        this.empid=empid;
        this.message=message;
        this.send_by=send_by;
        this.created_date=created_date;
        this.msg_status=msg_status;


    }

    public String geturId(){
        return id;
    }
    public void seturId(String id){
        this.id=id;
    }
    public String geturEmpid(){
        return empid;
    }
    public void seturEmpid(String empid){
        this.empid=empid;
    }
    public String geturMessage(){
        return message;
    }
    public void seturMessage(String message){
        this.message=message;
    }
    public String geturSend_by(){
        return send_by;
    }
    public void seturSend_by(String send_by){
        this.send_by=send_by;
    }
    public String geturCreated_date(){
        return created_date;
    }
    public void seturCreated_date(String created_date){
        this.created_date=created_date;
    }
    public String geturMsg_status(){
        return msg_status;
    }
    public void seturMsg_status(String msg_status){
        this.msg_status=msg_status;
    }
}

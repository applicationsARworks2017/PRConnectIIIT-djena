package com.iiit.amaresh.demotrack.Pojo;

/**
 * Created by LIPL on 25/01/17.
 */
public class MessageRead {
    String id,empid,message,send_by,created_date,msg_status;


    public MessageRead(String id, String empid, String message, String send_by, String created_date, String msg_status) {
        this.id=id;
        this.empid=empid;
        this.message=message;
        this.send_by=send_by;
        this.created_date=created_date;
        this.msg_status=msg_status;


    }

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id=id;
    }
    public String getEmpid(){
        return empid;
    }
    public void setEmpid(String empid){
        this.empid=empid;
    }
    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message=message;
    }
    public String getSend_by(){
        return send_by;
    }
    public void setSend_by(String send_by){
        this.send_by=send_by;
    }
    public String getCreated_date(){
        return created_date;
    }
    public void setCreated_date(String created_date){
        this.created_date=created_date;
    }
    public String getMsg_status(){
        return msg_status;
    }
    public void setMsg_status(String msg_status){
        this.msg_status=msg_status;
    }
}

package com.iiit.amaresh.demotrack.Pojo;

/**
 * Created by LIPL on 17/01/17.
 */
public class UserListing {
    String u_id,u_emp_id,u_emp_name,u_emp_add,u_emp_mail,u_emp_phone,
            u_emp_address,u_emp_pass,u_emp_imei,u_empl_type,u_emp_state,
            u_emp_dist,u_emp_block,u_emp_desig,u_usertype,u_user_status;




    public UserListing(String id, String emp_id, String emp_name, String emp_add, String emp_mail,
                       String emp_phone, String emp_address, String emp_pass, String emp_imei,
                       String empl_type, String emp_state, String emp_dist, String emp_block,
                       String emp_desig, String usertype, String user_status) {

        this.u_id=id;
        this.u_emp_id=emp_id;
        this.u_emp_name=emp_name;
        this.u_emp_add=emp_add;
        this.u_emp_mail=emp_mail;
        this.u_emp_phone=emp_phone;
        this.u_emp_address=emp_address;
        this.u_emp_imei=emp_imei;
        this.u_emp_pass=emp_pass;
        this.u_empl_type=empl_type;
        this.u_emp_state=emp_state;
        this.u_emp_dist=emp_dist;
        this.u_emp_block=emp_block;
        this.u_emp_desig=emp_desig;
        this.u_usertype=usertype;
        this.u_user_status=user_status;
    }
    public String getU_id(){
        return u_id;
    }

    public void setU_id(String u_id){
        this.u_id=u_id;
    }
    public String getU_emp_id(){
        return u_emp_id;
    }

    public void setU_emp_id(String u_emp_id){
        this.u_emp_id=u_emp_id;
    }
    public String getU_emp_name(){
        return u_emp_name;
    }

    public void setU_emp_name(String u_emp_name){
        this.u_emp_name=u_emp_name;
    }

    public String getU_emp_mail(){
        return u_emp_mail;
    }

    public void setU_emp_mail(String u_emp_mail){
        this.u_emp_mail=u_emp_mail;
    }

    public String getU_emp_phone(){
        return u_emp_phone;
    }

    public void setU_emp_phone(String u_emp_phone){
        this.u_emp_phone=u_emp_phone;
    }

    public String getU_emp_block(){
        return u_emp_block;
    }

    public void setU_emp_block(String u_emp_block){
        this.u_emp_block=u_emp_block;
    }

    public String getU_emp_dist(){
        return u_emp_dist;
    }

    public void setU_emp_dist(String u_emp_dist){
        this.u_emp_dist=u_emp_dist;
    }

    public String getU_emp_state(){
        return u_emp_state;
    }

    public void setU_emp_state(String u_emp_state){
        this.u_emp_state=u_emp_state;
    }

    public String getU_emp_desig(){
        return u_emp_desig;
    }

    public void setU_emp_desig(String u_emp_desig){
        this.u_emp_desig=u_emp_desig;
    }

    public String getU_usertype(){
        return u_usertype;
    }

    public void setU_usertype(String u_usertype){
        this.u_usertype=u_usertype;
    }
    public String getU_user_status(){
        return u_user_status;
    }

    public void setU_user_status(String u_user_status){
        this.u_user_status=u_user_status;
    }
}

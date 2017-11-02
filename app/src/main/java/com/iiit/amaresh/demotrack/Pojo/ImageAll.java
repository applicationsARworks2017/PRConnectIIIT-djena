package com.iiit.amaresh.demotrack.Pojo;

/**
 * Created by LIPL on 16/02/17.
 */
public class ImageAll {
    String id,im_emp_id,im_filename,im_title,im_latitude,im_longitude,im_created,im_username,im_userdesg,im_add;
    public ImageAll(String id, String im_emp_id, String im_filename, String im_title, String im_latitude, String im_longitude, String im_created, String im_username,String im_userdesg,String im_add) {
        this.id=id;
        this.im_emp_id=im_emp_id;
        this.im_filename=im_filename;
        this.im_title=im_title;
        this.im_latitude=im_latitude;
        this.im_longitude=im_longitude;
        this.im_created=im_created;
        this.im_username=im_username;
        this.im_userdesg=im_userdesg;
        this.im_add=im_add;

    }
    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id=id;
    }
    public String getIm_emp_id(){
        return im_emp_id;
    }
    public void setIm_emp_id(String im_emp_id){
        this.im_emp_id=im_emp_id;
    }
    public String getIm_filename(){
        return im_filename;
    }
    public void setIm_filename(String im_filename){
        this.im_filename=im_filename;
    }
    public String getIm_title(){
        return im_title;
    }
    public void setIm_title(String im_title){
        this.im_title=im_title;
    }
    public String getIm_latitude(){
        return im_latitude;
    }
    public void setIm_latitude(String im_latitude){
        this.im_latitude=im_latitude;
    }
    public String getIm_longitude(){
        return im_longitude;
    }
    public void setIm_longitude(String im_longitude){
        this.im_latitude=im_longitude;
    }
    public String getIm_created(){
        return im_created;
    }
    public void setIm_created(String im_created){
        this.im_created=im_created;
    }
    public String getIm_username(){
        return im_username;
    }
    public void setIm_username(String im_username){
        this.im_username=im_username;
    }
    public String getIm_userdesg(){
        return im_userdesg;
    }
    public void setIm_userdesg(String im_userdesg){
        this.im_userdesg=im_userdesg;
    } public String getIm_add(){
        return im_add;
    }
    public void setIm_add(String im_add){
        this.im_add=im_add;
    }
}

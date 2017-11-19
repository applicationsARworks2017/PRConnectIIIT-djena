package com.iiit.amaresh.demotrack.Pojo;

import java.io.File;

/**
 * Created by RN on 11/7/2017.
 */

public class Oflinedata {
    String longitude,latitude,address,title,image,video;
    int user_id;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getUser_id() {
        return user_id;
    }

    public Oflinedata(int user_id, String latitude, String longitude, String s_title, String saddress, String video, String file) {
        this.user_id=user_id;
        this.latitude=latitude;
        this.longitude=longitude;
        this.title=s_title;
        this.address=saddress;
        this.video=video;
        this.image=file;

    }

    public Oflinedata() {

    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setlatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setlongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setaddress(String address) {
        this.address = address;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public void setvideo(String video) {
        this.video = video;
    }

    public void setimage(String image) {
        this.image = image;
    }
}

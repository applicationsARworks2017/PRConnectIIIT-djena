package com.iiit.amaresh.demotrack.Pojo;

/**
 * Created by RN on 11/7/2017.
 */

public class Oflinedata {
    String longitude,latitude,address,title,video,image;
    int user_id;
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

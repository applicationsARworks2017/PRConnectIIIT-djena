package com.iiit.amaresh.demotrack.Pojo;

/**
 * Created by LIPL on 21/01/17.
 */

public class LocationList {
    String loc_id,location_emp_id,loc_emp_phone,loc_latitude,loc_longitude,loc_created;
    public LocationList(String id, String emp_id, String emp_phone, String latitude, String longitude, String created) {

        this.loc_id=id;
        this.location_emp_id=emp_id;
        this.loc_emp_phone=emp_phone;
        this.loc_latitude=latitude;
        this.loc_longitude=longitude;
        this.loc_created=created;

    }
    public String getLoc_id(){
        return loc_id;
    }
    public void setLoc_id(String loc_id){
        this.loc_id=loc_id;
    }

    public String getLocation_emp_id(){
        return location_emp_id;
    }
    public void setLocation_emp_id(String location_emp_id){
        this.location_emp_id=location_emp_id;
    }

    public String getLoc_emp_phone(){
        return loc_emp_phone;
    }
    public void setLoc_emp_phone(String loc_emp_phone){
        this.loc_emp_phone=loc_emp_phone;
    }

    public String getLoc_latitude(){
        return loc_latitude;
    }
    public void setLoc_latitude(String loc_latitude){
        this.loc_latitude=loc_latitude;
    }

    public String getLoc_longitude(){
        return loc_longitude;
    }
    public void setLoc_longitude(String loc_longitude){
        this.loc_longitude=loc_longitude;
    }

    public String getLoc_created(){
        return loc_created;
    }
    public void setLoc_created(String loc_id){
        this.loc_created=loc_created;
    }
}

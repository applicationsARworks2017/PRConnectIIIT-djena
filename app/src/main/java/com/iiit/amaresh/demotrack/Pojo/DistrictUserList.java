package com.iiit.amaresh.demotrack.Pojo;

/**
 * Created by mobileapplication on 11/21/17.
 */

public class DistrictUserList {

    String d_id,stat_id,title;
    private boolean selected;


    public DistrictUserList(String d_id, String stat_id, String title) {
        this.d_id=d_id;
        this.stat_id=stat_id;
        this.title=title;
    }

    public String getD_id() {
        return d_id;
    }

    public void setD_id(String d_id) {
        this.d_id = d_id;
    }

    public String getStat_id() {
        return stat_id;
    }

    public void setStat_id(String stat_id) {
        this.stat_id = stat_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

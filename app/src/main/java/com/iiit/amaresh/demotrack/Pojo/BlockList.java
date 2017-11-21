package com.iiit.amaresh.demotrack.Pojo;

/**
 * Created by mobileapplication on 11/21/17.
 */

public class BlockList {
    String b_id,distric_id,b_title;
    private boolean selected;

    public BlockList(String b_id, String distric_id, String b_title) {
        this.b_id=b_id;
        this.distric_id=distric_id;
        this.b_title=b_title;
    }

    public String getB_id() {
        return b_id;
    }

    public void setB_id(String b_id) {
        this.b_id = b_id;
    }

    public String getDistric_id() {
        return distric_id;
    }

    public void setDistric_id(String distric_id) {
        this.distric_id = distric_id;
    }

    public String getB_title() {
        return b_title;
    }

    public void setB_title(String b_title) {
        this.b_title = b_title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

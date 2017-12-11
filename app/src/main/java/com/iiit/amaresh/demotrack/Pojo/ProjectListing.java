package com.iiit.amaresh.demotrack.Pojo;

/**
 * Created by mobileapplication on 12/11/17.
 */

public class ProjectListing {
    String id,title,created,modified;
    public ProjectListing(String id, String title, String created, String modified) {
        this.id=id;
        this.title=title;
        this.created=created;
        this.modified=modified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}

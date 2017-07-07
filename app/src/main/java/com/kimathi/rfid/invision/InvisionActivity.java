package com.kimathi.rfid.invision;

/**
 * Created by Kiano_Kimathi on 04.07.2017.
 */

public class InvisionActivity extends Object {

    private long id;
    private String description;

    public InvisionActivity(long id, String description) {
        this.id = id;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;

    }

    @Override
    public String toString() {
        if(id==-1) {
            return null;
        } else {
            return description + " (" + id + ")";
        }
    }
}

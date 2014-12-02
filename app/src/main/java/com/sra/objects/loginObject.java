package com.sra.objects;

import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jakobhartman on 11/14/14.
 */
public class loginObject implements Serializable {
    private String username;
    private ArrayList<String> permissions;
    private ArrayList<String> areaNames;
    private boolean loggedIn;

    public String getUsername() {
        return username;
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void addToRoles(String addition){
        permissions.add(addition);
    }

    public void addToAreas(String area){
        areaNames.add(area);
    }

    public ArrayList<String> getAreaNames() {
        return areaNames;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public loginObject(String username){
        this.loggedIn = false;
        this.permissions = new ArrayList<String>();
        this.username = username;
        this.areaNames = new ArrayList<String>();
    }
}

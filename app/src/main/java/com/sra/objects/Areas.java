package com.sra.objects;

import java.io.Serializable;
import java.util.ArrayList;



/**
 * Created by jakobhartman on 11/15/14.
 */
public class Areas implements Serializable {
    private String AreaName;
    private String ref;
    private ArrayList <Households> households;
    private String region;

    public Areas(){
        households = new ArrayList<Households>();
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        AreaName = areaName;
    }

    public ArrayList<Households> getHouseholds() {
        return households;
    }

    public Households getHousehold(String householdName) {
        for (Households h : households) {
            if (h.getHouseholdName().equals(householdName))
                return h;
        }
        return null;
    }

    public void addHousehold(Households household){
        households.add(household);
    }

    public void setHouseholds(ArrayList<Households> households) {
        this.households = households;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }
}

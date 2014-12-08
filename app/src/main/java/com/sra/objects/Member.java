package com.sra.objects;

import java.io.Serializable;

/**
 * Created by imac on 12/8/14.
 */
public class Member implements Serializable {
    private String currentArea;
    private String currentHousehold;
    private String memberName;

    public String getCurrentArea() {
        return currentArea;
    }

    public String getCurrentHousehold() {
        return currentHousehold;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setCurrentArea(String currentArea) {
        this.currentArea = currentArea;
    }

    public void setCurrentHousehold(String currentHousehold) {
        this.currentHousehold = currentHousehold;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Member(){
        this.currentArea = "";
        this.currentHousehold = "";
        this.memberName = "";
    }

}

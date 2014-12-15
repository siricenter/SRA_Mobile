package com.sra.objects;

import java.io.Serializable;

/**
 * Created by imac on 12/8/14.
 */
public class Member implements Serializable {
    private String ref;
    private String memberName;

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }

    public String getMemberName() {
        return memberName;
    }



    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Member(){
        this.memberName = "";
    }

}

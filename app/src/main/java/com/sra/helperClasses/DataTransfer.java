package com.sra.helperClasses;

import com.sra.objects.Areas;

import java.util.ArrayList;

/**
 * Created by jakobhartman on 12/10/14.
 */
public class DataTransfer {

    private ArrayList<Areas> areasToLocal;
    private ArrayList<Areas> areasToFirebase;

    public DataTransfer(){
        this.areasToLocal = new ArrayList<Areas>();
        this.areasToFirebase = new ArrayList<Areas>();
    }

    public void setAreasToFirebase(ArrayList<Areas> areasToFirebase) {
        this.areasToFirebase = areasToFirebase;
    }

    public ArrayList<Areas> getAreasToFirebase() {
        return areasToFirebase;
    }

    public void setAreasToLocal(ArrayList<Areas> areasToLocal) {
        this.areasToLocal = areasToLocal;
    }

    public ArrayList<Areas> getAreasToLocal() {
        return areasToLocal;
    }

    public void addAreasToLocal(Areas area){
        areasToLocal.add(area);
    }

    public void addAreasToFirebase(Areas area){
        areasToFirebase.add(area);
    }
}

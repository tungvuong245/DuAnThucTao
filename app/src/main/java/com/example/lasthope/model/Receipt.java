package com.example.lasthope.model;

import java.util.ArrayList;

public class Receipt {
    private String id;
    private ArrayList<ProductToOder> listPoduct;
    private Double monney;
    private String time;
    private String idU ;

    public Receipt() {
    }

    public Receipt(String id, ArrayList<ProductToOder> listPoduct, Double monney, String time, String idU) {
        this.id = id;
        this.listPoduct = listPoduct;
        this.monney = monney;
        this.time = time;
        this.idU = idU;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<ProductToOder> getListPoduct() {
        return listPoduct;
    }

    public void setListPoduct(ArrayList<ProductToOder> listPoduct) {
        this.listPoduct = listPoduct;
    }

    public Double getMonney() {
        return monney;
    }

    public void setMonney(Double monney) {
        this.monney = monney;
    }
    public String getIdU() {
        return idU;
    }

    public void setIdU(String idU) {
        this.idU = idU;
    }
}

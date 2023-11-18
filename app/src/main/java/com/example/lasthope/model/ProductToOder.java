package com.example.lasthope.model;

public class ProductToOder {

    private String id ;
    private String idP;
    private String nameProduct;
    private double price;
    private int soLuong;
    private TypeProduct typeProduct;

    private String note;
    private String time;


    public ProductToOder() {
    }

    public ProductToOder(String id,String idP, String nameProduct, double price, int soLuong, TypeProduct typeProduct, String note, String time) {
        this.id = id;
        this.idP = idP;
        this.nameProduct = nameProduct;
        this.price = price;
        this.soLuong = soLuong;
        this.typeProduct = typeProduct;

        this.note = note;
        this.time = time;
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

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public TypeProduct getTypeProduct() {
        return typeProduct;
    }

    public void setTypeProduct(TypeProduct typeProduct) {
        this.typeProduct = typeProduct;
    }



    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIdP() {
        return idP;
    }

    public void setIdP(String idP) {
        this.idP = idP;
    }
}

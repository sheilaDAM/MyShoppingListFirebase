package com.sheilajnieto.myshoppinglistfirebase.models;/*
@author sheila j. nieto 
@version 0.1 2024 -01 - 09
*/

import java.util.ArrayList;
import java.util.Date;

public class ListClass {
    private String name;
    private String date;
    private int productQuantity;
   private ArrayList<ProductList> productsList;

    public ListClass() {
    }

    public ListClass(String name) {
        this.name = name;
    }

    public ListClass(String name, String date, int productQuantity) {
        this.name = name;
        this.date = date;
        this.productQuantity = productQuantity;
    }

    public ListClass(String name, String date, int productQuantity, ArrayList<ProductList> productsList) {
        this.name = name;
        this.date = date;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<ProductList> getProductsList() {
        return productsList;
    }

    public void setProductsList(ArrayList<ProductList> productsList) {
        this.productsList = productsList;
    }
}

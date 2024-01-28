package com.sheilajnieto.myshoppinglistfirebase.models;/*
@author sheila j. nieto 
@version 0.1 2024 -01 - 09
*/

public class Product {

    private String name;
    private boolean isPurchased;
    private String productImage;

    public Product() {
    }

    public Product(String name, Boolean isPurchased, String image) {
        this.name = name;
        this.isPurchased = isPurchased;
        this.productImage = image;
    }



    public boolean isPurchased() {
        return isPurchased;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return productImage;
    }


    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

    public void setImage(String image) {
        this.productImage = image;
    }

    public void setName(String name) {
        this.name = name;
    }

}

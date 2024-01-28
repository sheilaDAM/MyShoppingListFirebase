package com.sheilajnieto.myshoppinglistfirebase.models;/*
@author sheila j. nieto 
@version 0.1 2024 -01 - 09
*/

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private String name;
    private String categoryImage;
    private List<Product> products;

    public Category() {
    }

    public Category(String name, String image, List<Product> products) {
        this.name = name;
        this.categoryImage = image;
        this.products = products;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return categoryImage;
    }

    public void setImage(String image) {
        this.categoryImage = image;
    }
}

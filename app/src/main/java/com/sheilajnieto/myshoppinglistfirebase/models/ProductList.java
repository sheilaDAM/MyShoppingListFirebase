package com.sheilajnieto.myshoppinglistfirebase.models;/*
@author sheila j. nieto 
@version 0.1 2024 -01 - 09
*/

public class ProductList {

    private int fkListId;
    private int fkProductId;
    private boolean isPurchased;

    public ProductList() {
    }

    public ProductList(int fkListId, int fkProductId, boolean isPurchased) {
        this.fkListId= fkListId;
        this.fkProductId = fkProductId;
        this.isPurchased = isPurchased;
    }


    public int getFkListId() {
        return fkListId;
    }

    public void setFkListId(int fkListId) {
        this.fkListId = fkListId;
    }

    public int getFkProductId() {
        return fkProductId;
    }

    public void setFkProductId(int fkProductId) {
        this.fkProductId = fkProductId;
    }

    public void setPurchased(boolean purchased) {
        isPurchased = purchased;
    }

    public boolean isPurchased() {
        return isPurchased;
    }
}

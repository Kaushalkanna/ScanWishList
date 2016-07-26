package com.kaushal.barcodescanner;

public class Item {
    private String upc;
    private String name;
    private String price;
    private String imageurl;
    private String producturl;
    private String storename;

    public String getUPC() {
        return upc;
    }

    public void setUPC(String name) {
        this.upc = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageurl;
    }

    public void setImageUrl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getProductUrl() {
        return producturl;
    }

    public void setProductUrl(String producturl) {
        this.producturl = producturl;
    }

    public String getStoreName() {
        return storename;
    }

    public void setStoreName(String storename) {
        this.storename = storename;
    }

}

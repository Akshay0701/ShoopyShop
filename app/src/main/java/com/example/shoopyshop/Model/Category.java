package com.example.shoopyshop.Model;

public class Category {
    private String Name;
    private String Image;
    private String cId;

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public Category(String name, String image) {
        Name = name;
        Image = image;
    }

    public Category() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}

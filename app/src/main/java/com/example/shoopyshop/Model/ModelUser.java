package com.example.shoopyshop.Model;

public class ModelUser {


    String backimg,email,image,name,phone,uid;

    public ModelUser() {
    }


    public ModelUser(String backimg, String email, String image, String name, String phone, String uid) {
        this.backimg = backimg;
        this.email = email;
        this.image = image;
        this.name = name;
        this.phone = phone;
        this.uid = uid;
    }

    public String getBackimg() {
        return backimg;
    }

    public void setBackimg(String backimg) {
        this.backimg = backimg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

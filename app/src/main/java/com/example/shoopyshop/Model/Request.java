package com.example.shoopyshop.Model;

import java.util.List;

public class Request {

    private String phoneno;
    private String name;
    private String address;
    private String total;
    private String status,id;
    private List<Order> foods;//list of foods

    public Request() {
    }

    public Request(String phoneno, String name, String address, String total, String status, String id, List<Order> foods) {
        this.phoneno = phoneno;
        this.name = name;
        this.address = address;
        this.total = total;
        this.status = status;
        this.id = id;
        this.foods = foods;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}

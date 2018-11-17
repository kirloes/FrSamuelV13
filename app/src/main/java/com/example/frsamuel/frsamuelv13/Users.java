package com.example.frsamuel.frsamuelv13;

public class Users {
    private String name;
    private String phone;
    private String address;
    private String user_id;


    public Users()
    {

    }

    public Users(String user_id, String name, String phone, String address)
    {
        this.name = name;
        this.user_id = user_id;
        this.phone = phone;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}

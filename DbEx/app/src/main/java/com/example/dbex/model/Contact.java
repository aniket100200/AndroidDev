package com.example.dbex.model;

import org.jetbrains.annotations.NotNull;

public class Contact {
    int id;
    String name;
    String phoneNo;

    public Contact(@NotNull String name, @NotNull String phoneNo) {
        this.name = name;
        this.phoneNo = phoneNo;
    }

    public Contact(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    @Override
    public String toString() {
        return name + " - " + phoneNo;
    }
}

package com.adarshpanig.zomatotaskapp;

public class RestaurantItem {
    private String Rname;
    private String Raddress;
    private String Rcuisines;

    public RestaurantItem(String name, String address, String cuisines) {
        Rname = name;
        Raddress = address;
        Rcuisines = cuisines;
    }

    public String getRname() {
        return Rname;
    }

    public String getRaddress() {
        return Raddress;
    }

    public String getRcuisines() {
        return Rcuisines;
    }
}
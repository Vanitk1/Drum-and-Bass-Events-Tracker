package com.app.model;

public class Venue {

    private String venueName;
    private String address;
    private String city;


    public Venue(String venueName, String address, String city) {
        this.venueName = venueName;
        this.address = address;
        this.city = city;
    }

    public String getVenueName() {
        return venueName;
    }
    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return venueName + ", " + address + ", " + city;
    }
}

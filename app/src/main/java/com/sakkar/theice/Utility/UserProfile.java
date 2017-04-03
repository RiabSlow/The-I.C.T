package com.sakkar.theice.Utility;


public class UserProfile {
    private String email,name,phone,roll,season,photoUrl;

    public UserProfile(String email, String name, String phone, String roll, String season, String photoUrl) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.roll = roll;
        this.season = season;
        this.photoUrl = photoUrl;
    }

    public UserProfile(String email, String name, String roll, String season) {
        this.email = email;
        this.name = name;
        this.roll = roll;
        this.season = season;
    }


    public UserProfile() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}

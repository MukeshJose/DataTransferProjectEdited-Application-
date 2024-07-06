package com.example.datatransferprojectedited.model;

import com.google.gson.annotations.SerializedName;

public class Datum {

    private int id;
    @SerializedName("employee_name")
    private String username;
    @SerializedName("employee_salary")
    private String salary;
    @SerializedName("employee_age")
    private String age;
    @SerializedName("profile_image")
    private String profileImage;

    // Add fields for phone number and date of birth
    private String phoneNumber;
    private String dob;

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}

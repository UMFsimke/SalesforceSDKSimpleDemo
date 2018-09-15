package com.example.simpledemo.model.pojo.domain;

import com.example.simpledemo.model.pojo.salesforce.SalesforceUser;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName(SalesforceUser.FIELD_ID) private String id;
    @SerializedName(SalesforceUser.FIELD_USERNAME) private String username;
    @SerializedName(SalesforceUser.FIELD_NAME) private String name;
    @SerializedName(SalesforceUser.FIELD_ABOUT_ME) private String aboutMe;
    @SerializedName(SalesforceUser.FIELD_ADDRESS) private String address;
    @SerializedName(SalesforceUser.FIELD_COMPANY_NAME) private String companyName;
    @SerializedName(SalesforceUser.FIELD_DEPARTMENT) private String department;
    @SerializedName(SalesforceUser.FIELD_BANNER_PHOTO_URL) private String bannerPhotoUrl;
    @SerializedName(SalesforceUser.FIELD_MOBILE_PHONE) private String mobilePhone;
    @SerializedName(SalesforceUser.FIELD_PHONE) private String phone;
    @SerializedName(SalesforceUser.FIELD_PHOTO_URL) private String photoUrl;

    public static User parse(SalesforceUser user, Gson gson) {
        return gson.fromJson(user.getRawData().toString(), User.class);
    }

    private User() { }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBannerPhotoUrl() {
        return bannerPhotoUrl;
    }

    public void setBannerPhotoUrl(String bannerPhotoUrl) {
        this.bannerPhotoUrl = bannerPhotoUrl;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}

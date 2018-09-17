package com.example.simpledemo.model.pojo.domain;

import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("street") private String streetName;
    @SerializedName("city") private String city;
    @SerializedName("country") private String country;
    @SerializedName("postalCode") private String postalCode;

    public String formattedName() {
        return String.format("%s %s, %s %s", streetName, postalCode, city, country);
    }
}

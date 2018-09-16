package com.example.simpledemo.model.pojo.domain;

import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("street_number") private int streetNumber;
    @SerializedName("street_name") private String streetName;
    @SerializedName("city") private String city;

    public String formattedName() {
        return String.format("%s %d, %s", streetName, streetNumber, city);
    }
}

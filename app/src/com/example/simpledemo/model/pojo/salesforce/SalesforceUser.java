package com.example.simpledemo.model.pojo.salesforce;


import com.salesforce.androidsdk.smartsync.model.SalesforceObject;
import com.salesforce.androidsdk.smartsync.util.Constants;

import org.json.JSONObject;

public class SalesforceUser extends SalesforceObject {

    private static final String USER_OBJECT_TYPE = "User";
    public static final String FIELD_ID = Constants.ID;
    public static final String FIELD_USERNAME = "Username";
    public static final String FIELD_NAME = "Name";
    public static final String FIELD_ABOUT_ME = "AboutMe";
    public static final String FIELD_ADDRESS = "Address";
    public static final String FIELD_COMPANY_NAME = "CompanyName";
    public static final String FIELD_DEPARTMENT = "Department";
    public static final String FIELD_MOBILE_PHONE = "MobilePhone";
    public static final String FIELD_PHONE = "Phone";
    public static final String FIELD_PHOTO_URL = "MediumPhotoUrl";
    public static final String FIELD_BANNER_PHOTO_URL = "BannerPhotoUrl";

    public SalesforceUser(JSONObject data) {
        super(data);
    }
}
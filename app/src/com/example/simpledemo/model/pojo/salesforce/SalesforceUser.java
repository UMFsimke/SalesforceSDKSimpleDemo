package com.example.simpledemo.model.pojo.salesforce;


import android.text.TextUtils;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.model.pojo.domain.Address;
import com.example.simpledemo.model.pojo.domain.User;
import com.salesforce.androidsdk.smartsync.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class SalesforceUser extends SalesforceSyncable<User> {

    private static final String USER_OBJECT_TYPE = "User";
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

    @Override
    public void updateFrom(User user) throws JSONException {
        boolean isCreated = TextUtils.isEmpty(user.getId());

        if (isCreated) {
            generateId();
            final JSONObject attributes = new JSONObject();
            attributes.put(Constants.TYPE.toLowerCase(), USER_OBJECT_TYPE);
            updateField(Constants.ATTRIBUTES, attributes, true);
        }

        updateField(FIELD_NAME, user.getName(), isCreated);
        updateField(FIELD_ABOUT_ME, user.getAboutMe(), isCreated);
        updateField(FIELD_ADDRESS, MainApplication.getInstance().graph().getGson()
                .toJson(user.getAddress(), Address.class), isCreated);
        updateField(FIELD_MOBILE_PHONE, user.getMobilePhone(), isCreated);
        updateField(FIELD_PHONE, user.getPhone(), isCreated);
        updateField(FIELD_DEPARTMENT, user.getDepartment(), isCreated);
    }
}
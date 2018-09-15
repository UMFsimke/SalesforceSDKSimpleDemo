package com.example.simpledemo.repository.model.salesforce;

import com.salesforce.androidsdk.smartsync.model.SalesforceObject;
import com.salesforce.androidsdk.smartsync.util.Constants;

import org.json.JSONObject;

public class SalesforceEvent extends SalesforceObject {

    private static final String EVENT_OBJECT_TYPE = "Event";
    public static final String FIELD_ID = Constants.ID;
    public static final String FIELD_CREATED_BY_ID = "CreatedById";
    public static final String FIELD_DESCRIPTION = "Description";
    public static final String FIELD_LOCATION = "Location";
    public static final String FIELD_PRIVATE = "IsPrivate";
    public static final String FIELD_START_DATE_TIME = "StartDateTime";
    public static final String FIELD_END_DATE_TIME = "EndDateTime";
    public static final String FIELD_EVENT_SUBJECT = "Subject";
    public static final String FIELD_TYPE = "Type";
    public static final String FIELD_IS_ALL_DAY = "IsAllDayEvent";

    public SalesforceEvent(JSONObject data) {
        super(data);
    }
}
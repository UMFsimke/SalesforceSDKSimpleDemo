package com.example.simpledemo.model.pojo.salesforce;

import android.text.TextUtils;

import com.example.simpledemo.model.pojo.domain.Event;
import com.salesforce.androidsdk.smartsync.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class SalesforceEvent extends SalesforceSyncable<Event> {

    private static final String EVENT_OBJECT_TYPE = "Event";
    public static final String FIELD_CREATED_BY_ID = "CreatedById";
    public static final String FIELD_DESCRIPTION = "Description";
    public static final String FIELD_LOCATION = "Location";
    public static final String FIELD_PRIVATE = "IsPrivate";
    public static final String FIELD_START_DATE_TIME = "StartDateTime";
    public static final String FIELD_END_DATE_TIME = "EndDateTime";
    public static final String FIELD_EVENT_SUBJECT = "Subject";
    public static final String FIELD_IS_ALL_DAY = "IsAllDayEvent";

    public SalesforceEvent(JSONObject data) {
        super(data);
    }

    @Override
    public void updateFrom(Event event) throws JSONException {
        boolean isCreated = TextUtils.isEmpty(event.getId());

        if (isCreated) {
            generateId();
            final JSONObject attributes = new JSONObject();
            attributes.put(Constants.TYPE.toLowerCase(), EVENT_OBJECT_TYPE);
            updateField(Constants.ATTRIBUTES, attributes, true);
        }

        updateField(FIELD_DESCRIPTION, event.getDescription(), isCreated);
        updateField(FIELD_LOCATION, event.getLocation(), isCreated);
        updateField(FIELD_PRIVATE, event.isPrivateEvent(), isCreated);
        updateField(FIELD_START_DATE_TIME, event.getStartDateTime(), isCreated);
        updateField(FIELD_END_DATE_TIME, event.getEndDateTime(), isCreated);
        updateField(FIELD_EVENT_SUBJECT, event.getSubject(), isCreated);
        updateField(FIELD_IS_ALL_DAY, event.isAllDay(), isCreated);
    }

    public String getCreatedById() {
        return rawData.optString(FIELD_CREATED_BY_ID);
    }
}
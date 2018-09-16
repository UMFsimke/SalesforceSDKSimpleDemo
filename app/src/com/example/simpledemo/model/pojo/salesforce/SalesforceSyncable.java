package com.example.simpledemo.model.pojo.salesforce;

import android.text.TextUtils;

import com.example.simpledemo.model.pojo.domain.Syncable;
import com.salesforce.androidsdk.smartsync.model.SalesforceObject;
import com.salesforce.androidsdk.smartsync.target.SyncTarget;
import com.salesforce.androidsdk.smartsync.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public abstract class SalesforceSyncable<T extends Syncable> extends SalesforceObject {

    public static final String ID_PREFIX = "local_";
    public static final String FIELD_ID = Constants.ID;
    public static final String LOCALLY_CREATED = SyncTarget.LOCALLY_CREATED;
    public static final String LOCALLY_DELETED = SyncTarget.LOCALLY_DELETED;
    public static final String LOCALLY_UPDATED = SyncTarget.LOCALLY_UPDATED;

    protected SalesforceSyncable(JSONObject data) {
        super(data);
    }

    public abstract void updateFrom(T syncable) throws JSONException;

    protected void generateId() throws JSONException {
        rawData.put(Constants.ID,  ID_PREFIX + System.currentTimeMillis());
    }

    protected void updateField(String key, JSONObject object, boolean newlyCreated) throws JSONException {
        String sanitizedNewValue = sanitizeText(object != null ? object.toString() : null);
        String oldValue = sanitizeText(rawData.optString(key));
        if (!oldValue.equals(sanitizedNewValue)) {
            update(key, object, newlyCreated);
        }
    }

    protected void updateField(String key, boolean newValue, boolean newlyCreated) throws JSONException {
        boolean oldValue = rawData.optBoolean(key);
        if (newValue != oldValue) {
            update(key, newValue, newlyCreated);
        }
    }

    protected void updateField(String key, Date newValue, boolean newlyCreated) throws JSONException {
        long oldValue = rawData.optLong(key);
        long newValueMillis = newValue != null ? newValue.getTime() : 0L;
        if (newValueMillis != oldValue) {
            update(key, newValue, newlyCreated);
        }
    }

    private <T> void update(String key, T value, boolean newlyCreated) throws JSONException {
        rawData.put(key, value);
        rawData.put(LOCALLY_UPDATED, !newlyCreated);
    }

    protected void updateField(String key, String newValue, boolean newlyCreated) throws JSONException {
        String sanitizedNewValue = sanitizeText(newValue);
        String oldValue = sanitizeText(rawData.optString(key));
        if (!oldValue.equals(sanitizedNewValue)) {
            update(key, sanitizedNewValue, newlyCreated);
        }
    }

    private String sanitizeText(String text) {
        if (TextUtils.isEmpty(text)) {
            return Constants.EMPTY_STRING;
        }

        return text;
    }
}

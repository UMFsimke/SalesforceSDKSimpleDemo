package com.example.simpledemo.model.pojo.domain;

import com.example.simpledemo.model.pojo.salesforce.SalesforceSyncable;
import com.google.gson.annotations.SerializedName;

public abstract class Syncable<T extends SalesforceSyncable> {

    @SerializedName(SalesforceSyncable.FIELD_ID) private String id;
    @SerializedName(SalesforceSyncable.LOCAL) private boolean local;
    @SerializedName(SalesforceSyncable.LOCALLY_CREATED) private boolean locallyCreated;
    @SerializedName(SalesforceSyncable.LOCALLY_DELETED) private boolean locallyUpdated;
    @SerializedName(SalesforceSyncable.LOCALLY_UPDATED) private boolean locallyDeleted;
    private boolean locallyModified;

    protected void updateLocalFlags() {
        locallyModified = locallyCreated || locallyUpdated || locallyDeleted;
    }

    public String getId() {
        return id;
    }
}

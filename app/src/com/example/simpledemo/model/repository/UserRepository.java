package com.example.simpledemo.model.repository;

import com.example.simpledemo.model.pojo.domain.User;
import com.example.simpledemo.model.pojo.salesforce.SalesforceSyncable;
import com.example.simpledemo.model.pojo.salesforce.SalesforceUser;
import com.salesforce.androidsdk.smartstore.store.SmartStore;
import com.salesforce.androidsdk.smartsync.manager.SyncManager;

import org.json.JSONObject;

public class UserRepository extends BaseSyncableRepository<User> {

    private static final String SOUP_NAME = "users";
    private static final String SYNC_DOWN_STRATEGY_NAME = "syncDownUsers";

    public UserRepository(SmartStore smartStore, SyncManager syncManager) {
        super(smartStore, syncManager);
    }

    @Override
    protected String getSoupName() {
        return SOUP_NAME;
    }

    @Override
    protected String getOrderBy() {
        return SalesforceUser.FIELD_NAME;
    }

    @Override
    protected SalesforceUser parseSalesforceObject(JSONObject object) {
        return new SalesforceUser(object);
    }

    @Override
    protected User mapFromSalesforceSyncable(SalesforceSyncable salesforceSyncable) {
        if (salesforceSyncable instanceof SalesforceUser) {
            return User.parse((SalesforceUser) salesforceSyncable);
        }

        return null;
    }

    @Override
    protected String getSyncDownStrategyName() {
        return SYNC_DOWN_STRATEGY_NAME;
    }

    @Override
    protected String getSyncUpStrategyName() {
        return null;
    }

    public User getUser(String userId) {
        return null;
    }
}

package com.example.simpledemo.model.repository;

import com.example.simpledemo.model.pojo.domain.User;
import com.example.simpledemo.model.pojo.salesforce.SalesforceSyncable;
import com.example.simpledemo.model.pojo.salesforce.SalesforceUser;
import com.salesforce.androidsdk.smartstore.store.QuerySpec;
import com.salesforce.androidsdk.smartstore.store.SmartStore;
import com.salesforce.androidsdk.smartsync.manager.SyncManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.Single;

public class UserRepository extends BaseSyncableRepository<User> {

    private static final String QUERY_GET_USER_BY_ID = "SELECT {users:_soup} FROM {users} WHERE {users:Id} == '%s'";
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
        try {
            String query = String.format(QUERY_GET_USER_BY_ID, userId);
            JSONArray results = smartStore.query(QuerySpec.buildSmartQuerySpec(query, Integer.MAX_VALUE), 0);

            if (results != null && results.length() > 0) {
                JSONObject el = results.getJSONArray(0).getJSONObject(0);

                SalesforceUser salesforceUser = parseSalesforceObject(el);
                User user = mapFromSalesforceSyncable(salesforceUser);
                return user;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Observable<User> fetch(String userId) {
        return dataChanged
                .flatMap(changed -> {
                    User user = getUser(userId);
                    if (user != null) {
                        return Observable.just(user);
                    }

                    return Observable.empty();
                });
    }
}

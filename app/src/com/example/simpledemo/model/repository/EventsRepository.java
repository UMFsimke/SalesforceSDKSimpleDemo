package com.example.simpledemo.model.repository;

import com.example.simpledemo.model.pojo.domain.Event;
import com.example.simpledemo.model.pojo.domain.User;
import com.example.simpledemo.model.pojo.salesforce.SalesforceEvent;
import com.example.simpledemo.model.pojo.salesforce.SalesforceSyncable;
import com.salesforce.androidsdk.smartstore.store.SmartStore;
import com.salesforce.androidsdk.smartsync.manager.SyncManager;

import org.json.JSONObject;

public class EventsRepository extends BaseSyncableRepository<Event> {

    private static final String SOUP_NAME = "events";
    private static final String SYNC_DOWN_STRATEGY_NAME = "syncDownEvents";
    private static final String SYNC_UP_STRATEGY_NAME = "syncUpEvents";

    private UserRepository userRepository;

    public EventsRepository(UserRepository userRepository, SmartStore smartStore, SyncManager syncManager) {
        super(smartStore, syncManager);
        this.userRepository = userRepository;
    }

    @Override
    protected String getSoupName() {
        return SOUP_NAME;
    }

    @Override
    protected String getOrderBy() {
        return SalesforceEvent.FIELD_START_DATE_TIME;
    }

    @Override
    protected SalesforceEvent parseSalesforceObject(JSONObject object) {
        return new SalesforceEvent(object);
    }

    @Override
    protected Event mapFromSalesforceSyncable(SalesforceSyncable salesforceSyncable) {
        if (salesforceSyncable instanceof SalesforceEvent) {
            SalesforceEvent salesforceEvent = (SalesforceEvent) salesforceSyncable;
            User user = userRepository.getUser(salesforceEvent.getCreatedById());
            return Event.parse(salesforceEvent, user);
        }

        return null;
    }

    @Override
    protected String getSyncDownStrategyName() {
        return SYNC_DOWN_STRATEGY_NAME;
    }

    @Override
    protected String getSyncUpStrategyName() {
        return SYNC_UP_STRATEGY_NAME;
    }
}

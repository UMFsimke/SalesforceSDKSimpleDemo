package com.example.simpledemo.model.repository;

import com.example.simpledemo.model.pojo.domain.Event;
import com.example.simpledemo.model.pojo.domain.User;
import com.example.simpledemo.model.pojo.salesforce.SalesforceEvent;
import com.example.simpledemo.model.pojo.salesforce.SalesforceSyncable;
import com.salesforce.androidsdk.smartstore.store.QuerySpec;
import com.salesforce.androidsdk.smartstore.store.SmartStore;
import com.salesforce.androidsdk.smartsync.manager.SyncManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class EventsRepository extends BaseSyncableRepository<Event> {

    private static final String QUERY_GET_EVENTS_FOR_USER_WITH_ID = "SELECT {events:_soup} FROM {events} WHERE {events:CreatedById} == '%s'";
    private static final String QUERY_GET_EVENT_WITH_ID = "SELECT {events:_soup} FROM {events} WHERE {events:Id} == '%s'";

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

    public Observable<List<Event>> getEventsForUser(String userId) {
        return dataChanged
                .map(dataUpdated -> String.format(QUERY_GET_EVENTS_FOR_USER_WITH_ID, userId))
                .flatMap(smartQuery -> Observable.just(QuerySpec.buildSmartQuerySpec(smartQuery,
                        Integer.MAX_VALUE)))
                .map(query -> smartStore.query(query, 0))
                .map(this::mapToEvents);
    }

    private List<Event> mapToEvents(JSONArray results) throws JSONException {
        List<Event> events = new ArrayList<>();
        if (results != null && results.length() > 0) {
            JSONArray rows = results.getJSONArray(0);
            for (int i = 0; i < rows.length(); i++) {
                JSONObject el = rows.getJSONObject(i);

                SalesforceEvent salesforceEvent = parseSalesforceObject(el);
                Event event = mapFromSalesforceSyncable(salesforceEvent);
                events.add(event);
            }
        }

        return events;
    }

    public Observable<Event> getEvent(String eventId) {
        return dataChanged
                .map(dataUpdated -> String.format(QUERY_GET_EVENT_WITH_ID, eventId))
                .flatMap(smartQuery -> Observable.just(QuerySpec.buildSmartQuerySpec(smartQuery,
                        Integer.MAX_VALUE)))
                .map(query -> smartStore.query(query, 0))
                .map(this::mapToEvents)
                .map(events -> events.get(0));
    }
}

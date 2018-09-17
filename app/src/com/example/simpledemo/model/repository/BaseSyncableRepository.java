package com.example.simpledemo.model.repository;

import android.text.TextUtils;
import android.util.Log;

import com.example.simpledemo.model.pojo.domain.Syncable;
import com.example.simpledemo.model.pojo.salesforce.SalesforceSyncable;
import com.example.simpledemo.utils.ListUtils;
import com.salesforce.androidsdk.smartstore.store.QuerySpec;
import com.salesforce.androidsdk.smartstore.store.SmartStore;
import com.salesforce.androidsdk.smartsync.manager.SyncManager;
import com.salesforce.androidsdk.smartsync.util.SyncState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.BehaviorSubject;

public abstract class BaseSyncableRepository<T extends Syncable> implements Repository {

    protected SmartStore smartStore;
    private SyncManager syncManager;
    protected BehaviorSubject<Boolean> dataChanged;

    protected abstract String getSoupName();
    protected abstract String getOrderBy();
    protected abstract String getSyncDownStrategyName();
    protected abstract String getSyncUpStrategyName();

    public BaseSyncableRepository(SmartStore smartStore, SyncManager syncManager) {
        this.smartStore = smartStore;
        this.syncManager = syncManager;
        dataChanged = BehaviorSubject.createDefault(false);
    }

    public Observable<List<T>> getAll() {
        return dataChanged
                .flatMap(dataUpdated -> Observable.just(
                        QuerySpec.buildAllQuerySpec(getSoupName(), getOrderBy(),
                                QuerySpec.Order.ascending, Integer.MAX_VALUE)))
                .map(query -> smartStore.query(query, 0))
                .map(this::mapToSalesforceSyncables)
                .map(this::mapToDomainObjects);
    }

    protected List<SalesforceSyncable> mapToSalesforceSyncables(JSONArray array) {
        List<SalesforceSyncable> results = new ArrayList<>();
        if (array == null) {
            return results;
        }

        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(parseSalesforceObject(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    protected abstract <R extends SalesforceSyncable> R parseSalesforceObject(JSONObject object);

    protected List<T> mapToDomainObjects(List<SalesforceSyncable> salesforceSyncables) {
        List<T> results = new ArrayList<>();
        if (ListUtils.isEmpty(salesforceSyncables)) {
            return results;
        }

        for (SalesforceSyncable syncable : salesforceSyncables) {
            results.add(mapFromSalesforceSyncable(syncable));
        }

        return results;
    }

    protected abstract T mapFromSalesforceSyncable(SalesforceSyncable salesforceSyncable);

    @Override
    public Single<Boolean> syncDown() {
        return performSync(getSyncDownStrategyName());
    }

    protected Single<Boolean> performSync(String syncStrategyName) {
        if (TextUtils.isEmpty(syncStrategyName)) {
            return Single.just(true);
        }

        return Single.create(downstream -> syncManager.reSync(syncStrategyName,
                syncResult -> {
                    if (downstream.isDisposed()) { return; }

                    if (SyncState.Status.DONE.equals(syncResult.getStatus())) {
                        downstream.onSuccess(true);
                        dataChanged.onNext(true);
                    } else if (SyncState.Status.FAILED.equals(syncResult.getStatus())) {
                        downstream.onSuccess(false);
                    }
                })
        );
    }

    @Override
    public Single<Boolean> syncUp() {
        return performSync(getSyncUpStrategyName())
                .flatMap(dataSent -> performSync(getSyncDownStrategyName()));
    }
}

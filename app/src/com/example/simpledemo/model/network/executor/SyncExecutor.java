package com.example.simpledemo.model.network.executor;

import io.reactivex.Observable;

public interface SyncExecutor {

    void performFullSync();
    Observable<Boolean> performFullSyncBlocking();
    void performSyncDownOnly();
}

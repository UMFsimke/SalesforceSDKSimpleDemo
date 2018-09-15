package com.example.simpledemo.model.network.executor;

public interface SyncExecutor {

    void performFullSync();
    void performSyncDownOnly();
}

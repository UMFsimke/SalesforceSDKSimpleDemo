package com.example.simpledemo.syncs;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.model.network.executor.SyncExecutor;
import com.example.simpledemo.model.network.executor.SyncExecutorImpl;
import com.salesforce.androidsdk.smartsync.app.SmartSyncSDKManager;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private SyncExecutor syncExecutor;

    public SyncAdapter(Context context, boolean autoInitialize,
                       boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        MainApplication.getInstance().initGraph(SmartSyncSDKManager.getInstance().getClientManager().peekRestClient(account));
        syncExecutor = MainApplication.getInstance().graph().getSyncExecutor();
        syncExecutor.performFullSyncBlocking().blockingSubscribe();
    }
}
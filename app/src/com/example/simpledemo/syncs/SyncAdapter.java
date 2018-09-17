package com.example.simpledemo.syncs;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.model.network.executor.SyncExecutorImpl;

public class SyncAdapter extends AbstractThreadedSyncAdapter {


    public SyncAdapter(Context context, boolean autoInitialize,
                       boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        MainApplication.getInstance().initGraph(null);
        MainApplication.getInstance().graph().getSyncExecutor().performFullSync();
    }
}
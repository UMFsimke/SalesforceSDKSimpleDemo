package com.example.simpledemo.syncs;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SyncService extends Service {

    private static final Object SYNC_ADAPTER_LOCK = new Object();
    private static SyncAdapter SYNC_ADAPTER = null;

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (SYNC_ADAPTER_LOCK) {
            if (SYNC_ADAPTER == null) {
                SYNC_ADAPTER = new SyncAdapter(getApplicationContext(),
                        true, false);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return SYNC_ADAPTER.getSyncAdapterBinder();
    }
}
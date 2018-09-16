package com.example.simpledemo.model.di;

import android.app.Application;
import android.content.Context;

import com.salesforce.androidsdk.accounts.UserAccount;
import com.salesforce.androidsdk.smartstore.store.SmartStore;
import com.salesforce.androidsdk.smartsync.app.SmartSyncSDKManager;
import com.salesforce.androidsdk.smartsync.manager.SyncManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application app) {
        this.application = app;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    public Context provideApplicationContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    public SmartSyncSDKManager provideSmartSyncSDKManager() {
        return SmartSyncSDKManager.getInstance();
    }

    @Provides
    public UserAccount provideCurrentUser(SmartSyncSDKManager smartSyncSDKManager) {
        return smartSyncSDKManager.getUserAccountManager().getCurrentUser();
    }

    @Provides
    public SmartStore provideSmartStore(SmartSyncSDKManager smartSyncSDKManager,
                                        UserAccount currentUser) {
        return smartSyncSDKManager.getSmartStore(currentUser);
    }

    @Provides
    public SyncManager provideSyncManager(UserAccount currentUser) {
        return SyncManager.getInstance(currentUser);
    }


}

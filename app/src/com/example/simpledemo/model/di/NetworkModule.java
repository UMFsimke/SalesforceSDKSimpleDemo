package com.example.simpledemo.model.di;

import com.example.simpledemo.model.network.executor.RequestExecutor;
import com.example.simpledemo.model.network.executor.RequestExecutorImpl;
import com.example.simpledemo.model.network.executor.SyncExecutor;
import com.example.simpledemo.model.network.executor.SyncExecutorImpl;
import com.example.simpledemo.model.repository.Repository;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.smartsync.app.SmartSyncSDKManager;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
public class NetworkModule {

    private RestClient restClient;

    public NetworkModule(RestClient restClient) {
        this.restClient = restClient;
    }

    @Singleton
    @Provides
    public Gson provideGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    public RequestExecutor provideRequestExecutor() {
        return new RequestExecutorImpl(restClient, new JsonParser());
    }

    @Provides
    @Singleton
    public SyncExecutor provideSyncExecutor(SmartSyncSDKManager smartSyncSDKManager,
                                            List<Repository> repositories) {
        return new SyncExecutorImpl(smartSyncSDKManager,
                smartSyncSDKManager.getUserAccountManager(), repositories);
    }

    @Provides
    public OkHttpClient provideOkHttpClient() {
        return restClient.getOkHttpClient();
    }

}

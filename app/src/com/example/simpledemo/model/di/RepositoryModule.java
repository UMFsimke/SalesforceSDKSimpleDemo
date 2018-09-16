package com.example.simpledemo.model.di;

import com.example.simpledemo.model.repository.EventsRepository;
import com.example.simpledemo.model.repository.Repository;
import com.example.simpledemo.model.repository.UserRepository;
import com.salesforce.androidsdk.accounts.UserAccount;
import com.salesforce.androidsdk.smartstore.store.SmartStore;
import com.salesforce.androidsdk.smartsync.app.SmartSyncSDKManager;
import com.salesforce.androidsdk.smartsync.manager.SyncManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public UserRepository provideUserRepository(SmartStore smartStore, SyncManager syncManager) {
        return new UserRepository(smartStore, syncManager);
    }

    @Provides
    @Singleton
    public EventsRepository provideEventsRepository(UserRepository userRepository,
                                                    SmartStore smartStore,
                                                    SyncManager syncManager) {
        return new EventsRepository(userRepository, smartStore, syncManager);
    }

    @Provides
    @Singleton
    public List<Repository> provideRepositories(UserRepository userRepository,
                                                EventsRepository eventsRepository) {
        List<Repository> repositories = new ArrayList<>(1);
        repositories.add(userRepository);
        //repositories.add(eventsRepository);
        return repositories;
    }
}

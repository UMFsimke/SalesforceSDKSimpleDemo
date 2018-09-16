package com.example.simpledemo.model.di;

import android.app.Application;

import com.example.simpledemo.model.network.executor.SyncExecutor;
import com.example.simpledemo.model.repository.EventsRepository;
import com.example.simpledemo.model.repository.UserRepository;
import com.example.simpledemo.view.events.EventsListContract;
import com.example.simpledemo.view.users.UsersListContract;
import com.google.gson.Gson;
import com.salesforce.androidsdk.rest.RestClient;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * Graph component that is used to provide injectable members
 */
@Singleton
@Component(
        modules = { ApplicationModule.class, RepositoryModule.class, NetworkModule.class,
                PresentersModule.class }
)
public interface Graph {

    Gson getGson();
    UsersListContract.Presenter getUserListPresenter();
    EventsListContract.Presenter getEventsListPresenter();
    SyncExecutor getSyncExecutor();
    UserRepository getUserRepository();
    EventsRepository getEventsRepository();
    OkHttpClient getOkHttpClient();

    final class Initializer {
        public static Graph init(Application application, RestClient restClient) {
            return DaggerGraph.builder()
                    .applicationModule(new ApplicationModule(application))
                    .repositoryModule(new RepositoryModule())
                    .networkModule(new NetworkModule(restClient))
                    .presentersModule(new PresentersModule())
                    .build();
        }
    }
}

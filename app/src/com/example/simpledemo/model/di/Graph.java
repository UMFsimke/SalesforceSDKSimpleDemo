package com.example.simpledemo.model.di;

import android.app.Application;

import com.google.gson.Gson;
import com.salesforce.androidsdk.rest.RestClient;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Graph component that is used to provide injectable members
 */
@Singleton
@Component(
        modules = { ApplicationModule.class, RepositoryModule.class, NetworkModule.class }
)
public interface Graph {

    Gson getGson();

    final class Initializer {
        public static Graph init(Application application, RestClient restClient) {
            return DaggerGraph.builder()
                    .applicationModule(new ApplicationModule(application))
                    .repositoryModule(new RepositoryModule())
                    .networkModule(new NetworkModule(restClient))
                    .build();
        }
    }
}

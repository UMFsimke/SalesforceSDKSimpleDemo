package com.example.simpledemo.repository.network;

import com.google.gson.JsonElement;
import com.salesforce.androidsdk.rest.RestRequest;

import io.reactivex.Single;

public interface RequestExecutor {

    Single<JsonElement> execute(RestRequest request);
}

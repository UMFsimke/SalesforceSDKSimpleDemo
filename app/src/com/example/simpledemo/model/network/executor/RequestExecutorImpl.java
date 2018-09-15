package com.example.simpledemo.model.network.executor;

import com.example.simpledemo.model.network.exceptions.NetworkException;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.schedulers.Schedulers;

public class RequestExecutorImpl implements RequestExecutor {

    private RestClient restClient;
    private JsonParser jsonParser;

    RequestExecutorImpl(RestClient restClient, JsonParser jsonParser) {
        this.restClient = restClient;
        this.jsonParser = jsonParser;
    }

    @Override
    public Single<JsonElement> execute(final RestRequest request) {

        return Single.<JsonElement>create(emitter -> {
            try {
                executeRequest(request, emitter);
            } catch (IOException | JsonSyntaxException e) {
                if (emitter.isDisposed()) {
                    return;
                }

                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io());
    }

    private void executeRequest(final RestRequest request, SingleEmitter<JsonElement> emitter) throws IOException {
        RestResponse response = restClient.sendSync(request);
        response.consumeQuietly();
        if (response.isSuccess() && !emitter.isDisposed()) {
            JsonElement responseObj = jsonParser.parse(response.asString());
            emitter.onSuccess(responseObj);
            return;
        }

        if (!emitter.isDisposed()) {
            emitter.onError(new NetworkException(response.asString()));
        }
    }
}

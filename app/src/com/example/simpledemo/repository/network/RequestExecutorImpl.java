package com.example.simpledemo.repository.network;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

import java.io.IOException;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;

public class RequestExecutorImpl implements RequestExecutor {

    private RestClient restClient;
    private JsonParser jsonParser;

    RequestExecutorImpl(RestClient restClient, JsonParser jsonParser) {
        this.restClient = restClient;
        this.jsonParser = jsonParser;
    }

    @Override
    public Single<JsonElement> execute(final RestRequest request) {

        return Single.create( emitter -> {
            try {
                executeRequest(request, emitter);
            } catch (IOException | JsonSyntaxException e) {
                if (emitter.isDisposed()) {
                    return;
                }

                emitter.onError(e);
            }
        });
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

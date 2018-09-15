package com.example.simpledemo.model.network.requests;

import android.content.Context;

import com.salesforce.androidsdk.rest.ApiVersionStrings;
import com.salesforce.androidsdk.rest.RestRequest;

import java.io.UnsupportedEncodingException;

public class RequestCreator {

    private String versionNumber;

    public RequestCreator(Context context) {
        versionNumber = ApiVersionStrings.getVersionNumber(context);
    }

    public RestRequest query(Query query) throws UnsupportedEncodingException {
        return RestRequest.getRequestForQuery(versionNumber, query.raw());
    }
}

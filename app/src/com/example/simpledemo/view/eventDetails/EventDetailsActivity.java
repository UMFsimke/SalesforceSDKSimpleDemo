package com.example.simpledemo.view.eventDetails;

import android.os.Bundle;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.R;
import com.example.simpledemo.model.network.executor.SyncExecutor;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import butterknife.ButterKnife;

public class EventDetailsActivity extends SalesforceActivity {

    public static final String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";

    private String eventId;
    private SyncExecutor syncExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ButterKnife.bind(this);
        eventId = getIntent().getExtras().getString(EXTRA_EVENT_ID);
    }

    @Override
    public void onResume(RestClient client) {
        MainApplication.getInstance().initGraph(client);
        syncExecutor = MainApplication.getInstance().graph().getSyncExecutor();
        initDetailsFragment(eventId);
        syncExecutor.performSyncDownOnly();
    }

    private void initDetailsFragment(String eventId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, EventDetailsFragment.newInstance(eventId))
                .commit();
    }
}

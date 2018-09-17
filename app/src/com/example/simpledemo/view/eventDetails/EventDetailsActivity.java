package com.example.simpledemo.view.eventDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.R;
import com.example.simpledemo.model.network.executor.SyncExecutor;
import com.example.simpledemo.view.editEvent.EditEventActivity;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDetailsActivity extends SalesforceActivity {

    public static final String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";
    public static final int EVENT_DETAILS_REQUEST_CODE = 1231;
    public static final int EVENT_DELETED = 121;
    public static final int EVENT_CHANGED = 123;

    @BindView(R.id.toolbar) protected Toolbar toolbar;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        eventId = getIntent().getExtras().getString(EXTRA_EVENT_ID);
    }

    @Override
    public void onResume(RestClient client) {
        MainApplication.getInstance().initGraph(client);
        initDetailsFragment(eventId);
    }

    private void initDetailsFragment(String eventId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, EventDetailsFragment.newInstance(eventId))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.event_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit) {
            Intent intent = EditEventActivity.newInstance(this, eventId);
            startActivityForResult(intent, EVENT_DETAILS_REQUEST_CODE);
            return true;
        }

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EVENT_DETAILS_REQUEST_CODE && resultCode == EVENT_DELETED) {
            finish();
            return;
        }
    }
}

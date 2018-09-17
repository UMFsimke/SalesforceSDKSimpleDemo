package com.example.simpledemo.view.editEvent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.R;
import com.example.simpledemo.model.network.executor.SyncExecutor;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditEventActivity extends SalesforceActivity {

    private static final String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";

    @BindView(R.id.toolbar) protected Toolbar toolbar;

    public static Intent newInstance(Context context, String eventId) {
        Intent intent = new Intent(context, EditEventActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_EVENT_ID, eventId);
        intent.putExtras(bundle);
        return intent;
    }

    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        eventId = getIntent().getExtras().getString(EXTRA_EVENT_ID);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume(RestClient client) {
        MainApplication.getInstance().initGraph(client);
        initFragment(eventId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initFragment(String eventId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, EditEventFragment.newInstance(eventId))
                .commit();
    }
}

package com.example.simpledemo.view.userDetails;

import android.os.Bundle;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.R;
import com.example.simpledemo.model.network.executor.SyncExecutor;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import butterknife.ButterKnife;

public class UserDetailsActivity extends SalesforceActivity {

    public static final String EXTRA_USER_ID = "EXTRA_USER_ID";

    private String userId;
    private SyncExecutor syncExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        userId = getIntent().getExtras().getString(EXTRA_USER_ID);
    }

    @Override
    public void onResume(RestClient client) {
        MainApplication.getInstance().initGraph(client);
        syncExecutor = MainApplication.getInstance().graph().getSyncExecutor();
        initDetailsFragment(userId);
        syncExecutor.performSyncDownOnly();
    }

    private void initDetailsFragment(String userId) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, FragmentUserDetails.newInstance(userId))
                .commit();
    }
}

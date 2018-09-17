package com.example.simpledemo.view;

import android.accounts.Account;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.R;
import com.example.simpledemo.model.network.executor.SyncExecutor;
import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.smartsync.app.SmartSyncSDKManager;
import com.salesforce.androidsdk.smartsync.config.SyncsConfig;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends SalesforceActivity {

    private static final String SYNC_CONTENT_AUTHORITY = "com.example.simpledemo.syncs.SyncAdapter";
    private static final long SYNC_FREQUENCY_ONE_HOUR = 1 * 60 * 60;

    @BindView(R.id.toolbar) protected Toolbar toolbar;
	@BindView(R.id.pager) protected ViewPager viewPager;
	@BindView(R.id.tabs) protected TabLayout tabLayout;
	SyncExecutor syncExecutor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ButterKnife.bind(this);
		setSupportActionBar(toolbar);
	}

	private void initViewPager() {
	    if (viewPager.getAdapter() == null) {
            MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

	@Override
	public void onResume(RestClient client) {
		MainApplication.getInstance().initGraph(client);
		syncExecutor = MainApplication.getInstance().graph().getSyncExecutor();
        initViewPager();
        setupPeriodicSync();
        syncExecutor.performFullSync();
	}

	public void onLogoutClick() {
		SalesforceSDKManager.getInstance().logout(this);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == R.id.sign_out) {
	        onLogoutClick();
	        return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupPeriodicSync() {
        Account account = SmartSyncSDKManager.getInstance().getUserAccountManager().getCurrentAccount();
		ContentResolver.setSyncAutomatically(account, SYNC_CONTENT_AUTHORITY, true);
        ContentResolver.addPeriodicSync(account, SYNC_CONTENT_AUTHORITY,
                Bundle.EMPTY, SYNC_FREQUENCY_ONE_HOUR);
    }
}

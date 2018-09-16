package com.example.simpledemo.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.R;
import com.example.simpledemo.model.network.executor.SyncExecutor;
import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends SalesforceActivity {

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
	    MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
	    viewPager.setAdapter(adapter);
	    tabLayout.setupWithViewPager(viewPager);
    }

	@Override
	public void onResume(RestClient client) {
		MainApplication.getInstance().initGraph(client);
		syncExecutor = MainApplication.getInstance().graph().getSyncExecutor();
        initViewPager();
        syncExecutor.performSyncDownOnly();
	}

	public void onLogoutClick(View v) {
		SalesforceSDKManager.getInstance().logout(this);
	}

}
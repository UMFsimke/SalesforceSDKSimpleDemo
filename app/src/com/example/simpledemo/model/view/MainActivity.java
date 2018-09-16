package com.example.simpledemo.model.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.R;
import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.ui.SalesforceActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends SalesforceActivity {

	@BindView(R.id.toolbar) protected Toolbar toolbar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ButterKnife.bind(this);
		setActionBar(toolbar);
	}

	@Override
	public void onResume(RestClient client) {
		MainApplication.getInstance().initGraph(client);
	}

	public void onLogoutClick(View v) {
		SalesforceSDKManager.getInstance().logout(this);
	}

}

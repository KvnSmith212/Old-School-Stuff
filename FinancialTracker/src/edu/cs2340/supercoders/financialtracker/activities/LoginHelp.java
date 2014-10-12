package edu.cs2340.supercoders.financialtracker.activities;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import edu.cs2340.supercoders.financialtracker.R;

@SuppressWarnings("deprecation")
public class LoginHelp extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_help);
		
		TabHost th = getTabHost();
		
		
		TabSpec fu = th.newTabSpec("Forgotten Username");
		fu.setIndicator("Forgotten Username");
		Intent fuIntent = new Intent(getApplicationContext(), ForgottenUserName.class);
		fu.setContent(fuIntent);
		th.addTab(fu);
		
		TabSpec fp = th.newTabSpec("Forgotten Password");
		fp.setIndicator("Forgotten Password");
		Intent fpIntent = new Intent(getApplicationContext(), ForgottenPassword.class);
		fp.setContent(fpIntent);
		th.addTab(fp);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_help, menu);
		return true;
	}

}

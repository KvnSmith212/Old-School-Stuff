package edu.cs2340.supercoders.financialtracker.activities;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import edu.cs2340.supercoders.financialtracker.R;

public class Reports extends TabActivity {
	private static Reports theInstance;
	public static Reports getInstanceOf() {
		return Reports.theInstance;
	}
	
	public Reports() {
		Reports.theInstance = this;
	}
	
	public static int selectedTab = 0;
	public static int whereWeAre = 0;
	public static void setWhere (int i) {
		whereWeAre = i;
	}
	public static void setTab(int i) {
		selectedTab = i;
	}
	public static int getTab() {
		return selectedTab;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reports);
		final TabHost th = getTabHost();
		th.setCurrentTab(selectedTab);
		setDefaultTab(selectedTab);
		
		th.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String arg0) {
				setTab(th.getCurrentTab());
				setWhere(0);
			}
			
		});
	    
		
		TabSpec fu = th.newTabSpec("Spending Category Report");
		fu.setIndicator("Spending Category Report");
		Intent fuIntent;
		if (whereWeAre == 0) {
		    fuIntent = new Intent(getApplicationContext(), StartingTime.class);
		} else if (whereWeAre == 1) {
			fuIntent = new Intent(getApplicationContext(), EndingTime.class);
		} else {
			fuIntent = new Intent(getApplicationContext(), ActualSpendingReport.class);
		}
		fu.setContent(fuIntent);
		th.addTab(fu);
		
		TabSpec fp = th.newTabSpec("Income Source Report");
		fp.setIndicator("Income Source Report");
		Intent fpIntent;
		if (whereWeAre == 0) {
		    fpIntent = new Intent(getApplicationContext(), StartingTime.class);
		} else if (whereWeAre == 1) {
			fpIntent = new Intent(getApplicationContext(), EndingTime.class);
		} else {
			fpIntent = new Intent(getApplicationContext(), IncomeSource.class);
		}
		fp.setContent(fpIntent);
		th.addTab(fp);
		
		TabSpec c = th.newTabSpec("Cash Flow Report");
		c.setIndicator("Cash Flow Report");
		Intent x;
		if (whereWeAre == 0) {
		    x = new Intent(getApplicationContext(), StartingTime.class);
		} else if (whereWeAre == 1) {
			x = new Intent(getApplicationContext(), EndingTime.class);
		} else {
			x = new Intent(getApplicationContext(), CashFlow.class);
		}
		c.setContent(x);
		th.addTab(c);

		TabSpec b = th.newTabSpec("Account Listing Report");
		b.setIndicator("Account Listing Report");
		Intent y = new Intent(getApplicationContext(), AccountListing.class);
		b.setContent(y);
		th.addTab(b);
		
		TabSpec a = th.newTabSpec("Transaction History (Selected Days)");
		a.setIndicator("Transaction History (Selected Days)");
		Intent z;
		if (whereWeAre == 0) {
		    z = new Intent(getApplicationContext(), StartingTime.class);
		} else if (whereWeAre == 1) {
			z = new Intent(getApplicationContext(), EndingTime.class);
		} else if (whereWeAre == 2) {
			z = new Intent(getApplicationContext(), ChooseAccount.class);
		} else {
			z = new Intent(getApplicationContext(), TransactionHistory.class);
		}
		a.setContent(z);
		th.addTab(a);
		
		th.setCurrentTab(selectedTab);
		setDefaultTab(selectedTab);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reports, menu);
		return true;
	}

}

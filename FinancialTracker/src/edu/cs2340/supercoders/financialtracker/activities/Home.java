package edu.cs2340.supercoders.financialtracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import edu.cs2340.supercoders.financialtracker.R;

public class Home extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		// Setting up new account Button to start CreateAccount intent
		Button newAccount = (Button) findViewById(R.id.home_newAccount);
		newAccount.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent createAccountIntent = new Intent(
						getApplicationContext(), CreateAccount.class);
				startActivity(createAccountIntent);
			}
		});
		// Setting up report generator Button to start Report intent
				Button reports = (Button) findViewById(R.id.home_genReport);
				reports.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						Intent createReportIntent = new Intent(
								getApplicationContext(), Reports.class);
						Reports.setTab(0);
						startActivity(createReportIntent);
					}
				});
		/*
		 * This little area of code is a bit weird. Basically, we have a list
		 * view that starts out empty. What we're doing here is getting an array
		 * of Strings (that represent the accounts) and populating the list view
		 * by putting our array into an array adaptor, and then setting that
		 * adaptor to our list view.
		 * 
		 * After we set up the adaptor, we want to set up on OnItemClickListener
		 * so that we can click on our accounts. For now I have it going to a
		 * new transaction, but that can be easily changed to make it go to an
		 * account page or whatever else we want once clicked on.
		 */
		ListView list = (ListView) findViewById(R.id.home_accountList);
		String[] accountList = Welcome.getData().getCurrent().accountArray();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.account_list, accountList);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked,
					int position, long id) {
				Welcome.getData()
						.getCurrent()
						.setCurrAccount(
								Welcome.getData().getCurrent().getAccounts()
										.get(position));
				Intent transactionIntent = new Intent(getApplicationContext(),
						Transactions.class);
				startActivity(transactionIntent);
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	/*
	 * This basically forces the method to refresh, so when we back out of
	 * create account it calls onCreate() again and displays our new account
	 * list.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		this.onCreate(null);
	}

}

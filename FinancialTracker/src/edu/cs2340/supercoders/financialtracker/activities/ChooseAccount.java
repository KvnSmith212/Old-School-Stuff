package edu.cs2340.supercoders.financialtracker.activities;

import edu.cs2340.supercoders.financialtracker.R;
import edu.cs2340.supercoders.financialtracker.R.layout;
import edu.cs2340.supercoders.financialtracker.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChooseAccount extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_account);
		
		final int where = Reports.getTab();
		ListView list = (ListView) findViewById(R.id.TH_categoryList);
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
				Reports.setTab(where);
				Reports.setWhere(3);
				Intent transactionIntent = new Intent(getApplicationContext(),
						Reports.class);
				startActivity(transactionIntent);
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_account, menu);
		return true;
	}

}

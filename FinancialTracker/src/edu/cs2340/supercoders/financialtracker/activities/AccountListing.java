package edu.cs2340.supercoders.financialtracker.activities;

import java.util.HashMap;
import java.util.List;

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
import android.widget.TextView;
import edu.cs2340.supercoders.financialtracker.R;
import edu.cs2340.supercoders.financialtracker.model.Account;
import edu.cs2340.supercoders.financialtracker.model.Transaction;

public class AccountListing extends Activity {
	private static String start;
	private static String end;
	public static void setStart(String s) {
		start = s;
	}
	public static void setEnd(String s) {
		end = s;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_listing);
		
		
		TextView theName = (TextView) findViewById(R.id.AL_UserName);
		CharSequence text = Welcome.getCurrUser().toString();
		theName.setText(text);
		
		
		Button returnButton = (Button) findViewById(R.id.AL_return);
		returnButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent createSRIntent = new Intent(
						getApplicationContext(), Home.class);
				startActivity(createSRIntent);
			}
		});
		
		ListView list = (ListView) findViewById(R.id.AL_categoryList);
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
		getMenuInflater().inflate(R.menu.account_listing, menu);
		return true;
	}

}

package edu.cs2340.supercoders.financialtracker.activities;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import edu.cs2340.supercoders.financialtracker.R;
import edu.cs2340.supercoders.financialtracker.model.Transaction;

public class Transactions extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transactions);
		
		ListView list = (ListView) findViewById(R.id.transactionHistory);
		List<Transaction> transactions = Welcome.getData().getCurrent().getCurrAccount().getTransHistory();
		String[] transNamesAmounts = new String[transactions.size()];
		for (int i = 0; i < transactions.size(); i++) {
			String a = transactions.get(i).getName() + ": $" + transactions.get(i).getAmount();
			transNamesAmounts[i] = a;
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.account_list, transNamesAmounts);
		list.setAdapter(adapter);
		
		Button newTrans = (Button) findViewById(R.id.newTransact);
		newTrans.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent createAccountIntent = new Intent(
						getApplicationContext(), NewTransaction.class);
				startActivity(createAccountIntent);
			}
		});
		Button back = (Button) findViewById(R.id.backHome);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent createAccountIntent = new Intent(
						getApplicationContext(), Home.class);
				startActivity(createAccountIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.transactions, menu);
		return true;
	}

}

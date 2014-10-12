package edu.cs2340.supercoders.financialtracker.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import edu.cs2340.supercoders.financialtracker.R;
import edu.cs2340.supercoders.financialtracker.model.Transaction;

public class NewTransaction extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_transaction);
		final CheckBox deposit = (CheckBox) findViewById(R.id.deposit);
		final CheckBox withdrawal = (CheckBox) findViewById(R.id.withdrawal);
		deposit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (withdrawal.isChecked() && deposit.isChecked()) {
					Toast error = Toast.makeText(getApplicationContext(), "You can only choose withdrawal or deposit. Not both.", Toast.LENGTH_SHORT);
					error.show();
					deposit.setChecked(false);
				}
			}
		});
		withdrawal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (withdrawal.isChecked() && deposit.isChecked()) {
					Toast error = Toast.makeText(getApplicationContext(), "You can only choose withdrawal or deposit. Not both.", Toast.LENGTH_SHORT);
					error.show();
					withdrawal.setChecked(false);
				}
			}
		});
		
		final TextView date = (TextView) findViewById(R.id.newTransaction_transactionDate);
		date.addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(Editable arg0) {
					if (arg0.length() == 2 || arg0.length() == 5) {
						String changed = arg0.append('-').toString();
						date.setText(changed);
					    EditText d = (EditText) findViewById(R.id.newTransaction_transactionDate);
					    d.setSelection(changed.length());
					}
					
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {					
				}

				@Override
				public void onTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub
					
				}
		});
		/*
		 * Setting up the button to create a new transaction. See my descriptions in
		 * Register and Welcome if you're confused.
		 */
		Button create = (Button) findViewById(R.id.newTransaction_create);
		
		create.setOnClickListener(new OnClickListener() {

			
			@Override
			public void onClick(View v) {
				String name = ((EditText) findViewById(R.id.newTransaction_transactionName)).getText().toString();
				String source = ((EditText) findViewById(R.id.transkind)).getText().toString();
				String type;
				String date = ((EditText) findViewById(R.id.newTransaction_transactionDate))
						.getText().toString();
				try{
					Integer.parseInt(date.substring(0, 2));
					Integer.parseInt(date.substring(3, 5));
					Integer.parseInt(date.substring(6, 10));
					
					double amount = Double.parseDouble(((EditText) findViewById(R.id.newTransaction_transactionAmount))
						.getText().toString());
					if (deposit.isChecked()) {
						type = "deposit";
						Transaction nTrans = new Transaction(name, source, amount, type, date);
						Welcome.getCurrAccount().addTrans(nTrans);
						Intent transactionIntent = new Intent(getApplicationContext(),
								Transactions.class);
						startActivity(transactionIntent);
					} else if (withdrawal.isChecked()) {
						type = "withdrawal";
						Transaction nTrans = new Transaction(name, source, amount, type, date);
						Welcome.getCurrAccount().addTrans(nTrans);
						Intent transactionIntent = new Intent(getApplicationContext(),
								Transactions.class);
						startActivity(transactionIntent);
					} else if (!deposit.isChecked() && !withdrawal.isChecked()) {
						Toast error = Toast.makeText(getApplicationContext(), "Please choose an account type.", Toast.LENGTH_SHORT);
						error.show();
					}
				} catch (Exception e){
					Toast error = Toast.makeText(getApplicationContext(), "Please enter a valid amount (a number)/valid date.", Toast.LENGTH_SHORT);
					error.show();
				}
			}

		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_transaction, menu);
		return true;
	}
	
	public void onPause(){
		Welcome.save();
		super.onPause();
	}

}

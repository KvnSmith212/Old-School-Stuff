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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import edu.cs2340.supercoders.financialtracker.R;

public class StartingTime extends Activity {
	
	
	public static String start = null;
	
	public static String getStart() {
		return start;
	}
	public static void setStart(String input) {
		start = input;
	}
	public void sendStart() {
		EndingTime.setStart(start);
		switchMenu(Reports.selectedTab);
		start = null;
		Reports.setWhere(1);
	}
	
	
	public void switchMenu(int repo) {
		switch(repo) {
		case 0:
			ActualSpendingReport.setStart(start);
			break;
		case 1:
			IncomeSource.setStart(start);
			break;
		case 2:
			CashFlow.setStart(start);
			break;
		case 3:
			AccountListing.setStart(start);
			break;
		case 4:
			TransactionHistory.setStart(start);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starting_time);
		
		final int where = Reports.getTab();
		final TextView date = (TextView) findViewById(R.id.start);
		date.addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(Editable arg0) {
					if (arg0.length() == 2 || arg0.length() == 5) {
						String changed = arg0.append('-').toString();
						date.setText(changed);
					    EditText d = (EditText) findViewById(R.id.start);
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
		
		Button start = (Button) findViewById(R.id.nextButton1);
		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String startDate = ((EditText) findViewById(R.id.start))
						.getText().toString();
				StartingTime.setStart(startDate);
				if (StartingTime.getStart() != "MM/DD/YYYY") {
					sendStart();
					Intent loginIntent = new Intent(getApplicationContext(),
							Reports.class);
					Reports.setTab(where);
					Reports.setWhere(1);
					
					startActivity(loginIntent);
					StartingTime.this.finish();
				} else {
					Toast incLogin = Toast.makeText(getApplicationContext(),
							"Please enter a start date.",
							Toast.LENGTH_SHORT);
					incLogin.show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.starting_time, menu);
		return true;
	}

}

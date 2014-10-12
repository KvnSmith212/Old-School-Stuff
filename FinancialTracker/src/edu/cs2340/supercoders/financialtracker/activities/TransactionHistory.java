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

public class TransactionHistory extends Activity {
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
		setContentView(R.layout.activity_transaction_history);
		
		
		
		TextView theName = (TextView) findViewById(R.id.FI_UserName);
		CharSequence text = Welcome.getCurrUser().getCurrAccount().toString();
		theName.setText(text);
		
		TextView theDates = (TextView) findViewById(R.id.FI_Dates);
		CharSequence date = start + " - " + end;
		theDates.setText(date);
		
		ListView list = (ListView) findViewById(R.id.FI_categoryList);
		List<Transaction> trans = Welcome.getData().getCurrent().getCurrAccount().getTransHistory();
		for (Transaction items: trans) {
			String s = items.getTime().toString();
			if (!inBetween(s)) {
				trans.remove(items);
			}
		}
		String[] accountList = (String[]) trans.toArray();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.account_list, accountList);
		list.setAdapter(adapter);
		
		Button returnButton = (Button) findViewById(R.id.FI_return);
		returnButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent createSRIntent = new Intent(
						getApplicationContext(), Home.class);
				startActivity(createSRIntent);
			}
		});
	}
 
    private boolean inBetween(String time) {
    	String a = start;
    	String b = end;
    	int startDay = Integer.parseInt(a.substring(0, 2));
		int startMonth = Integer.parseInt(a.substring(3, 5));
		int startYear = Integer.parseInt(a.substring(6, 10));
		int endDay = Integer.parseInt(b.substring(0, 2));
		int endMonth = Integer.parseInt(b.substring(3, 5));
		int endYear = Integer.parseInt(b.substring(6, 10));
		int wantedDay = Integer.parseInt(time.substring(0, 2));
		int wantedMonth = Integer.parseInt(time.substring(3, 5));
		int wantedYear = Integer.parseInt(time.substring(6, 10));
		
		if (wantedYear >= startYear && wantedYear <= endYear) {
			if (wantedYear == startYear) {
				if (wantedMonth >= startMonth) {
					if (wantedMonth == startMonth) {
					    if (wantedDay >= startDay) {
						    return true;
					    } else {
						    return false;
					    }
					} else {
						return true;
					}
				} else {
					return false;
				}
			} else if (wantedYear == endYear) {
				if (wantedMonth <= endMonth) {
					if (wantedMonth == endMonth) {
					    if (wantedDay <= endDay) {
						    return true;
					    } else {
						    return false;
					    }
					} else {
						return true;
					}
				} else {
					return false;
				}
			} else {
				return true;
			}
		} else {
			return false;
		}
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.transaction_history, menu);
		return true;
	}

}

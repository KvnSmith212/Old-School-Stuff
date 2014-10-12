package edu.cs2340.supercoders.financialtracker.activities;

import java.util.HashMap;
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
import android.widget.TextView;
import edu.cs2340.supercoders.financialtracker.R;
import edu.cs2340.supercoders.financialtracker.model.Account;
import edu.cs2340.supercoders.financialtracker.model.Transaction;

public class CashFlow extends Activity {
	private HashMap<String, Double> hm = new HashMap<String, Double>();
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
		setContentView(R.layout.activity_cash_flow);
		
		
		
		
		//starts the empty values for the hashmap categories
		hm.put("Income", 0.00);
		hm.put("Expenses", 0.00);
		hm.put("Flow", 0.00);
		
		createTotals();
		TextView theName = (TextView) findViewById(R.id.CF_UserName);
		CharSequence text = Welcome.getCurrUser().toString();
		theName.setText(text);
		
		TextView theDates = (TextView) findViewById(R.id.CF_Dates);
		CharSequence date = start + " - " + end;
		theDates.setText(date);
		
		ListView list = (ListView) findViewById(R.id.CF_categoryList);
		double f = hm.get("Income");
		double r = hm.get("Expenses");
		double e = hm.get("Flow");
		
		String[] theList = new String[6];
		theList[0] = "Income " + String.valueOf(f);
		theList[1] = "Expenses " + String.valueOf(r);
		theList[2] = "Flow " + String.valueOf(e);
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.account_list, theList);
		list.setAdapter(adapter);
		
		Button returnButton = (Button) findViewById(R.id.CF_return);
		returnButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent createSRIntent = new Intent(
						getApplicationContext(), Home.class);
				startActivity(createSRIntent);
			}
		});
	}
    public void createTotals() {
    	List<Account> accountList = Welcome.getData().getCurrent().getAccounts();
    	int accountListSize = accountList.size();
    	for (int i = 0; i < accountListSize; i++) {
    		List<Transaction> trans = accountList.get(i).getTransHistory();
    		int transSize = trans.size();
    		for (int j = 0; j < transSize; j++) {
    			String time = trans.get(j).getTime();
    			if (inBetween(time)) {
    				double amount = trans.get(j).getAmount();
    				String type = trans.get(j).getType();
    				if (type.equals("withdrawal")) {
    		      		double newNum = hm.get(type) + amount;
        				hm.put(type, newNum);
        				double newTotal = hm.get("Expenses") + amount;
    			    	hm.put("Expenses", newTotal);
    				} else if (type.equals("deposit")) {
    					double newNum = hm.get(type) + amount;
        				hm.put(type, newNum);
        				double newTotal = hm.get("Income") + amount;
    			    	hm.put("Income", newTotal);
    				}
    			}
    		}
    	}
    	double expenses = hm.get("Expenses");
    	double income = hm.get("Income");
		double newTotal = income - expenses;
    	hm.put("Flow", newTotal);
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
		getMenuInflater().inflate(R.menu.cash_flow, menu);
		return true;
	}

}

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

public class ActualSpendingReport extends Activity {
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
		setContentView(R.layout.activity_actual_spending_report);
		
		
		
		
		//starts the empty values for the hashmap categories
		hm.put("food", 0.00);
		hm.put("rent", 0.00);
		hm.put("entertainment", 0.00);
		hm.put("clothing", 0.00);
		hm.put("other", 0.00);
		hm.put("total", 0.00);
		
		createTotals();
		TextView theName = (TextView) findViewById(R.id.SR_UserName);
		CharSequence text = Welcome.getCurrUser().toString();
		theName.setText(text);
		
		TextView theDates = (TextView) findViewById(R.id.SR_Dates);
		CharSequence date = start + " - " + end;
		theDates.setText(date);
		
		ListView list = (ListView) findViewById(R.id.SR_categoryList);
		double f = hm.get("food");
		double r = hm.get("rent");
		double e = hm.get("entertainment");
		double c = hm.get("clothing");
		double o = hm.get("other");
		double t = hm.get("total");
		
		String[] theList = new String[6];
		theList[0] = "Food " + String.valueOf(f);
		theList[1] = "Rent " + String.valueOf(r);
		theList[2] = "Entertainment " + String.valueOf(e);
		theList[3] = "Clothing " + String.valueOf(c);
		theList[4] = "Other " + String.valueOf(o);
		theList[5] = "Total " + String.valueOf(t);
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.account_list, theList);
		list.setAdapter(adapter);
		
		Button returnButton = (Button) findViewById(R.id.SR_return);
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
    				String name = trans.get(j).getSource();
    				double amount = trans.get(j).getAmount();
    				String type = trans.get(j).getType();
    				if (type.equals("withdrawal")) {
    		      		if (name.equals("food") || name.equals("rent") || name.equals("entertainment") || name.equals("clothing") || name.equals("other")) {
    	    				double newNum = hm.get(name) + amount;
        					hm.put(name, newNum);
        				   	double newTotal = hm.get("total") + amount;
    			    		hm.put("total", newTotal);   					
    		    		} else {
    	    				double newNum = hm.get("other") + amount;
        					hm.put("other", newNum);
    			    		double newTotal = hm.get("total") + amount;
    					    hm.put("total", newTotal);
    				    }
    				}
    			}
    		}
    	}
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
		getMenuInflater().inflate(R.menu.actual_spending_report, menu);
		return true;
	}

}

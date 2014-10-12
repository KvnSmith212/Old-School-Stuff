package edu.cs2340.supercoders.financialtracker.activities;



import edu.cs2340.supercoders.financialtracker.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EndingTime extends Activity {
	public static String start;
	public static void setStart(String st) {
		start = st;
	}

	public static String end = null;
	
	public static String getEnd() {
		return end;
	}
	public static void setEnd(String input) {
		end = input;
	}
	
	public void sendEnd() {
		switchMenu(Reports.selectedTab);
		end = null;
	}
	public void switchMenu(int repo) {
		switch(repo) {
		case 0:
			ActualSpendingReport.setEnd(end);
			break;
		case 1:
			IncomeSource.setEnd(end);
			break;
		case 2:
			CashFlow.setEnd(end);
			break;
		case 3:
			AccountListing.setEnd(end);
			break;
		case 4:
			TransactionHistory.setEnd(end);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ending_time);
		
		final int where = Reports.getTab();
		
		final TextView date = (TextView) findViewById(R.id.end);
		date.addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(Editable arg0) {
					if (arg0.length() == 2 || arg0.length() == 5) {
						String changed = arg0.append('-').toString();
						date.setText(changed);
					    EditText d = (EditText) findViewById(R.id.end);
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
		
		Button start2 = (Button) findViewById(R.id.nextButton2);
		start2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String endDate = ((EditText) findViewById(R.id.end))
						.getText().toString();
				EndingTime.setEnd(endDate);
				try {
				    boolean beforeOrNot = isBefore(start, EndingTime.getEnd());
				    if (beforeOrNot) {
				    	sendEnd();
				    	
				        Intent loginIntent = new Intent(getApplicationContext(),
							Reports.class);
				        Reports.setTab(where);
				        Reports.setWhere(2);
					    startActivity(loginIntent);
				    } else {
				    	Toast incLogin = Toast.makeText(getApplicationContext(),
								"The start date is after the end date. Try again.",
								Toast.LENGTH_SHORT);
						incLogin.show();
						end = null;
						Intent loginIntent = new Intent(getApplicationContext(),
								Reports.class);
						Reports.setTab(where);
				        Reports.setWhere(0);
						startActivity(loginIntent);
				    }
				} catch(Exception E) {
					Toast incLogin = Toast.makeText(getApplicationContext(),
							"You entered an invalid date. Try again.",
							Toast.LENGTH_SHORT);
					incLogin.show();
				}
			}
		});
	}
	//private helper method to see if the startDate is before the endDate
	private boolean isBefore(String a, String b) {
		int startDay = Integer.parseInt(a.substring(0, 2));
		int startMonth = Integer.parseInt(a.substring(3, 5));
		int startYear = Integer.parseInt(a.substring(6, 10));
		int endDay = Integer.parseInt(b.substring(0, 2));
		int endMonth = Integer.parseInt(b.substring(3, 5));
		int endYear = Integer.parseInt(b.substring(6, 10));
		
		if (endYear >= startYear) {
			if (endYear == startYear) {
				if (endMonth >= startMonth) {
					if (endMonth == startMonth) {
						if (endDay >= startDay) {
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
		getMenuInflater().inflate(R.menu.ending_time, menu);
		return true;
	}

}

package edu.cs2340.supercoders.financialtracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.cs2340.supercoders.financialtracker.R;

public class ForgottenUserName extends Activity {
	private static boolean changedIt = false;
	private static String userName;
	public static String getUserName() {
		return userName;
	}
	public static boolean getChangedIt() {
		return changedIt;
	}
	public static void setChangedIt() {
		changedIt = false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgotten_user_name);
		Button enter = (Button) findViewById(R.id.enterButton);
		
		
		enter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String email = ((EditText) findViewById(R.id.fu_email)).getText().toString();
				try {
				    userName = Welcome.getData().recoverUserName(email);
				    if (userName == null) {
				    	Toast error = Toast.makeText(getApplicationContext(),
								"There is no user with that email. Try again.",
								Toast.LENGTH_SHORT);
						error.show();
						changedIt = false;
				    } else {
				    	Toast error = Toast.makeText(getApplicationContext(),
								"Your username is " + userName + ".",
								Toast.LENGTH_LONG);
						error.show();
						Intent loginIntent = new Intent(getApplicationContext(),
								Login.class);
						changedIt = true;
						startActivity(loginIntent);
				    }
				} catch (Exception E) {
					Toast error = Toast.makeText(getApplicationContext(),
							"There is no user with that email. Try again.",
							Toast.LENGTH_SHORT);
					error.show();
					changedIt = false;
				}
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forgotten_user_name, menu);
		return true;
	}

}
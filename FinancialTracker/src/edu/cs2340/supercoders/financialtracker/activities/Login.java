package edu.cs2340.supercoders.financialtracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import edu.cs2340.supercoders.financialtracker.R;

/**
 * The Class Login.
 */
public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		/*
		 * Setting up the Login button. If the user is legit, the home activity
		 * is started. Else the user gets a popup saying "incorrect login"
		 */
		Button login = (Button) findViewById(R.id.login_login_button);
		TextView needHelp = (TextView) findViewById(R.id.login_helpLink);
		
		boolean changedIt = ForgottenUserName.getChangedIt();
        if (changedIt) {
        	TextView et = (TextView)findViewById(R.id.user_name);
        	String email = ForgottenUserName.getUserName();
        	et.setText(email);
        	ForgottenUserName.setChangedIt();
        } else {
        	TextView et = (TextView)findViewById(R.id.user_name);
        	et.setText("");
        }
		
		needHelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent loginHelp = new Intent(getApplicationContext(), LoginHelp.class);
				startActivity(loginHelp);
			}
		});
		
		login.setOnClickListener(new OnClickListener() {
      
			@Override
			public void onClick(View v) {
				String userName = ((EditText) findViewById(R.id.user_name))
						.getText().toString();
				String password = ((EditText) findViewById(R.id.password))
						.getText().toString();
				boolean result = Welcome.getData()
						.checkUser(userName, password);
				if (result) {
					Welcome.getData().setCurrent(
							Welcome.getData().getUser(userName));
					Intent loginIntent = new Intent(getApplicationContext(),
							Home.class);
					startActivity(loginIntent);
				} else {
					Toast incLogin = Toast.makeText(getApplicationContext(),
							"Incorrect Login", Toast.LENGTH_SHORT);
					incLogin.setGravity(Gravity.CENTER, 0, 0);
					incLogin.show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}

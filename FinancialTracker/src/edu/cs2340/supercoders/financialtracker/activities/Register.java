package edu.cs2340.supercoders.financialtracker.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.cs2340.supercoders.financialtracker.R;
import edu.cs2340.supercoders.financialtracker.model.User;

/**
 * This is the activity that's set up for Registering a new user.
 */
public class Register extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		/*
		 * Here I'm setting up everything needed to register a new user.
		 * 
		 * Notice the TextViews don't give strings. I first get an EditText
		 * object, which I then call it's getText() (Still not strings,) and
		 * then finally toString(). Then I check the username to make sure
		 * it is available for registration.
		 * 
		 * Below that I create a new user, add it to our data, and then
		 * display a little message that says "You may now log in" (That's
		 * what the Toast object does.) Then I call the activities finish()
		 * method to take us back to Welcome
		 */
		Button register = (Button) findViewById(R.id.register_register_button);
		
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String userName = ((EditText) findViewById(R.id.register_username))
						.getText().toString();
				String password = ((EditText) findViewById(R.id.register_password))
						.getText().toString();
				String email = ((EditText) findViewById(R.id.register_email)).getText().toString();
				String firstName = ((EditText) findViewById(R.id.register_firstName))
						.getText().toString();
				String lastName = ((EditText) findViewById(R.id.register_lastName))
						.getText().toString();
				boolean available = Welcome.getData().checkAvailability(
						userName);
				boolean hasNumber = true;
				char[] passwordChar = password.toCharArray();
				for (char items: passwordChar) {
					if (Character.isDigit(items)) {
						hasNumber = false;
					}
				}

				if (userName.length() < 5) {
					Toast incLogin = Toast.makeText(getApplicationContext(),
							"Username must be at least 5 characters long",
							Toast.LENGTH_SHORT);
					incLogin.show();
				} else if (password.length() < 9) {
					Toast incLogin = Toast.makeText(getApplicationContext(),
							"Password must be at least 8 characters and 1 number",
							Toast.LENGTH_SHORT);
					incLogin.show();
				} else if (hasNumber) {
					Toast incLogin = Toast.makeText(getApplicationContext(),
							"Password must contain at least one number",
							Toast.LENGTH_SHORT);
					incLogin.show();
				} else if (!email.matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
					Toast incLogin = Toast.makeText(getApplicationContext(),
							"Please enter a valid email e.g. email@domain.com",
							Toast.LENGTH_SHORT);
					incLogin.show();
				} else if (firstName.length() < 1) {
					Toast incLogin = Toast.makeText(getApplicationContext(),
							"Please enter a first name",
							Toast.LENGTH_SHORT);
					incLogin.show();
				} else if (lastName.length() < 1) {
					Toast incLogin = Toast.makeText(getApplicationContext(),
							"Please enter a last name",
							Toast.LENGTH_SHORT);
					incLogin.show();
				} else if (available) {
					User newUser = new User(userName, password, email, firstName,
							lastName);
					Welcome.getData().addNewUser(newUser);
					Toast success = Toast.makeText(getApplicationContext(),
							"You may now log in", Toast.LENGTH_SHORT);
					success.show();
					Register.this.finish();
				} else {
					Toast incLogin = Toast.makeText(getApplicationContext(),
							"That user name is not available",
							Toast.LENGTH_SHORT);
					incLogin.show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	
	public void onPause(){
		Welcome.save();
		super.onPause();
	}

}

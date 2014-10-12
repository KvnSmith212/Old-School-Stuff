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

public class ForgottenPassword extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgotten_password);
		
        Button enter = (Button) findViewById(R.id.fp_enter);
		
		
		enter.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String userName = ((EditText) findViewById(R.id.fp_username)).getText().toString();
				try {
				    String email = Welcome.getData().recoverPassword(userName);
				    if (email == null) {
				    	Toast error = Toast.makeText(getApplicationContext(),
								"There is no user with that username. Try again.",
								Toast.LENGTH_SHORT);
						error.show();
				    } else {
				    	Toast error = Toast.makeText(getApplicationContext(),
								"H",
								Toast.LENGTH_SHORT);
						error.show();
				    }
				} catch (Exception E) {
					Toast error = Toast.makeText(getApplicationContext(),
							"There is no user with that username. Try again.",
							Toast.LENGTH_SHORT);
					error.show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forgotten_password, menu);
		return true;
	}

}

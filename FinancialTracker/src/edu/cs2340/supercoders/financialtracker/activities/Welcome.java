package edu.cs2340.supercoders.financialtracker.activities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.cs2340.supercoders.financialtracker.R;
import edu.cs2340.supercoders.financialtracker.model.Account;
import edu.cs2340.supercoders.financialtracker.model.LoginData;
import edu.cs2340.supercoders.financialtracker.model.User;

import com.google.gson.Gson;

/**
 * The Class Welcome.
 * 
 * This is the first activity, or screen, for our android application. The
 * activity lifecycle contains a number of functions such as onCreate(),
 * onResume(), onStop(), etc, but the only one we're worrying about in this
 * class is the onCreate() method which is called when the activity is created.
 * 
 * Every activity has it's own xml file under res/layout which determines what
 * the activity looks like. To change the look of the activity, you change the
 * xml. Other useful things you can find other res include values such as
 * strings you may want to use.
 */
public class Welcome extends Activity {

	/**
	 * data will hold our usernames and passwords. See LoginData for
	 * implementation specifics.
	 */
	private static LoginData data;
	private MediaPlayer song;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/*
		 * This is auto generated when a new activity is created. You don't
		 * really have to worry about it, it just calls super() and sets the
		 * layout of the activity.
		 */
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		song = MediaPlayer.create(Welcome.this, R.raw.financejingle);
		song.start();
		
        
		data = new LoginData();
		load();

		/*
		 * Here we are creating our button and setting it up to load the Login
		 * activity. When we create a button in the xml file, android auto
		 * generates a class called R. In R we can find the ID's for every
		 * element specified in the xml files such as layout, strings, etc. Here
		 * we are calling findViewById() and passing in the ID for the button we
		 * want and casting it to a button. Then we want to set it's on click
		 * listener, so we create a new one.
		 * 
		 * The intent is used to tell the activity we want to do something, in
		 * this case open the Login activity. So we want to pass it the
		 * application context (Don't ask me what that is as I'm not fully
		 * sure,) and then the activity we want to open. Then we run
		 * startActivity() and pass it the intent.
		 */
		Button login = (Button) findViewById(R.id.welcome_login_button);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent loginIntent = new Intent(getApplicationContext(),
						Login.class);
				startActivity(loginIntent);
			}
		});

		// Button to start the Register activity
		Button register = (Button) findViewById(R.id.welcome_register_button);
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent registerIntent = new Intent(getApplicationContext(),
						Register.class);
				startActivity(registerIntent);
			}
		});

	}

	// Don't worry about this, it's auto generated and we don't need to do
	// anything with it for now.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

	/**
	 * Saving the application using a text file in JSON format. Notice I have
	 * the application saving any time we've changed anything using the
	 * onPause() function. So any time register, new transaction, or create
	 * account is paused or closed it will save our data.
	 */
	public static void save() {
		PrintWriter writer = null;
		try {
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/data.json");
			writer = new PrintWriter(new FileWriter(file));

			// Googles JSON library
			Gson gSon = new Gson();
			writer.println(gSon.toJson(data));
			writer.close();

			Log.e("Welcome", "Saved data successfully");
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("Welcome", "IO problem writing the json");
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	/**
	 * Load the application using a JSON text file. Notice the file is loaded
	 * whenever welcome is created.
	 */
	public void load() {
		String json = null;
		try {
			json = getJsonFromFile();
		} catch (IOException e) {
			Log.e("Welcome", "Error on closing file in Json load");
		}

		Gson gSon = new Gson();
		data = gSon.fromJson(json, LoginData.class);
		if (data == null) {
			data = new LoginData();
			Log.e("Welcome", "json failed and return null");
		}
	}

	/**
	 * Helper method to get the file as a string so it can be converted
	 * 
	 * @return the string rep of the entire file
	 * @throws IOException
	 */
	private String getJsonFromFile() throws IOException {
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/data.json");
		StringBuffer buff = new StringBuffer();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String s = reader.readLine();
			Log.d("Welcome", "Got First Line: " + s);
			while (s != null) {
				buff.append(s);
				s = reader.readLine();
				Log.d("Welcome", "Got Line: " + s);
			}
		} catch (FileNotFoundException e) {
			Log.e("Welcome", "Json file not found");
		} catch (IOException e) {
			Log.e("Welcome", "IO error when reading json file");
		} finally {
			if (reader != null)
				reader.close();
		}

		Log.d("Welcome", "got json: " + buff.toString());
		return buff.toString();
	}

	/**
	 * Getter for our data so we can manipulate it within any activity we want,
	 * like Register or CreateAccount
	 * 
	 * @return the data
	 */
	public static LoginData getData() {
		return data;
	}

	/*
	 * I added getCurrUser and getCurrAccount here to make it a bit easier to
	 * access these things from other places, rather than having to go through
	 * the data and shit every time we want to acces one of them.
	 */
	public static User getCurrUser() {
		return data.getCurrent();
	}

	public static Account getCurrAccount() {
		return data.getCurrent().getCurrAccount();
	}

}

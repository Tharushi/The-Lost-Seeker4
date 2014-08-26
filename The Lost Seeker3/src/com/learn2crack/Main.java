package com.learn2crack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.learn2crack.library.UserFunctions;
import com.learn2crack.library.DatabaseHandler;

import java.util.HashMap;

public class Main extends Activity {
	Button btnLogout;
	Button changepas;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button search = (Button) findViewById(R.id.Btnsearch);
		Button addfound=(Button) findViewById(R.id.btnaddfounditem);
		Button addlost=(Button) findViewById(R.id.Btnaddlostitem);
		changepas = (Button) findViewById(R.id.btchangepass);
		btnLogout = (Button) findViewById(R.id.logout);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Main.this, Search.class));
			}
		}
		);
		

		DatabaseHandler db = new DatabaseHandler(getApplicationContext());

		/**
		 * Hashmap to load data from the Sqlite database
		 **/
		HashMap<String, String> user = new HashMap<String, String>();
		user = db.getUserDetails();

		
		 // Change Password Activity Started
		 
		changepas.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {

				Intent chgpass = new Intent(getApplicationContext(),
						ChangePassword.class);

				startActivity(chgpass);
			}

		});
		
		 // Add found Item Activity Started
		 
			addfound.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {

					Intent addfound = new Intent(getApplicationContext(),
							AddFoundItem.class);

					startActivity(addfound);
				}

			});
			
			 // Add Lost Item Activity Started
			 
			addlost.setOnClickListener(new View.OnClickListener() {
				public void onClick(View arg0) {

					Intent addlost = new Intent(getApplicationContext(),
							AddLostItem.class);

					startActivity(addlost);
				}

			});

		/**
		 * Logout from the User Panel which clears the data in Sqlite database
		 **/
		btnLogout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {

				UserFunctions logout = new UserFunctions();
				logout.logoutUser(getApplicationContext());
				Intent login = new Intent(getApplicationContext(), Login.class);
				login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(login);
				finish();
			}
		});
		/**
		 * Sets user first name and last name in text view.
		 **/
		final TextView login = (TextView) findViewById(R.id.textwelcome);
		login.setText("Welcome  " + user.get("fname"));
		final TextView lname = (TextView) findViewById(R.id.lname);
		lname.setText(user.get("lname"));

	}
}
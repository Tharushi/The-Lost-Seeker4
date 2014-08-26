package com.Tharushi.thelostseeker;

/**
 * @author Tharushi 110226H
 *
 */

import android.app.Activity;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;


import com.Tharushi.DatabaseHandle.*;


public class MainActivity extends Activity {

		
	EditText inputEmail;
	EditText inputPassword;

	private TextView loginErrorMsg;
	
	 // Called when the activity is first created.
	 
	private static String KEY_SUCCESS = "success";
	private static String KEY_USERID= "userid";
	private static String KEY_USERNAME = "username";
	private static String KEY_EMAIL = "email";
	private static String KEY_TELEPHONE = "telephone";	
	private static String KEY_ADDRESS = "address";
	//private static String KEY_CREATED_AT = "created_at";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		Button register=(Button) findViewById(R.id.btnregister);
		inputEmail = (EditText) findViewById(R.id.edemail);
		inputPassword = (EditText) findViewById(R.id.edpassword);
		Button btnLogin = (Button) findViewById(R.id.btnlogin);
		loginErrorMsg = (TextView) findViewById(R.id.loginErrorMsg);
		register.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				startActivity(new Intent(MainActivity.this,Register.class));
				finish();
				
			}
		});
		
		
		 // Login button click event A Toast is set to alert when the Email and
		 // Password field is empty
		 
		btnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {

				if ((!inputEmail.getText().toString().equals(""))
						&& (!inputPassword.getText().toString().equals(""))) {
					NetAsync(view);
				} else if ((!inputEmail.getText().toString().equals(""))) {
					Toast.makeText(getApplicationContext(),
							"Password field empty", Toast.LENGTH_SHORT).show();
				} else if ((!inputPassword.getText().toString().equals(""))) {
					Toast.makeText(getApplicationContext(),
							"Email field empty", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"Email and Password field are empty",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	
	 // Async Task to check whether internet connection is working.
	

	private class NetCheck extends AsyncTask<String, String, Boolean> {
		private ProgressDialog nDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			nDialog = new ProgressDialog(MainActivity.this);
			nDialog.setTitle("Checking Network");
			nDialog.setMessage("Loading..");
			nDialog.setIndeterminate(false);
			nDialog.setCancelable(true);
			nDialog.show();
		}

		
		 // Gets current device state and checks for working internet connection
		// by trying Google.
	
		@Override
		protected Boolean doInBackground(String... args) {

			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()) {
				try {
					URL url = new URL("http://www.google.com");
					HttpURLConnection urlc = (HttpURLConnection) url
							.openConnection();
					urlc.setConnectTimeout(3000);
					urlc.connect();
					if (urlc.getResponseCode() == 200) {
						return true;
					}
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return false;

		}

		@Override
		protected void onPostExecute(Boolean th) {

			if (th == true) {
				nDialog.dismiss();
				new ProcessLogin().execute();
			} else {
				nDialog.dismiss();
				loginErrorMsg.setText("Error in Network Connection");
			}
		}
	}


	 // Async Task to get and send data to MySql database through JSON respone.
	
	private class ProcessLogin extends AsyncTask<String, String, JSONObject> {

		private ProgressDialog pDialog;

		String email, password;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			inputEmail = (EditText) findViewById(R.id.edemail);
			inputPassword = (EditText) findViewById(R.id.edpassword);
			email = inputEmail.getText().toString();
			password = inputPassword.getText().toString();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setTitle("Contacting Servers");
			pDialog.setMessage("Logging in ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {

			UserFunctions userFunction = new UserFunctions();
			JSONObject json = userFunction.loginUser(email, password);
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				if (json.getString(KEY_SUCCESS) != null) {

					String res = json.getString(KEY_SUCCESS);

					if (Integer.parseInt(res) == 1) {
						pDialog.setMessage("Loading User Space");
						pDialog.setTitle("Getting Data");
						DatabaseHandler db = new DatabaseHandler(
								getApplicationContext());
						JSONObject json_user = json.getJSONObject("user");
						
						 // Clear all previous data in SQlite database.
					
						UserFunctions logout = new UserFunctions();
						logout.logoutUser(getApplicationContext());
						db.addUser(
								json_user.getString(KEY_USERID),
								json_user.getString(KEY_USERNAME),
								json_user.getString(KEY_EMAIL),
								json_user.getString(KEY_TELEPHONE),
								json_user.getString(KEY_ADDRESS)					
								
								);
						/**
						 * If JSON array details are stored in SQlite it
						 * launches the Users main screen.
						 **/
						Intent upanel = new Intent(getApplicationContext(),
								UserMain.class);
						upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						pDialog.dismiss();
						startActivity(upanel);
						/**
						 * Close Login Screen
						 **/
						finish();
					} else {

						pDialog.dismiss();
						loginErrorMsg.setText("Incorrect username/password");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void NetAsync(View view) {
		new NetCheck().execute();
	}



	/**
	 * A placeholder fragment containing a simple view.
	 */
	/*public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.activity_login, container,
					false);
			return rootView;
		}
	}*/

}

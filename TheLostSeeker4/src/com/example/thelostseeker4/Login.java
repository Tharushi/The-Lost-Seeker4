package com.example.thelostseeker4;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import appsettings.Appsettings;

import java.util.ArrayList;

public class Login extends Activity {
	private String url1 = "http://"+Appsettings.ipAddress+"/thelostseekerWeb/login.php";
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
	private int status = 0;
	Button getData = null;
	EditText inputusername = null;
	EditText inputpassword = null;
	protected ProgressBar progBar;
	protected Intent intent;
	TextView message = null;

	// Session Manager Class
	SessionManagement session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Session Manager
		session = new SessionManagement(getApplicationContext());

		Button register = (Button) findViewById(R.id.registerbtn);
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Login.this, Register.class));

			}
		});

		getData = (Button) findViewById(R.id.login);
		inputusername = (EditText) findViewById(R.id.txtemail);
		inputpassword = (EditText) findViewById(R.id.txtpword);
		getData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Get the data
				DoPOST mDoPOST = new DoPOST(Login.this, inputusername.getText()
						.toString(), inputpassword.getText().toString());

				System.out.println("!!!!!!!!!!!!!!!!1**********************"
						+ inputusername.getText().toString());
				System.out
						.println("!!!!!!!!!!!!!!!!!!!!!!!**********************"
								+ inputpassword.getText().toString());

				mDoPOST.execute("");
				getData.setEnabled(false);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		return true;

	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_PROGRESS:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Please wait while connecting...");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mProgressDialog.setCancelable(false);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}

	public class DoPOST extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String username = "";
		String password = "";

		// Result data
		String strFirstName;
		String strLastName;

		String UserName;

		Exception exception = null;

		DoPOST(Context context, String username, String password) {
			mContext = context;
			this.username = username;
			this.password = password;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			setProgress(50);
			try {
				System.out
						.println("!!!!!!!!!!!!!!!!mmmmmmmm**********************");
				// Setup the parameters
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs
						.add(new BasicNameValuePair("username", username));
				nameValuePairs
						.add(new BasicNameValuePair("password", password));

				// Add more parameters as necessary

				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				System.out.println("!!!!!!!!!!!!xxxxxxxxxx*******************");
				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);
				System.out
						.println("!!!!!!!!!!!!!!aaaaaaaaaaaaaaaaaaaaaaa*********");
				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				System.out.println("!!!!!!!!!!!!!!aaaaaaa********");
				HttpPost httppost = new HttpPost(url1);
				System.out.println("!!!!!!!!!!!!!!a********");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				System.out.println("!!!!!!!!!!bbbbbbbbbbbbbbbbbbbbbbbb*******");
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				System.out.println("!!!!!!!ccccccccccccccccc*****");
				String result = EntityUtils.toString(entity);
				System.out
						.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! result entity is - "
								+ result);

				// Create a JSON object from the request response
				JSONObject jsonObject = new JSONObject(result);
				// Retrieve the data from the JSON object
				strFirstName = jsonObject.getString("Result");
				UserName = jsonObject.getString("Name");
				String value = strFirstName.trim();

				System.out.println("***************************username is"
						+ UserName);

				int x = Integer.parseInt(value);
				System.out
						.println("********************************** result string is "
								+ x);
				int test = 1;
				if (x == test) {
					System.out.println("********************** came to pass");
					status = 1;
					session.createLoginSession(UserName);
					startActivity(new Intent(Login.this, Main.class));
				} else {
					Context context = getApplicationContext();
					CharSequence text = "User name or password incorrect";
					System.out.println("!!!!!!!!!!!jjjjjjjjjjjjjjjjjjj");
					int duration = Toast.LENGTH_LONG;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
					System.out.println("mmmmm");
					System.out.println("********************* came to fail");
					status = 0;

				}

			} catch (Exception e) {
				Log.e("ClientServerDemo", "Error:", e);
				exception = e;
			}

			return true;
		}

		protected void onProgressUpdate(String... progress) {
			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
			setProgress(50);
		}

		@Override
		protected void onPostExecute(Boolean valid) {
			// startActivity(new Intent(MainActivity.this, mainmenu.class));
			setProgress(100);
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			if (status == 0) {
				getData.setEnabled(true);

			}

		}

	}

}

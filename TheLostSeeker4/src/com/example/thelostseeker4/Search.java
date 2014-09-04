package com.example.thelostseeker4;

import java.util.ArrayList;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.thelostseeker4.Login.DoPOST;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import appsettings.Appsettings;

public class Search extends Activity {
	private String url1 = "http://" + Appsettings.ipAddress
			+ "/mobile/search.php";
	private static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
	String category;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.category_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				category = spinner.getSelectedItem().toString();
				// Get the data
				RetrieveData mDoPOST = new RetrieveData(Search.this, category);

				mDoPOST.execute("");
			}

			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
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

	public class RetrieveData extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String strcategory = "";

		// Result data
		String strFirstName;
		String strLastName;
		int intAge;
		int intPoints;
		String UserName;

		Exception exception = null;

		RetrieveData(Context context, String nameToSearch) {
			mContext = context;
			strcategory = nameToSearch;

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
				nameValuePairs.add(new BasicNameValuePair("category",
						strcategory));

				// Add more parameters as necessary

				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				System.out.println("!!!!!!!!!!!!xxxxxxxxxx*******************");
				// Setup timeouts
				//HttpConnectionParams
				//		.setConnectionTimeout(httpParameters, 15000);
				//HttpConnectionParams.setSoTimeout(httpParameters, 15000);
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
				// parse json data
				try {
					JSONArray jArray = new JSONArray(result);
					for (int i = 0; i < jArray.length(); i++) {
						JSONObject json_data = jArray.getJSONObject(i);
						Log.i("log_tag",
								"foundItemID: "
										+ json_data.getInt("foundItemID")
										+ ", description: "
										+ json_data.getString("description")
										+ ",location: "
										+ json_data.getString("location") +

										", color: " + json_data.getInt("color"));
					}

				} catch (JSONException e) {
					Log.e("error_log", "Error parsing data " + e.toString());
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

		}

	}

}

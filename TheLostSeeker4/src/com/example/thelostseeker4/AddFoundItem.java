/**
 * 
 */
package com.example.thelostseeker4;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import appsettings.Appsettings;

/**
 * @author Tharushi 110226H
 * 
 */
public class AddFoundItem extends Activity {
	private String url4 = "http://" + Appsettings.ipAddress	+ "/mobile/sendmobile.php";

	Button add = null;
	Spinner category;
	TextView inputdescription = null;
	TextView inputlocation = null;

	private int status = 0;
	protected ProgressBar progBar;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
	static int count = 1;

	String IMAGE1;
	String IMAGE2;

	String y;
	String nameget;

	// String unicode= "?useUnicode=yes&characterEncoding=UTF-8";
	static Dialog d;

	SessionManagement session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addfounditem);

		// /////////////////////////////////////////////////////
		// Session class instance
		session = new SessionManagement(getApplicationContext());
		TextView lblName = (TextView) findViewById(R.id.lbluser);

		/**
		 * Call this function whenever you want to check user login This will
		 * redirect user to LoginActivity is he is not logged in
		 * */
		session.checkLogin();
		// get user data from session
		HashMap<String, String> user = session.getUserDetails();
		// name
		String name = user.get(SessionManagement.KEY_NAME);
		// displaying user data
		lblName.setText(Html.fromHtml("welcome <b>" + name + "!!! </b>"));

		System.out.println("welcome!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + name);

		nameget = name.toString();
		// /////////////////////////////////////////////////////

		add = (Button) findViewById(R.id.btnaddfound);

		category = (Spinner) findViewById(R.id.spinner3);
		inputdescription = (TextView) findViewById(R.id.txtdescrption);
		inputlocation = (TextView) findViewById(R.id.txtlocation1);

		// iv1=(ImageView) findViewById(R.id.image1);
		// iv2=(ImageView) findViewById(R.id.image2);

		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Get the data
				// editsringage.getText().toString(),
				DoPOST mDoPOST = new DoPOST(AddFoundItem.this, category
						.getSelectedItem().toString(), inputdescription
						.getText().toString(), inputlocation.getText()
						.toString(), nameget);

				System.out.println("!!nnnnnnnnnn!!!!!!!!!!!!!!!!");
				mDoPOST.execute("");
				add.setEnabled(false);

				System.out.println("******************************"
						+ inputdescription.getText().toString());
				System.out.println("******************************"
						+ inputlocation.getText().toString());

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
		String strCategory = "";
		String strDescription;
		String strLocation = "";

		String username = "";

		String imagetosearch1 = IMAGE1;
		String imagetosearch2 = IMAGE2;

		String strNameofint = y;
		String getusername = "";

		// Result data
		String strNameRice;
		// String strNameAge;

		String strNameIrri;
		int intAge;
		int intPoints;

		Exception exception = null;

		// String NameToSearch1,
		DoPOST(Context context, String nameToSearch, String NameToSearch1,
				String NameToSearch2, String NameToSearch3) {
			mContext = context;
			strCategory = nameToSearch;
			strDescription = NameToSearch1;
			strLocation = NameToSearch2;
			username = NameToSearch3;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_PROGRESS);
			System.out.println("!!!!!hhhhhhh!!!!!!!!!!!!");
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			setProgress(50);
			try {
				System.out.println("cameeeeeeeeeee");
				// Setup the parameters
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("category",
						strCategory));
				nameValuePairs.add(new BasicNameValuePair("description",
						strDescription));
				nameValuePairs.add(new BasicNameValuePair("location",
						strLocation));

				nameValuePairs
						.add(new BasicNameValuePair("username", username));

				// Add more parameters as necessary
				System.out.println("!!!!!xxxxxxxxxxxxx!!!!!!!!!!!");
				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost(url4);
				// "http://10.0.2.2/mobilesend.php");
				// "http://172.16.12.85/mobilesend.php");


				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				String result = EntityUtils.toString(entity);
				System.out
						.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! result entity is - "
								+ result);

				// Create a JSON object from the request response
				JSONObject jsonObject = new JSONObject(result);

				// Retrieve the data from the JSON object
				strNameRice = jsonObject.getString("Result");

				String value = strNameRice.trim();
				int x = Integer.parseInt(value);
				System.out
						.println("********************************** result string is "
								+ x);
				int test = 1;
				if (x == test) {
					System.out.println("!!!aaaaaaaaaaaaaaa!!!!!!!!!!!!");
					System.out.println("********************** came to pass");
					status = 1;
					startActivity(new Intent(AddFoundItem.this, Main.class));

				} else {

					System.out.println("!!!!yyyyyyyyyyy!!!!!!!!!!!!!");
					Context context = getApplicationContext();
					CharSequence text = "Cant send the problem Details. Try again later!!";
					int duration = Toast.LENGTH_LONG;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
					System.out.println("********************* came to fail");

					status = 0;

				}

			} catch (Exception e) {
				System.out.println("!!!!qqqqqqqqqqqqq!!!!!!!!!!");
				Log.e("ClientServerDemo", "Error:", e);
				exception = e;
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean valid) {
			setProgress(100);
			//removeDialog(DIALOG_DOWNLOAD_PROGRESS);
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			if (status == 0) {
				add.setEnabled(true);
			}

		}
	}

}

/**
 * 
 */
package com.example.thelostseeker4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.example.thelostseeker4.AddFoundItem.DoPOST;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import appsettings.Appsettings;

/**
 * @author Tharushi 110226H
 * 
 */
public class ItemDetails extends Activity {
	private String url4 = "http://" + Appsettings.ipAddress
			+ "/mobile/claimitem.php";
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */

	SessionManagement session;
	String nameget;
	ImageView iv;
	String photourl = null;
	String founditemsID = null;
	String newurl;
	String image_location;
	Button cl;
	private int status = 0;
	protected ProgressBar progBar;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
	static int count = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemdetails);

		// Session class instance
		session = new SessionManagement(getApplicationContext());
		TextView lblName = (TextView) findViewById(R.id.txtuser);
		TextView itemdetails = (TextView) findViewById(R.id.txtitem);
		iv = (ImageView) findViewById(R.id.ivitem);
		cl = (Button) findViewById(R.id.btnclaim);
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
		lblName.setText(Html.fromHtml("User <b>" + name + "!!! </b>"));

		System.out.println("welcome!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + name);

		nameget = name.toString();
		String founditem = null;
		String location = null;
		String description = null;
		String colour = null;

		Bundle b = getIntent().getExtras();
		if (b != null) {
			founditem = b.getString("details");
			photourl = b.getString("photourl");
			founditemsID = b.getString("founditemID");
			// photourl = (String) b.get("photourl");
			/*
			 * founditemID = b.getString("foundItemID"); location =
			 * b.getString("location"); description =
			 * b.getString("description"); colour = b.getString("colour"); date
			 * = b.getString("datetime");
			 */
			System.out.println("photourl isssssss : " + photourl);
			newurl = photourl.substring(3);
			System.out.println("newww  photourl isssssss : " + newurl);
			image_location = "http://" + Appsettings.ipAddress + "/" + newurl;

		}
		itemdetails.setText(Html.fromHtml("item  <b>\n" + founditem));
		JsonRead mDoPOST = new JsonRead(ItemDetails.this);
		mDoPOST.execute("");

		cl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				POSTClaim mPOSTClaim = new POSTClaim(ItemDetails.this,
						founditemsID, nameget);

				System.out.println("!!nnnnnnnnnn!!!!!!!!!!!!!!!!");
				mPOSTClaim.execute("");
				cl.setEnabled(false);

			}
		});
	}

	private class JsonRead extends AsyncTask<String, Void, String> {
		Bitmap bitmap;
		Context mContext = null;
		// Result data
		Exception exception = null;

		JsonRead(Context context) {

			mContext = context;
			// strcategory = nameToSearch;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			// showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected String doInBackground(String... params) {

			URL imageURL = null;

			try {
				imageURL = new URL(image_location);
			}

			catch (MalformedURLException e) {
				e.printStackTrace();
			}

			try {
				HttpURLConnection connection = (HttpURLConnection) imageURL
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream inputStream = connection.getInputStream();

				bitmap = BitmapFactory.decodeStream(inputStream);// Convert to
																	// bitmap

			} catch (IOException e) {

				e.printStackTrace();
			}
			return image_location;

		}

		@Override
		protected void onPostExecute(String result) {
			iv.setImageBitmap(bitmap);

		}
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

	public class POSTClaim extends AsyncTask<String, Void, Boolean> {

		Context mContext = null;
		String strUserID = "";
		String strFounditemID;
		String strDate = "";
		String username = "";
		String getusername = "";
		// Result data
		String strNameRice;
		// String strNameAge;

		Exception exception = null;

		
		POSTClaim(Context context, String NameToSearch1, String NameToSearch2) {
			mContext = context;
			strFounditemID = NameToSearch1;
			username = NameToSearch2;
			// strDate=NameToSearch4;

		}

		protected void onPreExecute() {
			super.onPreExecute();
			 showDialog(DIALOG_DOWNLOAD_PROGRESS);

		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			setProgress(50);
			try {
				System.out.println("cameeeeeeeeeee");
				// Setup the parameters
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

				nameValuePairs.add(new BasicNameValuePair("founditemID",
						strFounditemID));

				nameValuePairs
						.add(new BasicNameValuePair("username", username));
				// nameValuePairs
				// .add(new BasicNameValuePair("date", strDate));

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
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				String result = EntityUtils.toString(entity);
				System.out
						.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! result entity is - "
								+ result);

				// Create a JSON object from the request response
				try {
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
						status = 1;
						final Context context = getApplicationContext();
						final CharSequence text = "Successfully Send your Claim";
						final int duration = Toast.LENGTH_LONG;
						runOnUiThread(new Runnable() {
							public void run() {

								Toast.makeText(context, text, duration).show();
							}
						});
						startActivity(new Intent(ItemDetails.this, Search.class));

					} else {

						Context context = getApplicationContext();
						CharSequence text = "You Already claimed this Item!!";
						int duration = Toast.LENGTH_LONG;

						Toast toast = Toast.makeText(context, text, duration);
						toast.show();
						System.out
								.println("********************* came to fail");

						status = 0;

					}
				} catch (Exception e) {
					final Context context = getApplicationContext();
					final CharSequence text = "You Already claimed this Item!!";
					final int duration = Toast.LENGTH_LONG;

					// Toast toast = Toast.makeText(context, text, duration);
					// toast.show();
					System.out
							.println("!!!!!!!!!!!elseee...incorect adding of item");
					runOnUiThread(new Runnable() {
						public void run() {

							Toast.makeText(context, text, duration).show();
						}
					});
					Log.e("Fail 3", e.toString());
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
			// removeDialog(DIALOG_DOWNLOAD_PROGRESS);
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			if (status == 0) {
				cl.setEnabled(true);
			}

		}
	}

}

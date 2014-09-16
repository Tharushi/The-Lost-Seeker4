/**
 * 
 */
package com.example.thelostseeker4;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import appsettings.Appsettings;

/**
 * @author Tharushi 110226H
 * 
 */
public class AdminViewPhoto extends Activity {
	private String url5 = "http://" + Appsettings.ipAddress
			+ "/mobile/getAddedUser.php";
	
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
	Button contact;
	String user;
	String jsonResult;
	String contactNo;

	private int status = 0;
	protected ProgressBar progBar;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
	static int count = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adminviewphoto);

		// Session class instance
		session = new SessionManagement(getApplicationContext());
		TextView lblName = (TextView) findViewById(R.id.txtuser);
		TextView itemdetails = (TextView) findViewById(R.id.txtitem);
		iv = (ImageView) findViewById(R.id.ivitem);
		contact=(Button) findViewById(R.id.btncontact);
	
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
		String addeduser = null;
		String colour = null;

		Bundle b = getIntent().getExtras();
		if (b != null) {
			founditem = b.getString("details");
			photourl = b.getString("photourl");
			founditemsID = b.getString("founditemID");
			addeduser = b.getString("addeduser");
			
			/*
			 * founditemID = b.getString("foundItemID"); location =
			 * b.getString("location"); description =
			 * b.getString("description"); colour = b.getString("colour"); date
			 * = b.getString("datetime");
			 */
			System.out.println("user isssssss : " + addeduser);
			newurl = photourl.substring(3);
			System.out.println("newww  photourl isssssss : " + newurl);
			image_location = "http://" + Appsettings.ipAddress + "/" + newurl;
			itemdetails.setText(Html.fromHtml("item  <b>\n" + founditem));
			JsonRead mDoPOST = new JsonRead(AdminViewPhoto.this);
			mDoPOST.execute("");
			JsonReadTask getuser= new JsonReadTask(AdminViewPhoto.this, addeduser);
			getuser.execute("");

		}
	
	

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
	// Async Task to access the web
	private class JsonReadTask extends AsyncTask<String, Void, String> {

		Context mContext = null;
		// Result data
		Exception exception = null;

		JsonReadTask(Context context, String nameToSearch) {

			mContext = context;
			user = nameToSearch;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			// showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		@Override
		protected String doInBackground(String... params) {

			try {
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!mmmmmmmm");
				// Setup the parameters
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("userid",
						user));
				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);

				HttpPost httppost = new HttpPost(url5);

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				System.out.println("comming..kkkkkkkkk");
				jsonResult = EntityUtils.toString(entity);
				System.out
						.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! result entity is - "
								+ jsonResult);
				try {
					JSONObject jsonResponse = new JSONObject(jsonResult);
					 contactNo = jsonResponse.optString("firstName");
					 System.out.println("contact no!!!!!!!!!!!!!!!!!!!!!"+contactNo);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

	
		@Override
		protected void onPostExecute(String result) {

			try {
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	

}

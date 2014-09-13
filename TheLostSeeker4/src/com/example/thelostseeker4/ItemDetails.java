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
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import appsettings.Appsettings;

/**
 * @author Tharushi 110226H
 * 
 */
public class ItemDetails extends Activity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */

	SessionManagement session;
	String nameget;
	ImageView iv;
	String photourl = null;
	String newurl;
	String image_location;

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
		String founditemID = null;
		String location = null;
		String description = null;
		String colour = null;

		Bundle b = getIntent().getExtras();
		if (b != null) {
			founditemID = b.getString("details");
			photourl = b.getString("photourl");
			/*
			 * founditemID = b.getString("foundItemID"); location =
			 * b.getString("location"); description =
			 * b.getString("description"); colour = b.getString("colour"); date
			 * = b.getString("datetime");
			 */
			System.out.println("photourl isssssss : " + photourl);
		 newurl = photourl.substring(3);
	System.out.println("newww  photourl isssssss : " + newurl);
	image_location = "http://" + Appsettings.ipAddress+"/"+newurl;

		}
		itemdetails.setText(Html.fromHtml("item  <b>\n" + founditemID));
		// itemdetails.setText(Html.fromHtml("item  <b>" + founditemID +
		// " </b> location "+location+ " </b> description "+description+
		// " </b> colour "+colour+ " </b> Date "+date));
		JsonRead mDoPOST = new JsonRead(ItemDetails.this);
		mDoPOST.execute("");
	}

	

	

	// String image_location="http://"+ Appsettings.ipAddress+photourl;

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

}

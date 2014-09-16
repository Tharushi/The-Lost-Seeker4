/**
 * 
 */
package com.example.thelostseeker4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import appsettings.Appsettings;

/**
 * @author Tharushi 110226H
 * 
 */
public class LostandFound extends Activity {
	private String url5 = "http://" + Appsettings.ipAddress
			+ "/mobile/search.php";
	private String url6 = "http://" + Appsettings.ipAddress
			+ "/mobile/lostadmin.php";

	ListView lvfound;
	ListView lvlost;
	SessionManagement session;
	String strcategory;
	String jsonResult;
	Integer nameID;
	String named;
	String namel;
	String namec;
	String namedate;
	String addeduser;
	String photourl;

	String[] stringArray;
	String[] stringArray1;
	String[] stringArray2;
	String[] stringArray3;
	String[] stringArray4;
	String[] stringArray5;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lostandfound);
		final Spinner spinner = (Spinner) findViewById(R.id.spinnercategory);
		lvfound = (ListView) findViewById(R.id.lvfound);
		lvlost = (ListView) findViewById(R.id.lvlost);
		session = new SessionManagement(getApplicationContext());

		HashMap<String, String> user = session.getUserDetails();
		// name
		// name = user.get(SessionManagement.KEY_NAME);
		// lblName.setText(Html.fromHtml("Welcome !!! : <b>" + name + "</b>"));
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.category_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				strcategory = spinner.getSelectedItem().toString();
				// Get the data
				JsonReadTask mDoPOST = new JsonReadTask(LostandFound.this,
						strcategory);
				JsonReadTask1 mDoPOST1 = new JsonReadTask1(LostandFound.this,
						strcategory);
				mDoPOST.execute("");
				mDoPOST1.execute("");

			}

			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});
	}

	// Async Task to access the web
	private class JsonReadTask extends AsyncTask<String, Void, String> {

		Context mContext = null;
		// Result data
		Exception exception = null;

		JsonReadTask(Context context, String nameToSearch) {

			mContext = context;
			strcategory = nameToSearch;
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
				nameValuePairs.add(new BasicNameValuePair("category",
						strcategory));
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

				ListDrwaer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	// build hash set for list view
	public void ListDrwaer() throws IOException {
		List<Map<String, String>> itemDetails = new ArrayList<Map<String, String>>();
		// List<Map<String, String>> itemDetails1 = new ArrayList<Map<String,
		// String>>();

		Date Date = null;
		try {

			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("disease");
			 stringArray = new String[jsonMainNode.length()];
			 stringArray1 = new String[jsonMainNode.length()];
			 stringArray4 = new String[jsonMainNode.length()];

			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				nameID = jsonChildNode.optInt("foundItemID");
				named = jsonChildNode.optString("description");
				namel = jsonChildNode.optString("location");
				namec = jsonChildNode.optString("colour");
				photourl = jsonChildNode.optString("photo");
				namedate = jsonChildNode.optString("foundDate");
				addeduser=jsonChildNode.optString("userID");

				String outPut = "Found ItemID" + "-" + nameID + "\nDescription"
						+ "-" + named + "\nLocation" + "-" + namel + "\nColour"
						+ "-" + namec + "\nFound Date" + "-" + namedate;
			
				
				stringArray1[i]=nameID.toString();		
				
				stringArray[i]=photourl;
				stringArray4[i]=addeduser;

				itemDetails.add(createItem("disease", outPut));

				// loadImage("http://"+ Appsettings.ipAddress
				// + "/mobileimages/pic1img20140913022803.png");

				System.out.println("output isss :" + outPut + " i = " + i);
				lvfound.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						TextView c = (TextView) view; // <--this one
						String text = c.getText().toString();
						System.out
								.println("texttt!!!!!!!!!!!!!!!!!!!!!!!!!! : " + text);
						String photourll = ((TextView) view
								.findViewById(android.R.id.text1)).getText().toString();
						// String founditemidd = ((TextView)
						// view.findViewById(android.R.id.text2)).getText().toString();
						 Bundle b = new Bundle();
					        Intent intent = new Intent(LostandFound.this, AdminViewPhoto.class);
					        b.putString("details", text);
					       
					       b.putString("photourl", stringArray[position]);
					       	
					       System.out.println("photurl!!!!!!!!!!!!!!!!!!!!!!!!!! : "+position+" "+stringArray[position]);
					        
					        b.putString("founditemID",stringArray1[position]);
					        b.putString("addeduser",stringArray4[position]);
					  //     System.out.println("found item!!!!!!!!!!!!!!!!!!!!!!!!!! : "+founditemidd);
					       
					     
					        intent.putExtras(b);  
			                startActivity(intent);
					        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
					    		   
						// Toast.LENGTH_SHORT).show();


					}

				});

			}
		} catch (JSONException e) {

			Log.e("error_log", "Error parsing data " + e.toString());
			// Toast.makeText(getApplicationContext(),
			// "Errortree.." + e.toString(), Toast.LENGTH_LONG).show();
		}

		ListAdapter simpleAdapter = new SimpleAdapter(this, itemDetails,
				android.R.layout.simple_list_item_1, new String[] { "disease",
						"foundid", "url" }, new int[] { android.R.id.text1,
						android.R.id.text2 });

		lvfound.setAdapter(simpleAdapter);
	
	}

	// Async Task to access the web
	private class JsonReadTask1 extends AsyncTask<String, Void, String> {

		Context mContext = null;
		// Result data
		Exception exception = null;

		JsonReadTask1(Context context, String nameToSearch) {

			mContext = context;
			strcategory = nameToSearch;
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
				nameValuePairs.add(new BasicNameValuePair("category",
						strcategory));
				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);

				HttpPost httppost = new HttpPost(url6);

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				System.out.println("comming..kkkkkkkkk");
				jsonResult = EntityUtils.toString(entity);
				System.out
						.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! result entity is - "
								+ jsonResult);

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

				ListDrwaer1();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void ListDrwaer1() throws IOException {
		List<Map<String, String>> itemDetails = new ArrayList<Map<String, String>>();

		Date Date = null;
		try {

			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("disease");
			 stringArray2 = new String[jsonMainNode.length()];
			 stringArray3 = new String[jsonMainNode.length()];
			 stringArray5 = new String[jsonMainNode.length()];

			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				nameID = jsonChildNode.optInt("lostItemID");
				named = jsonChildNode.optString("description");
				namel = jsonChildNode.optString("location");
				namec = jsonChildNode.optString("colour");
				photourl = jsonChildNode.optString("photo");
				namedate = jsonChildNode.optString("lostDate");

				String outPut = "Lost ItemID" + "-" + nameID + "\nDescription"
						+ "-" + named + "\nLost Location" + "-" + namel
						+ "\nColour" + "-" + namec + "\nLost Date" + "-"
						+ namedate;
				stringArray2[i]=nameID.toString();		
				
				stringArray3[i]=photourl;
				stringArray5[i]=addeduser;
				// String outitemID="Found ItemID" + "-" + nameID;
				// String outurl="Photourl" + "-" + photourl;

				itemDetails.add(createItem("disease", outPut));

				// loadImage("http://"+ Appsettings.ipAddress
				// + "/mobileimages/pic1img20140913022803.png");

				System.out.println("output isss :" + outPut + " i = " + i);
			
				lvlost.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						TextView c = (TextView) view; // <--this one
						String text = c.getText().toString();
						System.out
								.println("texttt!!!!!!!!!!!!!!!!!!!!!!!!!! : " + text);
						String photourll = ((TextView) view
								.findViewById(android.R.id.text1)).getText().toString();
						// String founditemidd = ((TextView)
						// view.findViewById(android.R.id.text2)).getText().toString();
						 Bundle b = new Bundle();
					        Intent intent = new Intent(LostandFound.this, AdminViewPhoto.class);
					        b.putString("details", text);
					       
					       b.putString("photourl", stringArray3[position]);
					       	
					       System.out.println("photurl!!!!!!!!!!!!!!!!!!!!!!!!!! : "+position+" "+stringArray[position]);
					        
					        b.putString("founditemID",stringArray2[position]);
					        
					        b.putString("addeduser",stringArray5[position]);
					  //     System.out.println("found item!!!!!!!!!!!!!!!!!!!!!!!!!! : "+founditemidd);
					       
					     
					        intent.putExtras(b);  
			                startActivity(intent);
					        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
					    		   
						//Toast.makeText(getApplicationContext(), text,
							//	Toast.LENGTH_SHORT).show();

					

					}

				});

			}
		} catch (JSONException e) {

			Log.e("error_log", "Error parsing data " + e.toString());
			// Toast.makeText(getApplicationContext(),
			// "Errortree.." + e.toString(), Toast.LENGTH_LONG).show();
		}

		ListAdapter simpleAdapter = new SimpleAdapter(this, itemDetails,
				android.R.layout.simple_list_item_1,
				new String[] { "disease" }, new int[] { android.R.id.text1,
						android.R.id.text2 });

		lvlost.setAdapter(simpleAdapter);

	}

	private HashMap<String, String> createItem(String name, String name1) {
		HashMap<String, String> employeeNameNo = new HashMap<String, String>();
		employeeNameNo.put(name, name1);

	
		return employeeNameNo;

	}

}

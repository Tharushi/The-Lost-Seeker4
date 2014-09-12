package com.example.thelostseeker4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import appsettings.Appsettings;

public class Search extends Activity {
	long mLastClickedPosition = -1;
	private String url5 = "http://" + Appsettings.ipAddress
			+ "/mobile/search.php";
	ListView listViewdise;
	SessionManagement session;
	String name;
	TextView lblName;
	String jsonResult;
	String strcategory = "";
	Spinner spinner;
	String category = "";
	Integer nameID ;
	String named ;
	String namel ;
	String namec ;
	String namedate ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		listViewdise = (ListView) findViewById(R.id.listViewItems);
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

				category = spinner.getSelectedItem().toString();
				// Get the data
				JsonReadTask mDoPOST = new JsonReadTask(Search.this, category);
				mDoPOST.execute("");
				
			}

			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;

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

		private StringBuilder inputStreamToString(InputStream is) {

			String rLine = "";
			StringBuilder answer = new StringBuilder();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			try {
				while ((rLine = rd.readLine()) != null) {
					answer.append(rLine);
				}
			}

			catch (IOException e) {
				// e.printStackTrace();
				Toast.makeText(getApplicationContext(),
						"Error..." + e.toString(), Toast.LENGTH_LONG).show();

			}
			return answer;
		}

		@Override
		protected void onPostExecute(String result) {

			ListDrwaer();

		}
	}

	// build hash set for list view
	public void ListDrwaer() {
		List<Map<String, String>> itemDetails = new ArrayList<Map<String, String>>();

		try {

			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("disease");

			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				 nameID = jsonChildNode.optInt("foundItemID");
				 named = jsonChildNode.optString("description");
				 namel = jsonChildNode.optString("location");
				 namec = jsonChildNode.optString("colour");
				 namedate = jsonChildNode.optString("Found date");

				String outPut = "Found ItemID" + "-" + nameID + "\nDescription"
						+ "-" + named + "\nLocation" + "-" + namel + "\nColour"
						+ "-" + namec;

				itemDetails.add(createItem("disease", outPut));
				System.out.println("output isss :" + outPut+" i = "+i);
				
				
			}
		} catch (JSONException e) {

			Log.e("error_log", "Error parsing data " + e.toString());
			// Toast.makeText(getApplicationContext(),
			// "Errortree.." + e.toString(), Toast.LENGTH_LONG).show();
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(this, itemDetails,
				android.R.layout.simple_list_item_1,
				new String[] { "disease" }, new int[] { android.R.id.text1 });

		listViewdise.setAdapter(simpleAdapter);

	}

	private HashMap<String, String> createItem(String name, String name1) {
		HashMap<String, String> employeeNameNo = new HashMap<String, String>();
		employeeNameNo.put(name, name1);
	
		listViewdise.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

		        TextView c = (TextView) view; //<--this one 
		        String text = c.getText().toString();
		        Bundle b = new Bundle();
		        b.putString("details", text);
		        Intent intent = new Intent(Search.this, ItemDetails.class);
		        intent.putExtras(b);  
                startActivity(intent);
		        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
						   
				
				System.out.println("!!!!!!!!!!!!!! inside");
			

			}
			
		});
		return employeeNameNo;

	}

}

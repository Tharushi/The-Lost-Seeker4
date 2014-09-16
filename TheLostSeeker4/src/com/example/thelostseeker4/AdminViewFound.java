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

import com.example.thelostseeker4.R.id;

import android.app.Activity;

/**
 * @author Tharushi 110226H
 *
 */

import android.app.ListActivity;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import appsettings.Appsettings;

public class AdminViewFound extends ListActivity {
	private String url5 = "http://" + Appsettings.ipAddress
			+ "/mobile/testfound.php";
	private String url6 = "http://" + Appsettings.ipAddress
			+ "/mobile/lostadmin.php";
	
	ListView lvfound;
	ListView lvlost;
	SessionManagement session;
	String strcategory="category";
	String jsonResult;
	Integer nameID ;
	String named ;
	String namel ;
	String namec ;
	String namecategory;
	String namedate ;
	String photourl;
	ListView  lv;
	String outPut[];
	String[] stringArray;
	Button remove;

    Context ctx;
    Resources res;
    CheckBox cb;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.testlist);
        lv=getListView();
        remove=(Button) findViewById(R.id.btnremove);
    	JsonReadTask mDoPOST = new JsonReadTask(AdminViewFound.this, strcategory);
		
		mDoPOST.execute("");
   

         ctx = getApplicationContext();
        res = ctx.getResources();

        String[] options = {"Sri Lanka","India","Pakistan"};

          

    }



    @Override
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
			//	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			//	nameValuePairs.add(new BasicNameValuePair("category",
			//			strcategory));
				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);

				HttpPost httppost = new HttpPost(url5);

			//	httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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
	
		try {

			JSONObject jsonResponse = new JSONObject(jsonResult);
			JSONArray jsonMainNode = jsonResponse.optJSONArray("disease");
			 stringArray = new String[jsonMainNode.length()];

			for (int i = 0; i < jsonMainNode.length(); i++) {
				JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
				 nameID = jsonChildNode.optInt("foundItemID");
				 namecategory=jsonChildNode.optString("category");
				 named = jsonChildNode.optString("description");
				 namel = jsonChildNode.optString("location");
				 namec = jsonChildNode.optString("colour");
				 photourl=jsonChildNode.optString("photo");
				 namedate = jsonChildNode.optString("foundDate");
						
			 String outputtt = "Found ItemID" + "-" + nameID + "\nCategory"
						+ "-" + namecategory+ "\nDescription"
						+ "-" + named + "\nLocation" + "-" + namel + "\nColour"
						+ "-" + namec+ "\nFound Date"+"-"+namedate;
			 
			 System.out
				.println("output isssssss!!!!!!!!!!  i   "+i+"  "+outputtt);	
			 stringArray[i]=outputtt;
				
			}
		} catch (JSONException e) {

			Log.e("error_log", "Error parsing data " + e.toString());
			// Toast.makeText(getApplicationContext(),
			// "Errortree.." + e.toString(), Toast.LENGTH_LONG).show();
		}

	
	
	setListAdapter((ListAdapter) new CheckboxAdapter(ctx,R.layout.list_item,stringArray));
	
   
    for (int x = 0; x<lv.getChildCount();x++){
        cb = (CheckBox)lv.getChildAt(x).findViewById(R.id.checkBox1);
        if(cb.isChecked()){
          
        }
    }
	
	}

}

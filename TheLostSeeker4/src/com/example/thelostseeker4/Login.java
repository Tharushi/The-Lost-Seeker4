package com.example.thelostseeker4;


import android.os.StrictMode;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Login extends Activity {
	private String url1 = "http://" + Appsettings.ipAddress
			+ "/thelostseekerWeb/login.php";
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	
	private int status = 0;
	Button getData = null;
	EditText inputusername = null;
	EditText inputpassword = null;
	protected ProgressBar progBar;
	protected Intent intent;
	String username;
	String password;
	TextView message = null;
	InputStream is=null;
	String result=null;
	 String line=null;
	 int code;

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
				username=inputusername.getText().toString();
				password=inputpassword.getText().toString();

				trylogin();
			
			}
		});
	}
	   public void trylogin()
	    {
		   StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

			StrictMode.setThreadPolicy(policy); 
	    	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    	
	 
	    	nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			
	    	
	    	try
	    	{
			HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost("http://10.0.2.2/login.php");
		       // HttpPost httppost = new HttpPost("http:// 175.157.131.177/login.php");
		       
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost); 
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		        Log.e("pass 1", "connection success ");
		}
	        catch(Exception e)
		{
	        	Log.e("Fail 1", e.toString());
		    	Toast.makeText(getApplicationContext(), "Invalid IP Address",
				Toast.LENGTH_LONG).show();
		}     
	        
	        try
	        {
	         	BufferedReader reader = new BufferedReader
					(new InputStreamReader(is,"iso-8859-1"),8);
	            	StringBuilder sb = new StringBuilder();
	            	while ((line = reader.readLine()) != null)
			{
	       		    sb.append(line + "\n");
	           	}
	            	is.close();
	            	result = sb.toString();
		        Log.e("pass 2", "connection success ");
		        startActivity(new Intent(Login.this, Main.class));
		        
		}
	        catch(Exception e)
	    	{
			Log.e("Fail 2", e.toString());
		}     
	       
	   	try
	    	{
	        //	JSONObject json_data = new JSONObject(result);
	   		setContentView(R.layout.usermain);
	        	//name=(json_data.getString("name"));
		//	Toast.makeText(getBaseContext(), "Name : "+name,
			//	Toast.LENGTH_SHORT).show();
	    	}
	        catch(Exception e)
	    	{
	        	Log.e("Fail 3", e.toString());
	    	}
	    }
	    
	 
	
	

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * 
	 * return true;
	 * 
	 * }
	 * 
	 * protected Dialog onCreateDialog(int id) { switch (id) { case
	 * DIALOG_DOWNLOAD_PROGRESS: mProgressDialog = new ProgressDialog(this);
	 * mProgressDialog.setMessage("Please wait while connecting...");
	 * mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	 * mProgressDialog.setCancelable(false); mProgressDialog.show(); return
	 * mProgressDialog; default: return null; } }
	 * 
	 * public class DoPOST extends AsyncTask<String, Void, Boolean> {
	 * 
	 * Context mContext = null; String username = ""; String password = "";
	 * 
	 * // Result data String strFirstName; String strLastName;
	 * 
	 * String UserName;
	 * 
	 * Exception exception = null;
	 * 
	 * DoPOST(Context context, String username, String password) { mContext =
	 * context; this.username = username; this.password = password; }
	 * 
	 * protected void onPreExecute() { super.onPreExecute();
	 * showDialog(DIALOG_DOWNLOAD_PROGRESS); }
	 * 
	 * @Override protected Boolean doInBackground(String... arg0) {
	 * setProgress(50); try { System.out
	 * .println("!!!!!!!!!!!!!!!!mmmmmmmm**********************"); // Setup the
	 * parameters ArrayList<NameValuePair> nameValuePairs = new
	 * ArrayList<NameValuePair>(); nameValuePairs .add(new
	 * BasicNameValuePair("username", username)); nameValuePairs .add(new
	 * BasicNameValuePair("password", password));
	 * 
	 * // Add more parameters as necessary
	 * 
	 * // Create the HTTP request HttpParams httpParameters = new
	 * BasicHttpParams();
	 * 
	 * System.out.println("!!!!!!!!!!!!xxxxxxxxxx*******************"); // Setup
	 * timeouts HttpConnectionParams .setConnectionTimeout(httpParameters,
	 * 15000); HttpConnectionParams.setSoTimeout(httpParameters, 15000);
	 * System.out .println("!!!!!!!!!!!!!!aaaaaaaaaaaaaaaaaaaaaaa*********");
	 * HttpClient httpclient = new DefaultHttpClient(httpParameters);
	 * System.out.println("!!!!!!!!!!!!!!aaaaaaa********"); HttpPost httppost =
	 * new HttpPost(url1); System.out.println("!!!!!!!!!!!!!!a********");
	 * httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	 * System.out.println("!!!!!!!!!!bbbbbbbbbbbbbbbbbbbbbbbb*******");
	 * HttpResponse response = httpclient.execute(httppost); HttpEntity entity =
	 * response.getEntity();
	 * System.out.println("!!!!!!!ccccccccccccccccc*****"); String result =
	 * EntityUtils.toString(entity); System.out
	 * .println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! result entity is - " +
	 * result);
	 * 
	 * // Create a JSON object from the request response JSONObject jsonObject =
	 * new JSONObject(result); // Retrieve the data from the JSON object
	 * strFirstName = jsonObject.getString("Result"); UserName =
	 * jsonObject.getString("Name"); String value = strFirstName.trim();
	 * 
	 * System.out.println("***************************username is" + UserName);
	 * 
	 * int x = Integer.parseInt(value); System.out
	 * .println("********************************** result string is " + x); int
	 * test = 1; if (x == test) {
	 * System.out.println("********************** came to pass"); status = 1;
	 * session.createLoginSession(UserName); startActivity(new
	 * Intent(Login.this, Main.class)); } else { Context context =
	 * getApplicationContext(); CharSequence text =
	 * "User name or password incorrect";
	 * System.out.println("!!!!!!!!!!!jjjjjjjjjjjjjjjjjjj"); int duration =
	 * Toast.LENGTH_LONG;
	 * 
	 * Toast toast = Toast.makeText(context, text, duration); toast.show();
	 * System.out.println("mmmmm");
	 * System.out.println("********************* came to fail"); status = 0;
	 * 
	 * }
	 * 
	 * } catch (Exception e) { Log.e("ClientServerDemo", "Error:", e); exception
	 * = e; }
	 * 
	 * return true; }
	 * 
	 * protected void onProgressUpdate(String... progress) {
	 * mProgressDialog.setProgress(Integer.parseInt(progress[0]));
	 * setProgress(50); }
	 * 
	 * @Override protected void onPostExecute(Boolean valid) { //
	 * startActivity(new Intent(MainActivity.this, mainmenu.class));
	 * setProgress(100); dismissDialog(DIALOG_DOWNLOAD_PROGRESS); if (status ==
	 * 0) { getData.setEnabled(true);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 */

}

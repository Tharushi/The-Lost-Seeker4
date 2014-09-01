package com.example.thelostseeker4;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint.Join;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import appsettings.Appsettings;

public class Register extends Activity {

	Context ContextNew = null;
	String strnewfirstName = "";
	String  strnewlastName = "";
	String strNewAddress = "";
	 String strNewTP = "";
	 
	 String strNewEmail = "";
	 String strNewusername = "";
	 String	 strNewPassword = "";
	 InputStream is=null;
		String result=null;
	 String line=null;
	 int code;
	private String url3 = "http://" + Appsettings.ipAddress
			+ "/thelostseekerWeb/mobilenewuser.php";
	EditText inputfirstname = null;
	EditText inputlastname = null;
	EditText inputaddress = null;
	EditText editTP = null;
	EditText editEmail = null;
	EditText editusername = null;
	EditText editpassword = null;
	Button register = null;
	private int status = 0;
	protected ProgressBar progBar;
	private ProgressDialog ProgressDialognewuser;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	final Context contextalert = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		register = (Button) findViewById(R.id.btnregister);
		inputfirstname = (EditText) findViewById(R.id.txtfname);
		inputlastname = (EditText) findViewById(R.id.txtlname);
		inputaddress = (EditText) findViewById(R.id.txtaddress);
		editTP = (EditText) findViewById(R.id.txttelephone);

		editEmail = (EditText) findViewById(R.id.txtusername);
		editusername = (EditText) findViewById(R.id.txtuname);
		editpassword = (EditText) findViewById(R.id.txtpword);

		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
		
				
				strnewfirstName=inputfirstname.getText().toString();
				strnewlastName=inputlastname.getText().toString();
				strNewEmail=editEmail.getText().toString();
				strNewTP=editTP.getText().toString();
				strNewAddress=inputaddress.getText().toString();
				strNewusername=editusername.getText().toString();
				strNewPassword=editpassword.getText().toString();
				
				
				insert();
				// Get the data
				/*
				 * DoPOST mDoPOST = new DoPOST(Register.this, inputaddress
				 * .getText().toString(), inputlastname.getText() .toString(),
				 * inputaddress.getText().toString(), editTP
				 * .getText().toString(), editEmail.getText().toString(),
				 * editusername.getText() .toString(),
				 * editpassword.getText().toString());
				 * 
				 * System.out.println("******************************" +
				 * inputfirstname.getText().toString());
				 * System.out.println("******************************" +
				 * inputlastname.getText().toString());
				 * System.out.println("******************************" +
				 * inputaddress.getText().toString());
				 * System.out.println("******************************" +
				 * editusername.getText().toString());
				 * System.out.println("******************************" +
				 * editpassword.getText().toString());
				 * 
				 * mDoPOST.execute(""); register.setEnabled(false);
				 */
			}
		});
	}

	public void insert() {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 
		
		nameValuePairs.add(new BasicNameValuePair("GetFirstName",strnewfirstName));
		  nameValuePairs.add(new BasicNameValuePair("GetLastName",strnewlastName));
		  nameValuePairs.add(new BasicNameValuePair("GetEmail",strNewEmail));
		  
		  nameValuePairs.add(new BasicNameValuePair("GetTP", strNewTP));
		  nameValuePairs.add(new BasicNameValuePair("GetAddress", strNewAddress));
		  
		  
		  nameValuePairs.add(new BasicNameValuePair("GetUserName",strNewusername));
		  nameValuePairs.add(new BasicNameValuePair("GetPassword",strNewPassword));

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://10.0.2.2/insert.php");
			// HttpPost httppost = new HttpPost("http:// 175.157.131.177/login.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			Log.e("pass 1", "connection success ");
		} catch (Exception e) {
			Log.e("Fail 1", e.toString());
			Toast.makeText(getApplicationContext(), "Invalid IP Address",
					Toast.LENGTH_LONG).show();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			Log.e("pass 2", "connection success ");
		} catch (Exception e) {
			Log.e("Fail 2", e.toString());
		}

		try {
			JSONObject json_data = new JSONObject(result);
			code = (json_data.getInt("code"));

			if (code == 1) {
				Toast.makeText(getBaseContext(), "Inserted Successfully",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getBaseContext(), "Sorry, Try Again",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Log.e("Fail 3", e.toString());
		}
	}

	//@Override
	/*public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}*

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present. return true; }
	 * 
	 * protected Dialog onCreateDialog(int id) { switch (id) { case
	 * DIALOG_DOWNLOAD_PROGRESS: ProgressDialognewuser = new
	 * ProgressDialog(this);
	 * ProgressDialognewuser.setMessage("Please wait while connecting to database"
	 * ); ProgressDialognewuser
	 * .setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	 * ProgressDialognewuser.setCancelable(false); ProgressDialognewuser.show();
	 * return ProgressDialognewuser; default: return null; } }
	 * 
	 * public class DoPOST extends AsyncTask<String, Void, Boolean> {
	 * 
	 * Context ContextNew = null; String strnewfirstName = ""; String
	 * strnewlastName = ""; String strNewAddress = ""; String strNewTP = "";
	 * 
	 * String strNewEmail = ""; String strNewusername = ""; String
	 * strNewPassword = "";
	 * 
	 * // Result data String strfirstName; String stlastName; String strAddress;
	 * String strTP;
	 * 
	 * String strEmail;
	 * 
	 * String strusername; String strpassword; InputStream is;
	 * 
	 * Exception exception = null;
	 * 
	 * DoPOST(Context context, String usernameToSearch1, String
	 * usernameToSearch2, String usernameToSearch3, String usernameToSearch4,
	 * String usernameToSearch5, String usernameToSearch6, String
	 * usernameToSearch7) { ContextNew = context; strnewfirstName =
	 * usernameToSearch1; strnewlastName = usernameToSearch2; strNewAddress =
	 * usernameToSearch3; strNewTP = usernameToSearch4; strNewEmail =
	 * usernameToSearch5; strNewusername = usernameToSearch6; strNewPassword =
	 * usernameToSearch7; }
	 * 
	 * protected void onPreExecute() { super.onPreExecute();
	 * showDialog(DIALOG_DOWNLOAD_PROGRESS); }
	 * 
	 * @Override protected Boolean doInBackground(String... arg0) {
	 * setProgress(50); try {
	 * 
	 * // Setup the parameters ArrayList<NameValuePair> nameValuePairs = new
	 * ArrayList<NameValuePair>(); nameValuePairs.add(new
	 * BasicNameValuePair("GetFirstName",strnewfirstName));
	 * nameValuePairs.add(new BasicNameValuePair("GetLastName",strnewlastName));
	 * nameValuePairs.add(new BasicNameValuePair("GetEmail",strNewEmail));
	 * 
	 * nameValuePairs.add(new BasicNameValuePair("GetTP", strNewTP));
	 * nameValuePairs.add(new BasicNameValuePair("GetAddress", strNewAddress));
	 * 
	 * 
	 * nameValuePairs.add(new BasicNameValuePair("GetUserName",strNewusername));
	 * nameValuePairs.add(new BasicNameValuePair("GetPassword",strNewPassword));
	 * // Add more parameters as necessary
	 * 
	 * // Create the HTTP request HttpParams httpParameters = new
	 * BasicHttpParams();
	 * 
	 * // Setup timeouts HttpConnectionParams
	 * .setConnectionTimeout(httpParameters, 15000);
	 * HttpConnectionParams.setSoTimeout(httpParameters, 15000);
	 * 
	 * HttpClient httpclient = new DefaultHttpClient(httpParameters); HttpPost
	 * httppost = new HttpPost(url3);
	 * 
	 * httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	 * 
	 * HttpResponse response = httpclient.execute(httppost);
	 * 
	 * HttpEntity entity = response.getEntity();
	 * 
	 * 
	 * // try parse the string to a JSON object
	 * 
	 * 
	 * String result = EntityUtils.toString(entity); System.out
	 * .println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! result entity is - " +
	 * result);
	 * 
	 * // Create a JSON object from the request response JSONObject jsonObject =
	 * new
	 * JSONObject(result.substring(result.indexOf("{"),result.lastIndexOf("}") +
	 * 1));
	 * 
	 * // Retrieve the data from the JSON object strfirstName =
	 * jsonObject.getString("Result");
	 * 
	 * String value = strfirstName.trim(); int x = Integer.parseInt(value);
	 * System.out
	 * .println("********************************** result string is " + x); int
	 * test = 1; if (x == test) {
	 * System.out.println("********************** came to pass"); status = 1;
	 * startActivity(new Intent(Register.this, Login.class));
	 * 
	 * } else { Context context = getApplicationContext(); CharSequence text =
	 * "incorrect"; int duration = Toast.LENGTH_LONG;
	 * 
	 * Toast toast = Toast.makeText(context, text, duration); toast.show();
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
	 * ProgressDialognewuser.setProgress(Integer.parseInt(progress[0]));
	 * setProgress(50); }
	 * 
	 * @Override protected void onPostExecute(Boolean valid) { setProgress(100);
	 * dismissDialog(DIALOG_DOWNLOAD_PROGRESS); if (status == 0) {
	 * register.setEnabled(true);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 */

}

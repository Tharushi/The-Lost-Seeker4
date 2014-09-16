package com.example.thelostseeker4;
/**
 * @author Tharushi 110226H
 * 
 */
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
	private String url3 = "http://" + Appsettings.ipAddress
			+ "/mobile/mobilenewuser.php";
	EditText editTextnewname = null;
	EditText editTextnewname1 = null;
	EditText editTextnewaddress = null;
	EditText editTP = null;
	EditText editNIC = null;
	EditText editEmail = null;
	EditText editTextds = null;
	EditText editgn = null;
	EditText editusername = null;
	EditText editpassword = null;
	Button addnewuser= null;
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
	
	addnewuser = (Button) findViewById(R.id.btnregister);
	editTextnewname = (EditText) findViewById(R.id.txtfname);
	editTextnewname1 = (EditText) findViewById(R.id.txtlname);
	editTextnewaddress = (EditText) findViewById(R.id.txtaddress);
	editTP = (EditText) findViewById(R.id.txttelephone);

	editEmail = (EditText) findViewById(R.id.txtemail);
	
	editusername = (EditText) findViewById(R.id.txtuname);
	editpassword= (EditText) findViewById(R.id.txtpword);
	
	addnewuser.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			// Get the data
			DoPOST mDoPOST = new DoPOST(Register.this,
					editTextnewname.getText().toString(),
					editTextnewname1.getText().toString(),
					editTextnewaddress.getText().toString(),
					editTP.getText().toString(),
					
					editEmail.getText().toString(),
					editusername.getText().toString(),
					editpassword.getText().toString());

			System.out.println("******************************"
					+ editTextnewname.getText().toString());
			System.out.println("******************************"
					+ editTextnewaddress.getText().toString());
			System.out.println("******************************"
					+ editusername.getText().toString());
			System.out.println("******************************"
					+ editpassword.getText().toString());


			mDoPOST.execute("");
			addnewuser.setEnabled(false);
		}
	});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Inflate the menu; this adds items to the action bar if it is present.	
		return true;
	}
	
	protected Dialog onCreateDialog(int id) {
	    switch (id) {
	    case DIALOG_DOWNLOAD_PROGRESS:
	    	ProgressDialognewuser = new ProgressDialog(this);
	    	ProgressDialognewuser.setMessage("Please wait while connecting...");
	    	ProgressDialognewuser.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	    	ProgressDialognewuser.setCancelable(false);
	    	ProgressDialognewuser.show();
	        return ProgressDialognewuser;
	    default:
	    return null;
	    }
	}
	public class DoPOST extends AsyncTask<String, Void, Boolean> {

		Context ContextNew = null;
		String strNewName = "";
		String strNewName1 = "";
		String strNewAddress = "";
		String strNewTP = "";
		
		String strNewEmail = "";
		
		String strNewusername = "";
		String strNewPassword= "";


		// Result data
		String strName;
		String strName1;
		String strAddress;
		String strTP;
		String strNIC;
		String strEmail;
		String strDS;
		String strGN;
		String strusername;
		String strpassword;
		int intAge;
		int intPoints;

		Exception exception = null;

		DoPOST(Context context, String usernameToSearch1, String usernameToSearch2, String usernameToSearch3, String usernameToSearch4,
				String usernameToSearch5, String usernameToSearch6, String usernameToSearch7)
				{
			ContextNew= context;
			strNewName = usernameToSearch1;
			strNewName1 = usernameToSearch2;
			strNewAddress = usernameToSearch3;
			strNewTP = usernameToSearch4;
		   
			strNewEmail = usernameToSearch5;
		
			strNewusername = usernameToSearch6;
			strNewPassword=usernameToSearch7;
		}

		protected void onPreExecute() {
	        super.onPreExecute();
	        showDialog(DIALOG_DOWNLOAD_PROGRESS);
	    }
		
		@Override
		protected Boolean doInBackground(String... arg0) {
			 setProgress(50);
			try {

				// Setup the parameters
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("GetName",
						strNewName));
				nameValuePairs.add(new BasicNameValuePair("GetName1",
						strNewName1));
				nameValuePairs.add(new BasicNameValuePair("GetAddress",
						strNewAddress));
				nameValuePairs.add(new BasicNameValuePair("GetTP",
						strNewTP));
				
				nameValuePairs.add(new BasicNameValuePair("GetEmail",
						strNewEmail));
				
				nameValuePairs.add(new BasicNameValuePair("GetUserName",
						strNewusername));
				nameValuePairs.add(new BasicNameValuePair("GetPassword",
						strNewPassword));
				// Add more parameters as necessary

				// Create the HTTP request
				HttpParams httpParameters = new BasicHttpParams();

				// Setup timeouts
				HttpConnectionParams
						.setConnectionTimeout(httpParameters, 15000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpPost httppost = new HttpPost(url3);
					

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();

				String result = EntityUtils.toString(entity);
				System.out
						.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! result entity is - "
								+ result);

				// Create a JSON object from the request response
				try{
					JSONObject jsonObject = new JSONObject(result);

					// Retrieve the data from the JSON object
					strName = jsonObject.getString("Result");

					String value = strName.trim();
					int x = Integer.parseInt(value);
					System.out
							.println("********************************** result string is "
									+ x);
					int test = 1;
					if (x == test) {
						System.out.println("********************** came to pass");
						status = 1;
						final Context context = getApplicationContext();
						final CharSequence text = "Successfully Registered";
						 System.out.println("!!!!!!!!!!!jjjjjjjjjjjjjjjjjjj");
						final int duration = Toast.LENGTH_LONG;
						 
						runOnUiThread(new Runnable() {
							public void run() {

							    Toast.makeText(context, text,duration).show();
							    }
							});
						startActivity(new Intent(Register.this, Login.class));
						
					
					} else {
						final Context context = getApplicationContext();
						final CharSequence text = "incorrect";
						final int duration = Toast.LENGTH_LONG;

						//Toast toast = Toast.makeText(context, text, duration);
						//toast.show();
						 System.out.println("!!!!!!!!!!!elseee...incorect user");
						runOnUiThread(new Runnable() {
							public void run() {

							    Toast.makeText(context, text,duration).show();
							    }
							});
						System.out.println("********************* came to fail");
						status = 0;

					}
				}catch(Exception e){
					final Context context = getApplicationContext();
					final CharSequence text = "Incorrect registration OR Existing  user name";
					final int duration = Toast.LENGTH_LONG;

					//Toast toast = Toast.makeText(context, text, duration);
					//toast.show();
					 System.out.println("!!!!!!!!!!!elseee...incorect user");
					runOnUiThread(new Runnable() {
						public void run() {

						    Toast.makeText(context, text,duration).show();
						    }
						});
					Log.e("Fail 3", e.toString());
					
				}


	


			} catch (Exception e) {
				Log.e("ClientServerDemo", "Error:", e);
				exception = e;
			}

			return true;
		}
		
		 protected void onProgressUpdate(String... progress) {        
			    ProgressDialognewuser.setProgress(Integer.parseInt(progress[0]));
			    setProgress(50);
			    }
		 
		@Override
		protected void onPostExecute(Boolean valid) {
			setProgress(100);
			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			if(status==0){
				addnewuser.setEnabled(true);


			}
			
		}

	}
}

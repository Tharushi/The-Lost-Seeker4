/**
 * 
 */
package com.example.thelostseeker4;

import java.io.ByteArrayOutputStream;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.example.thelostseeker4.ColorPickerDialog.OnColorChangedListener;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
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
public class AddFoundItem extends Activity implements
		ColorPickerDialog.OnColorChangedListener {
	private String url4 = "http://" + Appsettings.ipAddress
			+ "/mobile/sendmobile.php";

	private static final int CAMERA_REQUEST = 1888; 
    private ImageView imageView;
	Button add = null;
	Spinner category;
	TextView inputdescription = null;
	TextView inputlocation = null;
	TextView inputdate = null;
	String IMAGE;

	private int status = 0;
	protected ProgressBar progBar;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
	static int count = 1;
	Date date;
	ColorPickerDialog clpicker;
	private Paint mPaint;
	OnColorChangedListener listener;
	DatePicker dp;
	TextView departDate;
	Button btnDate;

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
		
		  this.imageView = (ImageView)this.findViewById(R.id.imageView1);
	        Button photoButton = (Button) this.findViewById(R.id.btncapture);
	        photoButton.setOnClickListener(new View.OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
	                startActivityForResult(cameraIntent, CAMERA_REQUEST); 
	            }
	        });
		mPaint = new Paint();
		// /////////////////////////////////////////////////////
		// Session class instance
		session = new SessionManagement(getApplicationContext());
		TextView lblName = (TextView) findViewById(R.id.lbluser);
		departDate = (TextView) findViewById(R.id.txtdate);
		btnDate = (Button) findViewById(R.id.btndate);

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

		add = (Button) findViewById(R.id.btnaddlost);
		Button addcolor = (Button) findViewById(R.id.btnaddcolor);
		TextView txtcol = (TextView) findViewById(R.id.txtcolor);
		addcolor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				clpicker = new ColorPickerDialog(AddFoundItem.this,
						AddFoundItem.this, mPaint.getColor());
				clpicker.show();
				System.out.println("selected color ....." + mPaint.getColor());

			}
		});

		txtcol.setText(String.valueOf(mPaint.getColor()));

		category = (Spinner) findViewById(R.id.spinner3);
		inputdescription = (TextView) findViewById(R.id.txtdescrption);
		inputlocation = (TextView) findViewById(R.id.txtlocation1);
		inputdate=(TextView) findViewById(R.id.txtdate);

		// dp=(DatePicker) findViewById(R.id.datePicker);

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
						.toString(), nameget,inputdate.getText().toString(),IMAGE);

				System.out.println("!!nnnnnnnnnn!!!!!!!!!!!!!!!!");
				mDoPOST.execute("");
				add.setEnabled(false);

				System.out.println("******************************"
						+ inputdescription.getText().toString());
				System.out.println("******************************"
						+ inputlocation.getText().toString());

			}
		});

		btnDate.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				Calendar calender = Calendar.getInstance();
				Dialog mDialog = new DatePickerDialog(AddFoundItem.this,
						mDatesetListener, calender.get(Calendar.YEAR), calender
								.get(Calendar.MONTH), calender
								.get(Calendar.DAY_OF_MONTH));

				mDialog.show();
			}
		});

	}
	
	   protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
	            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
	            ByteArrayOutputStream out=new ByteArrayOutputStream();
			      photo.compress(Bitmap.CompressFormat.PNG,100,out);
			     // byte ByteArr[]=out.toByteArray();
			      byte[] ba = out.toByteArray();
	            imageView.setImageBitmap(photo);
	            IMAGE = Base64.encodeToString(ba,Base64.DEFAULT);
	        }  
	    }

	private DatePickerDialog.OnDateSetListener mDatesetListener = new OnDateSetListener() {

		public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
			arg2 = arg2 + 1;

			String my_date = arg1 + "-" + arg2 + "-" + arg3;
			departDate.setText(my_date);
		}
	};

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
		String strDate = "";

		String username = "";

		String imagetosearch1 = IMAGE;
		String imagetosearch2 = IMAGE2;

		String strNameofint = y;
		String getusername = "";

		// Result data
		String strNameRice;
		// String strNameAge;

		String strNameIrri;

		Exception exception = null;

		// String NameToSearch1,
		DoPOST(Context context, String nameToSearch, String NameToSearch1,
				String NameToSearch2, String NameToSearch3,String NameToSearch4,String NameToSearch5) {
			mContext = context;
			strCategory = nameToSearch;
			strDescription = NameToSearch1;
			strLocation = NameToSearch2;
			username = NameToSearch3;
			strDate=NameToSearch4;
			imagetosearch1=NameToSearch5;
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
				nameValuePairs
				.add(new BasicNameValuePair("date", strDate));
				nameValuePairs.add(new BasicNameValuePair("image1",
						imagetosearch1));

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
				// httppost.setHeader("Accept", "application/json");
				//   httppost.setHeader("Content-type", "application/json");
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
						System.out.println("!!!aaaaaaaaaaaaaaa!!!!!!!!!!!!");
						System.out
								.println("********************** came to pass");
						status = 1;
						final Context context = getApplicationContext();
						final CharSequence text = "Successfully added an Item";
						final int duration = Toast.LENGTH_LONG;

						// Toast toast = Toast.makeText(context, text,
						// duration);
						// toast.show();

						runOnUiThread(new Runnable() {
							public void run() {

								Toast.makeText(context, text, duration).show();
							}
						});
						startActivity(new Intent(AddFoundItem.this, Main.class));

					} else {

						System.out.println("!!!!yyyyyyyyyyy!!!!!!!!!!!!!");
						Context context = getApplicationContext();
						CharSequence text = "Cant send the problem Details. Try again later!!";
						int duration = Toast.LENGTH_LONG;

						Toast toast = Toast.makeText(context, text, duration);
						toast.show();
						System.out
								.println("********************* came to fail");

						status = 0;

					}
				} catch (Exception e) {
					final Context context = getApplicationContext();
					final CharSequence text = "incorect adding of item";
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
				add.setEnabled(true);
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.example.thelostseeker4.ColorPickerDialog.OnColorChangedListener#
	 * colorChanged(int)
	 */
	@Override
	public void colorChanged(int color) {
		// TODO Auto-generated method stub

	}

}

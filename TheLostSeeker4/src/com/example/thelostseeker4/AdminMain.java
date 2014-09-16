/**
 * 
 */
package com.example.thelostseeker4;



import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import appsettings.Appsettings;


/**
 * @author Tharushi 110226H
 *
 */
public class AdminMain extends Activity {
	private String url4 = "http://" + Appsettings.ipAddress
			+ "/mobile/claims.php";
	
	String nameget;
	Button btnLogout;
	Button btnview;
	Button found;
	Button lost;
	Button claims;

	// String unicode= "?useUnicode=yes&characterEncoding=UTF-8";
	static Dialog d;

	SessionManagement session;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adminview);
		session = new SessionManagement(getApplicationContext());
		TextView lblName = (TextView) findViewById(R.id.txtfname);
		TextView lblname2=(TextView) findViewById(R.id.txtlname);
		// Button logout
		btnLogout = (Button) findViewById(R.id.btnlogout);
		btnview=(Button) findViewById(R.id.btnviewall);
		found=(Button) findViewById(R.id.btnfound);
		lost=(Button) findViewById(R.id.btnlost);
		claims=(Button) findViewById(R.id.btnclaims);
		
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
	//	lblName.setText(Html.fromHtml("Welcome !!! : <b>" + name + "</b>"));
		//lblName.setText("Welcome  "+name);
		//lblname2.setText(name);
		

		btnLogout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// Clear the session data
				// This will clear all session data and
				// redirect user to LoginActivity
				
				session.logoutUser();
			}
		});
		btnview.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
			Intent intent=new Intent(getApplicationContext(), LostandFound.class);
			startActivity(intent);
			}
		});
		found.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
			Intent intent=new Intent(getApplicationContext(),AdminViewFound.class);
			startActivity(intent);
			}
		});
		lost.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
			Intent intent=new Intent(getApplicationContext(),AdminViewLost.class);
			startActivity(intent);
			}
		});
	}
}

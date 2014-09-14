/**
 * 
 */
package com.example.thelostseeker4;



import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * @author Tharushi 110226H
 *
 */
public class AdminMain extends Activity {
	
	String nameget;
	Button btnLogout;

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
	}
}

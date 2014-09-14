package com.example.thelostseeker4;
/**
 * @author Tharushi 110226H
 * 
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import com.example.thelostseeker4.*;

public class Main extends Activity {

	Button changepas;
	// Session Manager Class
	SessionManagement session;
	Button btnLogout;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		   final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
		// /////////////////////////////////////////////////////
	
		// Session class instance
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
		System.out.println("welcome!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + name);
		
		// displaying user data
	//	lblName.setText(Html.fromHtml("Welcome !!! : <b>" + name + "</b>"));
		//lblName.setText("Welcome  "+name);
		//lblname2.setText(name);
		

		btnLogout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// Clear the session data
				// This will clear all session data and
				// redirect user to LoginActivity
				arg0.startAnimation(animAlpha);
				session.logoutUser();
			}
		});

		// /////////////////////////////////////////////////////

		Button search = (Button) findViewById(R.id.btnsearch);
		Button addfound = (Button) findViewById(R.id.btnaddfounditem);
		Button addlost = (Button) findViewById(R.id.btnaddlostitem);
		
		btnLogout = (Button) findViewById(R.id.btnlogout);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				arg0.startAnimation(animAlpha);
				startActivity(new Intent(Main.this, Search.class));
			}
		});
		addfound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				arg0.startAnimation(animAlpha);

				startActivity(new Intent(Main.this, AddFoundItem.class));
			}
		});

		addlost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				arg0.startAnimation(animAlpha);

				startActivity(new Intent(Main.this, AddLostItem.class));
			}
		});

	}
}
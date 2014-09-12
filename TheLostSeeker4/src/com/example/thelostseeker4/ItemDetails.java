/**
 * 
 */
package com.example.thelostseeker4;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

/**
 * @author Tharushi 110226H
 *
 */
public class ItemDetails extends Activity {
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	
	SessionManagement session;
	String nameget;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemdetails);
		
		// Session class instance
		session = new SessionManagement(getApplicationContext());
		TextView lblName = (TextView) findViewById(R.id.txtuser);
		TextView itemdetails = (TextView) findViewById(R.id.txtitem);
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
		lblName.setText(Html.fromHtml("User <b>" + name + "!!! </b>"));

		System.out.println("welcome!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + name);

		nameget = name.toString();
		String founditemID = null;
		String location = null;
		String description = null;
		String colour = null;
		String date = null;
		
		Bundle b = getIntent().getExtras();
		if (b != null) 
		{
			 founditemID = b.getString("details");
			/*founditemID = b.getString("foundItemID");
		     location = b.getString("location");
		     description = b.getString("description");
		     colour = b.getString("colour");
		     date = b.getString("datetime");*/
		       
		    
		  
	}
		itemdetails.setText(Html.fromHtml("item  <b>\n" + founditemID));
		// itemdetails.setText(Html.fromHtml("item  <b>" + founditemID + " </b> location "+location+ " </b> description "+description+ " </b> colour "+colour+ " </b> Date "+date));
		

}
}

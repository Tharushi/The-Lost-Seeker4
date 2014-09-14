/**
 * 
 */
package com.example.thelostseeker4;



import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;


/**
 * @author Tharushi 110226H
 *
 */
public class AdminMain extends Activity {
	
	String nameget;

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
		
	}
}

package com.Tharushi.thelostseeker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ItemDetails extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.itemdetails);
		
		Button claim=(Button) findViewById(R.id.btnclaim);
		claim.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				
				
			}
		});
	}
}

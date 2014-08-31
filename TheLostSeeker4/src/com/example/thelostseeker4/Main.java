package com.example.thelostseeker4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;



import java.util.HashMap;

public class Main extends Activity {
	Button btnLogout;
	Button changepas;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button search = (Button) findViewById(R.id.Btnsearch);
		Button addfound=(Button) findViewById(R.id.btnaddfounditem);
		Button addlost=(Button) findViewById(R.id.Btnaddlostitem);
		changepas = (Button) findViewById(R.id.btchangepass);
		btnLogout = (Button) findViewById(R.id.logout);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Main.this, Search.class));
			}
		}
		);
		


	}
}
package com.example.helloandroid;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null){
			getFragmentManager().beginTransaction()
					.add(android.R.id.content, new SettingsFragment()).commit();
		}
	}

}

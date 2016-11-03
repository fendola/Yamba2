package com.example.helloandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class StatusActivity extends Activity {

	final static String TAG = "clong.StatusActivity"; 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_status);
        Log.w(TAG, "onCreate begin");
//        StatusFragment fragment = new StatusFragment();
        if (savedInstanceState == null) {
        	Log.w(TAG, "first add fragment");
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new StatusFragment())
                    .commit();
        }
        Log.w(TAG, "onCreate end");
    }

}

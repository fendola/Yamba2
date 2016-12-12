package com.example.helloandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class StatusActivity extends Activity {

	final static String TAG = "clong.StatusActivity"; 
	final static String updateAction = StatusActivity.class.getSimpleName() + ".upatestatus";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //load layout with xml
        //setContentView(R.layout.activity_status);

        Log.w(TAG, "onCreate begin");
        if (savedInstanceState == null) {
        	Log.w(TAG, "first add fragment");
            //load layout by dynamic fragment, activity_status.xml will useless
        	getFragmentManager().beginTransaction()
                    .add(android.R.id.content, new StatusFragment())
                    .commit();
        }
        Log.w(TAG, "onCreate end");
    }

}

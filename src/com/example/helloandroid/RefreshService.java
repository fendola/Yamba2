package com.example.helloandroid;
import java.util.List;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClient.Status;
import com.marakana.android.yamba.clientlib.YambaClientException;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


public class RefreshService extends IntentService {
	
	final static String TAG = "clong.RefreshService";

	public RefreshService() {
		super(TAG);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG, "onCreate");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "onDestroy"); 
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onHandleIntent");
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String username = prefs.getString("username", "");
		String password = prefs.getString("password", "");
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
//			startActivity(new Intent(this, SettingsActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			Log.d(TAG, "Please update your username and password");
			Toast.makeText(this, "Please update your username and password", Toast.LENGTH_LONG).show();
			return;
		}
		YambaClient yambaCloud = new YambaClient(username, password, "http://yamba.newcircle.com/api");
		try {
			List<Status> timeline = yambaCloud.getTimeline(20);
			for (Status status : timeline){
				Log.d(TAG, String.format("%s: %s", status.getUser(), status.getMessage()));
				Toast.makeText(this, String.format("%s: %s", status.getUser(), status.getMessage()), Toast.LENGTH_LONG).show();
			}
		} catch (YambaClientException e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "failed to get timeline");
			e.printStackTrace();
		}
	}

}

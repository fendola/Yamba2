package com.example.helloandroid;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	private static final String TAG = BootReceiver.class.getSimpleName();
	private static final long DEFAULT_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		context.startService(new Intent(context, RefreshService.class));
		Log.d(TAG, "onReceived");
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		long refreshInterval = Long.parseLong(prefs.getString("refreshInterval", Long.toString(DEFAULT_INTERVAL)));
		
		PendingIntent pIndent = PendingIntent.getService(context, -1, 
				new Intent(context, RefreshService.class), PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		if (refreshInterval == 0){
			alarmManager.cancel(pIndent);
			Log.d(TAG, "cancel repeat refresh");
		} else {
			alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), refreshInterval, pIndent);
			Log.d(TAG, "set repeat refresh for:" + refreshInterval);
		}
	}
}

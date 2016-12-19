package com.example.helloandroid;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;

public class YambaWidget extends AppWidgetProvider {
	private static final String TAG = YambaWidget.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);

		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		this.onUpdate(context, appWidgetManager, appWidgetManager
				.getAppWidgetIds(new ComponentName(context, YambaWidget.class)));
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		Log.d(TAG, "onUpdate");
		// Get the latest tweet
		Cursor cursor = context.getContentResolver().query(
				StatusConstants.CONTENT_URI, null, null, null,
				StatusConstants.DEFAULT_SORT);
		if (!cursor.moveToFirst())
			return;

		String user = cursor.getString(cursor
				.getColumnIndex(StatusConstants.Column.USER));
		String message = cursor.getString(cursor
				.getColumnIndex(StatusConstants.Column.MESSAGE));
		long createdAt = cursor.getLong(cursor
				.getColumnIndex(StatusConstants.Column.CREATED_AT));
		SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.US);
		CharSequence createdAt_ = format.format(new Date(createdAt));
		
		PendingIntent operation = PendingIntent.getActivity(context, -1,
				new Intent(context, MainActivity.class),
				PendingIntent.FLAG_UPDATE_CURRENT);
		
		// Loop through all the instances of YambaWidget
		for (int appWidgetId : appWidgetIds) {
			// Update the view
			RemoteViews view = new RemoteViews(context.getPackageName(),
					R.layout.widget);
			// Update the remote view
			view.setTextViewText(R.id.list_item_text_user, user);
			view.setTextViewText(R.id.list_item_text_message, message);
			view.setTextViewText(R.id.list_item_text_created_at,createdAt_);
			view.setOnClickPendingIntent(R.id.list_item_text_user, operation);
			view.setOnClickPendingIntent(R.id.list_item_text_message, operation);
			// Update the widget
			appWidgetManager.updateAppWidget(appWidgetId, view);
		}
	}

}

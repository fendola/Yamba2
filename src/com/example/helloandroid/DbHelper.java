package com.example.helloandroid;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

	final static String TAG = DbHelper.class.getSimpleName();
	
	public DbHelper(Context context) {
		super(context, StatusConstants.DB_NAME, null, StatusConstants.DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = String.format("create table %s (%s int primary key, %s text, %s text, %s int)", 
				StatusConstants.TABLE,
				StatusConstants.Column.ID,
				StatusConstants.Column.USER,
				StatusConstants.Column.MESSAGE,
				StatusConstants.Column.CREATED_AT);
		Log.d(TAG, "onCreate with SQL:" + sql);
		db.execSQL(sql);
	}

	// Gets called whenever existing version != new version, i.e. schema changed
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// Typically you do ALTER TABLE ...
		db.execSQL("drop table if exists " + StatusConstants.TABLE);
		onCreate(db);
	}

}

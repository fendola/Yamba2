package com.example.helloandroid;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class StatusProvider extends ContentProvider {

	private static final String TAG = StatusProvider.class.getSimpleName();
	DbHelper dbHelper;

	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		uriMatcher.addURI(StatusConstants.AUTHORITY, StatusConstants.TABLE, StatusConstants.STATUS_DIR);
		uriMatcher.addURI(StatusConstants.AUTHORITY, StatusConstants.TABLE + "/#", StatusConstants.STATUS_ITEM);
	}
	
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		dbHelper = new DbHelper(getContext());
		Log.d(TAG, "onCreated");
		return true;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		String where;
		
		switch (uriMatcher.match(uri)) {
		case StatusConstants.STATUS_DIR:
			// so we count deleted rows
			where = (selection == null) ? "1" : selection;
			break;
		case StatusConstants.STATUS_ITEM:
			long id = ContentUris.parseId(uri);
			where = StatusConstants.Column.ID 
					+ "=" 
				    + id
				    + (TextUtils.isEmpty(selection) ? "" : "add (" + selection + ")");
			break;
		default:
			throw new IllegalArgumentException("Illegal uri:" + uri);
		}
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int ret = db.delete(StatusConstants.TABLE, where, selectionArgs);
		
		if (ret > 0){
			// Notify that data for this uri has changed
			getContext().getContentResolver().notifyChange(uri, null);
			Log.d(TAG, "deleted records: " + ret);
		}
		return ret;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (uriMatcher.match(uri)) {
		case StatusConstants.STATUS_DIR:
			Log.d(TAG, "gotType: " + StatusConstants.STATUS_TYPE_DIR);
			return StatusConstants.STATUS_TYPE_DIR;
		case StatusConstants.STATUS_ITEM:
			Log.d(TAG, "gotType: " + StatusConstants.STATUS_TYPE_ITEM);
			return StatusConstants.STATUS_TYPE_ITEM;
		default:
			throw new IllegalArgumentException("Illegal uri:" + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		Uri ret = null;
		
		//assert correct uri
		if (uriMatcher.match(uri) != StatusConstants.STATUS_DIR){
			throw new IllegalArgumentException("Illegal uri:" + uri);
		}
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long rowId = db.insertWithOnConflict(StatusConstants.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
		
		// Was insert successful?
		if (rowId != -1){
			long id = values.getAsLong(StatusConstants.Column.ID);
			ret = ContentUris.withAppendedId(uri, id);
			Log.d(TAG, "rowId = " + rowId + "/id=" + id + "inserted uri:" + ret);
			
			// Notify that data for this uri has changed
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return ret;
	}


	// SELECT username, message, created_at FROM status WHERE user='bob' ORDER
	// BY created_at DESC;
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		// TODO Auto-generated method stub
		
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables( StatusConstants.TABLE );
		
		switch (uriMatcher.match(uri)) {
		case StatusConstants.STATUS_DIR:
			break;
		case StatusConstants.STATUS_ITEM:
			qb.appendWhere(StatusConstants.Column.ID + "="
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Illegal uri:" + uri);
		}
		
		String orderBy = (TextUtils.isEmpty(sortOrder))
				? StatusConstants.DEFAULT_SORT
				: sortOrder;
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = qb.query(db, projection, selection, selectionArgs,
						null, null, orderBy);
		
		// register for uri changes
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		Log.d(TAG, "queried records: "+cursor.getCount());
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		String where;
		
		switch (uriMatcher.match(uri)) {
		case StatusConstants.STATUS_DIR:
			// so we count updated rows
			where = selection;
			break;
		case StatusConstants.STATUS_ITEM:
			long id = ContentUris.parseId(uri);
			where = StatusConstants.Column.ID 
					+ "=" 
					+ id
					+ (TextUtils.isEmpty(selection) ? "" : "add (" + selection + ")");
			break;
		default:
			throw new IllegalArgumentException("Illegal uri:" + uri);
		}
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int ret = db.update(StatusConstants.TABLE, values, where, selectionArgs);
		
		if (ret > 0){
			// Notify that data for this uri has changed
			getContext().getContentResolver().notifyChange(uri, null);
			Log.d(TAG, "updated records: " + ret);
		}
		return ret;
	}

}

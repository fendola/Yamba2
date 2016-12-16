package com.example.helloandroid;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

public class TimelineFragment extends ListFragment implements LoaderCallbacks<Cursor>{
	private static final String TAG = TimelineFragment.class.getSimpleName();

	private static final String[] FROM = { StatusConstants.Column.USER,
			StatusConstants.Column.MESSAGE, StatusConstants.Column.CREATED_AT,
			StatusConstants.Column.CREATED_AT };
	private static final int[] TO = { R.id.list_item_text_user,
			R.id.list_item_text_message, R.id.list_item_text_created_at,
			R.id.list_item_text_created_at };
	
	private SimpleCursorAdapter mAdapter;
	private static final int LOADER_ID = 42;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item,
				null, FROM, TO, 0);
		mAdapter.setViewBinder(new TimelineViewBinder());
		setListAdapter(mAdapter);
		
		//an arbitrary ID that will help us make sure that the loader calling back is the one we initiated.
		getLoaderManager().initLoader(LOADER_ID, null, this);
	}

	//implement LoaderCallbacks<Cursor>, which is the set of callbacks that will be called when the data is available.
	//onCreateLoader() is where the data is actually loaded. Again, this runs on a
	//worker thread and may take a long time to complete.
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		if (id != LOADER_ID)
			return null;
		Log.d(TAG, "onCreateLoader");
		return new CursorLoader(getActivity(), StatusConstants.CONTENT_URI,
				null, null, null, StatusConstants.DEFAULT_SORT);//A CursorLoader loads the data from the content provider.
	}

	//Once the data is loaded, the system will call back our code via onLoadFinished(), passing in the data.
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// TODO Auto-generated method stub
		
		// Get the details fragment
		DetailsFragment fragment = (DetailsFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_details);
		// Is details fragment visible?
		if (fragment != null && fragment.isVisible() && cursor.getCount() == 0) {
			fragment.updateView(-1);
			Toast.makeText(getActivity(), "No data", Toast.LENGTH_LONG).show();
		}
		
		Log.d(TAG, "onLoadFinished with cursor: " + cursor.getCount());
		mAdapter.swapCursor(cursor);//update the data that the adapter is using to update the list view.
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		mAdapter.swapCursor(null);//In case the data is stale or unavailable, we remove it from the view.
	}

	/** Handles custom binding of data to view. */
	class TimelineViewBinder implements ViewBinder { //
		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) { //
			if (view.getId() != R.id.list_item_text_created_at) //
				return false;
			// Convert timestamp to relative time
			String timestamp = cursor.getString(columnIndex); //
			Log.d(TAG, "" + timestamp);
			SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.US);
			CharSequence relativeTime = format.format(new Date(timestamp));
			// CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(timestamp); //
			((TextView) view).setText(relativeTime); //
			return true; //
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		// Get the details fragment
		DetailsFragment fragment = (DetailsFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_details); //
		// Is details fragment visible?
		if (fragment != null && fragment.isVisible()) { //
			fragment.updateView(id); //
		} else {
			startActivity(new Intent(getActivity(), DetailsActivity.class)
					.putExtra(StatusConstants.Column.ID, id)); //
		}
	}
	
	
}

package com.example.helloandroid;

import android.app.Fragment;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailsFragment extends Fragment {
	
	private TextView textUser, textMessage, textCreateAt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.list_item, container, false);
		
		textUser = (TextView) view.findViewById(R.id.list_item_text_user);
		textMessage = (TextView) view.findViewById(R.id.list_item_text_message);
		textCreateAt = (TextView) view.findViewById(R.id.list_item_text_created_at);
		
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		long id = getActivity().getIntent().getLongExtra(
				StatusConstants.Column.ID, -1);
		updateView(id);
	}
	
	public void updateView(long id){
		if (id == -1) {
			textUser.setText("");
			textMessage.setText("");
			textCreateAt.setText("");
			return;
		}
		
		Uri uri = ContentUris.withAppendedId(StatusConstants.CONTENT_URI, id);
		Cursor cursor = getActivity().getContentResolver().query(uri, null,
				null, null, null);
		if (!cursor.moveToFirst())
			return;
		String user = cursor.getString(cursor
				.getColumnIndex(StatusConstants.Column.USER));
		String message = cursor.getString(cursor
				.getColumnIndex(StatusConstants.Column.MESSAGE));
		long createdAt = cursor.getLong(cursor
				.getColumnIndex(StatusConstants.Column.CREATED_AT));
		textUser.setText(user);
		textMessage.setText(message);
		textCreateAt.setText(DateUtils.getRelativeTimeSpanString(createdAt));
	}
	
	

}

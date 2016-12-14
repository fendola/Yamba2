package com.example.helloandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

public class StatusFragment extends android.app.Fragment implements OnClickListener{

	final static String TAG = "clong.StatusFragment"; 
	TelephonyManager mTelephonyManager;
	private EditText editStatus;
	private Button buttonTweet;
	private TextView textCount;
	private int defaultTextColor;
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
    	Log.w(TAG, "onCreateView begin");
    	
    	View view = inflater.inflate(R.layout.fragment_status, container, false);
    	
    	mTelephonyManager = (TelephonyManager) StatusFragment.this.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
        
        editStatus = (EditText) view.findViewById(R.id.editStatus);
        buttonTweet = (Button) view.findViewById(R.id.buttonTweet);
        textCount = (TextView) view.findViewById(R.id.textCount);
        
        
        buttonTweet.setOnClickListener(this);
        defaultTextColor = textCount.getTextColors().getDefaultColor();
        editStatus.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				int count = 140 - editStatus.length();
				textCount.setText(Integer.toString(count));
				textCount.setTextColor(Color.GREEN);
				if (count < 10)
					textCount.setTextColor(Color.RED);
//				else
//					textCount.setTextColor(defaultTextColor);
			}
		});
        Log.w(TAG, "onCreateView end");
		// TODO Auto-generated method stub
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String status = editStatus.getText().toString();
		Toast.makeText(StatusFragment.this.getActivity(), status, Toast.LENGTH_SHORT).show();
		new PostTask().execute(status);
		
	}
	
	private class PostTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.w(TAG, "doInBackground");
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
			String username = prefs.getString("username", "");
			String password = prefs.getString("password", "");
			if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
				getActivity().startActivity(new Intent(getActivity(), SettingsActivity.class));
				return "Please update your username and password";
			}
			YambaClient yambaCloud = new YambaClient(username, password, "http://yamba.newcircle.com/api");
			try {
				yambaCloud.postStatus(params[0]);
				return "Successfully posted";
			} catch (YambaClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "Failed to post status";
			}
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.w(TAG, "postExecute in PostTask");
			Toast.makeText(StatusFragment.this.getActivity().getApplicationContext(), result, Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Log.w(TAG, "preExecute in PostTask");
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
	}


	
    private class MyPhoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			switch(state){
			case TelephonyManager.CALL_STATE_IDLE:
				Toast.makeText(StatusFragment.this.getActivity(), "电话中断", Toast.LENGTH_LONG).show();
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				Toast.makeText(StatusFragment.this.getActivity(), "电话挂起", Toast.LENGTH_LONG).show();
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				Toast.makeText(StatusFragment.this.getActivity(), "来电号码：" + incomingNumber, Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
    	
    }

    @Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.main, menu);
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

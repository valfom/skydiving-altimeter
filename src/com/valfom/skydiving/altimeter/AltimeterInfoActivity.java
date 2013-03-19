package com.valfom.skydiving.altimeter;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class AltimeterInfoActivity extends Activity {

	private WebView wvGraphs;
	private Timer tSetData;
	
	private long id;
	
	private boolean convert = false;
	private boolean live = false;
	
	@SuppressLint("SetJavaScriptEnabled") 
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.info);
		
		ActionBar actionBar = getActionBar();
		
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		
		if (intent.hasExtra("trackId")) {
			
			id = intent.getLongExtra("trackId", 0);
			
			Uri uri = Uri.parse(AltimeterContentProvider.CONTENT_URI_TRACKS + "/" + id);
	        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
	        cursor.moveToFirst();
	        
	        actionBar.setTitle(cursor.getString(cursor.getColumnIndex(AltimeterDB.KEY_TRACKS_DATE)) + " " 
	        		+ cursor.getString(cursor.getColumnIndex(AltimeterDB.KEY_TRACKS_TIME)) );
	        
	        wvGraphs = (WebView) findViewById(R.id.wvGraphs);
	        wvGraphs.setVerticalScrollBarEnabled(false);
	        wvGraphs.setHorizontalScrollBarEnabled(false);
	        wvGraphs.getSettings().setJavaScriptEnabled(true);
	        wvGraphs.loadUrl("file:///android_asset/graphs/graphs.html");
	        
	        tSetData = new Timer();
	    	
			SharedPreferences sharedPreferences = getSharedPreferences(AltimeterActivity.PREF_FILE_NAME, Activity.MODE_PRIVATE);
			
			boolean logging = sharedPreferences.getBoolean("logging", false);
			
			if (logging && (sharedPreferences.getInt("trackId", 0) == id))  {
				
				tSetData.schedule(new setGraphsDataTask(), 0, 1000);
				
				live = true;
			}
			
		} else onBackPressed();
	}
	
	@Override
	protected void onResume() {

		super.onResume();
		
		String altitudeUnit;

		AltimeterSettings settings = new AltimeterSettings(this);

		altitudeUnit = settings.getAltitudeUnit();

		if (altitudeUnit.equals(getString(R.string.ft))) convert = true;
		else convert = false;
		
		if (!live) tSetData.schedule(new setGraphsDataTask(true), 500);
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		
		if (tSetData != null) {

			tSetData.cancel();
			tSetData = null;
    	}
	}

	class setGraphsDataTask extends TimerTask {

		private boolean showControlls = false;
		
		public setGraphsDataTask() {}
		
        public setGraphsDataTask(boolean showControlls) {

        	this.showControlls = showControlls;
		}

		@Override
        public void run() {
        	
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                	
                	Uri uri = Uri.parse(AltimeterContentProvider.CONTENT_URI_POINTS + "/" + id);
        	        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                	
                	if (cursor.getCount() > 0) {
        	        	
                		JSONArray altitudeData = new JSONArray();
                		JSONArray verticalSpeedData = new JSONArray();
        	        	
        	        	for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {

        	        		JSONArray altitudeEntry = new JSONArray();
        	        		JSONArray verticalSpeedEntry = new JSONArray();
        	        		int altitude, verticalSpeed;
        	        		
        	        		altitudeEntry.put(cursor.getPosition());
        	        		verticalSpeedEntry.put(cursor.getPosition());

        	        		altitude = cursor.getInt(cursor.getColumnIndex(AltimeterDB.KEY_POINTS_ALTITUDE));
        	        		verticalSpeed = cursor.getInt(cursor.getColumnIndex(AltimeterDB.KEY_POINTS_SPEED));
        	        		
        	        		if (convert) {

        	        			altitude = AltimeterSettings.convertAltitudeToFt(altitude);
        	        			verticalSpeed = AltimeterSettings.convertAltitudeToFt(verticalSpeed);
        	        		}
        	        		
        					altitudeEntry.put(altitude);
        					verticalSpeedEntry.put(verticalSpeed);

        					altitudeData.put(altitudeEntry);
        					verticalSpeedData.put(verticalSpeedEntry);
        	            }
        	        	
        	        	wvGraphs.loadUrl("javascript:setGraphsData(" + altitudeData.toString() + ", " + verticalSpeedData.toString() + ")");
        	        	
        	        	if (showControlls) wvGraphs.loadUrl("javascript:addControlls()");
        	        }
                }
            });
        }
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.menu_info, menu);

		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
        
	        case android.R.id.home:
	            
	        	onBackPressed();
	            
	            return true;
	            
	        case R.id.action_settings:
	        	
	        	Intent settings = new Intent(AltimeterInfoActivity.this, AltimeterPreferenceActivity.class);
	    		startActivity(settings);
	    		
	    		return true;
	        	
	        default:
	        	return super.onMenuItemSelected(featureId, item);
		}
	}
}

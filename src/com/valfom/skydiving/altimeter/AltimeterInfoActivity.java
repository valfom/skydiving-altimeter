package com.valfom.skydiving.altimeter;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

public class AltimeterInfoActivity extends Activity {

	private WebView wvGraphs;
	private long id;
	
	@SuppressLint("SetJavaScriptEnabled") 
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.info);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		
		if (intent.hasExtra("trackId")) {
			
			id = intent.getLongExtra("trackId", 0);
			
			Uri uri = Uri.parse(AltimeterContentProvider.CONTENT_URI_TRACKS + "/" + id);
	        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
	        cursor.moveToFirst();
	        TextView tvDate = (TextView) findViewById(R.id.tvDate);
	        tvDate.setText(cursor.getString(0));
	        
	        wvGraphs = (WebView) findViewById(R.id.wvGraphs);

	        wvGraphs.setVerticalScrollBarEnabled(false);
	        wvGraphs.setHorizontalScrollBarEnabled(false);
	        wvGraphs.getSettings().setJavaScriptEnabled(true);
	        wvGraphs.loadUrl("file:///android_asset/graphs/graphs.html");
	        
        	Timer tSetData = new Timer();
        	
//        	tSetData.schedule(new setGraphsDataTask(), 0, 1000);
        	tSetData.schedule(new setGraphsDataTask(), 1000);
	        
		} else onBackPressed();
	}
	
	class setGraphsDataTask extends TimerTask {

        @Override
        public void run() {
        	
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                	
                	Uri uri = Uri.parse(AltimeterContentProvider.CONTENT_URI_POINTS + "/" + id);
        	        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                	
                	if (cursor.getCount() > 0) {
        	        	
                		JSONArray altitudeData = new JSONArray();
        	        	
        	        	for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext()) {

        	        		JSONArray altitudeEntry = new JSONArray();
        	        		
        	        		altitudeEntry.put(cursor.getPosition());

        					altitudeEntry.put(cursor.getInt(0));

        					altitudeData.put(altitudeEntry);
        	            }
        	        	
        	        	wvGraphs.loadUrl("javascript:setGraphsData(" + altitudeData.toString() + ")");
        	        }
                }
            });
        }
   };
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
        
	        case android.R.id.home:
	            
	        	onBackPressed();
	            
	            return true;
	        	
	        default:
	        	return super.onMenuItemSelected(featureId, item);
		}
	}
}

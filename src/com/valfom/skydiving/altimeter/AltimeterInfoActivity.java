package com.valfom.skydiving.altimeter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class AltimeterInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.info);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		
		if (intent.hasExtra("trackId")) {
			
			long id = intent.getLongExtra("trackId", 0);
			
			Uri uri = Uri.parse(AltimeterContentProvider.CONTENT_URI_TRACKS + "/" + id);
	        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
	        cursor.moveToFirst();
	        TextView tv = (TextView) findViewById(R.id.textView1);
	        tv.setText(cursor.getString(0));
	        
		} else onBackPressed();
	}
	
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

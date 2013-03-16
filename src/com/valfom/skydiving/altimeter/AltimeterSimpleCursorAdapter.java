package com.valfom.skydiving.altimeter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class AltimeterSimpleCursorAdapter extends SimpleCursorAdapter {

	private Context context;
	private boolean logging;
	private int trackId;
	
	public AltimeterSimpleCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to, int flags) {
		
		super(context, layout, c, from, to, flags);
		
		this.context = context;
		
		SharedPreferences sharedPreferences = context.getSharedPreferences(AltimeterActivity.PREF_FILE_NAME, Activity.MODE_PRIVATE);
		
		logging = sharedPreferences.getBoolean("logging", false);
		
		if (logging) trackId = sharedPreferences.getInt("trackId", 0);
	}
	
	@Override
	public int getItemViewType(int position) {
		
		int type;
		Cursor cursor;
		
		cursor = getCursor();
		cursor.moveToPosition(position);
		type = cursor.getInt(cursor.getColumnIndex(AltimeterDB.KEY_TRACKS_TYPE));
		
		return type;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
    	
		if (convertView == null) {
		
			int type;
			Cursor cursor;
			
			cursor = getCursor();
			cursor.moveToPosition(position);
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			type = getItemViewType(position); 
			
			if (type == 1) {
			
				convertView = inflater.inflate(R.layout.list_row, null, true);
				
				TextView tvTime = (TextView) convertView.findViewById(R.id.tvTime);
				String time = cursor.getString(cursor.getColumnIndex(AltimeterDB.KEY_TRACKS_TIME));
				tvTime.setText(time);
				
				if (logging) {
					 
					if (getItemId(position) == trackId) {
						
						TextView tvLive = (TextView) convertView.findViewById(R.id.tvLive);
						tvLive.setVisibility(View.VISIBLE);
					}
				}
				
			} else if (type == 2) {
				
				convertView = inflater.inflate(R.layout.list_row_section, null, true);
				
				TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
				
				String date = cursor.getString(cursor.getColumnIndex(AltimeterDB.KEY_TRACKS_DATE));
				
				tvDate.setText(date);
			}
		}
	    
        return convertView;
    }

	@Override
	public boolean isEnabled(int position) {
		
		if (getItemViewType(position) == 1) return true;
		
		return false;
	}
	
}

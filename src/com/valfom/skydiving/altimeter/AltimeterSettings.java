package com.valfom.skydiving.altimeter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AltimeterSettings {

	private static final float FT_IN_M = 3.2808399f;
	
//	private Context context;
	
	private String altitudeUnit;

	public AltimeterSettings(Context context) {
		
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        
		String units = sharedPreferences.getString(AltimeterPreferenceActivity.KEY_MEASUREMENT_UNITS, context.getString(R.string.settings_units_metric));
		
        if (units.equals(context.getString(R.string.settings_units_metric))) altitudeUnit = context.getString(R.string.m);
        else altitudeUnit = context.getString(R.string.ft);
	}
	
	public static int convertAltitudeToFt(int altitude) {
		
		return (int) (altitude * FT_IN_M);
	}
	
//	public static int convertAltitudeToM(int altitude) {
//		
//		return (int) (altitude / FT_IN_M);
//	}
	
	public String getAltitudeUnit() {
		
		return altitudeUnit;
	}
}

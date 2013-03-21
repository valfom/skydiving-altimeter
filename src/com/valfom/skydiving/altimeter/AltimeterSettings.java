package com.valfom.skydiving.altimeter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AltimeterSettings {

	private static final float FT_IN_M = 3.2808399f;
	
	private String altitudeUnit;
	private String fallNotifications;
	private String climbNotifications;

	public AltimeterSettings(Context context) {
		
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        
		String units = sharedPreferences.getString(AltimeterPreferenceActivity.KEY_MEASUREMENT_UNITS, context.getString(R.string.settings_units_metric));
		
        if (units.equals(context.getString(R.string.settings_units_metric))) altitudeUnit = context.getString(R.string.m);
        else altitudeUnit = context.getString(R.string.ft);
        
        String firstAltitudeFall = sharedPreferences.getString(AltimeterPreferenceActivity.KEY_FIRST_ALTITUDE_FALL, context.getString(R.string.first_altitude_fall));
        String secondAltitudeFall = sharedPreferences.getString(AltimeterPreferenceActivity.KEY_SECOND_ALTITUDE_FALL, context.getString(R.string.second_altitude_fall));
        String thirdAltitudeFall = sharedPreferences.getString(AltimeterPreferenceActivity.KEY_THIRD_ALTITUDE_FALL, context.getString(R.string.third_altitude_fall));
        
        if (!firstAltitudeFall.equals("")) { 
        	
        	fallNotifications = firstAltitudeFall + " ";
        	
        	if (!secondAltitudeFall.equals("")) { 
            	
        		fallNotifications += secondAltitudeFall + " ";
            	
            	if (!thirdAltitudeFall.equals("")) { 
                	
            		fallNotifications += thirdAltitudeFall;
                }
            }
        }
        
        String firstAltitudeClimb = sharedPreferences.getString(AltimeterPreferenceActivity.KEY_FIRST_ALTITUDE_CLIMB, context.getString(R.string.first_altitude_climb));
        String secondAltitudeClimb = sharedPreferences.getString(AltimeterPreferenceActivity.KEY_SECOND_ALTITUDE_CLIMB, context.getString(R.string.second_altitude_climb));
        
        if (!firstAltitudeClimb.equals("")) { 
        	
        	climbNotifications = firstAltitudeClimb + " ";
        	
        	if (!secondAltitudeClimb.equals("")) { 
            	
        		climbNotifications += secondAltitudeClimb + " ";
            }
        }
	}
	
	public String getClimbNotifications() {
		
		return climbNotifications;
	}

	public String getFallNotifications() {
		
		return fallNotifications;
	}
	
	public static int convertAltitudeToFt(int altitude) {
		
		return (int) (altitude * FT_IN_M);
	}
	
	public String getAltitudeUnit() {
		
		return altitudeUnit;
	}
}

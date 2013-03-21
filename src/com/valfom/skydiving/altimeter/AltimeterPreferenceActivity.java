package com.valfom.skydiving.altimeter;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;

public class AltimeterPreferenceActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	public static final String KEY_MEASUREMENT_UNITS = "lUnits";
	public static final String KEY_SOUND = "rSound";
//	public static final String KEY_VIBRATION = "cbVibration";
	public static final String KEY_FIRST_ALTITUDE_FALL = "etFirstAltitudeFall";
	public static final String KEY_SECOND_ALTITUDE_FALL = "etSecondAltitudeFall";
	public static final String KEY_THIRD_ALTITUDE_FALL = "etThirdAltitudeFall";
	public static final String KEY_FIRST_ALTITUDE_CLIMB = "etFirstAltitudeClimb";
	public static final String KEY_SECOND_ALTITUDE_CLIMB = "etSecondAltitudeClimb";
	
	private ListPreference lMeasurementUnits;
	private EditTextPreference etFirstAltitudeFall;
	private EditTextPreference etSecondAltitudeFall;
	private EditTextPreference etThirdAltitudeFall;
	private RingtonePreference rSound;
	private EditTextPreference etFirstAltitudeClimb;
	private EditTextPreference etSecondAltitudeClimb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);
		
		lMeasurementUnits = (ListPreference) getPreferenceScreen().findPreference(KEY_MEASUREMENT_UNITS);
		etFirstAltitudeFall = (EditTextPreference) getPreferenceScreen().findPreference(KEY_FIRST_ALTITUDE_FALL);
		etSecondAltitudeFall = (EditTextPreference) getPreferenceScreen().findPreference(KEY_SECOND_ALTITUDE_FALL);
		etThirdAltitudeFall = (EditTextPreference) getPreferenceScreen().findPreference(KEY_THIRD_ALTITUDE_FALL);
		rSound = (RingtonePreference) getPreferenceScreen().findPreference(KEY_SOUND);
		etFirstAltitudeClimb = (EditTextPreference) getPreferenceScreen().findPreference(KEY_FIRST_ALTITUDE_CLIMB);
		etSecondAltitudeClimb = (EditTextPreference) getPreferenceScreen().findPreference(KEY_SECOND_ALTITUDE_CLIMB);
	}
	
	@Override
    protected void onPause() {
        
    	super.onPause();
          
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);    
    }
	
	@Override
	protected void onResume() {

		super.onResume();
		
		AltimeterSettings settings = new AltimeterSettings(this);
		
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		Uri ringtoneUri = Uri.parse(sharedPreferences.getString(KEY_SOUND, "Silent"));
		Ringtone ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
		
		String summary = getString(R.string.settings_silent);
		
		if (ringtone != null) summary = ringtone.getTitle(this);
		
		rSound.setSummary(summary);
		
        lMeasurementUnits.setSummary(sharedPreferences.getString(KEY_MEASUREMENT_UNITS, 
        		getString(R.string.settings_measurement_units)));
        
        String altitude = sharedPreferences.getString(KEY_FIRST_ALTITUDE_FALL, getString(R.string.first_altitude_fall));
        summary = altitude.equals("")? "" : altitude + " " + settings.getAltitudeUnit();
        
        etFirstAltitudeFall.setSummary(summary);
        etFirstAltitudeFall.setPositiveButtonText(getString(R.string.save));
        etFirstAltitudeFall.setNegativeButtonText(getString(R.string.cancel));
        
        altitude = sharedPreferences.getString(KEY_SECOND_ALTITUDE_FALL, getString(R.string.second_altitude_fall));
        summary = altitude.equals("")? "" : altitude + " " + settings.getAltitudeUnit();

        etSecondAltitudeFall.setSummary(summary);
        etSecondAltitudeFall.setPositiveButtonText(getString(R.string.save));
        etSecondAltitudeFall.setNegativeButtonText(getString(R.string.cancel));
        
        altitude = sharedPreferences.getString(KEY_THIRD_ALTITUDE_FALL, getString(R.string.third_altitude_fall));
        summary = altitude.equals("")? "" : altitude + " " + settings.getAltitudeUnit();
        
        etThirdAltitudeFall.setSummary(summary);
        etThirdAltitudeFall.setPositiveButtonText(getString(R.string.save));
        etThirdAltitudeFall.setNegativeButtonText(getString(R.string.cancel));
        
        altitude = sharedPreferences.getString(KEY_FIRST_ALTITUDE_CLIMB, getString(R.string.first_altitude_climb));
        summary = altitude.equals("")? "" : altitude + " " + settings.getAltitudeUnit();
        
        etFirstAltitudeClimb.setSummary(summary);
        etFirstAltitudeClimb.setPositiveButtonText(getString(R.string.save));
        etFirstAltitudeClimb.setNegativeButtonText(getString(R.string.cancel));
        
        altitude = sharedPreferences.getString(KEY_SECOND_ALTITUDE_CLIMB, getString(R.string.second_altitude_climb));
        summary = altitude.equals("")? "" : altitude + " " + settings.getAltitudeUnit();

        etSecondAltitudeClimb.setSummary(summary);
        etSecondAltitudeClimb.setPositiveButtonText(getString(R.string.save));
        etSecondAltitudeClimb.setNegativeButtonText(getString(R.string.cancel));
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		
		AltimeterSettings settings = new AltimeterSettings(this);
    	
    	if (key.equals(KEY_MEASUREMENT_UNITS)) {
        	
    		lMeasurementUnits.setSummary(sharedPreferences.getString(KEY_MEASUREMENT_UNITS, 
            		getString(R.string.settings_measurement_units)));
    		
    		String altitude = sharedPreferences.getString(KEY_FIRST_ALTITUDE_FALL, getString(R.string.first_altitude_fall));
    		String summary = altitude.equals("")? "" : altitude + " " + settings.getAltitudeUnit();
            
			etFirstAltitudeFall.setSummary(summary);
			
			altitude = sharedPreferences.getString(KEY_SECOND_ALTITUDE_FALL, getString(R.string.second_altitude_fall));
			summary = altitude.equals("")? "" : altitude + " " + settings.getAltitudeUnit();
	        
			etSecondAltitudeFall.setSummary(summary);
			
			altitude = sharedPreferences.getString(KEY_THIRD_ALTITUDE_FALL, getString(R.string.third_altitude_fall));
			summary = altitude.equals("")? "" : altitude + " " + settings.getAltitudeUnit();
	        
			etThirdAltitudeFall.setSummary(summary);
			
			altitude = sharedPreferences.getString(KEY_FIRST_ALTITUDE_CLIMB, getString(R.string.first_altitude_climb));
    		summary = altitude.equals("")? "" : altitude + " " + settings.getAltitudeUnit();
            
			etFirstAltitudeClimb.setSummary(summary);
			
			altitude = sharedPreferences.getString(KEY_SECOND_ALTITUDE_CLIMB, getString(R.string.second_altitude_climb));
			summary = altitude.equals("")? "" : altitude + " " + settings.getAltitudeUnit();
	        
			etSecondAltitudeFall.setSummary(summary);
    		
    	} else if (key.equals(KEY_FIRST_ALTITUDE_FALL)) {
    	
    		String altitude = sharedPreferences.getString(KEY_FIRST_ALTITUDE_FALL, getString(R.string.first_altitude_fall));
    		String summary = altitude.equals("")? "" : altitude + " " + settings.getAltitudeUnit();
            
    		etFirstAltitudeFall.setSummary(summary);
    		
    	} else if (key.equals(KEY_SECOND_ALTITUDE_FALL)) {
    	
    		String altitude = sharedPreferences.getString(KEY_SECOND_ALTITUDE_FALL, getString(R.string.second_altitude_fall));
			String summary = altitude.equals("")? "" : altitude + " " + settings.getAltitudeUnit();
        
			etSecondAltitudeFall.setSummary(summary);
    		
    	} else if (key.equals(KEY_THIRD_ALTITUDE_FALL)) {
    	
    		String altitude = sharedPreferences.getString(KEY_THIRD_ALTITUDE_FALL, getString(R.string.third_altitude_fall));
			String summary = altitude.equals("")? "" : altitude + " " + settings.getAltitudeUnit();
        
			etThirdAltitudeFall.setSummary(summary);
			
    	} else if (key.equals(KEY_FIRST_ALTITUDE_CLIMB)) {
    	
    		String altitude = sharedPreferences.getString(KEY_FIRST_ALTITUDE_CLIMB, getString(R.string.first_altitude_climb));
    		String summary = altitude.equals("")? "" : altitude + " " + settings.getAltitudeUnit();
            
    		etFirstAltitudeClimb.setSummary(summary);
    		
    	} else if (key.equals(KEY_SECOND_ALTITUDE_CLIMB)) {
    	
    		String altitude = sharedPreferences.getString(KEY_SECOND_ALTITUDE_CLIMB, getString(R.string.second_altitude_climb));
			String summary = altitude.equals("")? "" : altitude + " " + settings.getAltitudeUnit();
        
			etSecondAltitudeClimb.setSummary(summary);
    		
    	}
	}
}

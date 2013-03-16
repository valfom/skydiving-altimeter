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
	public static final String KEY_FIRST_ALTITUDE = "etFirstAltitude";
	public static final String KEY_SECOND_ALTITUDE = "etSecondAltitude";
	public static final String KEY_THIRD_ALTITUDE = "etThirdAltitude";
	
	private ListPreference lMeasurementUnits;
	private EditTextPreference etFirstAltitude;
	private EditTextPreference etSecondAltitude;
	private EditTextPreference etThirdAltitude;
	private RingtonePreference rSound;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);
		
		lMeasurementUnits = (ListPreference) getPreferenceScreen().findPreference(KEY_MEASUREMENT_UNITS);
		etFirstAltitude = (EditTextPreference) getPreferenceScreen().findPreference(KEY_FIRST_ALTITUDE);
		etSecondAltitude = (EditTextPreference) getPreferenceScreen().findPreference(KEY_SECOND_ALTITUDE);
		etThirdAltitude = (EditTextPreference) getPreferenceScreen().findPreference(KEY_THIRD_ALTITUDE);
		rSound = (RingtonePreference) getPreferenceScreen().findPreference(KEY_SOUND);
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
        
        String altitude = sharedPreferences.getString(KEY_FIRST_ALTITUDE, getString(R.string.first_altitude));
		
		summary = altitude + " " + settings.getAltitudeUnit();
        
        etFirstAltitude.setSummary(summary);
        etFirstAltitude.setPositiveButtonText(getString(R.string.save));
        etFirstAltitude.setNegativeButtonText(getString(R.string.cancel));
        
        altitude = sharedPreferences.getString(KEY_SECOND_ALTITUDE, getString(R.string.second_altitude));
		
		summary = altitude + " " + settings.getAltitudeUnit();
        
        etSecondAltitude.setSummary(summary);
        etSecondAltitude.setPositiveButtonText(getString(R.string.save));
        etSecondAltitude.setNegativeButtonText(getString(R.string.cancel));
        
        altitude = sharedPreferences.getString(KEY_THIRD_ALTITUDE, getString(R.string.third_altitude));
		
		summary = altitude + " " + settings.getAltitudeUnit();
        
        etThirdAltitude.setSummary(summary);
        etThirdAltitude.setPositiveButtonText(getString(R.string.save));
        etThirdAltitude.setNegativeButtonText(getString(R.string.cancel));
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		
		AltimeterSettings settings = new AltimeterSettings(this);
    	
    	if (key.equals(KEY_MEASUREMENT_UNITS)) {
        	
    		lMeasurementUnits.setSummary(sharedPreferences.getString(KEY_MEASUREMENT_UNITS, 
            		getString(R.string.settings_measurement_units)));
    		
    		String altitude = sharedPreferences.getString(KEY_FIRST_ALTITUDE, getString(R.string.first_altitude));
    		
    		String summary = altitude + " " + settings.getAltitudeUnit();
            
			etFirstAltitude.setSummary(summary);
			
			altitude = sharedPreferences.getString(KEY_SECOND_ALTITUDE, getString(R.string.second_altitude));
			
			summary = altitude + " " + settings.getAltitudeUnit();
	        
			etSecondAltitude.setSummary(summary);
			
			altitude = sharedPreferences.getString(KEY_THIRD_ALTITUDE, getString(R.string.third_altitude));
			
			summary = altitude + " " + settings.getAltitudeUnit();
	        
			etThirdAltitude.setSummary(summary);
    		
    	} else if (key.equals(KEY_FIRST_ALTITUDE)) {
    	
    		String altitude = sharedPreferences.getString(KEY_FIRST_ALTITUDE, getString(R.string.first_altitude));
    		
    		String summary = altitude + " " + settings.getAltitudeUnit();
            
    		etFirstAltitude.setSummary(summary);
    		
    	} else if (key.equals(KEY_SECOND_ALTITUDE)) {
    	
    		String altitude = sharedPreferences.getString(KEY_SECOND_ALTITUDE, getString(R.string.second_altitude));
    		
    		String summary = altitude + " " + settings.getAltitudeUnit();
            
    		etSecondAltitude.setSummary(summary);
    		
    	} else if (key.equals(KEY_THIRD_ALTITUDE)) {
    	
    		String altitude = sharedPreferences.getString(KEY_THIRD_ALTITUDE, getString(R.string.third_altitude));
    		
    		String summary = altitude + " " + settings.getAltitudeUnit();
            
    		etThirdAltitude.setSummary(summary);
    	}
	}
}

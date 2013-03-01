package com.valfom.skydiving.altimeter;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AltimeterActivity extends Activity implements SensorEventListener, OnClickListener {
	
	private static final String TAG = "AltimeterActivity";
	
	private SensorManager sensorManager;
	private Sensor sensorPressure;
//	private Vibrator vibrator;
//	private PowerManager.WakeLock wakeLock;
	
	public AltimeterSettings settings;
	
	private TextView tvAltitude;
	private TextView tvAltitudeUnit;
	private Button btnSetZero;
	private Button btnResetZero;
	
	private int altitude;
	private int zeroAltitude;
	
	private boolean convert = false; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
		
		if (sensorPressure != null) {
        
			Log.d(TAG, "Barometer: min delay " + sensorPressure.getMinDelay() 
					+ ", power " + sensorPressure.getPower() 
					+ ", max range " + sensorPressure.getMaximumRange());
			
	        tvAltitude = (TextView) findViewById(R.id.tvAltitude);
	        tvAltitudeUnit = (TextView) findViewById(R.id.tvAltitudeUnit);
	        btnSetZero = (Button) findViewById(R.id.btnSetZero);
	        btnResetZero = (Button) findViewById(R.id.btnResetZero);
	        
	        btnSetZero.setOnClickListener(this);
	        btnResetZero.setOnClickListener(this);
        
//			vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
//			PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//	    	wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
    	
//	    	wakeLock.acquire();
//        	vibrator.vibrate(1000);
//        	wakeLock.release();
		}
    }
    
    @Override
	protected void onPause() {
	
		super.onPause();
		
		sensorManager.unregisterListener(this);
	}

	@Override
	protected void onResume() {

		super.onResume();
		
		settings = new AltimeterSettings(this);
		
		String altitudeUnit = settings.getAltitudeUnit();
		if (altitudeUnit.equals(getString(R.string.ft))) convert = true;
		else convert = false;
		
		tvAltitudeUnit.setText(altitudeUnit);
		
		sensorManager.registerListener(this, sensorPressure, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

    	getMenuInflater().inflate(R.menu.altitude, menu);
        
    	return true;
    }

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		Intent settings = new Intent(this, AltimeterPreferenceActivity.class);
		startActivity(settings);
		
		return true;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	@Override
	public void onSensorChanged(SensorEvent event) {

		float atmosphericPressure, pressureAtSeaLevel;
		int customAltitude;
		
		atmosphericPressure = event.values[0];
		
		pressureAtSeaLevel = SensorManager.PRESSURE_STANDARD_ATMOSPHERE;
		
		altitude = (int) SensorManager.getAltitude(pressureAtSeaLevel, atmosphericPressure);
		
		customAltitude = altitude - zeroAltitude;
		
		if (convert) customAltitude = settings.convertAltitudeToFt(customAltitude);
		
		tvAltitude.setText(String.valueOf(customAltitude));
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.btnSetZero:
			zeroAltitude = altitude;
			break;
		case R.id.btnResetZero:
			zeroAltitude = 0;
			break;
		}
	}
    
}

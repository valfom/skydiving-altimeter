package com.valfom.skydiving.altimeter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AltimeterActivity extends Activity implements SensorEventListener, OnClickListener {
	
	private static final String TAG = "AltimeterActivity";
	private static final String PREF_FILE_NAME = "SAPrefs";
	private static final byte STEP = 10;
	private static final int GRAPHS_UPDATE_TIME = 1000; // ms
	
	private SensorManager sensorManager;
	private Sensor sensorPressure;
//	private Vibrator vibrator;
//	private PowerManager.WakeLock wakeLock;
	
	public AltimeterSettings settings;
	
	private TextView tvAltitude;
	private TextView tvAltitudeUnit;
	private Button btnSetZero;
	private Button btnLeft;
	private Button btnRight;
	private Button btnClear;
	
	private int altitude;
	private int zeroAltitude;
	private int maxAltitude;
	
	private ArrayList<Integer> lAltitude = new ArrayList<Integer>();
	
//	private boolean convert = false;
	private boolean showData = false;
	
	private Timer timerGraphs;
	private WebView wvGraphs;
	
	// Vertical speed
	private Integer firstAltitude = null;
	private Integer secondAltitude = null;
	private ArrayList<Long> lVerticalSpeed = new ArrayList<Long>();
	
    @SuppressLint("SetJavaScriptEnabled") 
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
	        btnLeft = (Button) findViewById(R.id.btnLeft);
	        btnRight = (Button) findViewById(R.id.btnRight);
	        btnClear = (Button) findViewById(R.id.btnClear);
	        
	        btnSetZero.setOnClickListener(this);
	        btnLeft.setOnClickListener(this);
	        btnRight.setOnClickListener(this);
	        btnClear.setOnClickListener(this);
	        
	        ToggleButton tbShowData = (ToggleButton) findViewById(R.id.tbShowData);
			
	        tbShowData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
			    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			    	
			        if (isChecked) {
			            
			        	showData = true;
			        	
			        	timerGraphs = new Timer();
			        	timerGraphs.schedule(new setGraphsDataTask(), 0, GRAPHS_UPDATE_TIME);
			        	
			        } else {
			        	
			        	showData = false;
			        	
			        	timerGraphs.cancel();
			        	timerGraphs = null;
			        }
			    }
			});
	        
	        wvGraphs = (WebView) findViewById(R.id.wvGraphs);

	        wvGraphs.setVerticalScrollBarEnabled(false);
	        wvGraphs.setHorizontalScrollBarEnabled(false);
	        wvGraphs.getSettings().setJavaScriptEnabled(true);
	        wvGraphs.loadUrl("file:///android_asset/graphs/graphs.html");
        
//			vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
//			PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
//	    	wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
    	
//	    	wakeLock.acquire();
//        	vibrator.vibrate(1000);
//        	wakeLock.release();
	        
	        sensorManager.registerListener(this, sensorPressure, SensorManager.SENSOR_DELAY_FASTEST);
		}
    }
    
    @Override
	protected void onDestroy() {

    	super.onDestroy();
    	
    	sensorManager.unregisterListener(this);
	}

	@Override
	protected void onResume() {

		super.onResume();
		
		SharedPreferences sharedPreferences = getSharedPreferences(PREF_FILE_NAME, Activity.MODE_PRIVATE);
		zeroAltitude = sharedPreferences.getInt("zeroAltitude", 0);
		
		settings = new AltimeterSettings(this);
		
		String altitudeUnit = settings.getAltitudeUnit();
		
//		if (altitudeUnit.equals(getString(R.string.ft))) convert = true;
//		else convert = false;
		
		tvAltitudeUnit.setText(altitudeUnit);
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
		
		if (customAltitude > maxAltitude) maxAltitude = customAltitude;
		
		tvAltitude.setText(String.valueOf(customAltitude));
		
//		if (showData) lAltitude.add(customAltitude);
	}
	
	private class setGraphsDataTask extends TimerTask {

        @Override
        public void run() {
        	
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                	
                	int customAltitude, diffAltitude;
                	long verticalSpeed;
                	
                	if (showData) {
                		
                		customAltitude = altitude - zeroAltitude;
                		lAltitude.add(customAltitude);
                		
                		if (firstAltitude != null) {
                			
                			secondAltitude = customAltitude;
                			
                			diffAltitude = Math.abs(secondAltitude - firstAltitude);
                			
                			verticalSpeed = diffAltitude / (GRAPHS_UPDATE_TIME / 1000);
                			
                			lVerticalSpeed.add(verticalSpeed);
                			
                			firstAltitude = secondAltitude;
                			
                		} else firstAltitude = customAltitude; 
                	}
                   
                	final JSONArray verticalSpeedData = new JSONArray();

        			for (int i = 0; i < lVerticalSpeed.size(); i++) {
        				
        				JSONArray verticalSpeedEntry = new JSONArray();

        				verticalSpeedEntry.put(i);
        				
        				verticalSpeedEntry.put(lVerticalSpeed.get(i));
        				
        				verticalSpeedData.put(verticalSpeedEntry);
        			}
                	
                	final JSONArray altitudeData = new JSONArray();
                	float customMaxAltitude;

        			for (int i = 0; i < lAltitude.size(); i++) {
        				
        				JSONArray altitudeEntry = new JSONArray();

        				altitudeEntry.put(i);
        				
        				altitudeEntry.put(lAltitude.get(i));
        				
        				altitudeData.put(altitudeEntry);
        			}
        			
        			customMaxAltitude = 1000 * (maxAltitude / 1000 + 1); // km

        	        wvGraphs.loadUrl("javascript:setGraphsData(" + altitudeData.toString() + ", " + customMaxAltitude + ", " + verticalSpeedData.toString() + ")");
                }
            });
        }
   };

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.btnSetZero:
			zeroAltitude = altitude;
			break;
			
		case R.id.btnLeft:
			zeroAltitude += STEP;
			break;
			
		case R.id.btnRight:
			zeroAltitude -= STEP;
			break;
			
		case R.id.btnClear:
			lAltitude.clear();
			break;
		}
		
		SharedPreferences sharedPreferences = getSharedPreferences(PREF_FILE_NAME, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("zeroAltitude", zeroAltitude);
		editor.apply();
	}
}

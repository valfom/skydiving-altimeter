package com.valfom.altimeter;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AltimeterActivity extends Activity implements SensorEventListener, OnClickListener {
	
	private SensorManager sensorManager;
	private Sensor sensorPressure;
	
	private TextView tvAltitude;
	private Button btnSetZero;
	private Button btnResetZero;
	
	private float altitude;
	private float zeroAltitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        tvAltitude = (TextView) findViewById(R.id.tvAltitude);
        btnSetZero = (Button) findViewById(R.id.btnSetZero);
        btnResetZero = (Button) findViewById(R.id.btnResetZero);
        
        btnSetZero.setOnClickListener(this);
        btnResetZero.setOnClickListener(this);
        
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorPressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
    }
    
    @Override
	protected void onPause() {
	
		super.onPause();
		
		sensorManager.unregisterListener(this);
	}

	@Override
	protected void onResume() {

		super.onResume();
		
		sensorManager.registerListener(this, sensorPressure, SensorManager.SENSOR_DELAY_NORMAL);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

    	getMenuInflater().inflate(R.menu.altitude, menu);
        
    	return true;
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	@Override
	public void onSensorChanged(SensorEvent event) {

		float atmosphericPressure, pressureAtSeaLevel;
		float customAltitude;
		
		atmosphericPressure = event.values[0];
		
		pressureAtSeaLevel = SensorManager.PRESSURE_STANDARD_ATMOSPHERE;
		
		altitude = SensorManager.getAltitude(pressureAtSeaLevel, atmosphericPressure);
		
		customAltitude = altitude - zeroAltitude;
		
		customAltitude /= 1000;
		
		if ((customAltitude < 0) && (customAltitude < 0.001)) customAltitude = Math.abs(customAltitude);
		
		tvAltitude.setText(String.format("%.3f", customAltitude));
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

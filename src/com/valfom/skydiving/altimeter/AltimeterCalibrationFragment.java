package com.valfom.skydiving.altimeter;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class AltimeterCalibrationFragment extends Fragment implements OnClickListener {

	public static final int CODE_INCREASE = 1;
	public static final int CODE_SET_TO_ZERO = 0;
	public static final int CODE_DECREASE = -1;
	
	private OnZeroAltitudeChangedListener onZeroAltitudeChangedListener;
	
	private Button btnSetToZero;
	private Button btnDecrease;
	private Button btnIncrease;
	
	public interface OnZeroAltitudeChangedListener {
		
        public void onZeroAltitudeChanged(int code);
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		
		View v = getView();
		
		btnSetToZero = (Button) v.findViewById(R.id.btnSetToZero);
		btnDecrease = (Button) v.findViewById(R.id.btnDecrease);
		btnIncrease = (Button) v.findViewById(R.id.btnIncrease);

		btnSetToZero.setOnClickListener(this);
		btnDecrease.setOnClickListener(this);
		btnIncrease.setOnClickListener(this);
	}
	
	@Override
	public void onAttach(Activity activity) {
	
		super.onAttach(activity);
		
		try {
        	
			onZeroAltitudeChangedListener = (OnZeroAltitudeChangedListener) activity;
            
        } catch (ClassCastException e) {
        	
            throw new ClassCastException(activity.toString() + " must implement OnZeroAltitudeChangedListener");
        }
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.fragment_calibration, container, false);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

			case R.id.btnSetToZero:
				onZeroAltitudeChangedListener.onZeroAltitudeChanged(CODE_SET_TO_ZERO);
				break;
	
			case R.id.btnDecrease:
				onZeroAltitudeChangedListener.onZeroAltitudeChanged(CODE_DECREASE);
				break;
	
			case R.id.btnIncrease:
				onZeroAltitudeChangedListener.onZeroAltitudeChanged(CODE_INCREASE);
				break;
				
			default:
				break;
		}
	}
}

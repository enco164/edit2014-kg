package com.comtrade.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.beaconapk.R;

public class SettingsFragment extends PreferenceFragment{
	
	public static final String DH_SERVER_URI_KEY = "pref_dh_server_url";
	public static final String SCAN_PERIOD_KEY = "prefScanPeriod";
	public static final String COMPAS_ON_OFF="compasOnOff";
	public static final String SERVER_SCAN="prefServerScan";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);
	}
	

}

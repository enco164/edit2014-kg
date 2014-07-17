package com.comtrade.ilserver.tasks;

import android.os.AsyncTask;

public class GetBeaconTask extends AsyncTask<String, Void, String>{
	private String username, password, macAddress;
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	@Override
	protected String doInBackground(String... params) {
		return HelperClass.getBeaconByMacAddress(params[0], getUsername(), getPassword(), getMacAddress());
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
	
}

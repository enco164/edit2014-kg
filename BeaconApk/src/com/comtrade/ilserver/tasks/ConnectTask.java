package com.comtrade.ilserver.tasks;

import android.os.AsyncTask;

public class ConnectTask extends AsyncTask<String, Void, String>{
	private String username;
	private String password;
	
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

	@Override
	protected String doInBackground(String... params) {
		try {
			return HelperClass.connect(params[0], getUsername(), getPassword());
		} catch (Exception e) {
			return null;
		}
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

}

package com.comtrade.ilserver.tasks;

import android.os.AsyncTask;

public class GetSpaceTask extends AsyncTask<String, Void, String>{
	private String username, password;
	private long spaceId;

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

	

	public long getSpaceId() {
		return spaceId;
	}

	public void setSpaceId(long spaceId) {
		this.spaceId = spaceId;
	}

	@Override
	protected String doInBackground(String... params) {
		return HelperClass.getSpaceById(params[0], getUsername(), getPassword(), getSpaceId());
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

}

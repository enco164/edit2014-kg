package com.comtrade.ilserver.tasks;

import android.os.AsyncTask;

public class UserRegistrationTask extends AsyncTask<String, Void, String>{
	private String firstName;
	private String surname;
	private String username;
	private String password;
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

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
	protected String doInBackground(String... urls) {
		try {
			return HelperClass.registerUser(urls[0], getUsername(), getPassword(), getFirstName(), getSurname());
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
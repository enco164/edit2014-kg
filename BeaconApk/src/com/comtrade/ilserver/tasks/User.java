package com.comtrade.ilserver.tasks;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * User class represents user of a system <br/>
 * example json: <br/>
 * 
 * { <br/>
 * "_id":1, <br/>
 * "_uuid":"c319a5c3-0bdc-40dc-b46e-a07fadabd7ec", <br/>
 * "firstName":"Initial", <br/>
 * "surname":"Administrator", <br/>
 * "username":"admin", <br/>
 * "roles":["admin" , "client", "bla"] <br/>
 * }
 */

public class User implements Parcelable {

	public static final String JSON_KEY_ID = "_id";
	public static final String JSON_KEY_UUID = "_uuid";
	public static final String JSON_KEY_FIRSTNAME = "firstName";
	public static final String JSON_KEY_SURNAME = "surname";
	public static final String JSON_KEY_USERNAME = "username";
	public static final String JSON_KEY_ROLES = "roles";
	public static final String EXTRA = "UserExtra";

	private int id;
	private String uuid;
	private String firstname;
	private String surname;
	private String username;
	private ArrayList<String> roles;

	public User(int id, String uuid, String firstname, String surname,
			String username, ArrayList<String> roles) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.firstname = firstname;
		this.surname = surname;
		this.username = username;
		this.roles = roles;
	}

	public User(Parcel source) {
		this.id = source.readInt();
		this.uuid = source.readString();
		this.firstname = source.readString();
		this.surname = source.readString();
		this.username = source.readString();
		this.roles = source.readArrayList(User.class.getClassLoader());
	}

	public static User fromJSON(String json) {
		try {
			
			JSONObject jsonObj = new JSONObject(json);
			

			int id = jsonObj.getInt(JSON_KEY_ID);

			String uuid = jsonObj.getString(JSON_KEY_UUID);
			String firstname = jsonObj.getString(JSON_KEY_FIRSTNAME);
			String surname = jsonObj.getString(JSON_KEY_SURNAME);

			String username = jsonObj.getString(JSON_KEY_USERNAME);
			JSONArray rolesJson = jsonObj.getJSONArray(JSON_KEY_ROLES);

			ArrayList<String> roles = new ArrayList<String>();

			for (int i = 0; i < rolesJson.length(); i++) {
				
				String obj = rolesJson.get(i).toString();
				Log.d("TAG",rolesJson.get(i).toString());
				roles.add(obj);
			}

			return new User(id, uuid, firstname, surname, username, roles);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", uuid=" + uuid + ", firstname=" + firstname
				+ ", surname=" + surname + ", username=" + username
				+ ", roles=" + roles + "]";
	}

	public static Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}

		@Override
		public User createFromParcel(Parcel source) {

			return new User(source);
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(uuid);
		dest.writeString(firstname);
		dest.writeString(surname);
		dest.writeString(username);
		dest.writeList(roles);
	}

	
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
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

	public ArrayList<String> getRoles() {
		return roles;
	}

	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}

	public static Parcelable.Creator<User> getCREATOR() {
		return CREATOR;
	}

	public static void setCREATOR(Parcelable.Creator<User> cREATOR) {
		CREATOR = cREATOR;
	}

	public static String getJsonKeyId() {
		return JSON_KEY_ID;
	}

	public static String getJsonKeyUuid() {
		return JSON_KEY_UUID;
	}

	public static String getJsonKeyFirstname() {
		return JSON_KEY_FIRSTNAME;
	}

	public static String getJsonKeySurname() {
		return JSON_KEY_SURNAME;
	}

	public static String getJsonKeyUsername() {
		return JSON_KEY_USERNAME;
	}

	public static String getJsonKeyRoles() {
		return JSON_KEY_ROLES;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
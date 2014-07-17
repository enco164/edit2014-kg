package com.comtrade.ilserver.tasks;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * User class represents user of a system <br/>
 * example json: <br/>
 * 
 * {	<br/>
 * 		"_id":1, <br/>
 * 		"_uuid":"c319a5c3-0bdc-40dc-b46e-a07fadabd7ec", <br/>
 * 		"firstName":"Initial", <br/>
 * 		"surname":"Administrator", <br/>
 * 		"username":"admin", <br/>
 * 		"roles":["admin"] <br/>
 * }
 */

public class User implements Parcelable{
	
	public static final String EXTRA = "User";
	
	public final static String JSON_KEY_ID = "_id";
	public final static String JSON_KEY_UUID = "_uuid";
	public final static String JSON_KEY_FIRST_NAME = "firstName";
	public final static String JSON_KEY_SURNAME = "surname";
	public final static String JSON_KEY_USERNAME = "username";
	public final static String JSON_KEY_ROLES = "roles";
	
	
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
		source.readInt();
		source.readString();
		source.readString();
		source.readString();
		source.readString();
		source.readList(roles, String.class.getClassLoader());
	}

	public static User fromJSON(String json){
		try {
			JSONObject jsonObj = new JSONObject(json);
			int id = jsonObj.getInt(JSON_KEY_ID);
			String uuid = jsonObj.getString(JSON_KEY_UUID);
			String firstName = jsonObj.getString(JSON_KEY_FIRST_NAME);
			String surname = jsonObj.getString(JSON_KEY_SURNAME);
			String username = jsonObj.getString(JSON_KEY_USERNAME);
			
			JSONArray noviA = jsonObj.getJSONArray(JSON_KEY_ROLES);
			ArrayList<String> listaRoles = new ArrayList<>();
			
			for (int i = 0; i < noviA.length(); i++) {
				listaRoles.add(noviA.get(i).toString());
			}

			return new User(id, uuid, firstName, surname, username, listaRoles);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
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
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
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
}

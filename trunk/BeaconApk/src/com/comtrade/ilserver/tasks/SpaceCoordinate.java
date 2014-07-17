package com.comtrade.ilserver.tasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * SpaceCoordinate class represents referent point of a space <br/>
 * example json <br/>
 * {
 * <br/>
 * "_id":6,<br/>
 * "_uuid":"000dd94c-d03e-41d8-938c-bf29596aec6e",<br/>
 * "_version":0,<br/>
 * "title":"D",<br/>
 * "x":0,<br/>
 * "y":500<br/>
 * }
 */
public class SpaceCoordinate implements Parcelable{

	public final static String JSON_KEY_ID = "_id";
	public final static String JSON_KEY_UUID = "_uuid";
	public final static String JSON_KEY_VERSION = "_version";

	public final static String JSON_KEY_X = "x";
	public final static String JSON_KEY_Y = "y";

	private int id;
	private String uuid;
	private int version;

	private int x, y;

	public SpaceCoordinate(int id, String uuid, int version, 
			int x, int y) {
		super();
		this.id = id;
		this.uuid = uuid;
		this.version = version;
		this.x = x;
		this.y = y;
	}

	public static SpaceCoordinate fromJSON(String json){
		try {
			JSONObject jsonObj = new JSONObject(json);
			int id = jsonObj.getInt(JSON_KEY_ID);
			String uuid = jsonObj.getString(JSON_KEY_UUID);  
			int version = jsonObj.getInt(JSON_KEY_VERSION);
			int x = jsonObj.getInt(JSON_KEY_X);
			int y = jsonObj.getInt(JSON_KEY_Y);

			return new SpaceCoordinate(id, uuid, version, x, y);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(uuid);
		dest.writeInt(version);
		dest.writeInt(x);
		dest.writeInt(y);
	}
	
	public static Parcelable.Creator<SpaceCoordinate> CREATOR = new Parcelable.Creator<SpaceCoordinate>() {

		@Override
		public SpaceCoordinate[] newArray(int size) {
			return new SpaceCoordinate[size];
		}

		@Override
		public SpaceCoordinate createFromParcel(Parcel source) {
			return new SpaceCoordinate(
					source.readInt(), 
					source.readString(),
					source.readInt(),
					source.readInt(),
					source.readInt()
					);
		}
	};
	
	

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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}



	
}

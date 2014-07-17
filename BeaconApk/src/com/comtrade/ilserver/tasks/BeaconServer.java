package com.comtrade.ilserver.tasks;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
/** 
 * BeaconServer class represent a beacon in space <br/>
 * example json:<br/>
 * 
 * {<br/>
 * "_id":13, <br/>
 * "_uuid":"dec43c9d-04ed-4694-9a11-7ec1c85676e4", <br/>
 * "_version":0, <br/>
 * "macAddress":"CF:A2:1C:85:CD:60", <br/>
 * "x":260.0, <br/>
 * "y":460.0, <br/>
 * "z":100.0, <br/>
 * "angle":90, <br/>
 * "message":"CF:A2:1C:85:CD:60", <br/>
 * "spaceId":640 <br/>}
 *
 */



/////// OBAVEZNO PROMENITI DA SA SERVERA KOORDINATE PROMENI U METRE A NE DA HVATA CM
public class BeaconServer implements Parcelable{
	
	public final static String JSON_KEY_ID = "_id";
	public final static String JSON_KEY_UUID = "_uuid";
	public final static String JSON_KEY_VERSION = "_version";
	
	public final static String JSON_KEY_X = "x";
	public final static String JSON_KEY_Y = "y";
	public final static String JSON_KEY_Z = "z";
	public final static String JSON_KEY_SPACEID = "spaceId";
	public final static String JSON_KEY_MAC = "macAddress";
	public final static String JSON_KEY_DESCRIPTION = "message";
	
	public int getId() {
		return id;
	}

	public String getUuid() {
		return uuid;
	}

	public int getVersion() {
		return version;
	}

	public String getDescription() {
		return description;
	}

	public int getSpaceId() {
		return spaceId;
	}

	public String getMac() {
		return mac;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	private int id;
	private String uuid;
	private int version;
	private String description;
	private int spaceId;
	private String mac;

	private int x, y, z;
	
	
	public BeaconServer(int _id, String _uuid, int _version, int x, int y, int z,
			String description, int spaceId, String mac) {

		super();
		this.id = _id;
		this.uuid = _uuid;
		this.version = _version;
		this.x = x;
		this.y = y;
		this.z = z;
		this.description = description;
		this.spaceId = spaceId;
		this.mac = mac;
		
	}
	
	public BeaconServer(Parcel source){
		this.id = source.readInt();
		this.uuid =  source.readString();
		this.version =  source.readInt();
		this.x = source.readInt();
		this.y = source.readInt();
		this.z = source.readInt();
		this.description =  source.readString(); 
		this.spaceId = source.readInt();
		this.mac =  source.readString();
	}

	
	public static BeaconServer fromJSON(String json){
		try {
			JSONObject jsonObj = new JSONObject(json);
			int id = jsonObj.getInt(JSON_KEY_ID);
			String uuid = jsonObj.getString(JSON_KEY_UUID);  
			int version = jsonObj.getInt(JSON_KEY_VERSION);
			int x = jsonObj.getInt(JSON_KEY_X);
			int y = jsonObj.getInt(JSON_KEY_Y);
			int z = jsonObj.getInt(JSON_KEY_Z);
			int spaceId = jsonObj.getInt(JSON_KEY_SPACEID);
			String mac = jsonObj.getString(JSON_KEY_MAC);
			String desc = jsonObj.getString(JSON_KEY_DESCRIPTION);

			return new BeaconServer(id, uuid, version, x, y, z, desc, spaceId, mac);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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
		// TODO Auto-generated method stub
		dest.writeInt(id);
		dest.writeString(uuid);
		dest.writeInt(version);
		dest.writeInt(x);
		dest.writeInt(y);
		dest.writeInt(z);
		dest.writeString(description);
		dest.writeInt(spaceId);
		dest.writeString(mac);

	}
	
	public static Parcelable.Creator<BeaconServer> CREATOR = new Parcelable.Creator<BeaconServer>() {

		@Override
		public BeaconServer[] newArray(int size) {
			return new BeaconServer[size];
		}

		@Override
		public BeaconServer createFromParcel(Parcel source) {
			return new BeaconServer(
					source.readInt(), 
					source.readString(),
					source.readInt(),
					source.readInt(),
					source.readInt(),
					source.readInt(),
					source.readString(), 
					source.readInt(),
					source.readString());
		}
	};

	
	

}

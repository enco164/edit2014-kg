package com.comtrade.ilserver.tasks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;


/**
 * Space class represents configured space for location <br/>
 * example json: <br/>
 * { <br/>
 * "_id":2, <br/>
 * "_uuid":"c610b966-d120-4f6c-bd68-9829afe6b08a", <br/>
 * "_version":0,<br/>
 * 
 * 
 * 
 * "title":"Test Space", <br/>
 * "description":"This is Test Space", <br/>
 * "welcomeMessage":"This is welcome message of Test space", <br/>
 * "mapAddress":"layout/img/example.png",<br/>
 * "beaconList":[<br/>
 * 	{<br/>
 * &nbsp"_id":9,<br/>
 * &nbsp	"_uuid":"6e59392e-2953-4d58-ae1c-70544782ad57", <br/>
 * &nbsp"_version":0, <br/>
 *	&nbsp 	"x":100,"y":150,"z":0, <br/>
 * &nbsp  "description":"FE Welcome Message", <br/>
 * &nbsp  "spaceId":2, <br/>
 * &nbsp  "mac":"FE:80:7C:B9:23:D0" <br/>
 *   }, <br/>
 *   { <br/>
 * &nbsp  "_id":7,<br/>
 *  &nbsp "_uuid":"a59065a9-8fe9-4397-bba3-aaf9872b2ec1",<br/>
 * &nbsp  "_version":0,<br/>
 * &nbsp  "x":240,"y":150,"z":150,<br/>
 * &nbsp  "description":"asdf",<br/>
 * &nbsp  "spaceId":2,"mac":"a1"<br/>
 *   }<br/>
 *   ],<br/>
 *   "cordinates":[<br/>
 *   { <br/>
 *  &nbsp "_id":6,<br/>
 * &nbsp  "_uuid":"000dd94c-d03e-41d8-938c-bf29596aec6e",<br/>
 *  &nbsp "_version":0,<br/>
 *  &nbsp "title":"D",<br/>
 * &nbsp  "x":0,<br/>
 * &nbsp  "y":500<br/>
 *   },<br/>
 *   {<br/>
 * &nbsp  "_id":3,<br/>
 * &nbsp  "_uuid":"3bc68807-0052-4c2e-b9f1-4e8d2f39c0f4",<br/>
 * &nbsp  "_version":0,<br/>
 *  &nbsp "title":"A",<br/>
 *  &nbsp "x":0,<br/>
 * &nbsp  "y":0<br/>
 *   },<br/>
 *   {<br/>
 *  &nbsp "_id":4,<br/>
 * &nbsp  "_uuid":"a680e9b7-af94-4419-9de6-dde897d97b6e",<br/>
 * &nbsp  "_version":0,<br/>
 *  &nbsp "title":"B",<br/>
 * &nbsp  "x":500,<br/>
 *  &nbsp "y":0<br/>
 *   },<br/>
 *   {"_id":5,<br/>
 *  &nbsp "_uuid":"35c4a4c4-1e38-4aee-916b-195f0c94cfb0",<br/>
 *  &nbsp "_version":0,<br/>
 *  &nbsp "title":"C",<br/>
 *  &nbsp "x":500,<br/>
 *  &nbsp "y":500<br/>
 *   }<br/>
 *   ]<br/>
 *  }<br/>
 */

public class Space implements Parcelable{
	public final static String JSON_KEY_ID = "_id";
	public final static String JSON_KEY_UUID = "_uuid";
	public final static String JSON_KEY_VERSION = "_version";
	public final static String JSON_KEY_TITLE = "spaceTitle";
	public final static String JSON_KEY_DESCRIPTION = "description";
	public final static String JSON_KEY_WELCOMEMESSEGE = "welcomeMessage";
	public final static String JSON_KEY_MAPADDRESS = "mapUrl";
	public final static String JSON_KEY_BEACONLIST = "iBeacons";
	public final static String JSON_KEY_COOLIST = "coordinates";
	
	
	private int id, version;
	private String uuid, title, description, welcomeMessage, mapAddress;
	private ArrayList<BeaconServer> beacons;
	private ArrayList<SpaceCoordinate> spaceCoordinates;
	
	public Space(int id, String uuid, int version, String title,
			String description, String welcomeMessage, String mapAddress,
			ArrayList<BeaconServer> beacons,
			ArrayList<SpaceCoordinate> spaceCoordinates) {
		super();
		this.id = id;
		this.version = version;
		this.title = title;
		this.uuid = uuid;
		this.description = description;
		this.welcomeMessage = welcomeMessage;
		this.mapAddress = mapAddress;
		this.beacons = beacons;
		this.spaceCoordinates = spaceCoordinates;
		
	}


	public int getId() {
		return id;
	}


	public int getVersion() {
		return version;
	}


	public String getUuid() {
		return uuid;
	}


	public String getTitle() {
		return title;
	}


	public String getDescription() {
		return description;
	}


	public String getWelcomeMessage() {
		return welcomeMessage;
	}


	public String getMapAddress() {
		return mapAddress;
	}


	public ArrayList<BeaconServer> getBeacons() {
		return beacons;
	}


	public ArrayList<SpaceCoordinate> getSpaceCoordinates() {
		return spaceCoordinates;
	}


	public Space(Parcel source) {
		this.id = source.readInt();
		this.uuid = source.readString();
		this.version = source.readInt();
		this.title = source.readString();
		this.description = source.readString();
		this.welcomeMessage = source.readString();
		this.mapAddress = source.readString();
		this.beacons = source.readArrayList(BeaconServer.class.getClassLoader());
		this.spaceCoordinates = source.readArrayList(SpaceCoordinate.class.getClassLoader());
	}

	
	public static Space fromJSON(String json){
		ArrayList<BeaconServer> listaBeacona = new ArrayList<BeaconServer>();
		ArrayList<SpaceCoordinate> listaCoo = new ArrayList<SpaceCoordinate>();
		Log.d("TAG!",  "AAAAAAA");
	    try {
			JSONObject jsonObj = new JSONObject(json);
			int id = jsonObj.getInt(JSON_KEY_ID);
			String uuid = jsonObj.getString(JSON_KEY_UUID);  
			int version = jsonObj.getInt(JSON_KEY_VERSION);
			String title = jsonObj.getString(JSON_KEY_TITLE);
			String mapA = jsonObj.getString(JSON_KEY_MAPADDRESS);
			
			JSONArray beaconsObject = jsonObj.getJSONArray(JSON_KEY_BEACONLIST);
			JSONArray cooObject = jsonObj.getJSONArray(JSON_KEY_COOLIST);
			
			for (int i = 0; i < beaconsObject.length(); i++) {
				BeaconServer beaconS =  BeaconServer.fromJSON(beaconsObject.get(i).toString());
				listaBeacona.add(beaconS);
			}
			for (int i = 0; i < cooObject.length(); i++) {
				SpaceCoordinate spaceS = SpaceCoordinate.fromJSON(cooObject.get(i).toString());
				listaCoo.add(spaceS);
			}
			
			return new Space(id, uuid, version, title, "", "", mapA, listaBeacona, listaCoo);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		return null;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(uuid);
		dest.writeInt(version);
		dest.writeString(title);
		dest.writeString(description);
		dest.writeString(welcomeMessage);
		dest.writeString(mapAddress);
		dest.writeList(beacons);
		dest.writeList(spaceCoordinates);
	}
	
	public static Parcelable.Creator<Space> CREATOR = new Parcelable.Creator<Space>() {

		@Override
		public Space[] newArray(int size) {
			return new Space[size];
		}

		@Override
		public Space createFromParcel(Parcel source) {
			
			return new Space(source);
			
		}
	};

	
}

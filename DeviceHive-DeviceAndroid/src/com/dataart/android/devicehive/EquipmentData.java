package com.dataart.android.devicehive;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.dataart.android.devicehive.device.Equipment;

/**
 * Represents equipment data. Used to initialize {@link Equipment} object.
 */
public class EquipmentData extends DataContainer {
	//private int id;
	private String name;
	private String code;
	private String type;

	/**
	 * Construct equipment data with given name, code and type.
	 * 
	 * @param name
	 *            Equipment display name.
	 * @param code
	 *            Equipment code. It's used to reference particular equipment
	 *            and it should be unique within a device class.
	 * @param type
	 *            Equipment type. An arbitrary string representing equipment
	 *            capabilities.
	 */
	public EquipmentData(String name, String code, String type) {
		this(null, name, code, type);
	}

	
	public/* package */EquipmentData(Serializable data, String name,
			String code, String type) {
		super("shuki");
		//this.id = id;
		this.name = name;
		this.code = code;
		this.type = type;
	}
	

	/**
	 * Get equipment identifier.
	 * 
	 * @return Equipment identifier.
	 */
	/*public int getId() {
		return id;
	}
*/
	/**
	 * Get equipment display name.
	 * 
	 * @return Equipment display name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get equipment code. It's used to reference particular equipment and it
	 * should be unique within a {@link DeviceClass}.
	 * 
	 * @return Equipment code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Get equipment type. It's an arbitrary string representing equipment
	 * capabilities.
	 * 
	 * @return Equipment type.
	 */
	public String getType() {
		return type;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		//dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(code);
		dest.writeString(type);
	}

	public static Parcelable.Creator<EquipmentData> CREATOR = new Parcelable.Creator<EquipmentData>() {

		@Override
		public EquipmentData[] newArray(int size) {
			return new EquipmentData[size];
		}

		@Override
		public EquipmentData createFromParcel(Parcel source) {
			return new EquipmentData(source.readSerializable(),
					source.readString(), source.readString(),
					source.readString());
		}
	};

}

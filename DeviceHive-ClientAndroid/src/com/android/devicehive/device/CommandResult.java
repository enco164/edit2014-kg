package com.android.devicehive.device;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.devicehive.ObjectWrapper;

/**
 * Command execution result which is reported to the server.
 */
public class CommandResult implements Parcelable {

	/**
	 * Command status "Completed" value.
	 */
	public static final String STATUS_COMLETED = "Completed";

	/**
	 * Command status "Failed" value.
	 */
	public static final String STATUS_FAILED = "Failed";

	private final String status;
	private final ObjectWrapper<Serializable> result;

	/**
	 * Constructs command result with given status and result.
	 * 
	 * @param status
	 *            Command status.
	 * @param result
	 *            Command execution result.
	 */
	public CommandResult(String status, Serializable result) {
		this.status = status;
		this.result = new ObjectWrapper<Serializable>(result);
	}

	/**
	 * Get command status.
	 * 
	 * @return Command status.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Get command execution result.
	 * 
	 * @return Command execution result.
	 */
	public Serializable getResult() {
		return result;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(status);
		dest.writeSerializable(result.getObject());
	}

	public static Parcelable.Creator<CommandResult> CREATOR = new Parcelable.Creator<CommandResult>() {

		@Override
		public CommandResult[] newArray(int size) {
			return new CommandResult[size];
		}

		@Override
		public CommandResult createFromParcel(Parcel source) {
			return new CommandResult(source.readString(),
					source.readSerializable());
		}
	};
}

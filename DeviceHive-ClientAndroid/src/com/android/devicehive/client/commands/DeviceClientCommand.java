package com.android.devicehive.client.commands;

import android.os.Parcel;

import com.android.devicehive.network.JsonNetworkCommand;

/**
 * Base command for Device Hive device client commands.
 */
public abstract class DeviceClientCommand extends JsonNetworkCommand {

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

	}

}

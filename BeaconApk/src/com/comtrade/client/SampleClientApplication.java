package com.comtrade.client;

import android.app.Application;

import com.android.devicehive.DeviceData;
import com.example.beaconapk.BuildConfig;

public class SampleClientApplication extends Application {

	private SampleDeviceClient client;

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public void resetClient() {
		if (client != null) {
			client.stopReceivingNotifications();
			client.clearAllListeners();
			client = null;
		}
	}

	public SampleDeviceClient setupClientForDevice(DeviceData device) {
		if (client != null) {
			if (!client.getDevice().getId().equals(device.getId())) {
				resetClient();
				client = getClientForDevice(device);
			}
		} else {
			client = getClientForDevice(device);
		}
		return client;
	}

	public SampleDeviceClient getClient() {
		return client;
	}

	private SampleDeviceClient getClientForDevice(DeviceData device) {
		SampleDeviceClient client = new SampleDeviceClient(
				getApplicationContext(), device);
		client.setApiEnpointUrl("http://nn5964.pg.devicehive.com/api");
		client.setAuthorisation("admin", "comtrade");
		client.setDebugLoggingEnabled(BuildConfig.DEBUG);
		return client;
	}

}

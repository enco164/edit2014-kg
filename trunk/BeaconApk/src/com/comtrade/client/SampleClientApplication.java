package com.comtrade.client;

import android.app.Application;

import com.android.devicehive.DeviceData;
import com.comtrade.device.BeaconApkConfig;
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
		
		client.setApiEnpointUrl(BeaconApkConfig.URI_DH_DEFAULT);
		client.setAuthorisation("dhadmin", "dhadmin_#911");
		client.setDebugLoggingEnabled(BuildConfig.DEBUG);
		return client;
	}

}

package com.comtrade.activities;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.devicehive.DeviceData;
import com.android.devicehive.Network;
import com.android.devicehive.client.commands.DeviceClientCommand;
import com.android.devicehive.client.commands.GetNetworkDevicesCommand;
import com.android.devicehive.network.DeviceHiveResultReceiver;
import com.comtrade.client.BaseActivity;
import com.comtrade.device.BeaconApkConfig;
import com.comtrade.ilserver.tasks.GetSpaceTask;
import com.comtrade.ilserver.tasks.Space;
import com.example.beaconapk.R;

public class SpaceDevicesActivity extends BaseActivity {

	public static final String EXTRA_NETWORK = SpaceDevicesActivity.class
			.getName() + ".EXTRA_NETWORK";
	private static final String EXTRA_DEVICE = MonitoringActivity.class.getName()
			+ ".EXTRA_DEVICE";
	public static void start(Context context, Network network) {
		Intent intent = new Intent(context, SpaceDevicesActivity.class);
		intent.putExtra(EXTRA_NETWORK, network);
		setParentActivity(intent, SpacesActivity.class);
		context.startActivity(intent);
	}

	private static final String TAG = "NetworkDevicesActivity";

	private ListView networkDevicesListView;
	public SharedPreferences sharedPref;
	public Space spaceS;
	private Network network;
	public Intent i;
	private Bitmap mIcon1 = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_space_devices);

		network = getIntent().getExtras().getParcelable(EXTRA_NETWORK);
		if (network == null) {
			throw new IllegalArgumentException(
					"Network extra should be provided");
		}

		setTitle(network.getName());
		
		sharedPref = getApplicationContext().getSharedPreferences(BeaconApkConfig.SHARE_PREFERENCE_REGISTER, 0);
		networkDevicesListView = (ListView) findViewById(R.id.networks_listView);
		networkDevicesListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View itemView, int position, long arg3) {
						NetworkDevicesAdapter adapter = (NetworkDevicesAdapter) adapterView
								.getAdapter();
						final DeviceData device = (DeviceData) adapter
								.getItem(position);
						
						GetSpaceTask task2 = new GetSpaceTask();
						task2.setUsername("admin");
						task2.setPassword("admin");
				    	task2.setSpaceId(640);
						
						task2.execute(sharedPref.getString(BeaconApkConfig.SHARE_URL, null)+"/rest/getSpace");
						try {
							spaceS = Space.fromJSON(task2.get());
						} catch (InterruptedException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						} catch (ExecutionException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						
						i = new Intent(SpaceDevicesActivity.this, MonitoringActivity.class);
						i.putExtra("space", spaceS);
						
						new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
							    URL url_value;
								ByteArrayOutputStream stream = new ByteArrayOutputStream();
								try {
									url_value = new URL(sharedPref.getString(BeaconApkConfig.SHARE_URL, null)+"/"+ spaceS.getMapAddress());
									Log.d("url", url_value.toString());
									mIcon1 = BitmapFactory.decodeStream(url_value.openConnection().getInputStream());
								//	Log.
								} catch (MalformedURLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								  
								mIcon1.compress(Bitmap.CompressFormat.PNG, 100, stream);
								byte[] byteArray = stream.toByteArray();
								i.putExtra("image", byteArray);
							
								
								i.putExtra("device", device);
								startActivity(i);
								
								return;
							}
						}).start();
						
				//		MonitoringActivity.start(SpaceDevicesActivity.this,
				//				device);
					}
				});
	}

	protected void onResume() {
		super.onResume();
		Log.d(TAG, "Starting Fetch Network devices request");
		networkDevicesListView.postDelayed(new Runnable() {
			@Override
			public void run() {
				startNetworkDevicesRequest();
			}
		}, 10);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected boolean showsActionBarProgress() {
		return true;
	}

	@Override
	protected boolean showsRefreshActionItem() {
		return true;
	}

	@Override
	protected void onRefresh() {
		startNetworkDevicesRequest();
	}
	
	private void startNetworkDevicesRequest() {
		incrementActionBarProgressOperationsCount(1);
		startCommand(new GetNetworkDevicesCommand(network.getId()));
	}

	private static class NetworkDevicesAdapter extends BaseAdapter {

		private final LayoutInflater inflater;
		private final List<DeviceData> devices;

		public NetworkDevicesAdapter(Context context, List<DeviceData> devices) {
			this.devices = devices;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return devices.size();
		}

		@Override
		public Object getItem(int position) {
			return devices.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater
						.inflate(R.layout.network_list_item, null);
				holder = new ViewHolder();
				holder.name = (TextView) convertView
						.findViewById(R.id.network_name_text_view);
				holder.description = (TextView) convertView
						.findViewById(R.id.network_description_text_view);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final DeviceData device = devices.get(position);
			holder.name.setText(device.getName());
			holder.description.setText(device.getDeviceClass().getName());
			return convertView;
		}

		private class ViewHolder {
			TextView name;
			TextView description;
		}

	}

	protected void onReceiveResult(final int resultCode, final int tagId,
			final Bundle resultData) {
		switch (resultCode) {
		case DeviceHiveResultReceiver.MSG_COMPLETE_REQUEST:	  
	         decrementActionBarProgressOperationsCount();
	         break;
		case DeviceHiveResultReceiver.MSG_EXCEPTION:
			final Throwable exception = DeviceClientCommand
					.getThrowable(resultData);
			Log.e(TAG, "Failed to execute network command", exception);
			break;
		case DeviceHiveResultReceiver.MSG_STATUS_FAILURE:
			int statusCode = DeviceClientCommand.getStatusCode(resultData);
			Log.e(TAG, "Failed to execute network command. Status code: "
					+ statusCode);
			break;
		case DeviceHiveResultReceiver.MSG_HANDLED_RESPONSE:
			if (tagId == TAG_GET_NETWORK_DEVICES) {
				final List<DeviceData> devices = GetNetworkDevicesCommand
						.getNetworkDevices(resultData);
				Log.d(TAG, "Fetched devices: " + devices);
				if (devices != null) {
					Collections.sort(devices, new Comparator<DeviceData>() {
						@Override
						public int compare(DeviceData lhs, DeviceData rhs) {
							return lhs.getName().compareToIgnoreCase(
									rhs.getName());
						}
					});
					networkDevicesListView
							.setAdapter(new NetworkDevicesAdapter(this, devices));
				}
			}
			break;
		}
	}

	private static final int TAG_GET_NETWORK_DEVICES = getTagId(GetNetworkDevicesCommand.class);
}

package com.comtrade.activities;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.ClientProtocolException;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.devicehive.Network;
import com.android.devicehive.client.commands.DeviceClientCommand;
import com.android.devicehive.client.commands.GetNetworksCommand;
import com.android.devicehive.network.DeviceHiveResultReceiver;
import com.comtrade.client.BaseActivity;
import com.example.beaconapk.R;
public class SpacesActivity extends BaseActivity {

	private ListView spacesListView;
	private static final String TAG = "SpcesActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spaces);
		
		spacesListView = (ListView) findViewById(R.id.spaces_listView);
		spacesListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View itemView, int position, long arg3) {
						NetworksAdapter adapter = (NetworksAdapter) adapterView
								.getAdapter();
						Network network = (Network) adapter.getItem(position);
						// start network devices activity
					SpaceDevicesActivity.start(SpacesActivity.this,
								network);
					}
				});

		ActionBar actionbar = getActionBar();
		actionbar.setTitle("Networks");
	}
	
	protected void onResume() {
		super.onResume();
	//	Log.d(TAG, "Starting Get Networks request");
		spacesListView.postDelayed(new Runnable() {
			@Override
			public void run() {
				startNetworksRequest();
			}
		}, 10);
	}
	
	
	private void startNetworksRequest() {
		incrementActionBarProgressOperationsCount(1);
		startCommand(new GetNetworksCommand());
	}

	
	private static class NetworksAdapter extends BaseAdapter {

		private final LayoutInflater inflater;
		private final List<Network> networks;

		public NetworksAdapter(Context context, List<Network> networks) {
			this.networks = networks;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return networks.size();
		}

		@Override
		public Object getItem(int position) {
			return networks.get(position);
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
			final Network network = networks.get(position);
			holder.name.setText(network.getName());
			holder.description.setText(network.getDescription());
			return convertView;
		}

		private class ViewHolder {
			TextView name;
			TextView description;
		}

	}
	
	@Override
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
			if (exception instanceof ClientProtocolException
					&& exception.getCause() instanceof MalformedChallengeException) {
				showSettingsDialog("Authentication error!",
						"Looks like your credentials are not valid.");
			} else {
				showSettingsDialog("Error", "Failed to connect to the server.");
			}
			break;
		case DeviceHiveResultReceiver.MSG_STATUS_FAILURE:
			int statusCode = DeviceClientCommand.getStatusCode(resultData);
			Log.e(TAG, "Failed to execute network command. Status code: "
					+ statusCode);
			if (statusCode == 404) {
				showSettingsDialog("Error", "Failed to connect to the server.");
			}
			break;
		case DeviceHiveResultReceiver.MSG_HANDLED_RESPONSE:
			if (tagId == TAG_GET_NETWORKS) {
				final List<Network> networks = GetNetworksCommand
						.getNetworks(resultData);
				Log.d(TAG, "Fetched networks: " + networks);
				if (networks != null) {
					Collections.sort(networks, new Comparator<Network>() {
						@Override
						public int compare(Network lhs, Network rhs) {
							return lhs.getName().compareToIgnoreCase(
									rhs.getName());
						}
					});
					spacesListView.setAdapter(new NetworksAdapter(this,
							networks));
				}
			}
			break;
		}
	}
	

	private void showSettingsDialog(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton("Edit settings",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							//	startSettingsActivity();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).create();
		dialog.show();
	}
	
	private static final int TAG_GET_NETWORKS = getTagId(GetNetworksCommand.class);

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

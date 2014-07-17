package com.comtrade.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import com.android.devicehive.network.DeviceHiveResultReceiver;
import com.android.devicehive.network.NetworkCommand;
import com.android.devicehive.network.NetworkCommandConfig;
import com.comtrade.activities.SpacesActivity;
import com.comtrade.device.BeaconApkConfig;
import com.example.beaconapk.BuildConfig;
import com.example.beaconapk.R;

public class BaseActivity extends Activity {

	private final static String NAMESPACE = BaseActivity.class.getName();

	private final static String EXTRA_PARENT_ACTIVITY = NAMESPACE
			.concat(".EXTRA_PARENT_ACTIVITY");

	private final static String EXTRA_PARENT_ACTIVITY_EXTRAS = NAMESPACE
			.concat(".EXTRA_PARENT_ACTIVITY_EXTRAS");

	private DeviceHiveResultReceiver resultReceiver = null;

	private final DeviceHiveResultReceiver.ResultListener resultListener = new DeviceHiveResultReceiver.ResultListener() {
		@Override
		public void onReceiveResult(int code, int tag, Bundle data) {
			BaseActivity.this.onReceiveResult(code, tag, data);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (resultReceiver != null) {
			resultReceiver.detachResultListener();
			resultReceiver = null;
		}
	}

	protected final <T extends NetworkCommand> void startCommand(final T command) {
		command.start(getApplicationContext(), getNetworkCommandConfig());
	}

	protected DeviceHiveResultReceiver getResultReceiver() {
		if (null == resultReceiver) {
			resultReceiver = new DeviceHiveResultReceiver();
			resultReceiver.setResultListener(resultListener, true);
		}
		return resultReceiver;
	}

	protected NetworkCommandConfig getNetworkCommandConfig() {
		// TODO SKINI OVO I PREBACI U SHARED PREFS
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String DHserverUrl = prefs.getString("ServerUrl", BeaconApkConfig.URI_DH_DEFAULT);
		
		if (DHserverUrl == null) {
			DHserverUrl = BeaconApkConfig.URI_DH_DEFAULT;
			
		}
		final NetworkCommandConfig config = new NetworkCommandConfig(DHserverUrl,
				getResultReceiver(), BuildConfig.DEBUG);

		config.setBasicAuthorisation("dhadmin", "dhadmin_#911");
		return config;
	}

	protected void onReceiveResult(final int resultCode, final int tagId,
			final Bundle resultData) {

	}

	protected static final int getTagId(final Class<?> tag) {
		return getTagId(tag.getName());
	}

	protected static final int getTagId(final String tag) {
		return DeviceHiveResultReceiver.getIdForTag(tag);
	}

	private static final int MENU_ID_SETTINGS = 0x01;
	private static final int MENU_ID_REFRESH = 0x02;

	private Menu optionsMenu;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		optionsMenu = menu;
		// TODO Sredi ovo
		/*
		if (showsSettingsActionItem()) {
			menu.add(Menu.NONE, MENU_ID_SETTINGS,
					Menu.NONE, "Settings")
					
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}
		if (showsRefreshActionItem()) {
			menu.add(Menu.NONE, MENU_ID_REFRESH,
					Menu.NONE, "Refresh")
					
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}*/
		return super.onCreateOptionsMenu(menu);
	}

	protected final static <T extends Activity> Intent setParentActivity(
			final Intent intent, final Class<T> parentActivityClass) {
		return setParentActivity(intent, parentActivityClass, null);
	}

	protected final static <T extends Activity> Intent setParentActivity(
			final Intent intent, final Class<T> parentActivityClass,
			Bundle parentActivityExtras) {
		intent.putExtra(EXTRA_PARENT_ACTIVITY, parentActivityClass.getName());
		if (parentActivityExtras != null) {
			intent.putExtra(EXTRA_PARENT_ACTIVITY_EXTRAS, parentActivityExtras);
		}
		return intent;
	}

	protected boolean showsHomeAsUpButton() {
		return true;
	}

	protected Intent getHomeUpNavigationTarget() {
		final String parentActivityClass = getIntent().getStringExtra(
				EXTRA_PARENT_ACTIVITY);
		final Intent intent;
		if (parentActivityClass == null) {
			intent = new Intent(this, SpacesActivity.class);
		} else {
			intent = new Intent();
			intent.setComponent(new ComponentName(this, parentActivityClass));
		}
		final Bundle parentExtras = getIntent().getBundleExtra(
				EXTRA_PARENT_ACTIVITY_EXTRAS);
		if (parentExtras != null) {
			intent.putExtras(parentExtras);
		}
		return intent;
	}

	@Override
	public boolean onOptionsItemSelected(
			MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			final Intent intent = getHomeUpNavigationTarget();
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}
		case MENU_ID_SETTINGS:
			onShowSettings();
			break;
		case MENU_ID_REFRESH:
			onRefresh();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	protected boolean showsSettingsActionItem() {
		return false;
	}

	protected boolean showsRefreshActionItem() {
		return false;
	}

	protected void onShowSettings() {

	}

	protected void onRefresh() {

	}

	protected boolean showsActionBarProgress() {
		return false;
	}

	private int progressOperationsCount = 0;

	protected void incrementActionBarProgressOperationsCount(int count) {
		this.progressOperationsCount += count;
		setActionBarProgressVisibility(count > 0);
	}

	protected void decrementActionBarProgressOperationsCount() {
		progressOperationsCount -= 1;
		if (progressOperationsCount == 0) {
			setActionBarProgressVisibility(false);
		}
		if (progressOperationsCount < 0) {
			progressOperationsCount = 0;
		}
	}

	protected int getActionBarProgressOperationsCount() {
		return progressOperationsCount;
	}

	protected void setActionBarProgressVisibility(boolean visible) {
		if (optionsMenu != null) {
			final MenuItem item = optionsMenu
					.findItem(MENU_ID_REFRESH);
			if (item != null) {
				if (visible) {
					item.setActionView(R.layout.menu_progress);
				} else {
					item.setActionView(null);
					progressOperationsCount = 0;
				}
			}
		}
	}

	protected void showErrorDialog(String message) {
		showDialog("Error!", message);
	}

	protected void showDialog(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.setTitle(title).setMessage(message)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).create();
		dialog.show();
	}

}

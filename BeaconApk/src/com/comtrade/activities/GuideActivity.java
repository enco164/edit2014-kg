package com.comtrade.activities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.comtrade.device.BeaconApkConfig;
import com.comtrade.ilserver.tasks.BeaconServer;
import com.comtrade.ilserver.tasks.GetBeaconTask;
import com.comtrade.ilserver.tasks.GetSpaceTask;
import com.comtrade.ilserver.tasks.Space;
import com.comtrade.ilserver.tasks.User;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.example.beaconapk.R;

public class GuideActivity extends Activity {
	private static final String TAG = GuideActivity.class.getSimpleName();

	private static final int REQUEST_ENABLE_BT = 1234;
	private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid",
			null, null, null);

	private BeaconManager beaconManager;
	private List<Beacon> beaconi;
	private Bitmap mIcon1 = null;
	private Space spaceS;
	private Intent i;
	
	private boolean Scanindicator=false;
	
	SharedPreferences sharedPref;
	Editor sharedPrefEditor;
	private User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		
		sharedPref = getApplicationContext().getSharedPreferences(BeaconApkConfig.SHARE_PREFERENCE_REGISTER, 0);
		user = (User) getIntent().getParcelableExtra(User.EXTRA);
		Log.d("user " + TAG , "" + user.toString());
		beaconManager = new BeaconManager(this);
		beaconManager.setRangingListener(new BeaconManager.RangingListener() {


			@Override
			public void onBeaconsDiscovered(Region arg0, List<Beacon> arg1) {
				// TODO Auto-generated method stub
				beaconi = arg1;
				runOnUiThread(new Runnable() {
					public void run() {
						if(Scanindicator==false){
						Beacon foundBeacon = null;
						if(beaconi.size() > 0){
							foundBeacon = beaconi.get(0);
							Scanindicator=true;
							String mac = foundBeacon.getMacAddress().toString();


						//	Toast.makeText(getApplicationContext(), mac, Toast.LENGTH_SHORT).show();


							GetBeaconTask task1 = new GetBeaconTask();
							task1.setUsername("admin");
							task1.setPassword("admin");
							task1.setMacAddress(mac);
							Log.d("url", sharedPref.getString(BeaconApkConfig.SHARE_URL, null));

							task1.execute(sharedPref.getString(BeaconApkConfig.SHARE_URL, null)+"/rest/getBeacon");
							
						
							try {
								BeaconServer beaconS =  BeaconServer.fromJSON(task1.get());
								int spaceId = beaconS.getSpaceId();
								
								GetSpaceTask task2 = new GetSpaceTask();
								task2.setUsername("admin");
								task2.setPassword("admin");
						    	task2.setSpaceId(spaceId);
								
								task2.execute(sharedPref.getString(BeaconApkConfig.SHARE_URL, null)+"/rest/getSpace");
								spaceS = Space.fromJSON(task2.get());
								Log.d(TAG, "" + getIntent().getExtras().containsKey("User"));
								//   .getParcelableExtra(User.EXTRA);
								i = new Intent(GuideActivity.this, DeviceActivity.class);
								i.putExtra("space", spaceS);
								i.putExtra(User.EXTRA, user);
								
								Log.d("GuideActivity", spaceS.getTitle());
								

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
									
										startActivity(i);
										
										return;
									}
								}).start();

	

							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							 

							//			Intent i = new Intent(BeaconAct.this, AdminAct.class);
							//				startActivity(i);
						}
						else{
							//Toast.makeText(getApplicationContext(), "Nije nadjen", Toast.LENGTH_SHORT).show();
						}
					}
					}
				});
			}
		});


	}

	@Override
	protected void onStart() {
		super.onStart();

		// iBeacons
		// Check if device supports Bluetooth Low Energy.
		if (!beaconManager.hasBluetooth()) {
			Toast.makeText(this, "Device does not have Bluetooth Low Energy",
					Toast.LENGTH_LONG).show();
			return;
		}

		// If Bluetooth is not enabled, let user enable it.
		if (!beaconManager.isBluetoothEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			connectToService();
		}

	}

	private void connectToService() {
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
				} catch (RemoteException e) {
					Toast.makeText(
							GuideActivity.this,
							"Cannot start ranging, something terrible happened",
							Toast.LENGTH_LONG).show();
					Log.e(TAG, "Cannot start ranging", e);
				}
			}
		});
	}

	@Override
	protected void onStop() {
		//Ovo mozda treba u onDestroy() :D
	
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		beaconManager.disconnect();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				connectToService();
			} else {
				Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG)
				.show();
				getActionBar().setSubtitle("Bluetooth not enabled");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public static Drawable LoadImageFromWebOperations(String url) {
	    try {
	        InputStream is = (InputStream) new URL(url).getContent();
	        Drawable d = Drawable.createFromStream(is, "src name");
	        return d;
	    } catch (Exception e) {
	        return null;
	    }
	}
}

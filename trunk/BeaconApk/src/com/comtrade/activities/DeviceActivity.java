package com.comtrade.activities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.comtrade.device.BeaconApkConfig;
import com.comtrade.device.TestDevice;
import com.comtrade.device.TestDevice.RegistrationListener;
import com.comtrade.device.TestEquipment;
import com.comtrade.ilserver.tasks.BeaconServer;
import com.comtrade.ilserver.tasks.Space;
import com.comtrade.ilserver.tasks.User;
import com.comtrade.map.MapFrame;
import com.comtrade.mathematics.BeaconRacun;
import com.comtrade.mathematics.Circle;
import com.dataart.android.devicehive.DeviceClass;
import com.dataart.android.devicehive.DeviceData;
import com.dataart.android.devicehive.Network;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.BeaconManager.MonitoringListener;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.example.beaconapk.R;

public class DeviceActivity extends Activity implements 
RegistrationListener, SensorEventListener{
	private static final String TAG = DeviceActivity.class.getSimpleName();

	public static final String EXTRAS_TARGET_ACTIVITY = "extrasTargetActivity";
	public static final String EXTRAS_BEACON = "extrasBeacon";
	private static final int REQUEST_ENABLE_BT = 1234;
	private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid",
			null, null, null);

	private NotificationManager notificationManager;
	private SensorManager mSensorManager;
	private MapFrame mapFrame;
	private float currentDegree = 0f;
	public SharedPreferences sherP;
	public Space spaceS;
	PointF currnetPosition;
	private int daljina; //daljina na koju ce da izbacuje notifikacije
	
	private BeaconManager beaconManager;
	private List<Beacon> listaBikonaSkeniranih;
	private ArrayList<BeaconServer> listaBikonaIzProstora;
	
	
	private List<Parameter> parameters = new LinkedList<Parameter>();

	public static class Parameter {
		public final String name;
		public final float value;

		public Parameter(String name, float value) {
			this.name = name;
			this.value = value;
		}
	}
	
	SharedPreferences sharedPref;
	Editor sharedPrefEditor;
	DeviceData deviceData;
	TestDevice device;
	Drawable image;
	Random rand;

	String ilsSeverUri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Setting up Content of Activity
		mapFrame = new MapFrame(getApplicationContext());
		setContentView(mapFrame);

		
		currnetPosition = new PointF();
		Intent i = getIntent();
		spaceS = (Space) 	i.getParcelableExtra("space");
		sherP = PreferenceManager.getDefaultSharedPreferences(this);
		mapFrame.getTouchView().PrimaListuBikona(spaceS.getBeacons());
		listaBikonaIzProstora = spaceS.getBeacons();
		Toast.makeText(getApplicationContext(), spaceS.getTitle(), Toast.LENGTH_LONG).show();
	
		byte[] byteArray = i.getByteArrayExtra("image");
		Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
		
		Drawable d = new BitmapDrawable(getResources(),bmp);
		Log.d("TAG@", d.toString());
		
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		daljina =Integer.parseInt(sherP.getString(SettingsFragment.DISTANCE_FOR_NOTIFICATIONS, "1"));
		Log.d("daljina", ""+daljina);
		
		
		int w = spaceS.getSpaceCoordinates().get(1).getX();
		int h = spaceS.getSpaceCoordinates().get(1).getY();
		int x0 = spaceS.getSpaceCoordinates().get(0).getX();
		int y0 = spaceS.getSpaceCoordinates().get(0).getY();
		
		mapFrame.setMapImage(d,x0, y0, w,h);		
		
		Log.d("Device", "" + w);
		Log.d("Device", "" + h);
	    // initialize your android device sensor capabilities
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		User user = (User) i.getParcelableExtra(User.EXTRA);
		Log.d("user" + TAG, "" + i.getParcelableExtra(User.EXTRA));
		
		String deviceId=user.getUuid();
		String deviceName=user.getFirstname()+" "+user.getSurname();
		String deviceKey="4D6B0A4A-CA77-4164-AAB0-52A7FE3DBD76";
		
		

		Network network = new Network(spaceS.getTitle(), spaceS.getTitle(), spaceS.getTitle());
		DeviceClass deviceClass = new DeviceClass("Indoor location device",
				"1.1");
		deviceData = new DeviceData(deviceId, deviceKey	, deviceName, DeviceData.DEVICE_STATUS_ONLINE, network, deviceClass);
		
		device = new TestDevice(getApplicationContext(), deviceData, new TestEquipment());

		//Setting up DeviceHive
		parameters = new LinkedList<Parameter>();
		
		// ...

		rand = new Random();
		Button btn = mapFrame.getButton();
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mapFrame.getTouchView().logoCentar();
			}
		});
		
		// Podesavanje iBeacon
		
		beaconManager = new BeaconManager(this);
		beaconManager.setRangingListener(new BeaconManager.RangingListener() {
			@Override
			public void onBeaconsDiscovered(Region region, final List<Beacon> discoveredBeacons) {
				listaBikonaSkeniranih = discoveredBeacons;
				
				Log.d("PSA", "pp ");
				// Note that results are not delivered on UI thread.
				runOnUiThread(new Runnable() {
					public void run() {
						Log.d("PSA", "pp "+ discoveredBeacons.size());
						if(discoveredBeacons.size() > 0){
						
							Log.d("PSA", "pp "+ Utils.computeAccuracy(discoveredBeacons.get(0)));
							
							//za svaki beacon na manje od 1 m salje notifikaciju
							for (int j = 0; j < discoveredBeacons.size(); j++) {
								Log.d("NOTIFIKACIJe",""+ Utils.computeAccuracy(discoveredBeacons.get(j)));
								if(Utils.computeAccuracy(discoveredBeacons.get(j)) < daljina){
									DecimalFormat df = new DecimalFormat("#.##");		
									//"Closest iBeacon is approximately " + df.format(Utils.computeAccuracy(discoveredBeacons.get(0)))+" meters away"
									int idNotifikacije = VratiIdBikona(discoveredBeacons.get(j));
									if (idNotifikacije != -1){
										postNotification(VratiIdBikona(discoveredBeacons.get(j)), VratiPorukuBikona(discoveredBeacons.get(j)));
									}
								}
							}	
								
							if(discoveredBeacons.size()>2){
								//ovde moze
							}
						}
						// Note that beacons reported here are already sorted by
						// estimated
						// distance between device and beacon.

						/* Porvera da li se nalazi u mapi beacon sa tom mac adresom
						 * ako se ne nalazi pravi novo polje u mapi sa tim kljucem i dodaje novi vektor sa daljinom
						 * inace
						 * 		ako nema barem 6 vrednosti upisanih za taj kljuc
						 * 			dodaje udaljenos u vektor
						 * 		else
						 * 			racuna prosek 6 vrednosti, stavlja u beaconDistances mapu i resetuje RSSI List
						 */

						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								
								updateMap();
							
							}


						});

					}
				}
						);

			}
		});
		
		beaconManager.setMonitoringListener(new MonitoringListener() {
			
			@Override
			public void onExitedRegion(Region arg0) {
				
				Toast.makeText(getApplicationContext(), "Out of region", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onEnteredRegion(Region arg0, List<Beacon> arg1) {
				
			
				
				
			}
		});

	}
	
	private String NadjiUListi(List<Beacon> arg1){
		ArrayList<BeaconServer> lisBe = spaceS.getBeacons();
		for (BeaconServer beaconServer : lisBe) {
			if(beaconServer.getMac() == arg1.get(0).getMacAddress()){
				return beaconServer.getDescription();
			}
		}
		return null;
	}

	private int VratiIdBikona(Beacon b)
	{
		for (BeaconServer izProstora : listaBikonaIzProstora) {											
			if(izProstora.getMac().equalsIgnoreCase(b.getMacAddress())){
				return izProstora.getId();
			}
		}
		Log.d("los id", "ne uzima id");
		return -1;
	}
	
	private String VratiPorukuBikona(Beacon b)
	{
		for (BeaconServer izProstora : listaBikonaIzProstora) {											
			if(izProstora.getMac().equalsIgnoreCase(b.getMacAddress())){
				return izProstora.getDescription();
			}
		}
		return "";
		
	}
	
	private void postNotification(int id, String msg) {				
		Notification.Builder notification = new Notification.Builder(this)
		.setSmallIcon(R.drawable.beacon_gray)
		.setContentTitle(spaceS.getTitle())//space
		.setContentText(msg);//massage iz bikona
		notificationManager.notify(id, notification.build());
		
	}



	private void updateMap() {
		Vector<BeaconRacun> beacons = new Vector<BeaconRacun>();

		Log.d("device", "prostor "+listaBikonaIzProstora.size());
		Log.d("device", "skenirai "+listaBikonaSkeniranih.size());

		for (Beacon skeniran : listaBikonaSkeniranih) {
			for (BeaconServer izProstora : listaBikonaIzProstora) {

				Log.e(TAG, skeniran.getMacAddress() + " == " + izProstora.getMac());
				if(skeniran.getMacAddress().equalsIgnoreCase(izProstora.getMac()))
				{
					float x = (float)izProstora.getX(); 
					float y = (float)izProstora.getY();
					//Log.d("device", "beacon size= ");
					BeaconRacun br = new BeaconRacun(new PointF(x,y), Utils.computeAccuracy(skeniran));
					br.setMac(skeniran.getMacAddress());
					beacons.add(br);

				}
			}
			Log.d("device", "===============");
		}


		Log.d("device", beacons.toString());

		if(beacons.size()>2)
		{


			Log.e(TAG, ""+beacons.size());
			Vector<PointF> points = Circle.potential_points(beacons);
			Log.e(TAG, points.toString());

			final PointF tacka = Circle.kandidat(points);


			if(tacka != null && !Float.isNaN(tacka.x) && !Float.isNaN(tacka.y)){
				runOnUiThread(new Runnable() {
					public void run() {
						if (currnetPosition.x < Math.abs(tacka.x+ 0.75) || currnetPosition.x < Math.abs(tacka.x+0.75) ){
							parameters.clear();
							addParameter("x", tacka.x);
							addParameter("y", tacka.y);
							sendDeviceDataNotification();
							Log.d(TAG, ""+tacka.x + " " + tacka.y);
							mapFrame.getTouchView().getDot().setCoordinates(tacka.x, tacka.y);
						}

					}
				});

			}
		}
	}



	private void sendDeviceDataNotification() {
		HashMap<String, Object> parameters = paramsAsMap(this.parameters);
		
		device.getDeviceData().setData(parameters);
		device.registerDevice();
		//Log.d(TAG, ""+device.getDeviceData().getData().toString());
	}



	private void connectToService() {
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {

			@Override
			public void onServiceReady() {
				try {
					beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
				//	beaconManager.startMonitoring(ALL_ESTIMOTE_BEACONS_REGION);
					Log.d("SSS", "OVDE2");
				} catch (RemoteException e) {
					Toast.makeText(
							DeviceActivity.this,
							"Cannot start ranging, something terrible happened",
							Toast.LENGTH_LONG).show();
					Log.e(TAG, "Cannot start ranging", e);
				}
			}
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	
		// Check if device supports Bluetooth Low Energy.
		if (!beaconManager.hasBluetooth()) {
			Toast.makeText(this, "Device does not have Bluetooth Low Energy",
					Toast.LENGTH_LONG).show();
			return;
		}

		// If Bluetooth is not enabled, let user enable it.
		if (!beaconManager.isBluetoothEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			connectToService();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		//podesavanje vremena skeniranja
		beaconManager.setBackgroundScanPeriod(Long.parseLong(sherP.getString(SettingsFragment.SCAN_PERIOD_KEY, "")), Long.parseLong(sherP.getString(SettingsFragment.SCAN_PERIOD_KEY, "")));
		daljina =Integer.parseInt(sherP.getString(SettingsFragment.DISTANCE_FOR_NOTIFICATIONS, "1"));
		Log.d("daljina", ""+daljina);
		//omogucavanje i onemogucavanje kompasa
		Log.d("dasdfa",""+ sherP.getBoolean(SettingsFragment.COMPAS_ON_OFF, true));
		if(sherP.getBoolean(SettingsFragment.COMPAS_ON_OFF, true) == true){
			mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
					SensorManager.SENSOR_DELAY_GAME);
		}
		else{
			mSensorManager.unregisterListener(this);
		}
        // for the system's orientation sensor registered listeners
	

		//Device
		device.setApiEnpointUrl(BeaconApkConfig.URI_DH_DEFAULT);
		device.addDeviceListener(this);
		if (!device.isRegistered()) { 
			device.registerDevice();
		} else {
			device.startProcessingCommands();
		}
		
		
		
		// iBeacons

		//TODO: MapFrame set beacon Position


	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float degree = Math.round(event.values[0]);
		mapFrame.getTouchView().rotateMap(degree, currentDegree);
		
		currentDegree = degree;
	
	};
	
	@Override
	protected void onPause() {
		super.onPause();

		//unregister shared preferences listener
        // to stop the listener and save battery
		mSensorManager.unregisterListener(this);

		if (isFinishing()) {
			//device.unregisterDevice();
		}
	}

	@Override
	protected void onStop() {
		// iBeacons
		try {
			beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
		} catch (RemoteException e) {
			Log.d(TAG, "Error while stopping ranging", e);
		}

		super.onStop();
	}


	@Override
	protected void onDestroy() {
		// disconnect device 
		//device.removeDeviceListener(this);
		
		if (isFinishing()) {
			//device.unregisterDevice();
		}
		// iBeacoons
		beaconManager.disconnect();

		super.onDestroy();
	}




	/*
	 *     ///////// DEVICE \\\\\\\\\\\ 
	 */

	@Override
	public void onDeviceRegistered() {
		Toast.makeText(getApplicationContext(), "Device is successfully registered to server", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDeviceFailedToRegister() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder
				.setTitle("Error")
				.setMessage("Failed to register device")
				.setPositiveButton("Retry",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						//device.registerDevice();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();
						finishActivity(0);
					}
				}).create();
		//dialog.show();
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

	protected void showErrorDialog(String message) {
		showDialog("Error!", message);
	}


	private static final int SETTINGS_REQUEST_CODE = 0x01;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SETTINGS_REQUEST_CODE && resultCode == RESULT_OK) {
			Log.d(TAG, "Changed settings!");
			//device.unregisterDevice();
			//device.setApiEnpointUrl(BeaconApkConfig.URI_DH_DEFAULT);
		}
	}

	public void addParameter(String name, float value) {
		this.parameters.add(new Parameter(name, value));
	}

	private HashMap<String, Object> paramsAsMap(List<Parameter> params) {
		HashMap<String, Object> paramsMap = new HashMap<String, Object>();
		for (Parameter param : params) {
			paramsMap.put(param.name, param.value);
		}
		paramsMap.put("spaceId", spaceS.getId());
		return paramsMap;
	}


	/*
	 * INIT FOR MENU
	 * 
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
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
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}


}
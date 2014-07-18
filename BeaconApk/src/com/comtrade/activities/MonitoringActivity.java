package com.comtrade.activities;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.devicehive.DeviceData;
import com.comtrade.client.BaseActivity;
import com.comtrade.client.SampleDeviceClient;
import com.comtrade.device.BeaconApkConfig;
import com.comtrade.ilserver.tasks.Space;
import com.comtrade.map.MapFrame;
import com.example.beaconapk.R;

public class MonitoringActivity extends BaseActivity {


	private static final String EXTRA_DEVICE = MonitoringActivity.class.getName()
			+ ".EXTRA_DEVICE";

	public static void start(Context context, DeviceData deviceData) {
		final Intent intent = new Intent(context, MonitoringActivity.class);
		final Bundle parentExtras = new Bundle(1);
		parentExtras.putParcelable(SpaceDevicesActivity.EXTRA_NETWORK,
				deviceData.getNetwork());
		intent.putExtra(EXTRA_DEVICE, deviceData);
		setParentActivity(intent, SpaceDevicesActivity.class, parentExtras);

		context.startActivity(intent);
	}
	Handler handler=new Handler();

	private DeviceData device;
	public SharedPreferences sherP;
	private Bitmap mIcon1 = null;
	public byte[] byteArray; 
	public MapFrame mapF;
	public double x = 0.0;
	public double y = 0.0;
	public Space spaceS;
	SampleDeviceClient sampleDeviceClient;
	Thread t;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mapF= new MapFrame(this);



		device = getIntent().getExtras().getParcelable("device");
		if (device == null) {
			throw new IllegalArgumentException(
					"Device extra should be provided");
		}

		sampleDeviceClient = new SampleDeviceClient(this, device);
		sampleDeviceClient.setApiEnpointUrl(BeaconApkConfig.URI_DH_DEFAULT);
		sampleDeviceClient.setAuthorisation("dhadmin", "dhadmin_#911");
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(device.getData().toString());
			x = (double) jsonObj.getDouble("x");
			y = (double) jsonObj.getDouble("y");
			// TODO
			Log.d("AA", ""+x);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Intent i = getIntent();
		spaceS = (Space) 	i.getParcelableExtra("space");
		sherP = PreferenceManager.getDefaultSharedPreferences(this);
		mapF.getTouchView().PrimaListuBikona(spaceS.getBeacons());

		Toast.makeText(getApplicationContext(), spaceS.getTitle(), Toast.LENGTH_LONG).show();

		byte[] byteArray = i.getByteArrayExtra("image");
		Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

		Drawable d = new BitmapDrawable(getResources(),bmp);
		Log.d("TAG@", d.toString());

		int w = spaceS.getSpaceCoordinates().get(1).getX();
		int h = spaceS.getSpaceCoordinates().get(1).getY();

		int x0 = spaceS.getSpaceCoordinates().get(0).getX();
		int y0 = spaceS.getSpaceCoordinates().get(0).getY();


		Log.d("AA", ""+x+" "+y);
		mapF.getTouchView().getDot().setxCoor((float)x*100);
		mapF.getTouchView().getDot().setyCoor((float)y*100);
		mapF.getTouchView().invalidate();

		mapF.setMapImage(d, x0, y0, w,h);		
		setContentView(mapF);


		/*
		Thread thread = new Thread()
		{
		    @Override
		    public void run() {
		        try {
		            while(true) {
		                sleep(3000);
		                sampleDeviceClient.reloadDeviceData();
		                device=sampleDeviceClient.getDevice();
		                JSONObject jsonObj;
		        		double newX=0,newY=0;
		                try {
		        			jsonObj = new JSONObject(device.getData().toString());
		        			newX = (double) jsonObj.get("x");
		        			newY = (double) jsonObj.get("y");
			        		mapF.getTouchView().mDot.setCoordinates((float) (newX/100.0), (float) (newY/100.0));
		        		} catch (JSONException e) {
		        			// TODO Auto-generated catch block
		        			e.printStackTrace();
		        		}
		            }
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		    }
		};
		thread.run();*/

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Runnable r = new Runnable(){
			public void run() 
			{

				handler.postDelayed(this, 1000);

				sampleDeviceClient.reloadDeviceData();
				device=sampleDeviceClient.getDevice();
				JSONObject jsonObj;

				try {
					jsonObj = new JSONObject(sampleDeviceClient.getDevice().getData().toString());
					final double newX = (double) jsonObj.getDouble("x");
					final double newY = (double) jsonObj.getDouble("y");
					Log.d("coors", ""+sampleDeviceClient.getDevice().getData().toString() + "       " + newX) ;
					if (newX == 0 && newY==0){

					} else {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								mapF.getTouchView().mDot.setCoordinates((float) (newX), (float) (newY));
							}
						});						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		t = new Thread(r);
		t.start();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		t.stop();
		super.onStop();
	}

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


}

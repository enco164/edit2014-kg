package com.comtrade.activities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.devicehive.DeviceData;
import com.comtrade.client.BaseActivity;
import com.comtrade.device.BeaconApkConfig;
import com.comtrade.ilserver.tasks.GetSpaceTask;
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
	
	private DeviceData device;
	public SharedPreferences sherP;
	private Bitmap mIcon1 = null;
	public byte[] byteArray; 
	public MapFrame mapF;
	public double x = 0.0;
	public double y = 0.0;
	public Space spaceS;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mapF= new MapFrame(this);
		
		
		
		device = getIntent().getExtras().getParcelable("device");
		if (device == null) {
			throw new IllegalArgumentException(
					"Device extra should be provided");
		}
		
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(device.getData().toString());
			x = (double) jsonObj.get("x");
			y = (double) jsonObj.get("y");
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
		
		Log.d("AA", ""+x+" "+y);
		mapF.getTouchView().getDot().setxCoor((float)x*100);
		mapF.getTouchView().getDot().setyCoor((float)y*100);
		mapF.getTouchView().invalidate();
		mapF.setMapImage(d,w,h);		
		setContentView(mapF);
		
		
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

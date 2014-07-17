package com.comtrade.map;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.beaconapk.R;

public class MapFrame extends FrameLayout {

	private Context mContext;
	private LayoutInflater layoutInflater;
	
	protected DotView dot;
	protected TouchView touchView;
	protected BeaconView beacon;
	
	protected MapGroup mapGroup;
	
	protected ArrayList<BeaconView> beacons;


	/**
	 * Button on mapFrame
	 */
	private Button btnFindMe;

	public MapFrame(Context context) {
		super(context);
		this.mContext = context;
		inflate();
		bindViews();
	}

	private void inflate() {
		layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.map_frame, this, true);
	}

	private void bindViews() {
		// bind all views here
		touchView = (TouchView) findViewById(R.id.touch_view);
	//	mapGroup = (MapGroup) findViewById(R.id.map_group);
		btnFindMe = (Button) findViewById(R.id.btnFindMe);
	}
	
	public DotView getDotView(){
		return dot;
	} 
	
	public TouchView getTouchView(){
		return touchView;
	}
	public BeaconView getBeaconView(){
		return beacon;
	}
	public Button getButton(){
		return btnFindMe;
	}
	public ArrayList<BeaconView> getBeacons() {
		return beacons;
	}

	public void setBeacons(ArrayList<BeaconView> beacons) {
		this.beacons = beacons;
	}
	/**
	 * sets Drawable mapImage for map image with coordinates Rect(0,0,w,h);
	 * @param mapImage
	 * @param w
	 * @param h
	 */
	public void setMapImage(Drawable mapImage, int w, int h) {
		if (mapImage != null){
			touchView.setMap(mapImage, w, h);
			Log.d("TAG#", mapImage.toString());
		}
		else {
			Log.e(VIEW_LOG_TAG, "map is null");
		}
	}
}

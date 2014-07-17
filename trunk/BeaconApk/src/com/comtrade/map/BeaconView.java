package com.comtrade.map;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.beaconapk.R;

public class BeaconView extends View{
	
	private float xCoor, yCoor;
	private int h,w;
	private String poruka;


	private View parentView;
	
	private Drawable logo;
	
	public BeaconView(Context context, View parentView){
		this(context);
		this.logo = getResources().getDrawable(R.drawable.beacon_gray);
		this.logo.setBounds(0, 0, 30, 30);
		w = 30;
		h = 30;
		this.parentView = parentView;
		xCoor = 0;
		yCoor = 0;		
	}
	
	public BeaconView(Context context){
        super(context);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Toast.makeText(getContext(), "Porukaa nekog bikona", Toast.LENGTH_SHORT).show();
		return true;
	}
	
	/**
	 * draws current position
	 */
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);

		canvas.save();
		canvas.translate(-getW() / 2, -getH() / 2);
		canvas.translate(xCoor, yCoor);
		logo.draw(canvas);

		canvas.restore();
		
	}
	

	
	public int getH() {
		return h;
	}
	
	public void setH(int h) {
		this.h = h;
	}
	
	public int getW() {
		return w;
	}
	
	public void setW(int w) {
		this.w = w;
	}
	
	public float getxCoor() {
		return xCoor;
	}

	public void setxCoor(float xCoor) {
		this.xCoor = xCoor;
	}

	public float getyCoor() {
		return yCoor;
	}

	public void setyCoor(float yCoor) {
		this.yCoor = yCoor;
	}
	public String getPoruka() {
		return poruka;
	}

	public void setPoruka(String poruka) {
		this.poruka = poruka;
	}
	
	public void setCoordinates(final float x, final float y){
		
	}
}


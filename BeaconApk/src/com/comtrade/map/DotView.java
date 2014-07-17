package com.comtrade.map;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.example.beaconapk.R;

public class DotView extends View{
	
	private float xCoor, yCoor;
	private int h,w;
	
	private ValueAnimator va;
	
	private float oldX, oldY;
	private long animationDuration;
	
	private View parentView;
	
	private Drawable logo;
	
	public DotView(Context context, View parentView){
		this(context);
		this.logo = getResources().getDrawable(R.drawable.ct_logo);
		this.logo.setBounds(0, 0, 30, 30);
		w = 30;
		h = 30;
		this.parentView = parentView;
		xCoor = 0;
		yCoor = 0;
		this.animationDuration = 500;		
	}
	
	public DotView(Context context){
        super(context);
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
	
	public void setAnimationDuration(long d) {
		animationDuration = d;
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
	
	/**
	 * animating method which lasts for animationDuration
	 * from current position to (x,y). 
	 * @param x
	 * @param y
	 */
	
	/* ovo se pokrece na istom thredu koji je pozvao ovu metodu
	 * thread treba da ima Looper inace izbacuje runtime exception
	 * 
	 */
	public void setCoordinates(final float x, final float y){
		
		
		//nije testirano
		
		//va = ValueAnimator.ofFloat(xCoor,x,yCoor,y); //mozda treba drugaciji raspored promenljivih
		
		
		va = ValueAnimator.ofFloat(0,1);
		
		va.setInterpolator(null); //linearno pomeranje
		va.setDuration(animationDuration);
		oldX = xCoor;
		oldY = yCoor;
		
		va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				
				//menja vrednosit xCoor i yCoor u zavisnosti od izvrsavanja animacije (getAnimatedFraction vraca izmedju 0 i 1)
				xCoor = oldX + va.getAnimatedFraction() * (x-oldX);
				yCoor = oldY + va.getAnimatedFraction() * (y-oldY);
				//if(va.getAnimatedFraction()==1) va.end();
				parentView.invalidate();
			}
		});
		va.start();
	}
}

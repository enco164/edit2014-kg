package com.comtrade.map;

import java.util.ArrayList;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.comtrade.ilserver.tasks.BeaconServer;
import com.comtrade.mathematics.BeaconRacun;
import com.example.beaconapk.R;
/*
 * TouchView showing map and user locaton
 * 
 */
public class TouchView extends View {
	
	private Drawable mMap;
	
	protected Context currContext;
	
	public Matrix matrix;

	public DotView mDot;
	public BeaconView mBeacon;
	protected GestureDetector gestureDetector;
	protected GestureDetector.OnGestureListener gestureDetectorListener;
	protected ScaleGestureDetector scaleGestureDetector;
	protected ScaleGestureDetector.OnScaleGestureListener scaleGestureDetectorListener;
	protected Drawable iBeacon;
	private Button dugme;
	private float scaleRatio;
	private int nizDim[] = new int[2];
	private Matrix m;
	public ArrayList<BeaconView> listBeacona = new ArrayList<>();

	private Vector<BeaconRacun> beaconPositions;	
	private float zomiranjeSkaliranje=1;
	public TouchView(Context context) {
		this(context, null, 0, new DotView(context));

	}

	public TouchView(Context context, AttributeSet attrs) {
		this(context, attrs, 0, new DotView(context));
	}

	public TouchView(Context context, DotView dot){
		this(context, null, 0, dot);
	}

	/**
	 * Creates instance of touch view. 
	 * It needs dotView to know that it exists
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 * @param dot
	 */
	
	public TouchView(Context context, AttributeSet attrs, int defStyle, DotView dot) {
		super(context, attrs, defStyle);
		
		gestureDetectorListener = new GestureListener();
		scaleGestureDetectorListener = new ScaleListener();
		gestureDetector = new GestureDetector(context, gestureDetectorListener);
		scaleGestureDetector = new ScaleGestureDetector(context, scaleGestureDetectorListener);	
		
		iBeacon = getResources().getDrawable(R.drawable.beacon_gray);
		mDot = new DotView(context, this);//svi bikoni
		mBeacon=new BeaconView(context,this);
		currContext = context;
		matrix = new Matrix(); 
		beaconPositions = new Vector<BeaconRacun>();
		//TODO BeaconRacun
	}
	
	public void PrimaListuBikona(ArrayList<BeaconServer> bs){
		for (BeaconServer beaconServer : bs) {
			BeaconView pBeacon=new BeaconView(getContext(),this);
			pBeacon.setxCoor(beaconServer.getX());
			pBeacon.setyCoor(beaconServer.getY());
			pBeacon.setPoruka(beaconServer.getDescription());
			listBeacona.add(pBeacon);
		}
		
	}
	/**
	 * Sets Drawable map as map image
	 * sets top left corner in (0, 0) 
	 * and bottom right cornet to (w, h)
	 * @param map
	 * @param w  
	 * @param h  
	 */
	public void setMap(Drawable map, int w, int h) {
		mMap = map;
		mMap.setBounds(0, 0, w, h);
	}
	
	/**
	 * sets beacon positions
	 * @param positions
	 */
	public void setBeaconsPositions(Vector<BeaconRacun> positions){
		beaconPositions = positions;
	}
	
	/**
	 * For drawing map
	 */
	@SuppressLint("WrongCall")
	@SuppressWarnings("deprecation")
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);		
		canvas.save();		
		
		///PODESAVANJE SCALE RATIO
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();

		getLocationInWindow(nizDim);
		
		float pom = height*1.0f/mMap.getBounds().height();
		float pom1 = width*1.0f/mMap.getBounds().width();
		
		float min = pom < pom1? pom : pom1;
		scaleRatio = min;
		//Log.d("Scale", "" + scaleRatio);
		//////////
		//zomiranjeSkaliranje=min;
		//canvas.translate(0, nizDim[1]);
		
		//Log.d("C1", canvas.toString());
		canvas.concat(matrix);
		canvas.scale(scaleRatio, scaleRatio);
		//Log.d("C2", canvas.toString());
		mMap.draw(canvas);
		mDot.draw(canvas);
		for (BeaconView bc : listBeacona) {
			bc.draw(canvas);
		}
		canvas.restore();
	}	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// try using the scroll/fling/tap/double-tap first
		//if (!scaleGestureDetector.onTouchEvent(event)) {
			// if not handling, send it through to the zoom gesture
			gestureDetector.onTouchEvent(event);
			scaleGestureDetector.onTouchEvent(event);
			
	//		mBeacon.onTouchEvent(event);
		//}
		return true;
	}
	/*
        boolean handled = false;
        if (isEnabled()) {
            handled = gestureDetector.onTouchEvent(event) || scaleGestureDetector.onTouchEvent(event) ||
                    event.getAction() == MotionEvent.ACTION_UP;
        }
        return handled || super.onTouchEvent(event); 
	}*/

	/**
	 * Sets coordinates for dotView to (x, y)
	 * @param x
	 * @param y
	 */
	public void setDotCoordinates(float x, float y) {
		mDot.setCoordinates(x*100, y*100);
	}
	public void setBeaconCoordinates(float x, float y){
		mBeacon.setX(x);
		mBeacon.setY(y);
	}
	/**
	 * 
	 * @return mDotView
	 */
	public DotView getDot(){
		return mDot;
	}
	public BeaconView getBeacon(){
		return mBeacon;
	}
	
	public void rotateMap(float degree, float oldDegree){
		float centerx = mMap.getBounds().centerX();
		float centery = mMap.getBounds().centerY();
		
		if(oldDegree < degree)
		{
			matrix.preRotate(-degree+oldDegree, centerx, centery);
			invalidate();
		}
		else if(oldDegree > degree)
		{
			matrix.preRotate(+oldDegree-degree, centerx, centery);
			invalidate();
		}

			/*
		matrix.postRotate(degree, centerx, centery);
		invalidate();*/
	}
	
	public void logoCentar()
	{
		float x,y;
		x = mDot.getX();
		y = mDot.getY();
		
		if(mMap.getBounds().exactCenterX() - mMap.getBounds().width()/2.0 < 0)
		{
			if(mMap.getBounds().exactCenterY()-mMap.getBounds().height()/2.0<0)
			{
				matrix.postTranslate(-getMeasuredWidth()/2+ mDot.getX(), -getMeasuredHeight()/2 + mDot.getY());
			}
			else
			{
				matrix.postTranslate(-getMeasuredWidth()/2+ mDot.getX(), getMeasuredHeight()/2 - mDot.getY());
			}
		}
		else if(mMap.getBounds().exactCenterX() - mMap.getBounds().width()/2.0 > 0)
		{
			if(mMap.getBounds().exactCenterY()-mMap.getBounds().height()/2.0<0)
			{
				matrix.postTranslate(getMeasuredWidth()/2- mDot.getX(), -getMeasuredHeight()/2 + mDot.getY());
			}
			else 
			{
				matrix.postTranslate(getMeasuredWidth()/2- mDot.getX(), getMeasuredHeight()/2 - mDot.getY());
			}
		}
		
		invalidate();
	}
	
	
	private class GestureListener implements OnGestureListener, OnDoubleTapListener{
		
		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			
			//Log.d("A","OnDown");
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			
			//Log.d("B", "OnScroll "+distanceX+" "+distanceY);
			//Log.d("M1", matrix.toString());
			matrix.postTranslate(-distanceX, -distanceY);
			//Log.d("M2", matrix.toString());
			invalidate();
			return true;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			// TODO Auto-generated method stub
			matrix.reset();
			zomiranjeSkaliranje = 1;
			invalidate();
			return false;
		}
	}

	private class ScaleListener implements OnScaleGestureListener{


		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			// TODO Auto-generated method stub
			Log.d("Scale",Float.toString(detector.getScaleFactor()));
			zomiranjeSkaliranje*=detector.getScaleFactor();
			
			Log.d("ScaleZomiranje",Float.toString(zomiranjeSkaliranje));
			if(zomiranjeSkaliranje<0.75 || zomiranjeSkaliranje>2.5){
				//ekran je veci od slike
					zomiranjeSkaliranje/=detector.getScaleFactor();
			}
			else{
				//ekran je manji od slike	matrix.preScale(detector.getScaleFactor(), detector.getScaleFactor(), detector.getFocusX(),detector.getFocusY());
				matrix.preScale(detector.getScaleFactor(), detector.getScaleFactor(), detector.getFocusX(),detector.getFocusY());	
			}
			invalidate();

			return true;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
			// TODO Auto-generated method stub
		}
		
	}


}




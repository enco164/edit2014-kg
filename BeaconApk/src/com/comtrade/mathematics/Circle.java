/**
 * 
 */
package com.comtrade.mathematics;

import java.util.Vector;

import com.comtrade.activities.DeviceActivity;

import android.bluetooth.BluetoothClass.Device;
import android.graphics.PointF;

/**
 * @author shuki
 * class that represents circle with its radius and center
 */
public class Circle {
	private PointF center;
	private float radius;

	public Circle(PointF center, float radius) {
		super();
		this.center = center;
		this.radius = radius;
	}

	/**
	 * Constructs the ratio circle from information about two beacons
	 * in their`s local coordinate system with origin
	 * in the middle of the line that connects two beacons
	 * @param d distance between two beacon
	 * @param k ratio of measured distances from beacons 
	 */
	public Circle(float d, float k){
		super();
		center.set((-d/2 * (k*k + 1))/(1 - k*k), 0);
		radius = (float) (- Math.sqrt((-d/2 * (k*k + 1))/(1 - k*k)*(-d/2 * (k*k + 1))/(1 - k*k) - d*d/4.0)); 
	}

	/**
	 * Translates points by vector which is represented as point
	 * @param point
	 */
	public void translate(PointF point){
		center.set(center.x+point.x, center.y+point.y);
	}

	/**
	 * Calculates the intersection points of two circles 
	 * @param a first circle
	 * @param b second circle
	 * @return vector of points
	 */
	public static Vector<PointF> circleIntersect(Circle a, Circle b){
		Vector<PointF> points = new Vector<PointF>();
		float x0, y0, r0, x1, y1, r1, dx, dy, d, d1, x2, y2, h, rx, ry, xi, xi_prime, yi, yi_prime;
		
		x0 = a.center.x;
		y0 = a.center.y;
		r0 = a.radius;
		x1 = b.center.x;
		y1 = b.center.y;
		r1 = b.radius;
		dx = x1 - x0;
		dy = y1 - y0;
		
		d = (float) Math.sqrt(dx * dx + dy * dy);
		
		if (d > (r0 + r1))
		{
			return null;
		}
		
		if (d < Math.abs(r0 - r1))
		{
			return null;
		}
		
		d1 = (float) (((r0*r0) - (r1*r1) + (d*d)) / (2.0 * d));
		x2 =  x0 + (dx * d1/d);
		y2 =  y0 + (dy * d1/d);
		h = (float) Math.sqrt((r0*r0) - (d1*d1));
		rx = -dy * (h/d);
        ry = dx * (h/d);
        
        xi = x2 + rx;
        xi_prime = x2 - rx;
        yi = y2 + ry;
        yi_prime = y2 - ry;
        
        points.add(new PointF(xi, yi));
        points.add(new PointF(xi_prime, yi_prime));
        
		return points;	

	}


	public PointF getCenter() {
		return center;
	}
	public void setCenter(PointF center) {
		this.center = center;
	}
	public float getRadius() {
		return radius;
	}
	public void setRadius(float radius) {
		this.radius = radius;
	}

	public void transform(PointF d, double teta) {
		// TODO Auto-generated method stub
		PointF p = new PointF();
		p.x = (float) (center.x * Math.cos(Math.toRadians(teta)) -
				center.y * Math.sin(Math.toRadians(teta)));

		p.y = (float) (center.x * Math.sin(Math.toRadians(teta)) +
				center.y * Math.cos(Math.toRadians(teta)));

		p.x += d.x;
		p.y += d.y;
		center = p;
	}

	/**
	 * Constructs the ratio circle from information from two beacons
	 * in the global coordinate system
	 * @param a first beacon 
	 * @param b second beacon
	 * @param ratio ratio of distances from two beacons
	 * @return circle object
	 */
	public static Circle getTwoBeaconsCircle(BeaconRacun a, BeaconRacun b){
		//rastojanje izmedju beacona
		double b_dist = Math.sqrt(Math.pow(a.position.x-b.position.x, 2) + Math.pow(a.position.y - b.position.y, 2));

		double teta ;
		if((a.position.x - b.position.x) != 0){
			teta = Math.toDegrees(Math.atan( (b.position.y - a.position.y) / (b.position.x - a.position.x) ));
		}

		else{
			if(b.position.y > a.position.y){
				teta = -90;
			}
			else{
				teta = 90;
			}
		}

		Circle kruznica  = null;
		if (b.position.x - a.position.x > 0){
			//a je levo
			kruznica = new Circle((float)b_dist/2, (float)(a.distance/b.distance));
		} else {
			//b je levo
			kruznica = new Circle((float)b_dist/2, (float)(b.distance/a.distance));
		}

		PointF d = new PointF((a.position.x+b.position.x)/2, (a.position.y+b.position.y)/2);

		kruznica.transform(d, teta);
		return kruznica;

	}


	public static Vector<PointF> tacke = new Vector<PointF>();
	/**
	 * calculates all the possible points of intersection from information about all beacons
	 * @param beacons vector of beacons
	 * @return vector of points
	 */
	//funkcija koja prima skup (vektor) Beacona sa rastojanjima, i vraca skup(vektor) "potencijalnih" tacaka
	public static Vector<PointF> potential_points(Vector<BeaconRacun> beacons){
		//potencijalne tacke (sve tacke preseka krivih)
		Vector<PointF> points = new Vector<PointF>();

		Vector<Circle> krive = new Vector<Circle>();
		for(int i = 0; i<beacons.size()-1; i++){
			for(int j=i+1; j<beacons.size(); j++){	
				Circle krug = getTwoBeaconsCircle(beacons.elementAt(i), beacons.elementAt(j)); 
				if(krug != null){
					krive.add(krug);

				}
				else{
					continue;
				}
			}

		}

		for(int i = 0; i < krive.size()-1; i++){
			for(int j=i+1; j<krive.size(); j++){
				tacke = Circle.circleIntersect(krive.elementAt(i), krive.elementAt(j));
				//tacke = presek_krivi(krive.elementAt(i), krive.elementAt(j));
				if(tacke != null){
					points.addAll(tacke);
				}
			}
		}

		return points;
	}

	/**
	 * 
	 * @param points vector of points
	 * @return best possible candidate
	 */

	public static PointF kandidat(Vector<PointF> points){

		if(points.size() == 0){ 
			return new PointF(0,0);
		}

		int k=points.size();
		double x = 0;
		double y = 0;

		for (PointF pointF : points) {


			if(pointF.x > 4.2 || pointF.x<0 || pointF.y < 0 || pointF.y > 5.1){

				k--;
				continue;
			}

			x += pointF.x;
			y += pointF.y;
		}

		return new PointF((float)(x/k), (float)(y/k));
	}
	
	
}

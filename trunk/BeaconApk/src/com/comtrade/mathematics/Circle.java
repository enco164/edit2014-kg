/**
 * 
 */
package com.comtrade.mathematics;

import java.util.Vector;

import android.graphics.PointF;
import android.util.Log;

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
		float xc = 0;
		if(k==1){
			k = (float)1.0001;
		}
		xc = (-d*(k*k + 1))/(1 - k*k);
		radius = (float) Math.sqrt(xc*xc - d*d);

		center = new PointF(xc, 0);	
	}

	/**
	 * Translates points by vector which is represented as point
	 * @param point
	 */
	public void translate(PointF point){
		center = new PointF(center.x + point.x, center.y + point.y);
	}

	/**
	 * Calculates the intersection points of two circles 
	 * @param a first circle
	 * @param b second circle
	 * @return vector of points
	 */
	public static Vector<PointF> circleIntersect(Circle a, Circle b){
		Vector<PointF> points = new Vector<PointF>();

		double c, dx, dy, d, h, rx, ry;
		double x2, y2;

		/* dx and dy are the vertical and horizontal distances between
		 * the circle centers.
		 */
		dx = b.center.x - a.center.x;
		dy = b.center.y - a.center.y;

		/* Determine the straight-line distance between the centers. */
		d = Math.sqrt((dy*dy) + (dx*dx));

		/* Check for solvability. */
		if (d > (a.radius + b.radius))
		{
			/* no solution. circles do not intersect. */
			return null;
		}
		if (d < Math.abs(a.radius - b.radius))
		{
			/* no solution. one circle is contained in the other */
			return null;
		}

		/* 'point 2' is the point where the line through the circle
		 * intersection points crosses the line between the circle
		 * centers.  
		 */

		/* Determine the distance from point 0 to point 2. */
		c = ((a.radius*a.radius) - (b.radius*b.radius) + (d*d)) / (2.0 * d) ;

		/* Determine the coordinates of point 2. */
		x2 = a.center.x + (dx * c/d);

		y2 = a.center.y + (dy * c/d);

		/* Determine the distance from point 2 to either of the
		 * intersection points.
		 */
		h = Math.sqrt((a.radius*a.radius) - (c*c));

		/* Now determine the offsets of the intersection points from
		 * point 2.
		 */
		rx = -dy * (h/d);
		ry = dx * (h/d);


		/* Determine the absolute intersection points. */
		PointF point1 = new PointF();
		PointF point2 = new PointF();

		point1.x = (float) (x2 + rx);
		point2.x = (float) (x2 - rx);
		point1.y = (float) (y2 + ry);
		point2.y = (float) (y2 - ry);

		points.clear();
		points.add(point1);
		points.add(point2);
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
		
		Log.e("Circle", "====================================");

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


			if( pointF.x<0 || pointF.y < 0){

				k--;
				continue;
			}

			x += pointF.x;
			y += pointF.y;
		}

		return new PointF((float)(x/k), (float)(y/k));
	}
}

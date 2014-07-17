package com.comtrade.mathematics;

import android.graphics.PointF;
//Beacon klasa sadrzi poziciju beacona, i trenutno izmerene udaljenosti od istog
/**
 * 
 * @author shuki
 * Represents a beacon with its position, measured distance and MAC address
 */
public class BeaconRacun {
	
	PointF position;
	double distance;
	String mac; 
	
	public BeaconRacun(PointF position, double distance) {
		super();
		this.position = position;
		this.distance = distance;
	}
	
	public BeaconRacun(PointF position) {
		super();
		this.position = position;
		this.distance = 0;
	}
	
	
	public void setMac(String mac){
		this.mac = mac;
	}
	
	public String getMac(){
		return mac;
	}
	
	public PointF getPosition() {
		return position;
	}
	public void setPosition(PointF position) {
		this.position = position;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "Beacon[mac=" + mac + " distance=" + distance
				+ ", position = "+position+"]";
	}
	
	
}

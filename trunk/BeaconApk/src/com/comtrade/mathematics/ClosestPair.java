package com.comtrade.mathematics;

import java.util.Arrays;
import java.util.Comparator;

import android.graphics.PointF;

public class ClosestPair {

	class PointCmp implements Comparator<PointF> {
	    public int compare(PointF a, PointF b) {
	        return (a.x < b.x) ? -1 : (a.x > b.x) ? 1 : 0;
	    }
	}
	
	// closest pair of points and their Euclidean distance
	private PointF best1, best2;
	private double bestDistance = Double.POSITIVE_INFINITY;

	public ClosestPair(PointF[] points) {
		int N = points.length;
		if (N <= 1) return;

		// sort by x-coordinate (breaking ties by y-coordinate)
		PointF[] pointsByX = new PointF[N];
		for (int i = 0; i < N; i++) pointsByX[i] = points[i];
		Arrays.sort(pointsByX, new PointCmp());

		// check for coincident points
		for (int i = 0; i < N-1; i++) {
			if (pointsByX[i].equals(pointsByX[i+1])) {
				bestDistance = 0.0;
				best1 = pointsByX[i];
				best2 = pointsByX[i+1];
				return;
			}
		}

		// sort by y-coordinate (but not yet sorted) 
		PointF[] pointsByY = new PointF[N];
		for (int i = 0; i < N; i++) pointsByY[i] = pointsByX[i];

		// auxiliary array
		PointF[] aux = new PointF[N];

		closest(pointsByX, pointsByY, aux, 0, N-1);
	}

	// find closest pair of points in pointsByX[lo..hi]
	// precondition:  pointsByX[lo..hi] and pointsByY[lo..hi] are the same sequence of points
	// precondition:  pointsByX[lo..hi] sorted by x-coordinate
	// postcondition: pointsByY[lo..hi] sorted by y-coordinate
	private double closest(PointF[] pointsByX, PointF[] pointsByY, PointF[] aux, int lo, int hi) {
		if (hi <= lo) return Double.POSITIVE_INFINITY;

		int mid = lo + (hi - lo) / 2;
		PointF median = pointsByX[mid];

		// compute closest pair with both endpoints in left subarray or both in right subarray
		double delta1 = closest(pointsByX, pointsByY, aux, lo, mid);
		double delta2 = closest(pointsByX, pointsByY, aux, mid+1, hi);
		double delta = Math.min(delta1, delta2);

		// merge back so that pointsByY[lo..hi] are sorted by y-coordinate
		merge(pointsByY, aux, lo, mid, hi);

		// aux[0..M-1] = sequence of points closer than delta, sorted by y-coordinate
		int M = 0;
		for (int i = lo; i <= hi; i++) {
			if (Math.abs(pointsByY[i].x - median.x) < delta)
				aux[M++] = pointsByY[i];
		}

		// compare each point to its neighbors with y-coordinate closer than delta
		for (int i = 0; i < M; i++) {
			// a geometric packing argument shows that this loop iterates at most 7 times
			for (int j = i+1; (j < M) && (aux[j].y - aux[i].y < delta); j++) {
				double distance = distanceTo(aux[i], aux[j]);
				if (distance < delta) {
					delta = distance;
					if (distance < bestDistance) {
						bestDistance = delta;
						best1 = aux[i];
						best2 = aux[j];
						// StdOut.println("better distance = " + delta + " from " + best1 + " to " + best2);
					}
				}
			}
		}
		return delta;
	}
	
	private double distanceTo(PointF a, PointF b){
		return  Math.sqrt((a.x-b.x)*(a.x-b.x)+(a.y-b.y)*(a.y-b.y));
	}

	public PointF either() { return best1; }
	public PointF other()  { return best2; }

	public double distance() {
		return bestDistance;
	}

	// is v < w ?
	private static boolean less(PointF aux, PointF aux2) {
		return aux.y < aux2.y ? true : false;
	}

	// stably merge a[lo .. mid] with a[mid+1 ..hi] using aux[lo .. hi]
	// precondition: a[lo .. mid] and a[mid+1 .. hi] are sorted subarrays
	private static void merge(PointF[] pointsByY, PointF[] aux, int lo, int mid, int hi) {
		// copy to aux[]
		for (int k = lo; k <= hi; k++) {
			aux[k] = pointsByY[k];
		}

		// merge back to a[] 
		int i = lo, j = mid+1;
		for (int k = lo; k <= hi; k++) {
			if      (i > mid)              pointsByY[k] = aux[j++];
			else if (j > hi)               pointsByY[k] = aux[i++];
			else if (less(aux[j], aux[i])) pointsByY[k] = aux[j++];
			else                           pointsByY[k] = aux[i++];
		}

	}
}

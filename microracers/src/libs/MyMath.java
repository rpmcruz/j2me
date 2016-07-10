// My J2ME auxiliary module - (C) 2011 Ricardo Cruz <rpmcruz0@gmail.com>

/*
 * Does sprite rotation using as feed an image of 5 frames
 * of the object rotated in steps of 18º (1st quadrant).
 */

package libs;

public class MyMath
{
	private static final double asinLookup[] = {
		0.0, 0.100167421162, 0.20135792079, 0.304692654015, 0.411516846067,
		0.523598775598, 0.643501108793, 0.775397496611, 0.927295218002,
		1.119769515, 1.57079631189
	};

	public static int sign(double value) {
		return value >= 0 ? +1 : -1;
	}
	
	public static int calcDistanceSq(int x1, int y1, int x2, int y2) {
		int dx = x2 - x1;
		int dy = y2 - y1;
		return (dx*dx) + (dy*dy);
	}
	
	/**
	 * The arcsin of a given value.
	 * @param value [-1,1] sin value
	 * @return [-PI,PI] angle
	 */
	public static double asin(double value) {
		double n = Math.abs(value) * (asinLookup.length-1);
		int n1 = (int) Math.floor(n);
		int n2 = (int) Math.ceil(n);
		// linear interpolation: the closer to the index the heavier it weights
		if(n1 == n2)
			return asinLookup[n1] * sign(value);
		return ((n2-n)*asinLookup[n1] + (n-n1)*asinLookup[n2]) * sign(value);
	}
	
	public static double calcAngleBetween(int x1, int y1, int x2, int y2) {
		double dy = y2 - y1;
		dy = -dy;  // note: computer display and trigonometric coordinate system differ
		double dist = Math.sqrt(calcDistanceSq(x1, y1, x2, y2));
		if(dist == 0)
			return 0;
		double angle = MyMath.asin(dy / dist);
		// Math.asin can only calculate for q1 and q4 ...
		if(x2 < x1) {
			if(y2 > y1)
				angle = 3*Math.PI - angle;
			else
				angle = Math.PI - angle;
		}
		return angle;
	}
	
	public static double normalizeAngle(double angle) {
		while(angle < 0)
			angle += Math.PI*2;
		while(angle >= Math.PI*2)
			angle -= Math.PI*2;
		return angle;
	}
}

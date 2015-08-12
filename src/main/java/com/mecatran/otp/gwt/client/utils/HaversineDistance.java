/* -------------------------------------------------------------------------
    OpenTripPlanner GWT Client
    Copyright (C) 2015 Mecatran - info@mecatran.com

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
   ------------------------------------------------------------------------- */
package com.mecatran.otp.gwt.client.utils;

public class HaversineDistance {

	private static final double EARTH_RADIUS = 6371 * 1000;

	/**
	 * Compute the great-circle distance (Haversine distance) between two points
	 * on a globe.
	 * 
	 * Approximation of the haversine formula, but much faster (use only one
	 * cosine and one square root). Works only if lat1 ~ lat2 and lon1 ~ lon2.
	 * Coordinates are given in decimal DEGREES.
	 */
	public static double computeHaversineDistance(double lat1, double lon1,
			double lat2, double lon2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1)
				* Math.cos(Math.toRadians((lat1 + lat2) / 2));
		return EARTH_RADIUS * Math.sqrt(dLat * dLat + dLon * dLon);
	}
}

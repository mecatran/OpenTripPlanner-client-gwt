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
package com.mecatran.otp.gwt.client.model;

/**
 * Warning: this class does not handle earth longitude wrap at lon=+/-180!
 * 
 * This will break in Wallis et Futuna for example.
 * 
 */
public class Wgs84BoundsBean {

	private double minLat;
	private double minLon;
	private double maxLat;
	private double maxLon;

	public Wgs84BoundsBean() {
		minLat = Double.MAX_VALUE;
		minLon = Double.MAX_VALUE;
		maxLat = -Double.MAX_VALUE; // And not MIN_VALUE!
		maxLon = -Double.MAX_VALUE;
	}

	public double getMinLat() {
		return minLat;
	}

	public double getMinLon() {
		return minLon;
	}

	public double getMaxLat() {
		return maxLat;
	}

	public double getMaxLon() {
		return maxLon;
	}

	public void extend(Wgs84LatLonBean latLng) {
		if (latLng.getLat() < minLat)
			minLat = latLng.getLat();
		if (latLng.getLat() > maxLat)
			maxLat = latLng.getLat();
		if (latLng.getLon() < minLon)
			minLon = latLng.getLon();
		if (latLng.getLon() > maxLon)
			maxLon = latLng.getLon();
	}

	public boolean contains(Wgs84LatLonBean latLng) {
		return (latLng.getLat() >= minLat && latLng.getLat() <= maxLat
				&& latLng.getLon() >= minLon && latLng.getLon() <= maxLon);
	}
}

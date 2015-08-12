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

import java.util.ArrayList;
import java.util.List;

import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;

public class PolylineEncoder {

	public static String encode(Wgs84LatLonBean[] points) {
		StringBuilder encodedPoints = new StringBuilder();
		int oLate5 = 0;
		int oLnge5 = 0;
		for (Wgs84LatLonBean p : points) {
			int pLate5 = floor1e5(p.getLat());
			int pLnge5 = floor1e5(p.getLon());
			int dLate5 = pLate5 - oLate5;
			int dLnge5 = pLnge5 - oLnge5;
			oLate5 = pLate5;
			oLnge5 = pLnge5;
			encodedPoints.append(encodeSignedNumber(dLate5)).append(
					encodeSignedNumber(dLnge5));
		}
		return encodedPoints.toString();
	}

	private static final int floor1e5(double coordinate) {
		return (int) Math.floor(coordinate * 1e5);
	}

	private static String encodeSignedNumber(int num) {
		int sgn_num = num << 1;
		if (num < 0) {
			sgn_num = ~(sgn_num);
		}
		return (encodeNumber(sgn_num));
	}

	private static String encodeNumber(int num) {
		StringBuffer encodeString = new StringBuffer();
		while (num >= 0x20) {
			int nextValue = (0x20 | (num & 0x1f)) + 63;
			encodeString.append((char) (nextValue));
			num >>= 5;
		}
		num += 63;
		encodeString.append((char) (num));
		return encodeString.toString();
	}

	public static Wgs84LatLonBean[] decode(String encodedGeometry) {
		List<Wgs84LatLonBean> points = new ArrayList<Wgs84LatLonBean>();
		int index = 0, len = encodedGeometry.length();
		int lat = 0, lng = 0;
		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encodedGeometry.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			shift = 0;
			result = 0;
			do {
				b = encodedGeometry.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;
			Wgs84LatLonBean point = new Wgs84LatLonBean((((double) lat / 1E5)),
					(((double) lng / 1E5)));
			points.add(point);
		}
		Wgs84LatLonBean[] retval = new Wgs84LatLonBean[points.size()];
		for (int i = 0; i < retval.length; i++)
			retval[i] = points.get(i);
		return retval;
	}
}

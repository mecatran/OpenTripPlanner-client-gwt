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

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.mecatran.otp.gwt.client.model.TransportMode;

public class FormatUtils {

	// TODO I18N
	private static DateTimeFormat timeFormat = DateTimeFormat
			.getFormat("HH:mm");
	private static DateTimeFormat dateFormat = DateTimeFormat
			.getFormat("dd MMM");

	public static void setTimeFormat(String timeFormat) {
		FormatUtils.timeFormat = DateTimeFormat.getFormat(timeFormat);
	}

	public static void setDateFormat(String dateFormat) {
		FormatUtils.dateFormat = DateTimeFormat.getFormat(dateFormat);
	}

	public static String formatTime(Date time) {
		return timeFormat.format(time);
	}

	public static String formatDate(Date date) {
		return dateFormat.format(date);
	}

	public static String formatDateTime(Date dateTime) {
		return timeFormat.format(dateTime) + " " + dateFormat.format(dateTime);
	}

	/**
	 * @param durationSec
	 *            Duration in seconds
	 * @return Duration nicely formatted (eg: 37mn, 1h38)
	 */
	public static String formatDuration(long durationSec) {
		long min = (durationSec + 30L) / 60L;
		long hour = min / 60L;
		min %= 60L;
		if (hour == 0L) {
			return min + smallerFont("mn");
		} else {
			return hour + smallerFont("h") + (min < 10 ? "0" : "") + min;
		}
	}

	/**
	 * Make a font smaller (css font class 'smaller').
	 * 
	 * @param html
	 * @return
	 */
	public static String smallerFont(String html) {
		return "<span class='smaller'>" + html + "</span>";
	}

	/**
	 * @param address
	 * @return The address, formatted.
	 */
	public static String formatAddress(String address) {
		int i = address.indexOf(',');
		if (i == -1)
			return address;
		return address.substring(0, i)
				+ smallerFont(address.substring(i, address.length()));
	}

	/**
	 * @param startAddress
	 * @param endAddress
	 * @return The start and end address, formatted.
	 */
	public static String[] formatAddresses(String startAddress,
			String endAddress) {
		int i1 = startAddress.length() - 1;
		int j1 = endAddress.length() - 1;
		while (i1 > 0 && j1 > 0
				&& startAddress.charAt(i1) == endAddress.charAt(j1)) {
			i1--;
			j1--;
		}
		i1++;
		j1++;
		while (i1 < startAddress.length() - 1 && j1 < endAddress.length() - 1
				&& startAddress.charAt(i1) != ',') {
			i1++;
			j1++;
		}
		int i2 = startAddress.indexOf(',');
		if (i2 == -1)
			i2 = startAddress.length();
		if (i2 > i1)
			i2 = i1;
		int j2 = endAddress.indexOf(',');
		if (j2 == -1)
			j2 = endAddress.length();
		if (j2 > j1)
			j2 = j1;
		return new String[] {
				startAddress.substring(0, i2)
						+ (i2 < i1 ? smallerFont(startAddress.substring(i2, i1))
								: ""),
				endAddress.substring(0, j2)
						+ (j2 < endAddress.length() ? smallerFont(endAddress
								.substring(j2, endAddress.length())) : "") };
	}

	/**
	 * @param distanceMeters
	 * @return A nicely formatted distance (eg: 1,3km, 850m, 75m...)
	 */
	public static String formatDistance(long distanceMeters) {
		long distanceKilometers = distanceMeters / 1000L;
		distanceMeters = distanceMeters % 1000L;
		if (distanceKilometers == 0L) {
			// Less than 1km: display number of meters
			if (distanceMeters > 100)
				// 100-1000m : approx. to 50m (~850m)
				distanceMeters = (distanceMeters + 25L) / 50L * 50L;
			else if (distanceMeters > 10)
				// 10-100m : approx. to 5m (~75m)
				distanceMeters = (distanceMeters + 2L) / 5L * 5L;
			return "~" + distanceMeters + smallerFont("m");
		} else {
			// More than 1km: display number of km with 1 decimal (2,7km)
			return "~" + distanceKilometers
					+ smallerFont("," + (distanceMeters / 100) + "km");
		}
	}

	/**
	 * Return a css class name (icon) for a transport mode.
	 * 
	 * @param transportMode
	 * @return
	 */
	public static String getCssClassNameFromTransportMode(
			TransportMode transportMode) {
		switch (transportMode) {
		default:
		case WALK:
			return "walk";
		case CAR:
			return "car";
		case BICYCLE:
			return "bicycle";
		case BICYCLE_RENTAL:
			return "bicycle-rental";
		case BUS:
			return "bus";
		case FERRY:
			return "ferry";
		case SUBWAY:
			return "subway";
		case GONDOLA:
			return "gondola";
		case RAIL:
			return "rail";
		case PLANE:
			return "plane";
		case TRAM:
			return "tram";
		case TROLLEY:
			return "trolley";
		}
	}
}

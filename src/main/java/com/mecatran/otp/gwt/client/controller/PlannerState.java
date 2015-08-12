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
package com.mecatran.otp.gwt.client.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.mecatran.otp.gwt.client.model.LocationBean;
import com.mecatran.otp.gwt.client.model.PlanRequestBean;
import com.mecatran.otp.gwt.client.model.TransportMode;
import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;

/**
 * This class encapsulate a specific state of the planner.
 * 
 */
public class PlannerState {

	private PlanRequestBean planRequestBean;
	private String url;

	/**
	 * Create a state based on URL parameters.
	 * 
	 * @param parameters
	 */
	public PlannerState(Map<String, List<String>> parameters) {
		planRequestBean = new PlanRequestBean();
		LocationBean origin = getLocationBean(parameters, "origin");
		if (origin != null)
			planRequestBean.setDeparture(origin);
		LocationBean destination = getLocationBean(parameters, "destination");
		if (destination != null)
			planRequestBean.setArrival(destination);
		Date depart = getDate(parameters, "depart");
		if (depart != null) {
			planRequestBean.setDate(depart);
			planRequestBean.setDateDeparture(true);
		}
		Date arrive = getDate(parameters, "arrive");
		if (arrive != null) {
			planRequestBean.setDate(arrive);
			planRequestBean.setDateDeparture(false);
		}
		Set<TransportMode> transportModes = getModes(parameters, "modes");
		if (transportModes != null)
			planRequestBean.setModes(transportModes);
		Boolean wheelchairAccessible = getBoolean(parameters, "wheelchair");
		if (wheelchairAccessible != null)
			planRequestBean.setWheelchairAccessible(wheelchairAccessible);
	}

	public PlannerState(PlanRequestBean planRequestBean) {
		this.planRequestBean = planRequestBean;
		buildUrl();
	}

	private String getString(Map<String, List<String>> parameters, String key) {
		List<String> value = parameters.get(key);
		if (value == null)
			return null;
		if (value.size() == 0)
			return null;
		return value.get(0);
	}

	private LocationBean getLocationBean(Map<String, List<String>> parameters,
			String key) {
		String value = getString(parameters, key);
		if (value == null)
			return null;
		LocationBean retval = new LocationBean();
		retval.setAddress(value);
		if (value.contains(",")) {
			String[] coordinates = value.split(",");
			if (coordinates.length < 2)
				return retval;
			try {
				double lat = Double.parseDouble(coordinates[0]);
				double lon = Double.parseDouble(coordinates[1]);
				retval.setLocation(new Wgs84LatLonBean(lat, lon));
				retval.setAddress(null);
				return retval;
			} catch (NumberFormatException e) {
				return retval;
			}
		} else {
			return retval;
		}
	}

	@SuppressWarnings("deprecation")
	private Date getDate(Map<String, List<String>> parameters, String key) {
		String value = getString(parameters, key);
		if (value == null)
			return null;
		final String[] DATE_TIME_FORMATS = new String[] {
				"yyyy/MM/dd@HH:mm:ss", "yyyy/MM/dd@HH:mm" };
		final String[] TIME_FORMATS = new String[] { "HH:mm:ss", "HH:mm" };
		for (String format : DATE_TIME_FORMATS) {
			DateTimeFormat dtf = DateTimeFormat.getFormat(format);
			try {
				return dtf.parse(value);
			} catch (IllegalArgumentException e) {
				// Try next format
			}
		}
		for (String format : TIME_FORMATS) {
			DateTimeFormat tf = DateTimeFormat.getFormat(format);
			try {
				Date today = new Date();
				Date retval = tf.parse(value);
				// GWT has not GregorianCalendar classes...
				retval.setDate(today.getDate());
				retval.setMonth(today.getMonth());
				retval.setYear(today.getYear());
				return retval;
			} catch (IllegalArgumentException e) {
				// Try next format
			}
		}
		return null;
	}

	private Boolean getBoolean(Map<String, List<String>> parameters, String key) {
		String value = getString(parameters, key);
		if (value == null)
			return null;
		return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes")
				|| value.equalsIgnoreCase("y");
	}

	private Set<TransportMode> getModes(Map<String, List<String>> parameters,
			String key) {
		String value = getString(parameters, key);
		if (value == null)
			return null;
		String[] values = value.split(",");
		Set<TransportMode> retval = new HashSet<TransportMode>(values.length);
		for (String val : values) {
			val = val.toUpperCase();
			// WALK, CAR, BICYCLE, BUS, TRAM, SUBWAY, RAIL, PLANE, FERRY,
			// TROLLEY, GONDOLA
			if (val.equals("TRANSIT")) {
				retval.add(TransportMode.BUS);
				retval.add(TransportMode.TRAM);
				retval.add(TransportMode.SUBWAY);
				retval.add(TransportMode.RAIL);
				retval.add(TransportMode.FERRY);
				retval.add(TransportMode.TROLLEY);
				retval.add(TransportMode.GONDOLA);
			} else {
				try {
					TransportMode mode = TransportMode.valueOf(val);
					retval.add(mode);
				} catch (IllegalArgumentException e) {
					// Ignore the value
				}
			}
		}
		if (retval.isEmpty())
			retval = null;
		return retval;
	}

	private void buildUrl() {
		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
		PlanRequestBean planRequest = getPlanRequestBean();
		if (planRequest.getDeparture().isSet(false))
			urlBuilder.setParameter("origin",
					getLocationAsStringParam(planRequest.getDeparture()));
		if (planRequest.getArrival().isSet(false))
			urlBuilder.setParameter("destination",
					getLocationAsStringParam(planRequest.getArrival()));
		DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy/MM/dd@HH:mm");
		urlBuilder.setParameter(planRequest.isDateDeparture() ? "depart"
				: "arrive", dtf.format(planRequest.getDate()));
		if (planRequest.isWheelchairAccessible())
			urlBuilder.setParameter("wheelchair", "y");
		Set<TransportMode> modes = planRequest.getModes();
		String modesStr = "";
		for (TransportMode mode : modes) {
			modesStr = modesStr + mode.toString() + ",";
		}
		modesStr = modesStr.substring(0, modesStr.length() - 1);
		urlBuilder.setParameter("modes", modesStr);
		url = urlBuilder.buildString();
	}

	private String getLocationAsStringParam(LocationBean location) {
		if (location.getLocation() != null)
			return location.getLocation().getLat() + ","
					+ location.getLocation().getLon() + ","
					+ location.getAddress();
		else
			return ",," + location.getAddress();
	}

	public PlanRequestBean getPlanRequestBean() {
		return planRequestBean;
	}

	public String getUrl() {
		return url;
	}
}

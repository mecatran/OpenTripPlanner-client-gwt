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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ItineraryBean {

	private PlanRequestBean request;
	private List<ItineraryLegBean> legs;
	private Wgs84BoundsBean bounds;
	private long distanceMeters;
	private long durationSeconds;
	private Date arrivalTime;
	private Date departureTime;
	private Wgs84LatLonBean startLocation;
	private Wgs84LatLonBean endLocation;
	private String startAddress;
	private String endAddress;
	private String copyrights;
	private Object nativeData;
	private List<AlertBean> alerts;

	public ItineraryBean() {
		legs = new ArrayList<ItineraryLegBean>();
	}

	public void addLeg(ItineraryLegBean leg) {
		legs.add(leg);
	}

	public PlanRequestBean getRequest() {
		return request;
	}

	public void setRequest(PlanRequestBean request) {
		this.request = request;
	}

	public Wgs84BoundsBean getBounds() {
		return bounds;
	}

	public void setBounds(Wgs84BoundsBean bounds) {
		this.bounds = bounds;
	}

	public List<ItineraryLegBean> getLegs() {
		return legs;
	}

	public long getDistanceMeters() {
		return distanceMeters;
	}

	public void setDistanceMeters(long distanceMeters) {
		this.distanceMeters = distanceMeters;
	}

	public long getDurationSeconds() {
		return durationSeconds;
	}

	public void setDurationSeconds(long durationSeconds) {
		this.durationSeconds = durationSeconds;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public Wgs84LatLonBean getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(Wgs84LatLonBean startLocation) {
		this.startLocation = startLocation;
	}

	public Wgs84LatLonBean getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(Wgs84LatLonBean endLocation) {
		this.endLocation = endLocation;
	}

	public String getStartAddress() {
		return startAddress;
	}

	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}

	public String getEndAddress() {
		return endAddress;
	}

	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}

	public String getCopyrights() {
		return copyrights;
	}

	public void setCopyrights(String copyrights) {
		this.copyrights = copyrights;
	}

	public Object getNativeData() {
		return nativeData;
	}

	public void setNativeData(Object nativeData) {
		this.nativeData = nativeData;
	}

	public List<AlertBean> getAlerts() {
		if (alerts == null)
			alerts = Collections.emptyList();
		return alerts;
	}

	public void setAlerts(List<AlertBean> alerts) {
		this.alerts = alerts;
	}
}

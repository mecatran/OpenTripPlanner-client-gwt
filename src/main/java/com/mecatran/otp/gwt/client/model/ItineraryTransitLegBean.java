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

import java.util.Date;

import com.google.gwt.i18n.client.TimeZone;

public class ItineraryTransitLegBean extends ItineraryLegBean {

	private TransitStopBean departureStop;
	private TransitStopBean arrivalStop;
	private Date departureTime;
	private Date arrivalTime;
	private long waitDurationSeconds;
	private TimeZone departureTimezone;
	private TimeZone arrivalTimezone;
	private String headsign;
	private TransitRouteBean route;

	public TransitStopBean getDepartureStop() {
		return departureStop;
	}

	public void setDepartureStop(TransitStopBean departureStop) {
		this.departureStop = departureStop;
	}

	public TransitStopBean getArrivalStop() {
		return arrivalStop;
	}

	public void setArrivalStop(TransitStopBean arrivalStop) {
		this.arrivalStop = arrivalStop;
	}

	public TimeZone getDepartureTimezone() {
		return departureTimezone;
	}

	public void setDepartureTimezone(TimeZone departureTimezone) {
		this.departureTimezone = departureTimezone;
	}

	public TimeZone getArrivalTimezone() {
		return arrivalTimezone;
	}

	public void setArrivalTimezone(TimeZone arrivalTimezone) {
		this.arrivalTimezone = arrivalTimezone;
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
	
	public long getWaitDurationSeconds() {
		return waitDurationSeconds;
	}
	
	public void setWaitDurationSeconds(long waitDurationSeconds) {
		this.waitDurationSeconds = waitDurationSeconds;
	}

	public String getHeadsign() {
		return headsign;
	}

	public void setHeadsign(String headsign) {
		this.headsign = headsign;
	}

	public TransitRouteBean getRoute() {
		return route;
	}

	public void setRoute(TransitRouteBean route) {
		this.route = route;
	}

	@Override
	public TravelType getTravelType() {
		return TravelType.TRANSIT;
	}
	
	@Override
	public ItineraryTransitLegBean getAsTransitLeg() {
		return this;
	}
}

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
import java.util.Set;

public class PlanRequestBean {

	private LocationBean departure;
	private LocationBean arrival;
	private Date date;
	private boolean isDateDeparture;
	private Set<TransportMode> modes;
	private int maxWalkDistanceMeters;
	private float walkReluctanceFactor;
	private int transferPenaltySeconds;
	private boolean wheelchairAccessible;
	private float bikeSafetyFactor;
	private float bikeSpeedFactor;
	private float bikeComfortFactor;
	private float walkSpeedKph;
	private float bikeSpeedKph;

	public PlanRequestBean() {
	}

	public LocationBean getDeparture() {
		return departure;
	}

	public void setDeparture(LocationBean departure) {
		this.departure = departure;
	}

	public LocationBean getArrival() {
		return arrival;
	}

	public void setArrival(LocationBean arrival) {
		this.arrival = arrival;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isDateDeparture() {
		return isDateDeparture;
	}

	public void setDateDeparture(boolean isDateDeparture) {
		this.isDateDeparture = isDateDeparture;
	}

	public Set<TransportMode> getModes() {
		return modes;
	}

	public void setModes(Set<TransportMode> modes) {
		this.modes = modes;
	}

	public int getMaxWalkDistanceMeters() {
		return maxWalkDistanceMeters;
	}

	public void setMaxWalkDistanceMeters(int maxWalkDistanceMeters) {
		this.maxWalkDistanceMeters = maxWalkDistanceMeters;
	}

	public float getWalkReluctanceFactor() {
		return walkReluctanceFactor;
	}

	public void setWalkReluctanceFactor(float walkReluctanceFactor) {
		this.walkReluctanceFactor = walkReluctanceFactor;
	}

	public int getTransferPenaltySeconds() {
		return transferPenaltySeconds;
	}

	public void setTransferPenaltySeconds(int transferPenaltySeconds) {
		this.transferPenaltySeconds = transferPenaltySeconds;
	}

	public boolean isWheelchairAccessible() {
		return wheelchairAccessible;
	}

	public void setWheelchairAccessible(boolean wheelchairAccessible) {
		this.wheelchairAccessible = wheelchairAccessible;
	}

	public float getBikeSafetyFactor() {
		return bikeSafetyFactor;
	}

	public void setBikeSafetyFactor(float bikeSafetyFactor) {
		this.bikeSafetyFactor = bikeSafetyFactor;
	}

	public float getBikeSpeedFactor() {
		return bikeSpeedFactor;
	}

	public void setBikeSpeedFactor(float bikeSpeedFactor) {
		this.bikeSpeedFactor = bikeSpeedFactor;
	}

	public float getBikeComfortFactor() {
		return bikeComfortFactor;
	}

	public void setBikeComfortFactor(float bikeComfortFactor) {
		this.bikeComfortFactor = bikeComfortFactor;
	}

	public float getWalkSpeedKph() {
		return walkSpeedKph;
	}

	public void setWalkSpeedKph(float walkSpeedKph) {
		this.walkSpeedKph = walkSpeedKph;
	}

	public float getBikeSpeedKph() {
		return bikeSpeedKph;
	}

	public void setBikeSpeedKph(float bikeSpeedKph) {
		this.bikeSpeedKph = bikeSpeedKph;
	}

}

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

public class ItineraryRoadStepBean {

	private String instructions;
	private long distanceMeters;
	private long durationSeconds;
	private RelativeDirection relativeDirection;
	private AbsoluteDirection absoluteDirection;
	private Wgs84LatLonBean startLocation;
	private Wgs84LatLonBean endLocation;

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
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

	public RelativeDirection getRelativeDirection() {
		return relativeDirection;
	}

	public void setRelativeDirection(RelativeDirection relativeDirection) {
		this.relativeDirection = relativeDirection;
	}

	public AbsoluteDirection getAbsoluteDirection() {
		return absoluteDirection;
	}

	public void setAbsoluteDirection(AbsoluteDirection absoluteDirection) {
		this.absoluteDirection = absoluteDirection;
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
}

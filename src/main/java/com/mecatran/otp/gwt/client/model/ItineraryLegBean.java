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

public abstract class ItineraryLegBean {

	private String instructions;
	private String customHtmlDetails;
	private long distanceMeters;
	private long durationSeconds;
	private Wgs84LatLonBean startLocation;
	private Wgs84LatLonBean endLocation;
	private Wgs84LatLonBean[] path;
	private TransportMode mode;

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public String getCustomHtmlDetails() {
		return customHtmlDetails;
	}

	public void setCustomHtmlDetails(String customHtmlDetails) {
		this.customHtmlDetails = customHtmlDetails;
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

	public Wgs84LatLonBean[] getPath() {
		return path;
	}

	public void setPath(Wgs84LatLonBean[] path) {
		this.path = path;
	}

	public abstract TravelType getTravelType();

	public TransportMode getMode() {
		return mode;
	}

	public void setMode(TransportMode mode) {
		this.mode = mode;
	}

	public ItineraryTransitLegBean getAsTransitLeg() {
		return null;
	}

	public ItineraryRoadLegBean getAsRoadLeg() {
		return null;
	}
}

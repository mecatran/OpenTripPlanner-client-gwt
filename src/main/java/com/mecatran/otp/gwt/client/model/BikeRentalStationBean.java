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

public class BikeRentalStationBean extends POIBean {

	private Integer totalCapacity;
	private Integer bikesAvailable;
	private Integer slotsAvailable;

	public BikeRentalStationBean() {
		setType(POIType.TRANSPORT_BIKE_RENTAL_STATION);
	}

	public Integer getTotalCapacity() {
		return totalCapacity;
	}

	public void setTotalCapacity(Integer totalCapacity) {
		this.totalCapacity = totalCapacity;
	}

	public Integer getBikesAvailable() {
		return bikesAvailable;
	}

	public void setBikesAvailable(Integer bikesAvailable) {
		this.bikesAvailable = bikesAvailable;
	}

	public Integer getSlotsAvailable() {
		return slotsAvailable;
	}

	public void setSlotsAvailable(Integer slotsAvailable) {
		this.slotsAvailable = slotsAvailable;
	}

	@Override
	public String getHtmlDescription() {
		String s = "<h3>" + getName() + "</h3>";
		// TODO i18n
		if (totalCapacity != null)
			s = s + "<p><b>Total capacity</b>: " + totalCapacity + "</p>";
		/*
		 * TODO OTP always return slots/bikes availability, even when bogus
		 * (static OSM node).
		 */
		// if (slotsAvailable != null)
		// s = s + "<p><b>Free slots</b>: " + slotsAvailable + "</p>";
		// if (bikesAvailable != null)
		// s = s + "<p><b>Bikes availables</b>: " + bikesAvailable + "</p>";
		return s;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof BikeRentalStationBean))
			return false;
		return getId().equals(((BikeRentalStationBean) other).getId());
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}
}

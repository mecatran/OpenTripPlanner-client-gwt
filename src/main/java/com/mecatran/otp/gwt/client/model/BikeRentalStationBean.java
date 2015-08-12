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

import com.mecatran.otp.gwt.client.i18n.I18nUtils;

public class BikeRentalStationBean extends POIBean {

	private Integer totalCapacity;
	private Integer bikesAvailable;
	private Integer slotsAvailable;
	private boolean hasRealTime;

	public BikeRentalStationBean() {
		setType(POIType.TRANSPORT_BIKE_RENTAL_STATION);
	}

	public Integer getTotalCapacity() {
		return totalCapacity;
	}

	public void setTotalCapacity(Integer totalCapacity) {
		this.totalCapacity = totalCapacity;
	}

	public void setHasRealTime(boolean hasRealTime) {
		this.hasRealTime = hasRealTime;
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
		if (totalCapacity != null)
			s = s + "<p><b>" + I18nUtils.tr("bikeshare.capacity") + "</b>: "
					+ totalCapacity + "</p>";
		if (hasRealTime && bikesAvailable != null)
			s = s + "<p><b>" + I18nUtils.tr("bikeshare.bikes.avail") + "</b>: "
					+ bikesAvailable + "</p>";
		if (hasRealTime && slotsAvailable != null)
			s = s + "<p><b>" + I18nUtils.tr("bikeshare.slots.avail") + "</b>: "
					+ slotsAvailable + "</p>";
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

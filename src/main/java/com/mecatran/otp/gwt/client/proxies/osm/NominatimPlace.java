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
package com.mecatran.otp.gwt.client.proxies.osm;

import com.google.gwt.core.client.JavaScriptObject;

public class NominatimPlace extends JavaScriptObject {

	protected NominatimPlace() {
	}

	protected final native String getError()
	/*-{
		return this.error;
	}-*/;

	protected final native String getPlaceId()
	/*-{
		return this.place_id;
	}-*/;

	protected final native double getLatitude()
	/*-{
		return this.lat;
	}-*/;

	protected final native double getLongitude()
	/*-{
		return this.lon;
	}-*/;

	protected final native String getDisplayName()
	/*-{
		return this.display_name;
	}-*/;

	protected final native String getHouseNumber()
	/*-{
		return this.address.house_number;
	}-*/;

	protected final native String getRoad()
	/*-{
		return this.address.road;
	}-*/;

	protected final native String getSuburb()
	/*-{
		return this.address.suburb;
	}-*/;

	protected final native String getCity()
	/*-{
		return this.address.city;
	}-*/;

	protected final native String getTown()
	/*-{
		return this.address.town;
	}-*/;

	protected final native String getVillage()
	/*-{
		return this.address.village;
	}-*/;

	protected final native String getHamlet()
	/*-{
		return this.address.hamlet;
	}-*/;

	protected final native String getDwelling()
	/*-{
		return this.address.isolated_dwelling;
	}-*/;

	protected final native String getCountry()
	/*-{
		return this.address.country;
	}-*/;

	protected final native String getPostCode()
	/*-{
		return this.address.postcode;
	}-*/;

	protected final String getFormattedAddress() {
		/*
		 * TODO Add the place name if amenity. (Note: this is rather hard, as
		 * the field name depends on the amenity type: "hospital", "cinema",
		 * etc..)
		 */
		StringBuffer sb = new StringBuffer();
		if (getHouseNumber() != null)
			sb.append(getHouseNumber()).append(", ");
		if (getRoad() != null)
			sb.append(getRoad()).append(", ");
		if (getPostCode() != null) {
			// In case we have several post codes, take the first one only
			String[] postCodes = getPostCode().split(";");
			sb.append(postCodes[0]).append(" ");
		}
		if (getCity() != null) {
			sb.append(getCity());
		} else if (getTown() != null) {
			sb.append(getTown());
		} else if (getVillage() != null) {
			sb.append(getVillage());
		} else if (getHamlet() != null) {
			sb.append(getHamlet());
		} else if (getDwelling() != null) {
			sb.append(getDwelling());
		}
		if (getSuburb() != null) {
			sb.append(" (").append(getSuburb()).append(")");
		}
		return sb.toString();
	}

}

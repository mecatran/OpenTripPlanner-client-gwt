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

public class POIBean {

	private String id;
	private String name;
	private POIType type;
	private Wgs84LatLonBean location;
	private String htmlDescription;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public POIType getType() {
		return type;
	}

	public void setType(POIType type) {
		this.type = type;
	}

	public Wgs84LatLonBean getLocation() {
		return location;
	}

	public void setLocation(Wgs84LatLonBean location) {
		this.location = location;
	}

	public String getHtmlDescription() {
		return htmlDescription;
	}

	public void setHtmlDescription(String htmlDescription) {
		this.htmlDescription = htmlDescription;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof POIBean))
			return false;
		return getId().equals(((POIBean) other).getId());
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}
}

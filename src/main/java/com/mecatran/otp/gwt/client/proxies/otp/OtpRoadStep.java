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
package com.mecatran.otp.gwt.client.proxies.otp;

import com.google.gwt.core.client.JavaScriptObject;

public class OtpRoadStep extends JavaScriptObject {

	protected OtpRoadStep() {
	}

	protected final native String getStreetName()
	/*-{
		return this.streetName;
	}-*/;

	protected final native double getDistance()
	/*-{
		return this.distance;
	}-*/;

	public final native String getRelativeDirection()
	/*-{
		return this.relativeDirection;
	}-*/;

	public final native String getAbsoluteDirection()
	/*-{
		return this.absoluteDirection;
	}-*/;

	public final native boolean isStayOn()
	/*-{
		return this.stayOn;
	}-*/;

	public final native boolean isGeneratedName()
	/*-{
		return this.bogusName;
	}-*/;

	public final native double getLongitude()
	/*-{
		return this.lon;
	}-*/;

	public final native double getLatitude()
	/*-{
		return this.lat;
	}-*/;
}

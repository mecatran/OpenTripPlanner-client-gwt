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
import com.google.gwt.core.client.JsArrayString;

public class OtpRouterConf extends JavaScriptObject {

	protected OtpRouterConf() {
	}

	protected final native double getRouterId()
	/*-{
		return this.routerId;
	}-*/;

	protected final native double getBuildTime()
	/*-{
		return this.buildTime;
	}-*/;

	protected final native JsArrayString getTransitModes()
	/*-{
		return this.transitModes;
	}-*/;

	protected final native double getLowerLeftLatitude()
	/*-{
		return this.lowerLeftLatitude;
	}-*/;

	protected final native double getLowerLeftLongitude()
	/*-{
		return this.lowerLeftLongitude;
	}-*/;

	protected final native double getUpperRightLatitude()
	/*-{
		return this.upperRightLatitude;
	}-*/;

	protected final native double getUpperRightLongitude()
	/*-{
		return this.upperRightLongitude;
	}-*/;
}

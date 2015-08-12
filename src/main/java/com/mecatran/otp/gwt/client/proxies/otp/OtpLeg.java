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
import com.google.gwt.core.client.JsArray;

public class OtpLeg extends JavaScriptObject {

	protected OtpLeg() {
	}

	protected final boolean isRoad() {
		return !isTransit();
	}

	protected final native boolean isTransit()
	/*-{
		return this.transitLeg;
	}-*/;

	protected final native String getMode()
	/*-{
		return this.mode;
	}-*/;

	protected final native double getStartTime()
	/*-{
		return this.startTime;
	}-*/;

	protected final native double getEndTime()
	/*-{
		return this.endTime;
	}-*/;

	protected final native OtpPlace getFrom()
	/*-{
		return this.from;
	}-*/;

	protected final native OtpPlace getTo()
	/*-{
		return this.to;
	}-*/;

	protected final native double getDistance()
	/*-{
		return this.distance;
	}-*/;

	protected final native String getEncodedGeometry()
	/*-{
		return this.legGeometry ? this.legGeometry.points : null;
	}-*/;

	protected final native JsArray<OtpRoadStep> getSteps()
	/*-{
		return this.steps;
	}-*/;

	protected final native String getRouteId()
	/*-{
		return this.routeId;
	}-*/;

	protected final native String getRouteShortName()
	/*-{
		return this.routeShortName;
	}-*/;

	protected final native String getRouteLongName()
	/*-{
		return this.routeLongName;
	}-*/;

	protected final native String getRouteColor()
	/*-{
		return this.routeColor;
	}-*/;

	protected final native String getRouteTextColor()
	/*-{
		return this.routeTextColor;
	}-*/;

	protected final native int getRouteType()
	/*-{
		return this.routeType;
	}-*/;

	protected final native String getTripId()
	/*-{
		return this.tripId;
	}-*/;

	protected final native String getHeadsign()
	/*-{
		return this.headsign;
	}-*/;

	protected final native String getAgencyId()
	/*-{
		return this.agencyId;
	}-*/;

	protected final native String getAgencyName()
	/*-{
		return this.agencyName;
	}-*/;

	protected final native String getAgencyUrl()
	/*-{
		return this.agencyUrl;
	}-*/;
}

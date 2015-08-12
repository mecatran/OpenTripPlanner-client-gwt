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

public class OtpItinerary extends JavaScriptObject {

	protected OtpItinerary() {
	}

	protected final native double getStartTime()
	/*-{
		return this.startTime;
	}-*/;

	protected final native double getEndTime()
	/*-{
		return this.endTime;
	}-*/;

	protected final native JsArray<OtpLeg> getLegs()
	/*-{
		return this.legs;
	}-*/;

	protected final native int getDuration()
	/*-{
		return this.duration;
	}-*/;

	protected final native int getTransitTime()
	/*-{
		return this.transitTime;
	}-*/;

	protected final native int getWalkTime()
	/*-{
		return this.walkTime;
	}-*/;

	protected final native String getWaitingTime()
	/*-{
		return this.waitingTime;
	}-*/;

	protected final native int getTransfers()
	/*-{
		return this.transfers;
	}-*/;
}

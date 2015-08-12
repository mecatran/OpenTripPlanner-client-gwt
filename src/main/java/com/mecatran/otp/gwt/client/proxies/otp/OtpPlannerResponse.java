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

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.mecatran.otp.gwt.client.model.TransportMode;

public class OtpPlannerResponse extends JavaScriptObject {

	protected OtpPlannerResponse() {
	}

	public final Date getCreationTime() {
		return new Date();
	}

	public final native OtpError getError()
	/*-{
		return this.error || null;
	}-*/;

	public final native JsArray<OtpItinerary> getItineraries()
	/*-{
		return this.plan.itineraries;
	}-*/;

	protected static final TransportMode convertMode(boolean hasBikeRental,
			String transportMode) {
		if (transportMode.equals("WALK"))
			return TransportMode.WALK;
		if (transportMode.equals("BICYCLE"))
			return hasBikeRental ? TransportMode.BICYCLE_RENTAL
					: TransportMode.BICYCLE;
		if (transportMode.equals("CAR"))
			return TransportMode.CAR;
		if (transportMode.equals("BUS"))
			return TransportMode.BUS;
		if (transportMode.equals("RAIL"))
			return TransportMode.RAIL;
		if (transportMode.equals("TRAM"))
			return TransportMode.TRAM;
		if (transportMode.equals("SUBWAY"))
			return TransportMode.SUBWAY;
		if (transportMode.equals("FERRY"))
			return TransportMode.FERRY;
		if (transportMode.equals("PLANE")) // TODO
			return TransportMode.PLANE;
		if (transportMode.equals("CABLE_CAR"))
			return TransportMode.TROLLEY;
		if (transportMode.equals("GONDOLA"))
			return TransportMode.GONDOLA; // Can also be FUNICULAR...
		return TransportMode.BUS;
	}

}

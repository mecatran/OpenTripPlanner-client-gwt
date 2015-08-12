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
package com.mecatran.otp.gwt.client;

import com.google.gwt.core.client.JavaScriptObject;

public class PlannerWidgetConfig extends JavaScriptObject {

	/*
	 * Do *NOT* change the constants below or you will break client code
	 * configuration!
	 */
	public static final String PROXY_OTP = "otp";

	protected PlannerWidgetConfig() {
	}

	/**
	 * @return The DOM ID of the div which will contain the widget on the host
	 *         page. Default to "opentripplanner_widget".
	 */
	public final native String getWidgetDivId()
	/*-{
		return this.widgetDivId || "opentripplanner_widget";
	}-*/;

	/**
	 * @return The lang ISO code. Default to "en". Only supported for now are
	 *         "en" and "fr". See OpenTripPlannerClientMessages for
	 *         translations.
	 */
	public final native String getLang()
	/*-{
		return this.lang || "en";
	}-*/;

	/**
	 * @return The name of the country, which will be stripped if present at the
	 *         end of an address (geocoded or leg instruction). Optional.
	 */
	public final native String getMainCountryName()
	/*-{
		return this.mainCountryName || null;
	}-*/;

	public final native double getMinLat()
	/*-{
		return this.minLat || NaN;
	}-*/;

	public final native double getMinLon()
	/*-{
		return this.minLon || NaN;
	}-*/;

	public final native double getMaxLat()
	/*-{
		return this.maxLat || NaN;
	}-*/;

	public final native double getMaxLon()
	/*-{
		return this.maxLon || NaN;
	}-*/;

	/**
	 * @return The type of the planner proxy to use. Default to "otp".
	 */
	public final native String getProxyType()
	/*-{
		return this.proxyType || "otp";
	}-*/;

	/**
	 * @return The ID of the router to use.
	 */
	public final native String getRouterId()
	/*-{
		return this.router || "default";
	}-*/;

	/**
	 * @return The base URL of the OTP server. Default to
	 *         "//localhost:8080/otp".
	 */
	public final native String getOtpUrl()
	/*-{
		return this.otpUrl || "//localhost:8080/otp";
	}-*/;

	public final native boolean isHasTransit()
	/*-{
		return this.hasTransit || true;
	}-*/;

	public final native boolean isHasWalkOnly()
	/*-{
		return this.hasWalkOnly || false; 
	}-*/;

	public final native boolean isHasBikeOnly()
	/*-{
		return this.hasBikeOnly || false;
	}-*/;

	public final native boolean isHasBikeRental()
	/*-{
		return this.hasBikeRental || false;
	}-*/;

	public final native boolean isHasBikeAndTransit()
	/*-{
		return this.hasBikeAndTransit || false;
	}-*/;

	public final native String getIntroMessage()
	/*-{
		return this.introMessage || null;
	}-*/;

	public final native int getMaxItineraries()
	/*-{
		return this.maxItineraries || 3;
	}-*/;
}

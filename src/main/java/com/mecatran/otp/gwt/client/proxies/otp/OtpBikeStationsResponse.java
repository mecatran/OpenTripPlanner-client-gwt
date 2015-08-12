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

public class OtpBikeStationsResponse extends JavaScriptObject {

	protected static class OtpBikeStation extends JavaScriptObject {

		protected OtpBikeStation() {
		}

		protected final native String getId()
		/*-{
			return this.id;
		}-*/;

		protected final native String getName()
		/*-{
			return this.name;
		}-*/;

		protected final native double getLatitude()
		/*-{
			return this.y;
		}-*/;

		protected final native double getLongitude()
		/*-{
			return this.x;
		}-*/;

		protected final native int getBikesAvailable()
		/*-{
			return this.bikesAvailable;
		}-*/;

		protected final native int getSpacesAvailable()
		/*-{
			return this.spacesAvailable;
		}-*/;

		protected final native boolean isAllowDropoff()
		/*-{
			return this.allowDropoff;
		}-*/;

		protected final native boolean isRealTimeData()
		/*-{
			return this.realTimeData;
		}-*/;
	}

	protected OtpBikeStationsResponse() {
	}

	protected final native JsArray<OtpBikeStation> getStations()
	/*-{
		return this.stations;
	}-*/;

}

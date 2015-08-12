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
package com.mecatran.otp.gwt.client.proxies;

import java.util.List;

import com.mecatran.otp.gwt.client.model.LocationBean;
import com.mecatran.otp.gwt.client.model.Wgs84BoundsBean;
import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;

public interface GeocoderProxy {

	public interface GeocoderListener {

		/* Return null in case no geocoding available. Do we need an error callback? */
		public void onGeocodingDone(List<LocationBean> locations);
	}

	public interface ReverseGeocoderListener {

		public void onReverseGeocodingDone(LocationBean location, int factor);
	}

	public void configure(Wgs84BoundsBean bounds, String mainCountryName);

	public void geocode(String address, GeocoderListener listener);

	public void reverseGeocode(Wgs84LatLonBean location,
			ReverseGeocoderListener listener);

}

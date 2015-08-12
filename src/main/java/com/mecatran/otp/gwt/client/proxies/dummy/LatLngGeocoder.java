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
package com.mecatran.otp.gwt.client.proxies.dummy;

import java.util.ArrayList;
import java.util.List;

import com.mecatran.otp.gwt.client.model.LocationBean;
import com.mecatran.otp.gwt.client.model.Wgs84BoundsBean;
import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;
import com.mecatran.otp.gwt.client.proxies.GeocoderProxy;

/**
 * A ultra-simple geocoder that transform back and forth a lat,lon numerical
 * coordinates.
 */
public class LatLngGeocoder implements GeocoderProxy {

	@Override
	public void configure(Wgs84BoundsBean bounds, String mainCountryName) {
		// Not really useful
	}

	@Override
	public void geocode(String address, final GeocoderListener listener) {
		try {
			String[] elements = address.split(",");
			if (elements.length >= 2) {
				double lat = Double.parseDouble(elements[0].trim());
				double lng = Double.parseDouble(elements[1].trim());
				List<LocationBean> locations = new ArrayList<>();
				LocationBean loc = new LocationBean();
				loc.setLocation(new Wgs84LatLonBean(lat, lng));
				loc.setAddress(lat + "," + lng);
				locations.add(loc);
				listener.onGeocodingDone(locations);
			} else {
				throw new IllegalArgumentException(
						"Invalid format, expected: 'latitude, longitude'");
			}
		} catch (Exception e) {
			listener.onGeocodingDone(null);
		}
	}

	@Override
	public void reverseGeocode(Wgs84LatLonBean location,
			ReverseGeocoderListener listener) {
		LocationBean loc = new LocationBean();
		loc.setLocation(location);
		loc.setAddress(location.getLat() + "," + location.getLon());
		listener.onReverseGeocodingDone(loc, 100);
	}

}

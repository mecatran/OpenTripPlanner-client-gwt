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

import java.util.ArrayList;
import java.util.List;

import com.mecatran.otp.gwt.client.model.LocationBean;
import com.mecatran.otp.gwt.client.model.Wgs84BoundsBean;
import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;

public class GeocoderMultiplexer implements GeocoderProxy {

	private class MultiplexedGeocoderRequest {
		private int n;
		private List<LocationBean> locations = new ArrayList<LocationBean>();
	}

	private class MultiplexedReverseGeocoderRequest {
		private int n;
		private int bestFactor;
		private LocationBean bestLocation;
	}

	private List<GeocoderProxy> subProxies;

	public GeocoderMultiplexer(GeocoderProxy... subProxies) {
		this.subProxies = new ArrayList<GeocoderProxy>(subProxies.length);
		for (GeocoderProxy subProxy : subProxies) {
			this.subProxies.add(subProxy);
		}
	}

	public void addGeocoder(GeocoderProxy subProxy) {
		subProxies.add(subProxy);
	}

	@Override
	public void configure(Wgs84BoundsBean bounds, String mainCountryName) {
		for (GeocoderProxy subProxy : subProxies) {
			subProxy.configure(bounds, mainCountryName);
		}
	}

	@Override
	public void geocode(String address, final GeocoderListener listener) {
		final MultiplexedGeocoderRequest mux = new MultiplexedGeocoderRequest();
		mux.n = subProxies.size();
		for (GeocoderProxy subProxy : subProxies) {
			subProxy.geocode(address, new GeocoderListener() {
				@Override
				public void onGeocodingDone(List<LocationBean> locations) {
					mux.n--;
					if (locations != null)
						mux.locations.addAll(locations);
					if (mux.n == 0) {
						listener.onGeocodingDone(mux.locations);
					}
				}
			});
		}
	}

	@Override
	public void reverseGeocode(Wgs84LatLonBean location,
			final ReverseGeocoderListener listener) {
		/*
		 * We need only one answer, so multiplexing has no sense: we return the
		 * best one only.
		 */
		final MultiplexedReverseGeocoderRequest mux = new MultiplexedReverseGeocoderRequest();
		mux.n = subProxies.size();
		mux.bestFactor = 0;
		mux.bestLocation = null;
		for (GeocoderProxy subProxy : subProxies) {
			subProxy.reverseGeocode(location, new ReverseGeocoderListener() {
				@Override
				public void onReverseGeocodingDone(LocationBean location,
						int factor) {
					mux.n--;
					if (location != null) {
						if (factor > mux.bestFactor) {
							mux.bestFactor = factor;
							mux.bestLocation = location;
						}
					}
					if (mux.n == 0) {
						listener.onReverseGeocodingDone(mux.bestLocation,
								mux.bestFactor);
					}
				}
			});
		}
	}

}

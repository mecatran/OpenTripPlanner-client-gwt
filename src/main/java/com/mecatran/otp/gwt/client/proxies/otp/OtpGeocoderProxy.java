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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JsArray;
import com.mecatran.otp.gwt.client.model.LocationBean;
import com.mecatran.otp.gwt.client.model.Wgs84BoundsBean;
import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;
import com.mecatran.otp.gwt.client.proxies.GeocoderProxy;
import com.mecatran.otp.gwt.client.utils.HttpUtils;
import com.mecatran.otp.gwt.client.utils.HttpUtils.DownloadListener;

public class OtpGeocoderProxy implements GeocoderProxy {

	private String baseUrl;
	private String routerId;

	public OtpGeocoderProxy(String baseUrl, String routerId) {
		this.baseUrl = baseUrl;
		this.routerId = routerId;
	}

	@Override
	public void configure(Wgs84BoundsBean bounds, String mainCountryName) {
		// Ignore bounds and country name as we rely on the graph data only
	}

	@Override
	public void geocode(String address, final GeocoderListener listener) {
		String url = buildQueryUrl(address);
		HttpUtils.downloadJson(url,
				new DownloadListener<JsArray<OtpGeocodingResult>>() {

					@Override
					public void onSuccess(JsArray<OtpGeocodingResult> results) {
						List<LocationBean> locations = new ArrayList<>(results
								.length());
						for (int i = 0; i < results.length(); i++) {
							OtpGeocodingResult result = results.get(i);
							LocationBean location = new LocationBean();
							location.setAddress(result.getDescription());
							location.setLocation(new Wgs84LatLonBean(result
									.getLatitude(), result.getLongitude()));
							locations.add(location);
						}
						listener.onGeocodingDone(locations);
					}

					@Override
					public void onFailure(String msg) {
						listener.onGeocodingDone(null);
					}
				});
	}

	@Override
	public void reverseGeocode(Wgs84LatLonBean location,
			ReverseGeocoderListener listener) {
		// There is no reverse geocoding available
		LocationBean addr = new LocationBean();
		addr.setLocation(location);
		addr.setAddress(location.getLat() + "," + location.getLon());
		listener.onReverseGeocodingDone(addr, 0);
	}

	private String buildQueryUrl(String query) {
		StringBuffer sb = new StringBuffer(baseUrl);
		sb.append("routers/").append(routerId).append("/geocode?query=")
				.append(query);
		return sb.toString();
	}
}

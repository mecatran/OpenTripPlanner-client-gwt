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
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.mecatran.otp.gwt.client.model.LocationBean;
import com.mecatran.otp.gwt.client.model.Wgs84BoundsBean;
import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;
import com.mecatran.otp.gwt.client.proxies.GeocoderProxy;

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
		try {
			String url = buildQueryUrl(address);
			RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
			builder.sendRequest(null, new RequestCallback() {
				public void onError(Request request, Throwable exception) {
					listener.onGeocodingDone(null);
				}

				public void onResponseReceived(Request request,
						Response response) {
					if (200 == response.getStatusCode()) {
						JsArray<OtpGeocodingResult> results = JsonUtils
								.<JsArray<OtpGeocodingResult>> safeEval(response
										.getText());
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
					} else {
						listener.onGeocodingDone(null);
					}
				}
			});
		} catch (RequestException e1) {
			listener.onGeocodingDone(null);
		}
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
		String retval = sb.toString();
		GWT.log("OTP geocode URL: " + retval);
		return retval;
	}
}

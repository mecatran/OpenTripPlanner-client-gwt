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
package com.mecatran.otp.gwt.client.proxies.osm;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JsArray;
import com.mecatran.otp.gwt.client.model.LocationBean;
import com.mecatran.otp.gwt.client.model.Wgs84BoundsBean;
import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;
import com.mecatran.otp.gwt.client.proxies.GeocoderProxy;
import com.mecatran.otp.gwt.client.utils.HttpUtils;
import com.mecatran.otp.gwt.client.utils.HttpUtils.DownloadListener;

public class NominatimGeocoderProxy implements GeocoderProxy {

	public static final String OSM_NOMINATIM_URL = "http://nominatim.openstreetmap.org";
	public static final String MAPQUEST_NOMINATIM_URL = "http://open.mapquestapi.com/nominatim/v1";

	private String baseUrl;
	private Wgs84BoundsBean bounds;
	private int limit;

	public NominatimGeocoderProxy(String baseUrl, int limit) {
		this.baseUrl = baseUrl;
		this.limit = limit;
	}

	@Override
	public void configure(Wgs84BoundsBean bounds, String mainCountryName) {
		this.bounds = bounds;
	}

	@Override
	public void geocode(String address, final GeocoderListener listener) {
		String url = buildSearchUrl(address);
		HttpUtils.downloadJson(url,
				new DownloadListener<JsArray<NominatimPlace>>() {

					@Override
					public void onSuccess(JsArray<NominatimPlace> results) {
						List<LocationBean> locations = new ArrayList<>(
								results.length());
						for (int i = 0; i < results.length(); i++) {
							NominatimPlace result = results.get(i);
							LocationBean location = new LocationBean();
							location.setAddress(result.getFormattedAddress());
							location.setLocation(
									new Wgs84LatLonBean(result.getLatitude(),
											result.getLongitude()));
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
			final ReverseGeocoderListener listener) {
		String url = buildReverseUrl(location);
		HttpUtils.downloadJson(url, new DownloadListener<NominatimPlace>() {

			@Override
			public void onSuccess(NominatimPlace result) {
				if (result.getError() != null) {
					listener.onReverseGeocodingDone(null, 0);
				} else {
					LocationBean addr = new LocationBean();
					addr.setLocation(new Wgs84LatLonBean(result.getLatitude(),
							result.getLongitude()));
					addr.setAddress(result.getFormattedAddress());
					listener.onReverseGeocodingDone(addr, 100);
				}
			}

			@Override
			public void onFailure(String msg) {
				listener.onReverseGeocodingDone(null, 0);
			}
		});

	}

	private String buildSearchUrl(String query) {
		StringBuffer sb = new StringBuffer(baseUrl);
		sb.append("/search?format=json");
		sb.append("&limit=").append(limit);
		sb.append("&addressdetails=1");
		sb.append("&viewport=").append(bounds.getMinLon()).append(",")
				.append(bounds.getMinLat()).append(",")
				.append(bounds.getMaxLon()).append(",")
				.append(bounds.getMaxLat());
		sb.append("&bounded=1");
		sb.append("&q=").append(query); // TODO URL-encode
		return sb.toString();
	}

	private String buildReverseUrl(Wgs84LatLonBean location) {
		StringBuffer sb = new StringBuffer(baseUrl);
		sb.append("/reverse?format=json");
		sb.append("&addressdetails=1");
		sb.append("&lat=").append(location.getLat());
		sb.append("&lon=").append(location.getLon());
		return sb.toString();
	}

}

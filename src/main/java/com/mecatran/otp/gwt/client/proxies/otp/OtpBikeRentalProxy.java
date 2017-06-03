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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mecatran.otp.gwt.client.model.BikeRentalStationBean;
import com.mecatran.otp.gwt.client.model.POIBean;
import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;
import com.mecatran.otp.gwt.client.proxies.POISource;
import com.mecatran.otp.gwt.client.proxies.otp.OtpBikeStationsResponse.OtpBikeStation;
import com.mecatran.otp.gwt.client.utils.HttpUtils;
import com.mecatran.otp.gwt.client.utils.HttpUtils.DownloadListener;

public class OtpBikeRentalProxy implements POISource {

	private String baseUrl;
	private String routerId;
	private List<POIListener> listeners = new ArrayList<>();

	public OtpBikeRentalProxy(String baseUrl, String routerId) {
		this.baseUrl = baseUrl;
		this.routerId = routerId;
		// TODO: if a station is marked as "real-time", reload every n minutes
		load();
	}

	@Override
	public void addListener(POIListener listener) {
		listeners.add(listener);
	}

	@Override
	public String getUniqueId() {
		return "otp::" + routerId + "::bikes";
	}

	private void load() {
		String url = buildUrl();
		HttpUtils.downloadJson(url,
				new DownloadListener<OtpBikeStationsResponse>() {

					@Override
					public void onSuccess(OtpBikeStationsResponse stations) {
						Map<String, POIBean> beans = new HashMap<>(
								stations.getStations().length());
						for (int i = 0; i < stations.getStations()
								.length(); i++) {
							OtpBikeStation otpStation = stations.getStations()
									.get(i);
							BikeRentalStationBean poiStation = new BikeRentalStationBean();
							poiStation.setId(otpStation.getId());
							// Sometimes OTP station have no name, make sure
							// they have one
							poiStation.setName(otpStation.getName() == null
									? "STATION #" + otpStation.getId()
									: otpStation.getName());
							poiStation.setHasRealTime(
									otpStation.isRealTimeData());
							poiStation.setBikesAvailable(
									otpStation.getBikesAvailable());
							poiStation.setSlotsAvailable(
									otpStation.getSpacesAvailable());
							poiStation.setTotalCapacity(
									otpStation.getBikesAvailable()
											+ otpStation.getSpacesAvailable());
							poiStation.setLocation(new Wgs84LatLonBean(
									otpStation.getLatitude(),
									otpStation.getLongitude()));
							beans.put(poiStation.getId(), poiStation);
						}
						for (POIListener listener : listeners) {
							listener.poiUpdate(OtpBikeRentalProxy.this, beans);
						}
					}

					@Override
					public void onFailure(String msg) {
						// Ignore
					}
				});
	}

	private String buildUrl() {
		return baseUrl + "/routers/" + routerId + "/bike_rental";
	}
}

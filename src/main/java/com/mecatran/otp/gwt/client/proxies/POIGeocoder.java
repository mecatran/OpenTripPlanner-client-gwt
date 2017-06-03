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
import java.util.Map;

import com.mecatran.otp.gwt.client.i18n.I18nUtils;
import com.mecatran.otp.gwt.client.model.LocationBean;
import com.mecatran.otp.gwt.client.model.POIBean;
import com.mecatran.otp.gwt.client.model.Wgs84BoundsBean;
import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;
import com.mecatran.otp.gwt.client.proxies.POISource.POIListener;
import com.mecatran.otp.gwt.client.utils.HaversineDistance;

/**
 * This class encapsulate a POISource to make a GeocoderProxy.
 * 
 */
public class POIGeocoder implements GeocoderProxy, POIListener {

	private Map<String, POIBean> pois;
	private Wgs84BoundsBean bounds;
	private boolean useBounds;

	public POIGeocoder(POISource poiSource, boolean useBounds) {
		this.useBounds = useBounds;
		poiSource.addListener(this);
	}

	@Override
	public void poiUpdate(POISource source, Map<String, POIBean> pois) {
		this.pois = pois;
	}

	@Override
	public void configure(Wgs84BoundsBean bounds, String mainCountryName) {
		this.bounds = bounds;
	}

	@Override
	public void geocode(String address, GeocoderListener listener) {
		List<LocationBean> locations = new ArrayList<LocationBean>();
		String upperAddress = address.toUpperCase();
		if (address.length() >= 3 && pois != null) {
			for (POIBean poi : pois.values()) {
				if (poi.getName().toUpperCase().contains(upperAddress)
						&& (useBounds && bounds.contains(poi.getLocation())
								|| !useBounds)) {
					LocationBean location = new LocationBean();
					location.setLocation(poi.getLocation());
					location.setAddress(translatePoi(poi));
					locations.add(location);
					if (locations.size() > 10)
						break;
				}
			}
		}
		listener.onGeocodingDone(locations);
	}

	@Override
	public void reverseGeocode(Wgs84LatLonBean location,
			ReverseGeocoderListener listener) {
		POIBean bestPoi = null;
		double bestDist = 10000;
		if (pois != null) {
			for (POIBean poi : pois.values()) {
				double dist = HaversineDistance.computeHaversineDistance(
						location.getLat(), location.getLon(),
						poi.getLocation().getLat(), poi.getLocation().getLon());
				if (dist < bestDist) {
					bestDist = dist;
					bestPoi = poi;
				}
			}
		}
		if (bestDist < 500) {
			LocationBean location2 = new LocationBean();
			location2.setAddress(translatePoi(bestPoi));
			location2.setLocation(bestPoi.getLocation());
			// bestDist = 0 m -> factor = 10000/50 = 200
			// bestDist = 100 m -> factor = 10000/150 = 66
			// bestDist = 500 m -> factor = 10000/550 = 18
			listener.onReverseGeocodingDone(location2,
					(int) (10000 / (bestDist + 50)));
		} else {
			listener.onReverseGeocodingDone(null, 0);
		}
	}

	private String translatePoi(POIBean poi) {
		String msg;
		switch (poi.getType()) {
		case TRANSPORT_BIKE_RENTAL_STATION:
			msg = I18nUtils.tr("geocode.bike-rental", poi.getName());
			break;
		case TRANSPORT_STATION:
			msg = I18nUtils.tr("geocode.transport", poi.getName());
			break;
		default:
			msg = I18nUtils.tr("geocode.poi", poi.getName());
			break;
		}
		return msg;
	}

}

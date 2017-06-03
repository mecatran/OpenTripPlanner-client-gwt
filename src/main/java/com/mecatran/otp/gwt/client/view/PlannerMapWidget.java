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
package com.mecatran.otp.gwt.client.view;

import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;
import com.mecatran.otp.gwt.client.model.ItineraryBean;
import com.mecatran.otp.gwt.client.model.LocationBean;
import com.mecatran.otp.gwt.client.model.POIBean;
import com.mecatran.otp.gwt.client.model.TransportMode;
import com.mecatran.otp.gwt.client.model.Wgs84BoundsBean;
import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;
import com.mecatran.otp.gwt.client.proxies.POISource;

public interface PlannerMapWidget {

	public static interface MapListener {

		public void onStartPointSelected(Wgs84LatLonBean position);

		public void onEndPointSelected(Wgs84LatLonBean position);
	}

	public Widget getAsWidget();

	public void setMapListener(MapListener mapListener);

	public void setBounds(Wgs84BoundsBean bounds);

	public void updateSize();

	public void showItineraryOnMap(ItineraryBean itinerary);

	public void previewItineraryOnMap(ItineraryBean itinerary);

	public void previewLocation(LocationBean location);

	public void updateDeparturePosition(Wgs84LatLonBean departurePosition,
			boolean noAutoPan);

	public void updateArrivalPosition(Wgs84LatLonBean arrivalPosition,
			boolean noAutoPan);

	public void highlightItineraryStep(String instructionsHtml,
			Wgs84LatLonBean location);

	public void focusDeparture();

	public void focusArrival();

	public void switchMapBackground(Set<TransportMode> modes);

	public void updatePOIs(POISource source, Map<String, POIBean> pois);

}

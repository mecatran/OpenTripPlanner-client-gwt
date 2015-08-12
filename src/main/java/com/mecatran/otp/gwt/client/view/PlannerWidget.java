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

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;
import com.mecatran.otp.gwt.client.model.AlertBean;
import com.mecatran.otp.gwt.client.model.ItineraryBean;
import com.mecatran.otp.gwt.client.model.LocationBean;
import com.mecatran.otp.gwt.client.model.ModeCapabilitiesBean;
import com.mecatran.otp.gwt.client.model.POIBean;
import com.mecatran.otp.gwt.client.model.PlanRequestBean;
import com.mecatran.otp.gwt.client.model.TransportMode;
import com.mecatran.otp.gwt.client.model.Wgs84BoundsBean;
import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;
import com.mecatran.otp.gwt.client.proxies.GeocoderProxy;
import com.mecatran.otp.gwt.client.proxies.POISource;

public interface PlannerWidget {

	public interface PlannerWidgetListener {

		public void onPlanningRequested();

		public void onItineraryRemoved(ItineraryBean itinerary);

		public void onItinerarySelected(ItineraryBean itinerary);

		public void onItineraryHover(ItineraryBean itinerary);

		public void onItineraryPinned(ItineraryBean itinerary, boolean pinned);

		public void onItineraryPrintRequest(ItineraryBean itinerary);

		public void onStartPointSelected(Wgs84LatLonBean location);

		public void onEndPointSelected(Wgs84LatLonBean location);

		public void onStartLocationSelected(LocationBean location);

		public void onEndLocationSelected(LocationBean location);

		public void onLocationHover(LocationBean location);

		public void onTransportModeChange(Set<TransportMode> modes);
	}

	public Widget getAsWidget();

	public void setPlannerWidgetListener(
			PlannerWidgetListener plannerWidgetListener);

	public void setModeCapabilities(ModeCapabilitiesBean modeCapabilities);

	public void setGeocoderProxy(GeocoderProxy geocoderProxy);

	public void setBounds(Wgs84BoundsBean bounds);

	public void addItinerary(ItineraryBean itinerary, boolean autoSelect);

	public void clearItineraries();

	public void showItineraryOnMap(ItineraryBean itinerary, boolean preview);

	public void unselectItinerary();

	public void showError(String errorMessage);

	public void setBusy(boolean busy);

	public void setStartLocation(LocationBean location, boolean autoPan);

	public void setEndLocation(LocationBean location, boolean autoPan);

	public void previewLocation(LocationBean location);

	public void setDateDeparture(boolean dateDeparture);

	public void setDate(Date date);

	public void setModes(Set<TransportMode> modes);

	public void setWheelchairAccessible(boolean wheelchairAccessible);

	public void focusDeparture();

	public void focusArrival();

	public PlanRequestBean getPlanRequestBean();

	public void publishGeneralAlert(AlertBean alert);

	public void updatePOIs(POISource source, Map<String, POIBean> pois);

	public void switchMapBackground(Set<TransportMode> modes);

}

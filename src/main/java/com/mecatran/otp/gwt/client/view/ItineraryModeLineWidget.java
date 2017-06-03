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

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.mecatran.otp.gwt.client.model.AlertBean;
import com.mecatran.otp.gwt.client.model.ItineraryBean;
import com.mecatran.otp.gwt.client.model.ItineraryLegBean;
import com.mecatran.otp.gwt.client.model.ItineraryRoadLegBean;
import com.mecatran.otp.gwt.client.model.ItineraryTransitLegBean;
import com.mecatran.otp.gwt.client.model.TransitRouteBean;
import com.mecatran.otp.gwt.client.model.TravelType;
import com.mecatran.otp.gwt.client.utils.FormatUtils;

public class ItineraryModeLineWidget extends Composite {

	private ItineraryBean itinerary;

	public ItineraryModeLineWidget(ItineraryBean anItinerary) {
		itinerary = anItinerary;
		HorizontalPanel rootPanel = new HorizontalPanel();
		rootPanel.addStyleName("itinerary-modeline");
		// Display alerts (warn, crit) count summary
		int alertsCount = 0;
		for (AlertBean alert : anItinerary.getAlerts())
			if (alert.getLevel() >= AlertBean.LEVEL_WARN)
				alertsCount++;
		if (alertsCount > 0) {
			Label alertCountLabel = new Label("" + alertsCount);
			rootPanel.add(alertCountLabel);
			alertCountLabel.addStyleName("itinerary-alerts-count");
			alertCountLabel.addStyleName("warn-icon");
		}
		for (ItineraryLegBean leg : itinerary.getLegs()) {
			if (leg.getTravelType() == TravelType.ROAD) {
				ItineraryRoadLegBean roadLeg = leg.getAsRoadLeg();
				// Road mode icons
				Label roadLabel = new Label("");
				roadLabel.addStyleName("itinerary-modeline-road-icon");
				roadLabel.addStyleName(
						FormatUtils.getCssClassNameFromTransportMode(
								roadLeg.getMode()) + "-icon");
				rootPanel.add(roadLabel);
			} else if (leg.getTravelType() == TravelType.TRANSIT) {
				ItineraryTransitLegBean transitLeg = leg.getAsTransitLeg();
				TransitRouteBean route = transitLeg.getRoute();
				// Transit mode icons
				HTML transitLabel = new HTML();
				transitLabel.addStyleName("itinerary-modeline-transit-icon");
				transitLabel.addStyleName(
						FormatUtils.getCssClassNameFromTransportMode(
								transitLeg.getMode()) + "-icon");
				String code = transitLeg.getRoute().getCode();
				// Limit code to first 5 characters
				if (code.length() > 5)
					code = code.substring(0, 5);
				// TODO Use dedicated class for displaying route colors
				transitLabel.setHTML("<span class='route-code' style='color:"
						+ route.getForegroundColor() + "; background-color:"
						+ route.getBackgroundColor() + "'>" + code + "</span>");
				rootPanel.add(transitLabel);
			}
		}
		initWidget(rootPanel);
	}
}

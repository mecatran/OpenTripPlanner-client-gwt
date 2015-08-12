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

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mecatran.otp.gwt.client.model.ItineraryBean;
import com.mecatran.otp.gwt.client.model.ItineraryLegBean;
import com.mecatran.otp.gwt.client.model.ItineraryRoadLegBean;
import com.mecatran.otp.gwt.client.model.ItineraryTransitLegBean;
import com.mecatran.otp.gwt.client.model.TravelType;
import com.mecatran.otp.gwt.client.utils.FormatUtils;

public class ItineraryTimeLineWidget extends Composite {

	private ItineraryBean itinerary;

	public ItineraryTimeLineWidget(ItineraryBean anItinerary) {
		itinerary = anItinerary;
		long totalTravelTimeSec = anItinerary.getDurationSeconds();
		SimplePanel rootPanel = new SimplePanel();
		rootPanel.addStyleName("itinerary-timeline");
		rootPanel.addStyleName("timeline-clock-icon");
		Grid timesPanel = new Grid(1, 0);
		rootPanel.add(timesPanel);
		int timesPanelColumns = 0;
		for (ItineraryLegBean leg : itinerary.getLegs()) {
			if (leg.getTravelType() == TravelType.ROAD) {
				ItineraryRoadLegBean roadLeg = leg.getAsRoadLeg();
				// Road time scale
				long legTravelTimeSec = roadLeg.getDurationSeconds();
				timesPanelColumns++;
				timesPanel.resizeColumns(timesPanelColumns);
				HTML roadTimeLabel = new HTML(secToString(legTravelTimeSec));
				timesPanel.setWidget(0, timesPanelColumns - 1, roadTimeLabel);
				String styleName = "timeline-"
						+ FormatUtils.getCssClassNameFromTransportMode(roadLeg
								.getMode()) + "-icon";
				timesPanel.getCellFormatter().addStyleName(0,
						timesPanelColumns - 1, styleName);
				timesPanel
						.getCellFormatter()
						.getElement(0, timesPanelColumns - 1)
						.getStyle()
						.setWidth(legTravelTimeSec * 100 / totalTravelTimeSec,
								Style.Unit.PCT);
			} else if (leg.getTravelType() == TravelType.TRANSIT) {
				ItineraryTransitLegBean transitLeg = leg.getAsTransitLeg();
				// Wait time scale
				long legWaitTimeSec = transitLeg.getWaitDurationSeconds();
				if (legWaitTimeSec > 60) {
					timesPanelColumns++;
					timesPanel.resizeColumns(timesPanelColumns);
					HTML waitTimeLabel = new HTML(secToString(legWaitTimeSec));
					timesPanel.setWidget(0, timesPanelColumns - 1,
							waitTimeLabel);
					timesPanel.getCellFormatter().addStyleName(0,
							timesPanelColumns - 1, "timeline-wait-icon");
					timesPanel
							.getCellFormatter()
							.getElement(0, timesPanelColumns - 1)
							.getStyle()
							.setWidth(
									legWaitTimeSec * 100 / totalTravelTimeSec,
									Style.Unit.PCT);
				}
				// Transit time scale
				long legTravelTimeSec = transitLeg.getDurationSeconds();
				timesPanelColumns++;
				timesPanel.resizeColumns(timesPanelColumns);
				HTML transitTimeLabel = new HTML(secToString(legTravelTimeSec));
				timesPanel
						.setWidget(0, timesPanelColumns - 1, transitTimeLabel);
				timesPanel.getCellFormatter().addStyleName(0,
						timesPanelColumns - 1, "timeline-transit-icon");
				String styleName = "timeline-"
						+ FormatUtils
								.getCssClassNameFromTransportMode(transitLeg
										.getMode()) + "-icon";
				timesPanel.getCellFormatter().addStyleName(0,
						timesPanelColumns - 1, styleName);
				timesPanel
						.getCellFormatter()
						.getElement(0, timesPanelColumns - 1)
						.getStyle()
						.setWidth(legTravelTimeSec * 100 / totalTravelTimeSec,
								Style.Unit.PCT);
			}
		}
		initWidget(rootPanel);
	}

	private String secToString(long durationSec) {
		return "";
		// Disabled, clutter the screen and info is already there.
		// return ((durationSec + 30) / 60) + "min";
	}
}

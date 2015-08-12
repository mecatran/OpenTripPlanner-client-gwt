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

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mecatran.otp.gwt.client.PlannerResources;
import com.mecatran.otp.gwt.client.i18n.I18nUtils;
import com.mecatran.otp.gwt.client.model.ItineraryBean;
import com.mecatran.otp.gwt.client.utils.FormatUtils;

public class ItinerarySummaryWidget extends Composite {

	private ToggleButton pinButton;
	private Anchor showHideDetailsLink;
	private Anchor printLink;

	public ItinerarySummaryWidget(ItineraryBean itinerary,
			ClickHandler closeHandler, ClickHandler pinHandler) {
		VerticalPanel rootPanel = new VerticalPanel();

		// Addresses and close button top line
		HorizontalPanel departureAndButtonsPanel = new HorizontalPanel();
		departureAndButtonsPanel.addStyleName("itinerary-topbar-panel");
		rootPanel.add(departureAndButtonsPanel);

		// Departure and arrival time and addresses
		Grid departureAndArrivalPanel = new Grid(2, 2);
		departureAndArrivalPanel.addStyleName("itinerary-addresses-panel");
		departureAndButtonsPanel.add(departureAndArrivalPanel);
		departureAndButtonsPanel.setCellWidth(departureAndArrivalPanel, "100%");
		Label departureTimeLabel = new Label(FormatUtils.formatTime(itinerary
				.getDepartureTime()));
		departureTimeLabel.addStyleName("itinerary-departure-time");
		Label arrivalTimeLabel = new Label(FormatUtils.formatTime(itinerary
				.getArrivalTime()));
		arrivalTimeLabel.addStyleName("itinerary-arrival-time");
		String[] formattedAddresses = FormatUtils.formatAddresses(
				itinerary.getStartAddress(), itinerary.getEndAddress());
		// TODO Use HTML::setStyleName() ?
		HTML departureAddressLabel = new HTML(
				"<div class='itinerary-address-inner'>" + formattedAddresses[0]
						+ "</div>");
		departureAddressLabel.addStyleName("itinerary-address");
		HTML arrivalAddressLabel = new HTML(
				"<div class='itinerary-address-inner'>" + formattedAddresses[1]
						+ "</div>");
		arrivalAddressLabel.addStyleName("itinerary-address");
		departureAndArrivalPanel.setWidget(0, 0, departureTimeLabel);
		departureAndArrivalPanel.setWidget(0, 1, departureAddressLabel);
		departureAndArrivalPanel.setWidget(1, 0, arrivalTimeLabel);
		departureAndArrivalPanel.setWidget(1, 1, arrivalAddressLabel);
		departureAndArrivalPanel.getColumnFormatter().setWidth(1, "100%");
		departureAndArrivalPanel.getCellFormatter().setVerticalAlignment(0, 1,
				HasVerticalAlignment.ALIGN_TOP);
		departureAndArrivalPanel.getCellFormatter().setVerticalAlignment(1, 1,
				HasVerticalAlignment.ALIGN_TOP);

		// Buttons panel
		VerticalPanel buttonsPanel = new VerticalPanel();
		departureAndButtonsPanel.add(buttonsPanel);
		buttonsPanel.setCellHorizontalAlignment(buttonsPanel,
				HasHorizontalAlignment.ALIGN_RIGHT);

		// Close button
		PushButton closeButton = new PushButton(new Image(
				PlannerResources.INSTANCE.closeButtonPng()));
		closeButton.addStyleName("itinerary-close-button");
		closeButton.addClickHandler(closeHandler);
		buttonsPanel.add(closeButton);

		// Pin button
		pinButton = new ToggleButton(new Image(
				PlannerResources.INSTANCE.noFavoritePng()), new Image(
				PlannerResources.INSTANCE.favoritePng()));
		pinButton.addStyleName("itinerary-pin-button");
		pinButton.addClickHandler(pinHandler);
		buttonsPanel.add(pinButton);

		// Date, duration and travel mode summary
		HorizontalPanel timesAndSummaryPanel = new HorizontalPanel();
		timesAndSummaryPanel.setWidth("100%");
		timesAndSummaryPanel
				.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		rootPanel.add(timesAndSummaryPanel);

		// Date and duration
		VerticalPanel dateAndDurationPanel = new VerticalPanel();
		dateAndDurationPanel.addStyleName("itinerary-date-duration-panel");
		timesAndSummaryPanel.add(dateAndDurationPanel);
		String departureDate = FormatUtils.formatDate(itinerary
				.getDepartureTime());
		String duration = FormatUtils.formatDuration(itinerary
				.getDurationSeconds());
		Label departureDateLabel = new Label(departureDate);
		departureDateLabel.addStyleName("itinerary-departure-date");
		dateAndDurationPanel.add(departureDateLabel);
		HTML durationLabel = new HTML(duration);
		durationLabel.addStyleName("itinerary-duration");
		dateAndDurationPanel.add(durationLabel);

		// Travel modes
		Panel modesAndLinksPanel = new VerticalPanel();
		timesAndSummaryPanel.add(modesAndLinksPanel);
		timesAndSummaryPanel.setCellWidth(modesAndLinksPanel, "100%");
		ItineraryModeLineWidget modeLine = new ItineraryModeLineWidget(
				itinerary);
		modeLine.addStyleName("itinerary-modeline-panel");
		modesAndLinksPanel.add(modeLine);

		// Links (show details, print)
		HorizontalPanel linksPanel = new HorizontalPanel();
		showHideDetailsLink = new Anchor(I18nUtils.tr("show.itinerary.details"));
		showHideDetailsLink.addStyleName("itinerary-details-show-hide");
		linksPanel.add(showHideDetailsLink);
		printLink = new Anchor(I18nUtils.tr("print.itinerary"));
		printLink.addStyleName("itinerary-details-print-link");
		linksPanel.add(printLink);
		modesAndLinksPanel.add(linksPanel);

		initWidget(rootPanel);
	}

	public boolean isPinned() {
		return pinButton.isDown();
	}

	public void addPrintClickHandler(ClickHandler clickHandler) {
		printLink.addClickHandler(clickHandler);
	}

	public void setOpened(boolean opened) {
		showHideDetailsLink.setText(I18nUtils
				.tr(opened ? "hide.itinerary.details"
						: "show.itinerary.details"));
	}
}

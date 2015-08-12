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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mecatran.otp.gwt.client.model.ItineraryBean;
import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;

public class ItineraryWidget extends Composite {

	public static interface ItineraryListener {

		public void onItineraryCloseButtonClicked(ItineraryWidget widget);

		public void onItineraryPinButtonClicked(ItineraryWidget widget,
				boolean pinned);

		public void onItineraryPrintButtonClicked(ItineraryWidget widget);

		public void onItineraryClicked(ItineraryWidget widget);

		public void onItineraryHover(ItineraryWidget widget, boolean start);

		public void onItineraryStepClicked(String instructionsHtml,
				Wgs84LatLonBean location);
	}

	private ItineraryBean itinerary;
	private ItineraryListener listener;
	private ItinerarySummaryWidget itinerarySummaryWidget;
	private ItineraryDetailsWidget itineraryDetailsWidget;
	private ItineraryTimeLineWidget itineraryTimeLineWidget;
	private boolean opened;
	private boolean selected;
	private boolean pinned;

	public ItineraryWidget(ItineraryBean anItinerary,
			ItineraryListener aListener) {
		itinerary = anItinerary;
		listener = aListener;

		FocusPanel focusPanel = new FocusPanel();
		VerticalPanel openablePanel = new VerticalPanel();
		openablePanel.setWidth("100%");

		// Header: summary
		itinerarySummaryWidget = new ItinerarySummaryWidget(itinerary,
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						event.stopPropagation();
						listener.onItineraryCloseButtonClicked(ItineraryWidget.this);
					}
				}, new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						event.stopPropagation();
						listener.onItineraryPinButtonClicked(
								ItineraryWidget.this,
								itinerarySummaryWidget.isPinned());
					}
				});
		itinerarySummaryWidget.addStyleName("itinerary-summary-panel");
		openablePanel.add(itinerarySummaryWidget);

		// Print link click handler
		itinerarySummaryWidget.addPrintClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				event.stopPropagation();
				listener.onItineraryPrintButtonClicked(ItineraryWidget.this);
			}
		});

		// Timeline (not visible by default)
		itineraryTimeLineWidget = new ItineraryTimeLineWidget(itinerary);
		openablePanel.add(itineraryTimeLineWidget);
		itineraryTimeLineWidget.setVisible(false);
		
		// Details (not visible by default)
		itineraryDetailsWidget = new ItineraryDetailsWidget(itinerary, listener);
		itineraryDetailsWidget.addStyleName("itinerary-details-panel");
		itineraryDetailsWidget.setVisible(false);
		opened = false;
		openablePanel.add(itineraryDetailsWidget);

		focusPanel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				boolean wasSelected = selected;
				listener.onItineraryHover(ItineraryWidget.this, false);
				listener.onItineraryClicked(ItineraryWidget.this);
				if (!opened) {
					itinerarySummaryWidget.setOpened(true);
					itineraryDetailsWidget.setVisible(true);
					itineraryTimeLineWidget.setVisible(true);
					opened = true;
				} else if (wasSelected && selected) {
					// We close it only if we were already selected.
					itinerarySummaryWidget.setOpened(false);
					itineraryDetailsWidget.setVisible(false);
					itineraryTimeLineWidget.setVisible(false);
					opened = false;
				}
			}
		});
		focusPanel.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if (!selected)
					listener.onItineraryHover(ItineraryWidget.this, true);
			}
		});
		focusPanel.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (!selected)
					listener.onItineraryHover(ItineraryWidget.this, false);
			}
		});

		focusPanel.add(openablePanel);
		initWidget(focusPanel);
		this.addStyleName("itinerary-panel-outer");
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		itineraryDetailsWidget.setSelected(selected);
		if (selected) {
			ItineraryWidget.this.addStyleName("itinerary-panel-opened");
		} else {
			ItineraryWidget.this.removeStyleName("itinerary-panel-opened");
		}
	}

	public void setPinned(boolean pinned) {
		this.pinned = pinned;
		if (pinned) {
			ItineraryWidget.this.addStyleName("itinerary-panel-pinned");
		} else {
			ItineraryWidget.this.removeStyleName("itinerary-panel-pinned");
		}
	}

	public boolean isPinned() {
		return pinned;
	}

	public ItineraryBean getItinerary() {
		return itinerary;
	}
}

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

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel.Direction;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
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
import com.mecatran.otp.gwt.client.view.ItineraryWidget.ItineraryListener;
import com.mecatran.otp.gwt.client.view.PlannerMapWidget.MapListener;
import com.mecatran.otp.gwt.client.view.PrintWidget.PrintWidgetListener;

/**
 * This default implementation implement a screen widget.
 * 
 */
public class PlannerWidgetImpl extends Composite implements PlannerWidget,
		PrintWidgetListener, ItineraryListener, MapListener {

	private PlannerFormWidget plannerFormWidget;
	private PlannerMapWidget plannerMapWidget;
	private AlertStackWidget alertStackWidget;
	private ItineraryStackWidget itineraryStackWidget;
	private LeftPanelWidget leftPanelWidget;
	private ItineraryWidget selectedItineraryWidget;

	private SplitLayoutPanel screenWidget;
	private DeckLayoutPanel rootPanel;

	private PlannerWidgetListener plannerWidgetListener;
	private PrintWidgetListener printWidgetListener;

	public PlannerWidgetImpl(ModeCapabilitiesBean modeCapabilities) {
		rootPanel = new DeckLayoutPanel();

		// Screen interaction widget
		screenWidget = new SplitLayoutPanel() {
			@Override
			public void onResize() {
				super.onResize();
				if (plannerMapWidget != null)
					plannerMapWidget.updateSize();
			}
		};
		screenWidget.setSize("100%", "100%");
		selectedItineraryWidget = null;

		plannerMapWidget = new OpenLayersPlannerMapWidget();
		plannerMapWidget.setMapListener(this);
		plannerFormWidget = new PlannerFormWidget(modeCapabilities);
		plannerFormWidget.setPlannerWidget(this);
		itineraryStackWidget = new ItineraryStackWidget();
		alertStackWidget = new AlertStackWidget();

		// Firefox/Opera hack: no scroll panel...
		boolean useScrollPanel = false;
		leftPanelWidget = new LeftPanelWidget(plannerFormWidget,
				itineraryStackWidget, alertStackWidget, useScrollPanel);
		screenWidget.insert(leftPanelWidget, Direction.WEST, 300, null);
		if (!useScrollPanel) {
			leftPanelWidget.getElement().getParentElement().getStyle()
					.setOverflow(Overflow.AUTO);
		}
		screenWidget.insert(plannerMapWidget.getAsWidget(), Direction.CENTER,
				0, null);
		rootPanel.add(screenWidget);
		rootPanel.showWidget(screenWidget);
		rootPanel.setSize("100%", "100%");

		initWidget(rootPanel);
	}

	@Override
	public void onItineraryCloseButtonClicked(ItineraryWidget itineraryWidget) {
		itineraryStackWidget.removeItineraryWidget(itineraryWidget);
		plannerWidgetListener
				.onItineraryRemoved(itineraryWidget.getItinerary());
		plannerWidgetListener.onItineraryHover(null);
		if (itineraryWidget == selectedItineraryWidget) {
			plannerMapWidget.showItineraryOnMap(null);
			selectedItineraryWidget = null;
		}
	}

	@Override
	public void onItineraryPinButtonClicked(ItineraryWidget itineraryWidget,
			boolean pinned) {
		itineraryWidget.setPinned(pinned);
		plannerWidgetListener.onItineraryPinned(itineraryWidget.getItinerary(),
				itineraryWidget.isPinned());
	}

	@Override
	public void onItineraryPrintButtonClicked(ItineraryWidget itineraryWidget) {
		plannerWidgetListener.onItineraryPrintRequest(itineraryWidget
				.getItinerary());
	}

	@Override
	public void onItineraryClicked(ItineraryWidget itineraryWidget) {
		selectItinerary(itineraryWidget);
	}

	@Override
	public void onItineraryHover(ItineraryWidget itineraryWidget, boolean start) {
		if (start)
			plannerWidgetListener.onItineraryHover(itineraryWidget
					.getItinerary());
		else
			plannerWidgetListener.onItineraryHover(null);
	}

	@Override
	public void onItineraryStepClicked(String instructionsHtml,
			Wgs84LatLonBean location) {
		plannerMapWidget.highlightItineraryStep(instructionsHtml, location);
	}

	@Override
	public void onStartPointSelected(Wgs84LatLonBean position) {
		plannerWidgetListener.onStartPointSelected(position);
	}

	@Override
	public void onEndPointSelected(Wgs84LatLonBean position) {
		plannerWidgetListener.onEndPointSelected(position);
	}

	@Override
	public Widget getAsWidget() {
		return this;
	}

	@Override
	public void setPlannerWidgetListener(
			PlannerWidgetListener plannerWidgetListener) {
		this.plannerWidgetListener = plannerWidgetListener;
		plannerFormWidget.setPlannerWidgetListener(plannerWidgetListener);
	}

	@Override
	public void setGeocoderProxy(GeocoderProxy geocoderProxy) {
		plannerFormWidget.setGeocoderProxy(geocoderProxy);
	}

	@Override
	public void setBounds(Wgs84BoundsBean bounds) {
		plannerMapWidget.setBounds(bounds);
	}

	@Override
	public void addItinerary(ItineraryBean itinerary, boolean autoSelect) {
		ItineraryWidget itineraryWidget = new ItineraryWidget(itinerary, this);
		itineraryStackWidget.addItineraryWidget(itineraryWidget);
		if (autoSelect)
			selectItinerary(itineraryWidget);
	}

	@Override
	public void clearItineraries() {
		itineraryStackWidget.removeAllUnpinned();
	}

	@Override
	public void showItineraryOnMap(ItineraryBean itinerary, boolean preview) {
		if (preview)
			plannerMapWidget.previewItineraryOnMap(itinerary);
		else
			plannerMapWidget.showItineraryOnMap(itinerary);
	}

	@Override
	public void unselectItinerary() {
		if (selectedItineraryWidget != null) {
			selectedItineraryWidget.setSelected(false);
			selectedItineraryWidget = null;
			plannerMapWidget.showItineraryOnMap(null);
		}
	}

	@Override
	public void showError(String errorMessage) {
		Window.alert(errorMessage);
	}

	@Override
	public void setBusy(boolean busy) {
		plannerFormWidget.setBusy(busy);
	}

	@Override
	public void setStartLocation(LocationBean location, boolean autoPan) {
		plannerFormWidget.setStartLocation(location);
		plannerMapWidget.updateDeparturePosition(location.getLocation(),
				autoPan);
	}

	@Override
	public void setEndLocation(LocationBean location, boolean autoPan) {
		plannerFormWidget.setEndLocation(location);
		plannerMapWidget.updateArrivalPosition(location.getLocation(), autoPan);
	}

	@Override
	public void previewLocation(LocationBean location) {
		plannerMapWidget.previewLocation(location);
	}

	@Override
	public void setDateDeparture(boolean dateDeparture) {
		plannerFormWidget.setDateDeparture(dateDeparture);
	}

	@Override
	public void setDate(Date date) {
		plannerFormWidget.setDateTime(date);
	}

	@Override
	public void setModes(Set<TransportMode> modes) {
		plannerFormWidget.setModes(modes);
	}

	@Override
	public void setWheelchairAccessible(boolean wheelchairAccessible) {
		plannerFormWidget.setWheelchairAccessible(wheelchairAccessible);
	}

	@Override
	public void focusDeparture() {
		unselectItinerary();
		plannerMapWidget.focusDeparture();
	}

	@Override
	public void focusArrival() {
		unselectItinerary();
		plannerMapWidget.focusArrival();
	}

	@Override
	public PlanRequestBean getPlanRequestBean() {
		return plannerFormWidget.getPlanRequestBean();
	}

	@Override
	public void publishGeneralAlert(AlertBean alert) {
		AlertWidget alertWidget = new AlertWidget(alert);
		alertWidget.setStyleName("general-alert");
		alertStackWidget.addAlert(alertWidget);
	}

	@Override
	public void updatePOIs(POISource source, Map<String, POIBean> pois) {
		plannerMapWidget.updatePOIs(source, pois);
	}

	@Override
	public void switchMapBackground(Set<TransportMode> modes) {
		plannerMapWidget.switchMapBackground(modes);
	}

	private void selectItinerary(ItineraryWidget itineraryWidget) {
		if (itineraryWidget == selectedItineraryWidget)
			return;
		if (selectedItineraryWidget != null) {
			selectedItineraryWidget.setSelected(false);
		}
		selectedItineraryWidget = itineraryWidget;
		selectedItineraryWidget.setSelected(true);
		plannerWidgetListener.onItinerarySelected(itineraryWidget
				.getItinerary());
	}

	@Override
	public void onPrintDone() {
		// Delegate
		rootPanel.showWidget(screenWidget);
		printWidgetListener.onPrintDone();
	}

}

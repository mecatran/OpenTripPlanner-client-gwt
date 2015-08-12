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
import java.util.Set;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mecatran.otp.gwt.client.controller.PlannerState;
import com.mecatran.otp.gwt.client.i18n.I18nUtils;
import com.mecatran.otp.gwt.client.model.LocationBean;
import com.mecatran.otp.gwt.client.model.ModeCapabilitiesBean;
import com.mecatran.otp.gwt.client.model.PlanRequestBean;
import com.mecatran.otp.gwt.client.model.TransportMode;
import com.mecatran.otp.gwt.client.proxies.GeocoderProxy;
import com.mecatran.otp.gwt.client.view.AddressBox.AddressSuggestBoxListener;
import com.mecatran.otp.gwt.client.view.ModeSelectorWidget.ModeSelectorListener;
import com.mecatran.otp.gwt.client.view.PlannerWidget.PlannerWidgetListener;

public class PlannerFormWidget extends Composite implements
		ModeSelectorListener {

	private AddressBox departureSuggestBox;
	private AddressBox arrivalSuggestBox;
	private Button planButton;
	private Anchor linkAnchor;
	private Widget pleaseWaitPanel;
	private PlannerWidget plannerWidget;
	private ModeSelectorWidget modeSelector;

	private PlannerWidgetListener plannerWidgetListener;

	public PlannerFormWidget() {
		VerticalPanel rootPanel = new VerticalPanel();
		// Departure
		departureSuggestBox = new AddressProposalBox();
		departureSuggestBox.getAsWidget().addStyleName("departure-panel");
		departureSuggestBox.getAsWidget().addStyleName("departure-icon");
		departureSuggestBox
				.setAddressSuggestBoxListener(new AddressSuggestBoxListener() {
					@Override
					public void onLocationSelected(LocationBean location) {
						plannerWidgetListener.onStartLocationSelected(location);
					}

					@Override
					public void onLocationHover(LocationBean location) {
						plannerWidgetListener.onLocationHover(location);
					}

					@Override
					public void onFocus() {
						plannerWidget.focusDeparture();
					}
				});
		rootPanel.add(departureSuggestBox.getAsWidget());
		// Arrival
		arrivalSuggestBox = new AddressProposalBox();
		arrivalSuggestBox.getAsWidget().addStyleName("arrival-panel");
		arrivalSuggestBox.getAsWidget().addStyleName("arrival-icon");
		arrivalSuggestBox
				.setAddressSuggestBoxListener(new AddressSuggestBoxListener() {
					@Override
					public void onLocationSelected(LocationBean location) {
						plannerWidgetListener.onEndLocationSelected(location);
					}

					@Override
					public void onLocationHover(LocationBean location) {
						plannerWidgetListener.onLocationHover(location);
					}

					@Override
					public void onFocus() {
						plannerWidget.focusArrival();
					}
				});
		rootPanel.add(arrivalSuggestBox.getAsWidget());
		// Mode selector
		DropDownModeSelectorWidget modeSelectorWidget = new DropDownModeSelectorWidget();
		modeSelectorWidget.setModeSelectorListener(this);
		modeSelector = modeSelectorWidget;
		rootPanel.add(modeSelectorWidget);
		// Plan button
		HorizontalPanel planButtonPanel = new HorizontalPanel();
		planButtonPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		planButton = new Button(I18nUtils.tr("plan.itinerary"));
		planButtonPanel.add(planButton);
		planButton.addStyleName("plan-button");
		planButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				plannerWidgetListener.onPlanningRequested();
			}
		});
		pleaseWaitPanel = new Label(I18nUtils.tr("please.wait"));
		planButtonPanel.add(pleaseWaitPanel);
		pleaseWaitPanel.setVisible(false);
		pleaseWaitPanel.addStyleName("please-wait-panel");
		pleaseWaitPanel.addStyleName("loading-icon");
		// Link anchor
		linkAnchor = new Anchor(I18nUtils.tr("link.to.this.page"));
		linkAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				linkTo();
			}
		});
		linkAnchor.addStyleName("link-anchor");
		planButtonPanel.add(linkAnchor);
		rootPanel.add(planButtonPanel);

		rootPanel.setWidth("100%");
		rootPanel.addStyleName("planner-form-panel");
		initWidget(rootPanel);
		setWidth("100%");
	}

	public void setModeCapabilities(ModeCapabilitiesBean modeCapabilities) {
		modeSelector.setModeCapabilities(modeCapabilities);
	}

	public void setPlannerWidgetListener(
			PlannerWidgetListener plannerWidgetListener) {
		this.plannerWidgetListener = plannerWidgetListener;
	}

	public void setPlannerWidget(PlannerWidget plannerWidget) {
		this.plannerWidget = plannerWidget;
	}

	public void setGeocoderProxy(GeocoderProxy geocoderProxy) {
		departureSuggestBox.setGeocoderProxy(geocoderProxy);
		arrivalSuggestBox.setGeocoderProxy(geocoderProxy);
	}

	public void setBusy(boolean busy) {
		pleaseWaitPanel.setVisible(busy);
		planButton.setVisible(!busy);
	}

	public void setStartLocation(LocationBean location) {
		departureSuggestBox.setLocation(location);
	}

	public void setEndLocation(LocationBean location) {
		arrivalSuggestBox.setLocation(location);
	}

	public void setDateDeparture(boolean dateDeparture) {
		modeSelector.setDateDeparture(dateDeparture);
	}

	public void setDateTime(Date dateTime) {
		modeSelector.setDateTime(dateTime);
	}

	public void setModes(Set<TransportMode> modes) {
		modeSelector.setModes(modes);
	}

	public void setWheelchairAccessible(boolean wheelchairAccessible) {
		modeSelector.setWheelchairAccessible(wheelchairAccessible);
	}

	public PlanRequestBean getPlanRequestBean() {
		PlanRequestBean retval = new PlanRequestBean();
		retval.setDeparture(departureSuggestBox.getLocation());
		retval.setArrival(arrivalSuggestBox.getLocation());

		retval.setDate(modeSelector.getDateTime());
		retval.setDateDeparture(modeSelector.isDateDeparture());
		retval.setModes(modeSelector.getSelectedModes());
		retval.setWalkSpeedKph(modeSelector.getWalkSpeedKph());
		retval.setBikeSpeedKph(modeSelector.getBikeSpeedKph());
		retval.setMaxWalkDistanceMeters(modeSelector.getMaxWalkDistanceMeters());
		retval.setWalkReluctanceFactor(modeSelector.getWalkReluctanceFactor());
		retval.setWheelchairAccessible(modeSelector.isWheelchairAccessible());
		retval.setTransferPenaltySeconds(modeSelector
				.getTransferPenaltySeconds());
		retval.setBikeSpeedFactor(modeSelector.getBikeSpeedFactor());
		retval.setBikeSafetyFactor(modeSelector.getBikeSafetyFactor());
		retval.setBikeComfortFactor(modeSelector.getBikeComfortFactor());
		return retval;
	}

	@Override
	public void onTransportModeChange(Set<TransportMode> modes) {
		if (plannerWidgetListener != null)
			plannerWidgetListener.onTransportModeChange(modes);
	}

	private void linkTo() {
		// Create URL
		String url = new PlannerState(getPlanRequestBean()).getUrl();
		// Display dialog box
		final DialogBox dialogBox = new DialogBox(true, true);
		VerticalPanel dialogBoxContents = new VerticalPanel();
		dialogBoxContents.setWidth("100%");
		dialogBox.setText(I18nUtils.tr("link.to.this.page"));
		Label message = new Label(I18nUtils.tr("copy.paste.link.hint"));
		dialogBoxContents.add(message);
		final TextBox urlTextBox = new TextBox();
		urlTextBox.setText(url);
		urlTextBox.setWidth("100%");
		dialogBoxContents.add(urlTextBox);
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				urlTextBox.selectAll();
				urlTextBox.setFocus(true);
			}
		});
		Button button = new Button(I18nUtils.tr("ok"), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		dialogBoxContents.add(button);
		dialogBox.setWidth("400px");
		dialogBox.setWidget(dialogBoxContents);
		dialogBox.center();
	}

}

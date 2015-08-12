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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.mecatran.otp.gwt.client.i18n.I18nUtils;
import com.mecatran.otp.gwt.client.model.ModeCapabilitiesBean;
import com.mecatran.otp.gwt.client.model.ModeSet;
import com.mecatran.otp.gwt.client.model.TransportMode;

public class DropDownModeSelectorWidget extends Composite implements
		ModeSelectorWidget {

	private static final float BIKE_COMFORT_FACTOR = 0.3f;

	private static final float WALK_SPEED_WHEELCHAIR = 2.0f;
	private static final float WALK_SPEED_SLOW = 4.0f;
	private static final float WALK_SPEED_MEDIUM = 5.0f;
	private static final float WALK_SPEED_FAST = 6.0f;

	private FlowPanel dateTimePanel;
	private ListBox departureOrArrivalListBox;
	private DateBox dateBox;
	private TimePicker timePicker;

	private ExtValueListBox<ModeSet> modeDropdown;
	private ExtValueListBox<Float> walkSpeedDropdown;
	private ExtValueListBox<Float> bikeSpeedDropdown;
	private ExtValueListBox<Float> bikeFactorsDropdown;
	private ExtValueListBox<Integer> transfersDropdown;
	private ExtValueListBox<Integer> maxWalkDistanceDropdown;
	private Anchor showHideAdvOptsAnchor;
	private Panel toHidePanel;
	private Panel walkOptionsPanel;
	private Panel bikeOptionsPanel;
	private Panel transitOptionsPanel;
	private float walkSpeedKph;
	private float bikeSpeedKph;
	private boolean wheelchairAccessible;
	private float bikeSpeedFactor;
	private int maxWalkDistanceMeters;
	private int transferPenaltySeconds;
	private ModeSelectorListener listener;

	public DropDownModeSelectorWidget() {
		// Build the components
		Panel rootPanel = new VerticalPanel();

		// Mode selector
		Panel modePanel = new HorizontalPanel();
		modePanel.addStyleName("modes-panel");
		modePanel.addStyleName("modes-icon");
		modeDropdown = new ExtValueListBox<ModeSet>();
		modeDropdown.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				handleModeChange();
			}
		});
		modeDropdown.setSelectedIndex(0);
		modePanel.add(modeDropdown);
		rootPanel.add(modePanel);

		// Departure/arrival
		dateTimePanel = new FlowPanel();
		dateTimePanel.addStyleName("datetime-panel");
		dateTimePanel.addStyleName("clock-icon");
		departureOrArrivalListBox = new ListBox();
		departureOrArrivalListBox.addItem(I18nUtils.tr("depart.at"));
		departureOrArrivalListBox.addItem(I18nUtils.tr("arrive.at"));
		departureOrArrivalListBox.setSelectedIndex(0);
		departureOrArrivalListBox.getElement().getStyle()
				.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		dateTimePanel.add(departureOrArrivalListBox);
		// Date & time
		rootPanel.add(dateTimePanel);
		HorizontalPanel datePanel = new HorizontalPanel();
		datePanel.addStyleName("date-panel");
		datePanel.getElement().getStyle()
				.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		dateTimePanel.add(datePanel);
		DateTimeFormat dateFormat = DateTimeFormat.getFormat(I18nUtils
				.tr("date.format.long"));
		dateBox = new DateBox();
		dateBox.setValue(new Date());
		dateBox.setFormat(new DateBox.DefaultFormat(dateFormat));
		datePanel.add(dateBox);
		Label toLabel = new Label(I18nUtils.tr("at.time"));
		toLabel.addStyleName("datetime-to-label");
		toLabel.getElement().getStyle()
				.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		dateTimePanel.add(toLabel);
		HorizontalPanel timePanel = new HorizontalPanel();
		timePanel.addStyleName("time-panel");
		timePanel.getElement().getStyle()
				.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		dateTimePanel.add(timePanel);
		timePicker = new TimePicker(new Date());
		timePanel.add(timePicker);

		// Show/hide bar & panel
		showHideAdvOptsAnchor = new Anchor(I18nUtils.tr("show.options"));
		showHideAdvOptsAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				boolean visible = !toHidePanel.isVisible();
				toHidePanel.setVisible(visible);
				showHideAdvOptsAnchor.setText(I18nUtils
						.tr(visible ? "hide.options" : "show.options"));
				if (visible) {
					showHideAdvOptsAnchor.removeStyleName("collapsed-icon");
					showHideAdvOptsAnchor.addStyleName("expanded-icon");
				} else {
					showHideAdvOptsAnchor.addStyleName("collapsed-icon");
					showHideAdvOptsAnchor.removeStyleName("expanded-icon");
				}
			}
		});
		showHideAdvOptsAnchor.addStyleName("show-hide-options");
		showHideAdvOptsAnchor.addStyleName("collapsed-icon");
		// showHideAdvOptsAnchor.setVisible(false);
		rootPanel.add(showHideAdvOptsAnchor);
		toHidePanel = new VerticalPanel();
		toHidePanel.setVisible(false);
		rootPanel.add(toHidePanel);

		// Walk options panel
		walkOptionsPanel = new HorizontalPanel();
		walkOptionsPanel.addStyleName("walk-speed-panel");
		walkOptionsPanel.addStyleName("walk-speed-icon");
		walkSpeedDropdown = new ExtValueListBox<Float>();
		// TODO Customize walk speeds in config
		refillWalkSpeedDropdown();
		walkOptionsPanel.add(walkSpeedDropdown);
		maxWalkDistanceDropdown = new ExtValueListBox<Integer>();
		maxWalkDistanceDropdown.addItem("< 200 m", 200);
		maxWalkDistanceDropdown.addItem("< 500 m", 500);
		maxWalkDistanceDropdown.addItem("< 1 km", 1000);
		maxWalkDistanceDropdown.addItem("< 2 km", 2000);
		maxWalkDistanceDropdown.addItem("< 5 km", 5000);
		maxWalkDistanceDropdown.setSelectedValue(1000);
		walkOptionsPanel.add(maxWalkDistanceDropdown);
		toHidePanel.add(walkOptionsPanel);

		// Bike options panel
		bikeOptionsPanel = new HorizontalPanel();
		bikeOptionsPanel.addStyleName("bike-speed-panel");
		bikeOptionsPanel.addStyleName("bike-speed-icon");
		bikeSpeedDropdown = new ExtValueListBox<Float>();
		// TODO Customize bike speeds in config
		bikeSpeedDropdown.addItem(I18nUtils.tr("bike.speed.slow"), 12.0f);
		bikeSpeedDropdown.addItem(I18nUtils.tr("bike.speed.medium"), 16.0f);
		bikeSpeedDropdown.addItem(I18nUtils.tr("bike.speed.fast"), 22.0f);
		bikeSpeedDropdown.setSelectedIndex(1);
		bikeOptionsPanel.add(bikeSpeedDropdown);
		bikeFactorsDropdown = new ExtValueListBox<Float>();
		bikeFactorsDropdown.addItem(I18nUtils.tr("bike.factor.safest"), 0.0f);
		bikeFactorsDropdown.addItem(I18nUtils.tr("bike.factor.medium"),
				(1.0f - BIKE_COMFORT_FACTOR) / 2.0f);
		bikeFactorsDropdown.addItem(I18nUtils.tr("bike.factor.fastest"),
				1.0f - BIKE_COMFORT_FACTOR);
		bikeFactorsDropdown.setSelectedIndex(1);
		bikeOptionsPanel.add(bikeFactorsDropdown);
		toHidePanel.add(bikeOptionsPanel);

		// Transit options panel
		transitOptionsPanel = new HorizontalPanel();
		transitOptionsPanel.addStyleName("transfers-options-panel");
		transitOptionsPanel.addStyleName("transfers-options-icon");
		transfersDropdown = new ExtValueListBox<Integer>();
		transfersDropdown.addItem(I18nUtils.tr("transit.best.trip"), 120);
		transfersDropdown.addItem(I18nUtils.tr("transit.least.transfers"), 240);
		transfersDropdown.addItem(I18nUtils.tr("transit.least.walk"), 60);
		transitOptionsPanel.add(transfersDropdown);
		toHidePanel.add(transitOptionsPanel);
		// Show/hide according to selected modes
		handleModeChange();
		initWidget(rootPanel);
	}

	@Override
	public void setModeCapabilities(ModeCapabilitiesBean modeCapabilities) {
		modeDropdown.clear();
		for (ModeSet modeSet : buildModeSetList(modeCapabilities)) {
			modeDropdown.addItem(modeSet.getHumanName(), modeSet);
		}
		showHideAdvOptsAnchor.setVisible(modeCapabilities
				.isHasAdvancedOptions());
		handleModeChange();
	}

	@Override
	public Set<TransportMode> getSelectedModes() {
		ModeSet selected = modeDropdown.getSelectedVal();
		if (selected != null) {
			return selected.getModes();
		} else {
			return Collections.emptySet();
		}
	}

	@Override
	public int getMaxWalkDistanceMeters() {
		updateOptions();
		if (shouldIgnoreLazynessParameters(getSelectedModes()))
			return 1000000;
		return maxWalkDistanceMeters;
	}

	@Override
	public float getWalkReluctanceFactor() {
		updateOptions();
		if (shouldIgnoreLazynessParameters(getSelectedModes()))
			return 1.0f;
		return 2.0f;
	}

	/**
	 * Should ignore "lazyness parameters" (maxWalkDistance, walkReluctance) due
	 * to requested modes: bike-share or bike+transit.
	 * 
	 * @param modes
	 * @return
	 */
	private boolean shouldIgnoreLazynessParameters(Set<TransportMode> modes) {
		// If we have bike mode, always ignore lazyness :)
		return modes.contains(TransportMode.BICYCLE)
				|| modes.contains(TransportMode.BICYCLE_RENTAL);
	}

	@Override
	public boolean isWheelchairAccessible() {
		updateOptions();
		return wheelchairAccessible;
	}

	@Override
	public float getWalkSpeedKph() {
		updateOptions();
		return walkSpeedKph;
	}

	@Override
	public float getBikeSpeedKph() {
		updateOptions();
		return bikeSpeedKph;
	}

	@Override
	public int getTransferPenaltySeconds() {
		updateOptions();
		return transferPenaltySeconds;
	}

	@Override
	public float getBikeSpeedFactor() {
		updateOptions();
		return bikeSpeedFactor;
	}

	@Override
	public float getBikeSafetyFactor() {
		updateOptions();
		return 1.0f - BIKE_COMFORT_FACTOR - bikeSpeedFactor;
	}

	@Override
	public float getBikeComfortFactor() {
		return BIKE_COMFORT_FACTOR;
	}

	private void refillWalkSpeedDropdown() {
		// TODO Make the values parametrable
		Float oldWalkSpeed = walkSpeedDropdown.getSelectedVal();
		Set<TransportMode> modes = getSelectedModes();
		boolean hasBike = modes.contains(TransportMode.BICYCLE)
				|| modes.contains(TransportMode.BICYCLE_RENTAL);
		walkSpeedDropdown.clear();
		if (oldWalkSpeed == null)
			oldWalkSpeed = 5.0f;
		if (hasBike) {
			if (oldWalkSpeed == 2.0f)
				oldWalkSpeed = 5.0f;
		} else {
			walkSpeedDropdown.addItem(I18nUtils.tr("walk.speed.handicap"),
					WALK_SPEED_WHEELCHAIR);
		}
		walkSpeedDropdown.addItem(I18nUtils.tr("walk.speed.slow"),
				WALK_SPEED_SLOW);
		walkSpeedDropdown.addItem(I18nUtils.tr("walk.speed.medium"),
				WALK_SPEED_MEDIUM);
		walkSpeedDropdown.addItem(I18nUtils.tr("walk.speed.fast"),
				WALK_SPEED_FAST);
		// Reselect currently selected value if available
		walkSpeedDropdown.setSelectedValue(oldWalkSpeed);
	}

	private void updateOptions() {
		walkSpeedKph = walkSpeedDropdown.getSelectedVal();
		// Wheelchair access is just a form a walk speed...
		wheelchairAccessible = walkSpeedKph <= 2;
		bikeSpeedKph = bikeSpeedDropdown.getSelectedVal();
		bikeSpeedFactor = bikeFactorsDropdown.getSelectedVal();
		transferPenaltySeconds = transfersDropdown.getSelectedVal();
		maxWalkDistanceMeters = maxWalkDistanceDropdown.getSelectedVal();
	}

	private void handleModeChange() {
		Set<TransportMode> modes = getSelectedModes();
		refillWalkSpeedDropdown();
		boolean hasWalk = modes.contains(TransportMode.WALK);
		boolean hasTransit = modes.contains(TransportMode.BUS)
				|| modes.contains(TransportMode.TRAM)
				|| modes.contains(TransportMode.RAIL)
				|| modes.contains(TransportMode.SUBWAY)
				|| modes.contains(TransportMode.FERRY)
				|| modes.contains(TransportMode.TROLLEY);
		boolean hasBike = modes.contains(TransportMode.BICYCLE)
				|| modes.contains(TransportMode.BICYCLE_RENTAL);
		walkOptionsPanel.setVisible(hasWalk);
		maxWalkDistanceDropdown.setVisible(hasWalk && hasTransit);
		bikeOptionsPanel.setVisible(hasBike);
		transitOptionsPanel.setVisible(hasTransit);
		dateTimePanel.setVisible(hasTransit);
		if (listener != null)
			listener.onTransportModeChange(modes);
	}

	private List<ModeSet> buildModeSetList(ModeCapabilitiesBean modeCapabilities) {
		List<ModeSet> retval = new ArrayList<ModeSet>();
		if (modeCapabilities.isHasTransit()) {
			// Transit only
			retval.add(new ModeSet(I18nUtils.tr("mode.transit", "Casimir"),
					TransportMode.WALK, TransportMode.BUS, TransportMode.FERRY,
					TransportMode.GONDOLA, TransportMode.RAIL,
					TransportMode.SUBWAY, TransportMode.TRAM,
					TransportMode.TROLLEY));
		}
		if (modeCapabilities.isHasWalkOnly()) {
			// Walk only
			retval.add(new ModeSet(I18nUtils.tr("mode.walk"),
					TransportMode.WALK));
		}
		if (modeCapabilities.isHasBikeOnly()) {
			// Bike only
			retval.add(new ModeSet(I18nUtils.tr("mode.bike"),
					TransportMode.BICYCLE));
		}
		if (modeCapabilities.isHasBikeAndTransit()) {
			// Bike + transit
			retval.add(new ModeSet(I18nUtils.tr("mode.transit.bike"),
					TransportMode.BICYCLE, TransportMode.BUS,
					TransportMode.FERRY, TransportMode.GONDOLA,
					TransportMode.RAIL, TransportMode.SUBWAY,
					TransportMode.TRAM, TransportMode.TROLLEY));
		}
		if (modeCapabilities.isHasBikeRental()) {
			// Rental bike only
			retval.add(new ModeSet(I18nUtils.tr("mode.bike-rental"),
					TransportMode.BICYCLE_RENTAL));
		}
		if (modeCapabilities.isHasBikeRental()
				&& modeCapabilities.isHasTransit()
				&& modeCapabilities.isHasBikeAndTransit()) {
			// Rental bike + transit
			retval.add(new ModeSet(I18nUtils.tr("mode.transit.bike-rental"),
					TransportMode.BICYCLE_RENTAL, TransportMode.BUS,
					TransportMode.FERRY, TransportMode.GONDOLA,
					TransportMode.RAIL, TransportMode.SUBWAY,
					TransportMode.TRAM, TransportMode.TROLLEY));
		}
		return retval;
	}

	@Override
	public boolean isDateDeparture() {
		return departureOrArrivalListBox.getSelectedIndex() == 0;
	}

	@Override
	public void setDateDeparture(boolean dateDeparture) {
		departureOrArrivalListBox.setSelectedIndex(dateDeparture ? 0 : 1);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Date getDateTime() {
		Date dateTime = dateBox.getValue();
		Date time = new Date(timePicker.getValue());
		dateTime.setHours(time.getHours());
		dateTime.setMinutes(time.getMinutes());
		dateTime.setSeconds(time.getSeconds());
		return dateTime;
	}

	@Override
	public void setDateTime(Date dateTime) {
		dateBox.setValue(dateTime);
		timePicker.setValue(dateTime.getTime());
	}

	@Override
	public void setModes(Set<TransportMode> modes) {
		/*
		 * Guess the best selection in the mode dropdown by computing the number
		 * of matches between the set of modes of each choice and the asked mode
		 * set. We select the best match. Please be aware that it could be not
		 * exactly what the called requested, but here we have to select amongst
		 * a restricted choice of mode sets anyway.
		 */
		int bestIndex = -1;
		int bestMatch = -100;
		for (int i = 0; i < modeDropdown.getItemCount(); i++) {
			ModeSet modeSet = modeDropdown.getExtValue(i);
			Set<TransportMode> modes2 = modeSet.getModes();
			int n1 = 0;
			int n2 = 0;
			for (TransportMode mode : modes) {
				if (modes2.contains(mode))
					n1++;
			}
			for (TransportMode mode2 : modes2) {
				if (!modes.contains(mode2))
					n2++;
			}
			int match = n1 - n2;
			if (match > bestMatch) {
				bestMatch = match;
				bestIndex = i;
			}
		}
		if (bestIndex > -1) {
			modeDropdown.setSelectedIndex(bestIndex);
			/*
			 * The "on change" event is *not* called by setSelectedIndex(), so
			 * we have to call it ourselves.
			 */
			handleModeChange();
		}
	}

	@Override
	public void setWheelchairAccessible(boolean wheelchairAccessible) {
		walkSpeedDropdown
				.setSelectedValue(wheelchairAccessible ? WALK_SPEED_WHEELCHAIR
						: WALK_SPEED_MEDIUM);
	}

	@Override
	public void setModeSelectorListener(
			ModeSelectorListener modeSelectorListener) {
		this.listener = modeSelectorListener;
	}
}

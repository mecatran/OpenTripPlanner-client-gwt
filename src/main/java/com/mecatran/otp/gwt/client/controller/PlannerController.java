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
package com.mecatran.otp.gwt.client.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.mecatran.otp.gwt.client.PlannerWidgetConfig;
import com.mecatran.otp.gwt.client.i18n.I18nUtils;
import com.mecatran.otp.gwt.client.model.AlertBean;
import com.mecatran.otp.gwt.client.model.ItineraryBean;
import com.mecatran.otp.gwt.client.model.LocationBean;
import com.mecatran.otp.gwt.client.model.ModeCapabilitiesBean;
import com.mecatran.otp.gwt.client.model.POIBean;
import com.mecatran.otp.gwt.client.model.PlanRequestBean;
import com.mecatran.otp.gwt.client.model.TransportMode;
import com.mecatran.otp.gwt.client.model.Wgs84BoundsBean;
import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;
import com.mecatran.otp.gwt.client.proxies.AlertsSourceProxy;
import com.mecatran.otp.gwt.client.proxies.AlertsSourceProxy.AlertsListener;
import com.mecatran.otp.gwt.client.proxies.GeocoderMultiplexer;
import com.mecatran.otp.gwt.client.proxies.GeocoderProxy;
import com.mecatran.otp.gwt.client.proxies.GeocoderProxy.ReverseGeocoderListener;
import com.mecatran.otp.gwt.client.proxies.POIGeocoder;
import com.mecatran.otp.gwt.client.proxies.POISource;
import com.mecatran.otp.gwt.client.proxies.POISource.POIListener;
import com.mecatran.otp.gwt.client.proxies.TransitPlannerProxy;
import com.mecatran.otp.gwt.client.proxies.TransitPlannerProxy.TransitPlannerListener;
import com.mecatran.otp.gwt.client.proxies.dummy.LatLngGeocoder;
import com.mecatran.otp.gwt.client.proxies.otp.OtpGeocoderProxy;
import com.mecatran.otp.gwt.client.proxies.otp.OtpPlannerProxy;
import com.mecatran.otp.gwt.client.utils.FormatUtils;
import com.mecatran.otp.gwt.client.view.PlannerWidget;
import com.mecatran.otp.gwt.client.view.PlannerWidget.PlannerWidgetListener;
import com.mecatran.otp.gwt.client.view.PlannerWidgetImpl;
import com.mecatran.otp.gwt.client.view.PrintWidget;
import com.mecatran.otp.gwt.client.view.PrintWidget.PrintWidgetListener;
import com.mecatran.otp.gwt.client.view.PrintWidgetWindowImpl;

public class PlannerController implements PlannerWidgetListener,
		PrintWidgetListener, TransitPlannerListener {

	private PlannerWidgetConfig config;
	private PlannerWidget plannerWidget;
	private PrintWidget printWidget;
	private TransitPlannerProxy plannerProxy;
	private GeocoderProxy geocoderProxy;
	private AlertsSourceProxy alertsSourceProxy;
	private List<POISource> poiSources;
	private ReverseGeocoderListener startAddressReverseGeocoderListener;
	private ReverseGeocoderListener endAddressReverseGeocoderListener;
	private boolean busyPlanning = false;
	private int autoPlanAfterGeocode = 0;

	public PlannerController(PlannerWidgetConfig config) {
		this.config = config;
		I18nUtils.setLocale(config.getLang());
		FormatUtils.setTimeFormat(I18nUtils.tr("time.format.small"));
		FormatUtils.setDateFormat(I18nUtils.tr("date.format.small"));

		PlannerWidgetImpl theWidget = new PlannerWidgetImpl();
		plannerWidget = theWidget;
		plannerWidget.setPlannerWidgetListener(this);
		// Use the "external window" print form
		printWidget = new PrintWidgetWindowImpl(null);
		printWidget.setPrintWidgetListener(this);
		buildProxiesFromConfiguration(config);
		for (POISource poiSource : poiSources) {
			poiSource.addListener(new POIListener() {
				@Override
				public void poiUpdate(POISource source,
						Map<String, POIBean> pois) {
					plannerWidget.updatePOIs(source, pois);
				}
			});
		}
		if (alertsSourceProxy != null)
			alertsSourceProxy.setListener(new AlertsListener() {
				@Override
				public void generalAlertPublished(AlertBean alert) {
					plannerWidget.publishGeneralAlert(alert);
				}
			});
		plannerWidget.setGeocoderProxy(geocoderProxy);
		startAddressReverseGeocoderListener = new ReverseGeocoderListener() {
			@Override
			public void onReverseGeocodingDone(LocationBean location, int factor) {
				plannerWidget.setStartLocation(location, false);
				if (autoPlanAfterGeocode > 0)
					autoPlanAfterGeocode--;
				if (autoPlanAfterGeocode == 0)
					planRouteIfPossible();
			}
		};
		endAddressReverseGeocoderListener = new ReverseGeocoderListener() {
			@Override
			public void onReverseGeocodingDone(LocationBean location, int factor) {
				plannerWidget.setEndLocation(location, false);
				if (autoPlanAfterGeocode > 0)
					autoPlanAfterGeocode--;
				if (autoPlanAfterGeocode == 0)
					planRouteIfPossible();
			}
		};
		if (!Double.isNaN(config.getMinLat())
				&& !Double.isNaN(config.getMinLon())
				&& !Double.isNaN(config.getMaxLat())
				&& !Double.isNaN(config.getMaxLon())) {
			Wgs84BoundsBean bounds = new Wgs84BoundsBean();
			bounds.extend(new Wgs84LatLonBean(config.getMinLat(), config
					.getMinLon()));
			bounds.extend(new Wgs84LatLonBean(config.getMaxLat(), config
					.getMaxLon()));
			setBounds(bounds);
		}
	}

	public Widget getPlannerWidget() {
		return plannerWidget.getAsWidget();
	}

	public void restoreState(PlannerState state) {
		PlanRequestBean planRequestBean = state.getPlanRequestBean();
		LocationBean departure = planRequestBean.getDeparture();
		if (departure != null) {
			plannerWidget.setStartLocation(departure, false);
			autoPlanAfterGeocode++;
			// Reverse geocode if needed
			if (departure.getLocation() != null) {
				onStartPointSelected(departure.getLocation());
			}
		}
		LocationBean arrival = planRequestBean.getArrival();
		if (arrival != null) {
			plannerWidget.setEndLocation(arrival, false);
			autoPlanAfterGeocode++;
			// Reverse geocode if needed
			if (arrival.getLocation() != null) {
				onEndPointSelected(arrival.getLocation());
			}
		}
		plannerWidget.setDateDeparture(planRequestBean.isDateDeparture());
		if (planRequestBean.getDate() != null)
			plannerWidget.setDate(planRequestBean.getDate());
		if (planRequestBean.getModes() != null)
			plannerWidget.setModes(planRequestBean.getModes());
		plannerWidget.setWheelchairAccessible(planRequestBean
				.isWheelchairAccessible());
	}

	private void buildProxiesFromConfiguration(PlannerWidgetConfig config) {

		/* Planner proxy */
		if (config.getProxyType().equals(PlannerWidgetConfig.PROXY_OTP)) {
			plannerProxy = new OtpPlannerProxy(this, config.getOtpUrl(),
					config.getRouterId(), config.getMaxItineraries());
		} else {
			Window.alert("Invalid proxy type: " + config.getProxyType());
			throw new RuntimeException("Invalid proxy type: "
					+ config.getProxyType());
		}

		/* Alert sources */
		// TODO Configure
		// alertsSourceProxy = new XyzAlertsSourceProxy();
		// alertsSourceProxy.configure(config.getLang());

		poiSources = new ArrayList<POISource>();
		/* Bike rental sources */
		if (config.getProxyType().equals(PlannerWidgetConfig.PROXY_OTP)
				&& config.isHasBikeRental()) {
			// TODO
			// poiSources.add(new OtpBikeRentalPoiSource(config.getOtpUrl(),
			// config.getRouterId()));
		}

		/* POIs sources */
		// TODO Configure
		// poiSources.add(new XyzPOISource());

		/* Geocoder(s) */
		GeocoderMultiplexer geocoderMultiplexer = new GeocoderMultiplexer();
		if (config.getProxyType().equals(PlannerWidgetConfig.PROXY_OTP)) {
			geocoderMultiplexer.addGeocoder(new OtpGeocoderProxy(config
					.getOtpUrl(), config.getRouterId()));
		} else {
			// TODO Implement OSM Nominatim geocoder
			// Poor man fallback: use a lat,lng dummy geocoder
			geocoderMultiplexer.addGeocoder(new LatLngGeocoder());
		}
		// Add a geocoder for each POI source
		for (POISource poiSource : poiSources) {
			// TODO Configure "useBounds" if needed. How?
			geocoderMultiplexer.addGeocoder(new POIGeocoder(poiSource, false));
		}
		geocoderProxy = geocoderMultiplexer;
	}

	private void setBounds(Wgs84BoundsBean bounds) {
		plannerWidget.setBounds(bounds);
		geocoderProxy.configure(bounds, config.getMainCountryName());
	}

	private void setModeCapabilities(ModeCapabilitiesBean modeCapabilities) {
		plannerWidget.setModeCapabilities(modeCapabilities);
		plannerWidget.switchMapBackground(plannerWidget.getPlanRequestBean()
				.getModes());
	}

	/* === PlannerWidgetListener === */

	@Override
	public void onPlanningRequested() {
		PlanRequestBean planRequest = plannerWidget.getPlanRequestBean();
		boolean okDeparture = planRequest.getDeparture().isSet(
				plannerProxy.isRequireGeocoding());
		boolean okArrival = planRequest.getArrival().isSet(
				plannerProxy.isRequireGeocoding());
		if (!okDeparture || !okArrival) {
			String message = "";
			if (!okDeparture)
				message += I18nUtils.tr("departure.address.not.found",
						planRequest.getDeparture().getAddress());
			if (!okArrival)
				message += I18nUtils.tr("arrival.address.not.found",
						planRequest.getDeparture().getAddress());
			if (!okDeparture || !okArrival)
				message += "\n\n" + I18nUtils.tr("address.not.found");
			plannerWidget.showError(message);
		} else {
			planRouteIfPossible();
		}
	}

	private void planRouteIfPossible() {
		PlanRequestBean planRequest = plannerWidget.getPlanRequestBean();
		if (!busyPlanning
				&& planRequest.getDeparture().isSet(
						plannerProxy.isRequireGeocoding())
				&& planRequest.getArrival().isSet(
						plannerProxy.isRequireGeocoding())) {
			busyPlanning = true;
			plannerWidget.setBusy(true);
			plannerWidget.clearItineraries();
			plannerProxy.planRoute(planRequest);
		}
	}

	@Override
	public void onItineraryRemoved(ItineraryBean itinerary) {
		// Nothing special for the moment.
	}

	@Override
	public void onItinerarySelected(ItineraryBean itinerary) {
		plannerWidget.showItineraryOnMap(itinerary, false);
	}

	@Override
	public void onItineraryHover(ItineraryBean itinerary) {
		plannerWidget.showItineraryOnMap(itinerary, true);
	}

	@Override
	public void onItineraryPinned(ItineraryBean itinerary, boolean pinned) {
		// Nothing special for the moment.
	}

	@Override
	public void onItineraryPrintRequest(ItineraryBean itinerary) {
		printWidget.printItinerary(itinerary);
	}

	@Override
	public void onStartPointSelected(Wgs84LatLonBean location) {
		plannerWidget.unselectItinerary();
		geocoderProxy.reverseGeocode(location,
				startAddressReverseGeocoderListener);
	}

	@Override
	public void onEndPointSelected(Wgs84LatLonBean location) {
		plannerWidget.unselectItinerary();
		geocoderProxy.reverseGeocode(location,
				endAddressReverseGeocoderListener);
	}

	@Override
	public void onStartLocationSelected(LocationBean location) {
		plannerWidget.setStartLocation(location, true);
		if (autoPlanAfterGeocode > 0) {
			autoPlanAfterGeocode--;
			if (autoPlanAfterGeocode == 0)
				planRouteIfPossible();
		}
	}

	@Override
	public void onEndLocationSelected(LocationBean location) {
		plannerWidget.setEndLocation(location, true);
		if (autoPlanAfterGeocode > 0) {
			autoPlanAfterGeocode--;
			if (autoPlanAfterGeocode == 0)
				planRouteIfPossible();
		}
	}

	@Override
	public void onLocationHover(LocationBean location) {
		plannerWidget.previewLocation(location);
	}

	@Override
	public void onTransportModeChange(Set<TransportMode> modes) {
		if (plannerWidget != null)
			plannerWidget.switchMapBackground(modes);
	}

	/* === TransitPlannerListener === */

	@Override
	public void onItineraryFound(List<ItineraryBean> itineraries) {
		busyPlanning = false;
		plannerWidget.setBusy(false);
		// It is already sorted, but add first last element
		for (int i = itineraries.size() - 1; i >= 0; i--) {
			ItineraryBean itinerary = itineraries.get(i);
			if (alertsSourceProxy != null)
				alertsSourceProxy.fillAlerts(itinerary);
			plannerWidget.addItinerary(itinerary, i == 0);
		}
	}

	@Override
	public void onItineraryError(String errorMessage) {
		busyPlanning = false;
		plannerWidget.setBusy(false);
		plannerWidget.showError(errorMessage);
	}

	@Override
	public void onPlannerConfigured(Wgs84BoundsBean bounds,
			ModeCapabilitiesBean modeCapabilities) {
		if (bounds != null) {
			setBounds(bounds);
		}
		if (modeCapabilities != null) {
			modeCapabilities.setHasWalkOnly(modeCapabilities.isHasWalkOnly()
					&& config.isHasWalkOnly());
			modeCapabilities.setHasBikeOnly(modeCapabilities.isHasBikeOnly()
					&& config.isHasBikeOnly());
			modeCapabilities.setHasTransit(modeCapabilities.isHasTransit()
					&& config.isHasTransit());
			modeCapabilities.setHasBikeRental(modeCapabilities
					.isHasBikeRental() && config.isHasBikeRental());
			modeCapabilities.setHasBikeAndTransit(modeCapabilities
					.isHasBikeAndTransit() && config.isHasBikeAndTransit());
			setModeCapabilities(modeCapabilities);
		}
	}

	/* === PrintWidgetListener === */

	@Override
	public void onPrintDone() {

	}
}

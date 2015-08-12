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
package com.mecatran.otp.gwt.client.proxies.otp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.mecatran.otp.gwt.client.i18n.I18nUtils;
import com.mecatran.otp.gwt.client.model.AbsoluteDirection;
import com.mecatran.otp.gwt.client.model.ItineraryBean;
import com.mecatran.otp.gwt.client.model.ItineraryLegBean;
import com.mecatran.otp.gwt.client.model.ItineraryRoadLegBean;
import com.mecatran.otp.gwt.client.model.ItineraryRoadStepBean;
import com.mecatran.otp.gwt.client.model.ItineraryTransitLegBean;
import com.mecatran.otp.gwt.client.model.PlanRequestBean;
import com.mecatran.otp.gwt.client.model.RelativeDirection;
import com.mecatran.otp.gwt.client.model.TransitRouteBean;
import com.mecatran.otp.gwt.client.model.TransitStopBean;
import com.mecatran.otp.gwt.client.model.TransportMode;
import com.mecatran.otp.gwt.client.model.Wgs84BoundsBean;
import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;
import com.mecatran.otp.gwt.client.proxies.TransitPlannerProxy;
import com.mecatran.otp.gwt.client.utils.HttpUtils;
import com.mecatran.otp.gwt.client.utils.HttpUtils.DownloadListener;
import com.mecatran.otp.gwt.client.utils.PolylineEncoder;

public class OtpPlannerProxy implements TransitPlannerProxy {

	private TransitPlannerListener plannerListener;
	private String baseUrl;
	private String routerId;
	private String mainCountryName;
	private int maxItineraries = 3;

	public OtpPlannerProxy(String baseUrl, String routerId, int maxItineraries) {
		this.baseUrl = baseUrl;
		this.routerId = routerId;
	}

	@Override
	public boolean isRequireGeocoding() {
		return true;
	}

	@Override
	public void setTransitPlannerListener(TransitPlannerListener listener) {
		plannerListener = listener;
	}

	@Override
	public void planRoute(final PlanRequestBean planRequest) {

		String url = buildGetUrl(planRequest);
		HttpUtils.downloadJson(url, new DownloadListener<OtpPlannerResponse>() {

			@Override
			public void onSuccess(OtpPlannerResponse otpResponse) {
				OtpError otpError = otpResponse.getError();
				if (otpError != null) {
					plannerListener.onItineraryError(otpError.getMsg() + " ("
							+ otpError.getId() + ")");
				} else {
					List<ItineraryBean> itineraries = new ArrayList<ItineraryBean>();
					for (int i = 0; i < otpResponse.getItineraries().length(); i++) {
						itineraries.add(convert(otpResponse.getItineraries()
								.get(i), planRequest));
					}
					for (ItineraryBean itinerary : itineraries) {
						itinerary.setStartAddress(humanize(planRequest
								.getDeparture().getAddress()));
						itinerary.setEndAddress(humanize(planRequest
								.getArrival().getAddress()));
						itinerary.setRequest(planRequest);
					}
					plannerListener.onItineraryFound(itineraries);
				}
			}

			@Override
			public void onFailure(String msg) {
				plannerListener.onItineraryError(msg);
			}
		});
	}

	private ItineraryBean convert(OtpItinerary otpiti,
			PlanRequestBean planRequest) {
		ItineraryBean iti = new ItineraryBean();
		iti.setDepartureTime(new Date(Math.round(otpiti.getStartTime())));
		iti.setArrivalTime(new Date(Math.round(otpiti.getEndTime())));
		iti.setDurationSeconds(otpiti.getDuration());
		iti.setStartLocation(planRequest.getDeparture().getLocation());
		iti.setEndLocation(planRequest.getArrival().getLocation());
		iti.setRequest(planRequest);
		for (int i = 0; i < otpiti.getLegs().length(); i++) {
			iti.addLeg(convert(otpiti.getLegs().get(i), planRequest, iti
					.getEndLocation().toString()));
		}

		// Itinerary distance is sum of all legs distances
		// Compute bounds
		long distanceMeters = 0L;
		Wgs84BoundsBean bounds = new Wgs84BoundsBean();
		long time = iti.getDepartureTime().getTime();
		for (ItineraryLegBean leg : iti.getLegs()) {
			distanceMeters += leg.getDistanceMeters();
			for (Wgs84LatLonBean point : leg.getPath()) {
				bounds.extend(point);
			}
			ItineraryTransitLegBean transitLeg = leg.getAsTransitLeg();
			if (transitLeg != null) {
				long departureTime = transitLeg.getDepartureTime().getTime();
				if (time < departureTime) {
					transitLeg
							.setWaitDurationSeconds((departureTime - time) / 1000);
				}
				time = transitLeg.getArrivalTime().getTime();
			} else {
				time += leg.getDurationSeconds() * 1000;
			}
		}
		iti.setDistanceMeters(distanceMeters);
		iti.setBounds(bounds);
		return iti;
	}

	private ItineraryLegBean convert(OtpLeg segment,
			PlanRequestBean planRequest, String defaultEndLocation) {
		ItineraryLegBean leg;
		if (segment.isRoad()) {
			ItineraryRoadLegBean roadLeg = new ItineraryRoadLegBean();
			leg = roadLeg;
		} else if (segment.isTransit()) {
			ItineraryTransitLegBean transitLeg = new ItineraryTransitLegBean();
			leg = transitLeg;
		} else {
			throw new IllegalArgumentException("Invalid trip segment type");
		}
		leg.setDistanceMeters(Math.round(segment.getDistance()));
		leg.setDurationSeconds(Math.round(segment.getEndTime()
				- segment.getStartTime() + 500) / 1000);
		leg.setStartLocation(convertLocation(segment.getFrom()));
		leg.setEndLocation(convertLocation(segment.getTo()));
		leg.setPath(convert(leg.getStartLocation(), leg.getEndLocation(),
				segment.getEncodedGeometry()));
		leg.setMode(OtpPlannerResponse.convertMode(planRequest.getModes()
				.contains(TransportMode.BICYCLE_RENTAL), segment.getMode()));
		if (segment.isRoad()) {
			String arrivalPlaceName = segment.getTo().getName();
			ItineraryRoadLegBean roadLeg = leg.getAsRoadLeg();
			switch (leg.getMode()) {
			default:
			case WALK:
				leg.setInstructions(I18nUtils.tr("walk.to", arrivalPlaceName));
				break;
			case BICYCLE:
			case BICYCLE_RENTAL:
				leg.setInstructions(I18nUtils.tr("bike.to", arrivalPlaceName));
				break;
			case CAR:
				leg.setInstructions(I18nUtils.tr("drive.to", arrivalPlaceName));
				break;
			}
			List<ItineraryRoadStepBean> roadSteps = new ArrayList<ItineraryRoadStepBean>();
			ItineraryRoadStepBean lastStep = null;
			for (int i = 0; i < segment.getSteps().length(); i++) {
				OtpRoadStep ostep = segment.getSteps().get(i);
				ItineraryRoadStepBean step = new ItineraryRoadStepBean();
				step.setStartLocation(new Wgs84LatLonBean(ostep.getLatitude(),
						ostep.getLongitude()));
				if (lastStep != null)
					lastStep.setEndLocation(step.getStartLocation());
				step.setAbsoluteDirection(convertAbsDir(ostep
						.getAbsoluteDirection()));
				step.setRelativeDirection(convertRelDir(ostep
						.getRelativeDirection()));
				step.setDistanceMeters(Math.round(ostep.getDistance()));
				float speedMs;
				// We do not have a duration per leg.
				// TODO: Do linear interpolation based on leg distance and time
				switch (roadLeg.getMode()) {
				default:
				case WALK:
					speedMs = planRequest.getWalkSpeedKph() / 3.6f;
					break;
				case BICYCLE:
				case BICYCLE_RENTAL:
					speedMs = planRequest.getBikeSpeedKph() / 3.6f;
					break;
				case CAR:
					speedMs = 10; // TODO
					break;
				}
				step.setDurationSeconds(Math.round(step.getDistanceMeters()
						* speedMs));
				String message = ostep.getStreetName();
				String roadName = ostep.getStreetName();
				if (ostep.isGeneratedName())
					roadName = I18nUtils.tr("unnamed.road");
				if (ostep.isStayOn())
					message = I18nUtils.tr("stay.on", roadName);
				else if (step.getRelativeDirection() != null)
					message = I18nUtils.tr("reldir."
							+ step.getRelativeDirection().toString(), roadName);
				else if (step.getAbsoluteDirection() != null)
					message = I18nUtils.tr("absdir."
							+ step.getAbsoluteDirection().toString(), roadName);
				step.setInstructions(message);
				roadSteps.add(step);
				lastStep = step;
			}
			if (lastStep != null) {
				lastStep.setEndLocation(leg.getEndLocation());
			}
			roadLeg.setRoadSteps(roadSteps);
		} else if (segment.isTransit()) {
			ItineraryTransitLegBean transitLeg = leg.getAsTransitLeg();
			transitLeg.setDepartureTime(new Date(Math.round(segment
					.getStartTime())));
			transitLeg
					.setArrivalTime(new Date(Math.round(segment.getEndTime())));
			transitLeg.setDepartureStop(convertTransitStop(segment.getFrom()));
			transitLeg.setArrivalStop(convertTransitStop(segment.getTo()));
			transitLeg.setDepartureTimezone(null); // TODO Unused
			transitLeg.setArrivalTimezone(null); // TODO Unused
			transitLeg.setDistanceMeters(Math.round(segment.getDistance()));
			transitLeg.setDurationSeconds(Math.round(segment.getEndTime()
					- segment.getStartTime() + 500) / 1000);
			transitLeg.setHeadsign(segment.getHeadsign());
			transitLeg.setWaitDurationSeconds(0); // TODO Unused
			transitLeg.setRoute(convertRoute(segment));
			String routeName = transitLeg.getRoute().getCode()
					+ (transitLeg.getRoute().getName() != null ? " ("
							+ transitLeg.getRoute().getName() + ")" : "");
			String headsign = transitLeg.getHeadsign() != null ? transitLeg
					.getHeadsign() : "";
			transitLeg.setInstructions(I18nUtils.tr("take.the.route",
					routeName, headsign));
		}
		return leg;
	}

	private Wgs84LatLonBean[] convert(Wgs84LatLonBean from, Wgs84LatLonBean to,
			String encodedGeometry) {
		if (encodedGeometry == null || encodedGeometry.length() == 0) {
			return new Wgs84LatLonBean[] { from, to };
		} else {
			return PolylineEncoder.decode(encodedGeometry);
		}
	}

	private Wgs84LatLonBean convertLocation(OtpPlace place) {
		return new Wgs84LatLonBean(place.getLatitude(), place.getLongitude());
	}

	private TransitStopBean convertTransitStop(OtpPlace stopPlace) {
		TransitStopBean stop = new TransitStopBean();
		stop.setId(stopPlace.getStopId());
		stop.setCode(stopPlace.getStopCode());
		stop.setName(stopPlace.getName());
		stop.setLocation(convertLocation(stopPlace));
		return stop;
	}

	private TransitRouteBean convertRoute(OtpLeg oleg) {
		TransitRouteBean route = new TransitRouteBean();
		route.setCode(oleg.getRouteShortName());
		route.setName(oleg.getRouteLongName());
		route.setForegroundColor("#" + oleg.getRouteTextColor());
		route.setBackgroundColor("#" + oleg.getRouteColor());
		return route;
	}

	private String convertTransportMode(TransportMode mode) {
		switch (mode) {
		default:
		case WALK:
			return "WALK";
		case BICYCLE:
			return "BICYCLE";
		case BICYCLE_RENTAL:
			return "WALK,BICYCLE_RENT";
		case CAR:
			return "CAR";
		case BUS:
			return "BUS";
		case RAIL:
			return "RAIL";
		case TRAM:
			return "TRAM";
		case SUBWAY:
			return "SUBWAY";
		case FERRY:
			return "FERRY";
		case PLANE:
			return "PLANE";
		case TROLLEY:
			// Note: CABLE_CAR seems to be broken in OTP (qualified modes)
			return "FUNICULAR";
		case GONDOLA:
			return "GONDOLA";
		}
	}

	// Assume a bit of compatibility
	private AbsoluteDirection convertAbsDir(String absDir) {
		for (AbsoluteDirection dir : AbsoluteDirection.values()) {
			if (dir.toString().equalsIgnoreCase(absDir))
				return dir;
		}
		return null;
	}

	// Assume a bit of compatibility
	private RelativeDirection convertRelDir(String relDir) {
		relDir = relDir.replace("_", "");
		for (RelativeDirection dir : RelativeDirection.values()) {
			if (dir.toString().equalsIgnoreCase(relDir))
				return dir;
		}
		return null;
	}

	private String buildGetUrl(PlanRequestBean planRequest) {
		// TODO We can't easily use the GWT UrlBuilder class,
		// as we would need to parse the provided base URL...
		StringBuffer sb = new StringBuffer(baseUrl);
		sb.append("routers/").append(routerId).append("/plan");
		Wgs84LatLonBean departure = planRequest.getDeparture().getLocation();
		Wgs84LatLonBean arrival = planRequest.getArrival().getLocation();
		sb.append("?fromPlace=");
		sb.append(URL.encodeQueryString(departure.getLat() + ","
				+ departure.getLon()));
		sb.append("&toPlace=");
		sb.append(URL.encodeQueryString(arrival.getLat() + ","
				+ arrival.getLon()));
		DateTimeFormat df = DateTimeFormat.getFormat("yyyy/MM/dd");
		DateTimeFormat tf = DateTimeFormat.getFormat("HH:mm:ss");
		Date date = planRequest.getDate();
		sb.append("&date=").append(df.format(date));
		sb.append("&time=").append(tf.format(date));
		sb.append("&arriveBy=").append(!planRequest.isDateDeparture());
		Set<TransportMode> modes = planRequest.getModes();
		sb.append("&mode=");
		boolean removeComma = false;
		for (TransportMode mode : modes) {
			sb.append(convertTransportMode(mode)).append(",");
			removeComma = true;
		}
		if (removeComma)
			sb.setLength(sb.length() - 1);
		sb.append("&wheelchair=").append(planRequest.isWheelchairAccessible());
		sb.append("&walkSpeed=").append(planRequest.getWalkSpeedKph());
		sb.append("&bikeSpeed=").append(planRequest.getBikeSpeedKph());
		sb.append("&transferPenalty=").append(
				planRequest.getTransferPenaltySeconds());
		sb.append("&triangleTimeFactor=").append(
				planRequest.getBikeSpeedFactor());
		sb.append("&triangleSafetyFactor=").append(
				planRequest.getBikeSafetyFactor());
		sb.append("&triangleSlopeFactor=").append(
				planRequest.getBikeComfortFactor());
		sb.append("&maxWalkDistance=").append(
				planRequest.getMaxWalkDistanceMeters());
		sb.append("&walkReluctance=").append(
				planRequest.getWalkReluctanceFactor());
		sb.append("&numItineraries=").append(maxItineraries);
		String retval = sb.toString();
		GWT.log("OTP request URL: " + retval);
		return retval;
	}

	private String humanize(String address) {
		if (address.endsWith(", " + mainCountryName))
			address = address.substring(0,
					address.length() - mainCountryName.length() - 2);
		return address;
	}
}

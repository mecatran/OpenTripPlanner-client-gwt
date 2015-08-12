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
import java.util.List;

import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Pixel;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.LineString;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.resources.client.ImageResource;
import com.mecatran.otp.gwt.client.PlannerResources;
import com.mecatran.otp.gwt.client.model.ItineraryBean;
import com.mecatran.otp.gwt.client.model.ItineraryLegBean;
import com.mecatran.otp.gwt.client.model.ItineraryTransitLegBean;
import com.mecatran.otp.gwt.client.model.TransportMode;
import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;

public class OpenLayersItinerary {

	private static final Projection WGS84_PROJECTION = new Projection(
			"EPSG:4326");

	private List<VectorFeature> vectorFeatures;
	private OpenLayersWaypoint departure;
	private OpenLayersWaypoint arrival;
	private Vector layer;
	private Projection mapProjection;

	public OpenLayersItinerary(Vector layer, Projection mapProjection,
			ItineraryBean itinerary, double opacity) {
		this.layer = layer;
		this.mapProjection = mapProjection;
		vectorFeatures = new ArrayList<VectorFeature>();

		// Style pointStyle = new Style();
		// pointStyle.setStrokeColor("#000000");
		// pointStyle.setStrokeWidth(2.0);
		// pointStyle.setStrokeOpacity(1.0 * opacity);
		// pointStyle.setFillColor("#FFFFFF");
		// pointStyle.setFillOpacity(1.0 * opacity);
		// pointStyle.setPointRadius(6.0);
		boolean isFirstLeg = true;
		for (ItineraryLegBean leg : itinerary.getLegs()) {
			Wgs84LatLonBean[] path = leg.getPath();
			if (path.length >= 2) {
				Style layer1Style = new Style();
				Style layer2Style = new Style();
				Style layer3Style = new Style();
				switch (leg.getTravelType()) {
				case ROAD:
					layer3Style = new Style();
					layer3Style.setStrokeColor("#000000");
					layer3Style.setStrokeOpacity(0.8 * opacity);
					layer3Style.setStrokeWidth(6);
					layer1Style.setStrokeColor("#FFFFFF");
					layer1Style.setStrokeOpacity(1.0 * opacity);
					layer1Style.setStrokeWidth(4);
					switch (leg.getMode()) {
					default:
					case WALK:
						layer2Style.setStrokeColor("#222222");
						layer2Style.setStrokeOpacity(0.6 * opacity);
						break;
					case BICYCLE:
					case BICYCLE_RENTAL:
						layer2Style.setStrokeColor("#23C30B");
						layer2Style.setStrokeOpacity(0.8 * opacity);
						break;
					case CAR:
						layer2Style.setStrokeColor("#000000");
						layer2Style.setStrokeOpacity(0.8 * opacity);
						break;
					}
					layer2Style.setStrokeWidth(4);
					layer2Style.setStrokeDashstyle("7 5");
					layer2Style.setStrokeLinecap("butt");
					break;
				default:
				case TRANSIT:
					ItineraryTransitLegBean transitLeg = (ItineraryTransitLegBean) leg;
					layer1Style.setStrokeColor("#000000");
					layer1Style.setStrokeOpacity(0.8 * opacity);
					layer1Style.setStrokeWidth(6);
					layer2Style.setStrokeColor(transitLeg.getRoute()
							.getBackgroundColor());
					layer2Style.setStrokeOpacity(1.0 * opacity);
					layer2Style.setStrokeWidth(4);
					break;
				}
				Point[] points = new Point[path.length];
				for (int i = 0; i < points.length; i++) {
					LonLat lonLat = convertLonLat(path[i]);
					points[i] = new Point(lonLat.lon(), lonLat.lat());
				}
				VectorFeature layer2Feature = new VectorFeature(new LineString(
						points), layer2Style);
				vectorFeatures.add(0, layer2Feature);
				VectorFeature layer1Feature = new VectorFeature(new LineString(
						points), layer1Style);
				vectorFeatures.add(0, layer1Feature);
				if (layer3Style != null) {
					VectorFeature layer3Feature = new VectorFeature(
							new LineString(points), layer3Style);
					vectorFeatures.add(0, layer3Feature);
				}
				if (points.length > 0 && !isFirstLeg) {
					VectorFeature startModeFeature = createFeatureFromMode(
							leg.getMode(), false, points[0], opacity);
					if (startModeFeature != null) {
						startModeFeature.getAttributes().setAttribute(
								OpenLayersPlannerMapWidget.POPUP_CONTENT_KEY,
								leg.getCustomHtmlDetails());
						startModeFeature.getAttributes().setAttribute(
								OpenLayersPlannerMapWidget.POPUP_CLASS_KEY,
								"instructions-popup");
						vectorFeatures.add(startModeFeature);
					}
				}
			}
			isFirstLeg = false;
		}
		// Z-indexing is add order by default.
		for (VectorFeature featureToAdd : vectorFeatures) {
			layer.addFeature(featureToAdd);
		}

		// Last: departure/arrival (on top of the rest)
		ImageResource departureFlagIcon = PlannerResources.INSTANCE
				.flagmapDeparturePng();
		String departurePopupContent = null;
		if (itinerary.getLegs().size() > 0) {
			ItineraryLegBean firstLeg = itinerary.getLegs().get(0);
			switch (firstLeg.getMode()) {
			case WALK:
				departureFlagIcon = PlannerResources.INSTANCE
						.flagmapDepartureWalkPng();
				break;
			case BICYCLE:
			case BICYCLE_RENTAL:
				departureFlagIcon = PlannerResources.INSTANCE
						.flagmapDepartureBikePng();
				break;
			case CAR:
				departureFlagIcon = PlannerResources.INSTANCE
						.flagmapDepartureCarPng();
				break;
			default:
				// Take default flag
				break;
			}
			departurePopupContent = firstLeg.getCustomHtmlDetails();
		}
		departure = new OpenLayersWaypoint(layer, "itinerary-departure", false,
				departureFlagIcon, opacity);
		departure.moveTo(convertLonLat(itinerary.getStartLocation()));
		departure.setAttribute(OpenLayersPlannerMapWidget.POPUP_CONTENT_KEY,
				departurePopupContent);
		departure.setAttribute(OpenLayersPlannerMapWidget.POPUP_CLASS_KEY,
				"instructions-popup");
		arrival = new OpenLayersWaypoint(layer, "itinerary-arrival", false,
				PlannerResources.INSTANCE.flagmapArrivalPng(), opacity);
		arrival.moveTo(convertLonLat(itinerary.getEndLocation()));
		this.layer.redraw();
	}

	public void removeFromLayer() {
		for (VectorFeature feature : vectorFeatures) {
			layer.removeFeature(feature);
		}
		arrival.moveTo(null);
		arrival = null;
		departure.moveTo(null);
		departure = null;
		layer.redraw();
	}

	private LonLat convertLonLat(Wgs84LatLonBean latLon) {
		LonLat lonLat = new LonLat(latLon.getLon(), latLon.getLat());
		lonLat.transform(WGS84_PROJECTION.getProjectionCode(),
				mapProjection.getProjectionCode());
		return lonLat;
	}

	private VectorFeature createFeatureFromMode(TransportMode mode,
			boolean leftish, Point position, double opacity) {
		ImageResource imageResource;
		switch (mode) {
		case WALK:
			imageResource = leftish ? PlannerResources.INSTANCE
					.modemaplWalkPng() : PlannerResources.INSTANCE
					.modemaprWalkPng();
			break;
		case BICYCLE:
		case BICYCLE_RENTAL: // TODO Make dedicated icon
			imageResource = leftish ? PlannerResources.INSTANCE
					.modemaplBicyclePng() : PlannerResources.INSTANCE
					.modemaprBicyclePng();
			break;
		case BUS:
			imageResource = leftish ? PlannerResources.INSTANCE
					.modemaplBusPng() : PlannerResources.INSTANCE
					.modemaprBusPng();
			break;
		case CAR:
			imageResource = leftish ? PlannerResources.INSTANCE
					.modemaplCarPng() : PlannerResources.INSTANCE
					.modemaprCarPng();
			break;
		case FERRY:
			imageResource = leftish ? PlannerResources.INSTANCE
					.modemaplFerryPng() : PlannerResources.INSTANCE
					.modemaprFerryPng();
			break;
		case GONDOLA:
			imageResource = leftish ? PlannerResources.INSTANCE
					.modemaplGondolaPng() : PlannerResources.INSTANCE
					.modemaprGondolaPng();
			break;
		case PLANE:
			imageResource = leftish ? PlannerResources.INSTANCE
					.modemaplPlanePng() : PlannerResources.INSTANCE
					.modemaprPlanePng();
			break;
		case RAIL:
			imageResource = leftish ? PlannerResources.INSTANCE
					.modemaplRailPng() : PlannerResources.INSTANCE
					.modemaprRailPng();
			break;
		case SUBWAY:
			imageResource = leftish ? PlannerResources.INSTANCE
					.modemaplSubwayPng() : PlannerResources.INSTANCE
					.modemaprSubwayPng();
			break;
		case TRAM:
			imageResource = leftish ? PlannerResources.INSTANCE
					.modemaplTramPng() : PlannerResources.INSTANCE
					.modemaprTramPng();
			break;
		case TROLLEY:
			imageResource = leftish ? PlannerResources.INSTANCE
					.modemaplTrolleyPng() : PlannerResources.INSTANCE
					.modemaprTrolleyPng();
			break;
		default:
			return null;
		}
		Style pointStyle = new Style();
		pointStyle.setExternalGraphic(imageResource.getSafeUri().asString());
		pointStyle.setGraphicSize(imageResource.getWidth(),
				imageResource.getHeight());
		Pixel anchor = leftish ? new Pixel(-34, -34) : new Pixel(0, -34);
		pointStyle.setGraphicOffset(anchor.x(), anchor.y());
		pointStyle.setFillOpacity(1.0 * opacity);
		pointStyle.setStrokeOpacity(1.0 * opacity);
		VectorFeature retval = new VectorFeature(position, pointStyle);
		retval.getAttributes().setAttribute(
				OpenLayersPlannerMapWidget.WAYPOINT_DRAGGABLE_KEY, false);
		return retval;
	}

}

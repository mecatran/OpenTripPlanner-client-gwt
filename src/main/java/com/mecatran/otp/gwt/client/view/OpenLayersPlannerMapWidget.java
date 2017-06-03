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
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.gwtopenmaps.openlayers.client.Bounds;
import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.Pixel;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.Size;
import org.gwtopenmaps.openlayers.client.control.DragFeature;
import org.gwtopenmaps.openlayers.client.control.DragFeature.DragFeatureListener;
import org.gwtopenmaps.openlayers.client.control.DragFeatureOptions;
import org.gwtopenmaps.openlayers.client.control.LayerSwitcher;
import org.gwtopenmaps.openlayers.client.control.OverviewMap;
import org.gwtopenmaps.openlayers.client.control.ScaleLine;
import org.gwtopenmaps.openlayers.client.control.SelectFeature;
import org.gwtopenmaps.openlayers.client.control.SelectFeature.ClickFeatureListener;
import org.gwtopenmaps.openlayers.client.control.SelectFeatureOptions;
import org.gwtopenmaps.openlayers.client.event.MapClickListener;
import org.gwtopenmaps.openlayers.client.event.MapMoveListener;
import org.gwtopenmaps.openlayers.client.event.MapZoomListener;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.layer.Layer;
import org.gwtopenmaps.openlayers.client.layer.OSM;
import org.gwtopenmaps.openlayers.client.layer.OSMOptions;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.VectorOptions;
import org.gwtopenmaps.openlayers.client.layer.XYZ;
import org.gwtopenmaps.openlayers.client.layer.XYZOptions;
import org.gwtopenmaps.openlayers.client.popup.FramedCloud;
import org.gwtopenmaps.openlayers.client.popup.Popup;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mecatran.otp.gwt.client.PlannerResources;
import com.mecatran.otp.gwt.client.i18n.I18nUtils;
import com.mecatran.otp.gwt.client.model.ItineraryBean;
import com.mecatran.otp.gwt.client.model.LocationBean;
import com.mecatran.otp.gwt.client.model.POIBean;
import com.mecatran.otp.gwt.client.model.TransportMode;
import com.mecatran.otp.gwt.client.model.Wgs84BoundsBean;
import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;
import com.mecatran.otp.gwt.client.proxies.POISource;
import com.mecatran.otp.gwt.client.view.POIUtils.POILayerType;

public class OpenLayersPlannerMapWidget extends Composite
		implements PlannerMapWidget {

	private static final Projection WGS84_PROJECTION = new Projection(
			"EPSG:4326");

	protected final static String POPUP_CONTENT_KEY = "__POPUP_CONTENT__";
	protected final static String POPUP_CLASS_KEY = "__POPUP_CLASS__";
	protected final static String WAYPOINT_TYPE_KEY = "__WAYPOINT_TYPE__";
	protected final static String WAYPOINT_DRAGGABLE_KEY = "__WAYPOINT_DRAGGABLE__";

	private MapListener mapListener;
	private MapWidget mapWidget;
	private Map map;
	private Projection mapProjection;
	private Vector waypointLayer, itineraryLayer, previewItineraryLayer;
	private Popup infoPopup;
	private DragFeature dragFeature;
	private OpenLayersWaypoint departureWaypoint;
	private OpenLayersWaypoint arrivalWaypoint;
	private OpenLayersWaypoint popupMarker;
	private OpenLayersItinerary selectedItinerary;
	private OpenLayersItinerary previewItinerary;
	private PopupPanel popupMenu;
	private LonLat popupMenuPosition;
	private Wgs84BoundsBean bounds;
	private Layer cycleLayer;
	private Layer stopsLayer;
	private Layer routesLayer;
	private java.util.Map<POILayerType, OpenLayersPOILayer> poiLayers = new HashMap<POILayerType, OpenLayersPOILayer>();

	@SuppressWarnings("unused")
	public OpenLayersPlannerMapWidget() {

		// create some MapOptions
		MapOptions defaultMapOptions = new MapOptions();
		defaultMapOptions.setNumZoomLevels(16);
		defaultMapOptions.setDisplayProjection(WGS84_PROJECTION);

		// Create a MapWidget
		mapWidget = new MapWidget("100%", "100%", defaultMapOptions);
		map = mapWidget.getMap();

		// Create map layers
		// Cloud-made map
		if (false) {
			XYZOptions cloudmadeOptions = new XYZOptions();
			cloudmadeOptions.setIsBaseLayer(true);
			cloudmadeOptions.setSphericalMercator(true);
			XYZ cloudmadeLayer = new XYZ("Cloudmade",
					"http://tile.cloudmade.com/YOUR_KEY_HERE/99823/256/${z}/${x}/${y}.png",
					cloudmadeOptions);
			map.addLayer(cloudmadeLayer);
		}

		// MapQuest open
		OSMOptions mapQuestOptions = new OSMOptions();
		mapQuestOptions.setAttribution(
				"Tiles Courtesy of <a href='http://www.mapquest.com/' target='_blank'>MapQuest</a> <img src='http://developer.mapquest.com/content/osm/mq_logo.png'>. Map data provided by <a href='http://www.openstreetmap.org/copyright'>OpenStreetMap</a> and contributors, <a href='http://wiki.openstreetmap.org/wiki/Legal_FAQ'>ODbL</a>.");
		OSM mapQuest = new OSM(I18nUtils.tr("layer.mapquest.street"),
				new String[] {
						"http://otile1.mqcdn.com/tiles/1.0.0/map/${z}/${x}/${y}.jpg",
						"http://otile2.mqcdn.com/tiles/1.0.0/map/${z}/${x}/${y}.jpg",
						"http://otile3.mqcdn.com/tiles/1.0.0/map/${z}/${x}/${y}.jpg",
						"http://otile4.mqcdn.com/tiles/1.0.0/map/${z}/${x}/${y}.jpg" },
				mapQuestOptions);
		mapQuest.setIsBaseLayer(true);
		map.addLayer(mapQuest);

		// OSM Mapnik
		OSM mapnik = OSM.Mapnik(I18nUtils.tr("layer.osm.street"));
		mapnik.setIsBaseLayer(true);
		map.addLayer(mapnik);

		// OpenCycleMap / Transport / Outdoor
		XYZOptions thunderforestOptions = new XYZOptions();
		thunderforestOptions.setIsBaseLayer(true);
		thunderforestOptions.setSphericalMercator(true);
		thunderforestOptions.setAttribution(
				"Copyright CC-BY-SA 2.0 <a href='www.openstreetmap.org'>OpenStreetMap</a> and <a href='www.thunderforest.com'>Thunderforest</a>");
		XYZ transportLayer = new XYZ("Transport",
				new String[] {
						"http://a.tile2.opencyclemap.org/transport/${z}/${x}/${y}.png",
						"http://b.tile2.opencyclemap.org/transport/${z}/${x}/${y}.png",
						"http://c.tile2.opencyclemap.org/transport/${z}/${x}/${y}.png" },
				thunderforestOptions);
		map.addLayer(transportLayer);
		// Outdoor
		XYZ outdoorLayer = new XYZ("Outdoor",
				new String[] {
						"http://a.tile.thunderforest.com/outdoors/${z}/${x}/${y}.png",
						"http://b.tile.thunderforest.com/outdoors/${z}/${x}/${y}.png",
						"http://c.tile.thunderforest.com/outdoors/${z}/${x}/${y}.png" },
				thunderforestOptions);
		map.addLayer(outdoorLayer);
		// Landscape
		XYZ landscapeLayer = new XYZ("Landscape",
				new String[] {
						"http://a.tile3.opencyclemap.org/landscape/${z}/${x}/${y}.png",
						"http://b.tile3.opencyclemap.org/landscape/${z}/${x}/${y}.png",
						"http://c.tile3.opencyclemap.org/landscape/${z}/${x}/${y}.png" },
				thunderforestOptions);
		map.addLayer(landscapeLayer);
		// Cyclemap
		cycleLayer = OSM.CycleMap(I18nUtils.tr("layer.osm.cycle"));
		cycleLayer.setIsBaseLayer(true);
		map.addLayer(cycleLayer);

		mapProjection = new Projection(map.getProjection());

		// Create vector layers
		List<Vector> allLayers = new ArrayList<Vector>();

		// Preview itinerary
		VectorOptions previewItineraryLayerOptions = new VectorOptions();
		previewItineraryLayerOptions.setDisplayInLayerSwitcher(false);
		previewItineraryLayer = new Vector("Itinerary preview",
				previewItineraryLayerOptions);
		previewItineraryLayer.setIsVisible(true);
		allLayers.add(previewItineraryLayer);
		map.addLayer(previewItineraryLayer);

		// Waypoint layer
		VectorOptions vectorLayerOptions = new VectorOptions();
		vectorLayerOptions.setDisplayInLayerSwitcher(false);
		vectorLayerOptions.setLayerOpacity(1.0);
		waypointLayer = new Vector("Waypoints", vectorLayerOptions);
		waypointLayer.setIsVisible(true);
		map.addLayer(waypointLayer);
		allLayers.add(waypointLayer);

		// Itinerary layer
		itineraryLayer = new Vector("Itinerary", vectorLayerOptions);
		itineraryLayer.setIsVisible(true);
		map.addLayer(itineraryLayer);
		allLayers.add(itineraryLayer);

		// Pre-create POI layers
		for (POILayerType layerType : POILayerType.values()) {
			// Lazy-create a new layer
			OpenLayersPOILayer poiLayer = new OpenLayersPOILayer(map,
					layerType);
			poiLayers.put(layerType, poiLayer);
			allLayers.add(poiLayer.getLayer());
		}

		// Drag feature for departure/arrival
		DragFeatureOptions dragFeatureOptions = new DragFeatureOptions();
		dragFeatureOptions.onComplete(new DragFeatureListener() {
			@Override
			public void onDragEvent(VectorFeature vectorFeature, Pixel pixel) {
				handleDrag(vectorFeature);
			}
		});
		dragFeatureOptions.onStart(new DragFeatureListener() {
			@Override
			public void onDragEvent(VectorFeature vectorFeature, Pixel pixel) {
				if (!vectorFeature.getAttributes()
						.getAttributeAsBoolean(WAYPOINT_DRAGGABLE_KEY)) {
					dragFeature.deactivate();
					dragFeature.activate();
				}
			}
		});
		dragFeature = new DragFeature(waypointLayer, dragFeatureOptions);
		map.addControl(dragFeature);
		dragFeature.activate();

		// Select feature for click on POI/waypoint/mode icon
		SelectFeatureOptions selectFeatureOptions = new SelectFeatureOptions();
		selectFeatureOptions.clickFeature(new ClickFeatureListener() {
			@Override
			public void onFeatureClicked(VectorFeature vectorFeature) {
				handleFeatureSelection(vectorFeature);
			}
		});
		SelectFeature selectFeature = new SelectFeature(
				allLayers.toArray(new Vector[0]), selectFeatureOptions);
		map.addControl(selectFeature);
		selectFeature.activate();

		// Create departure/arrival waypoints
		departureWaypoint = new OpenLayersWaypoint(waypointLayer, "departure",
				true, PlannerResources.INSTANCE.flagmapDeparturePng(), 1.0);
		arrivalWaypoint = new OpenLayersWaypoint(waypointLayer, "arrival", true,
				PlannerResources.INSTANCE.flagmapArrivalPng(), 1.0);
		popupMarker = new OpenLayersWaypoint(waypointLayer, "popup", false,
				PlannerResources.INSTANCE.flagmapWaypointPng(), 1.0);

		// Lets add some default controls to the map
		// + sign in the upperright corner to display the layer switcher
		map.addControl(new LayerSwitcher());
		// + sign in the lowerright to display the overviewmap
		map.addControl(new OverviewMap());
		// Display the scaleline
		map.addControl(new ScaleLine());

		// Add map handlers
		map.addMapClickListener(new MapClickListener() {
			@Override
			public void onClick(MapClickEvent mapClickEvent) {
				handleClickOnMap(mapClickEvent.getLonLat(),
						mapClickEvent.getPixel());
			}
		});
		map.addMapMoveListener(new MapMoveListener() {
			@Override
			public void onMapMove(MapMoveEvent eventObject) {
				hidePopupMenu();
			}
		});
		map.addMapZoomListener(new MapZoomListener() {
			@Override
			public void onMapZoom(MapZoomEvent eventObject) {
				hidePopupMenu();
			}
		});

		// Create popup menu
		popupMenu = new PopupPanel(true);
		popupMenu.setWidth("15em");
		popupMenu.addStyleName("popup-menu");
		MenuBar menuBar = new MenuBar(true);
		popupMenu.add(menuBar);
		MenuItem startHereMenuItem = new MenuItem(I18nUtils.tr("start.here"),
				new Command() {
					@Override
					public void execute() {
						startHere();
					}
				});
		startHereMenuItem.addStyleName("departure-icon");
		menuBar.addItem(startHereMenuItem);
		MenuItem endHereMenuItem = new MenuItem(I18nUtils.tr("end.here"),
				new Command() {
					@Override
					public void execute() {
						endHere();
					}
				});
		endHereMenuItem.addStyleName("arrival-icon");
		menuBar.addItem(endHereMenuItem);
		MenuItem zoomInMenuItem = new MenuItem(I18nUtils.tr("zoom.in"),
				new Command() {
					@Override
					public void execute() {
						map.setCenter(map.getCenter(), map.getZoom() + 1);
					}
				});
		zoomInMenuItem.addStyleName("zoomin-icon");
		menuBar.addItem(zoomInMenuItem);
		MenuItem zoomOutMenuItem = new MenuItem(I18nUtils.tr("zoom.out"),
				new Command() {
					@Override
					public void execute() {
						map.setCenter(map.getCenter(), map.getZoom() - 1);
					}
				});
		zoomOutMenuItem.addStyleName("zoomout-icon");
		menuBar.addItem(zoomOutMenuItem);
		MenuItem centerMapMenuItem = new MenuItem(I18nUtils.tr("center.map"),
				new Command() {
					@Override
					public void execute() {
						map.setCenter(popupMarker.getLonLat());
					}
				});
		centerMapMenuItem.addStyleName("centermap-icon");
		menuBar.addItem(centerMapMenuItem);
		popupMenu.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				hidePopupMenu();
			}
		});

		// Force the map to fall behind popups
		mapWidget.getElement().getFirstChildElement().getStyle().setZIndex(0);
		initWidget(mapWidget);
	}

	@Override
	public Widget getAsWidget() {
		return this;
	}

	@Override
	public void setMapListener(MapListener mapListener) {
		this.mapListener = mapListener;
	}

	@Override
	public void setBounds(Wgs84BoundsBean aBounds) {
		bounds = aBounds;
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				map.updateSize();
				Bounds extent = convertBounds(bounds);
				map.setMaxExtent(extent);
				map.setRestrictedExtent(extent);
				map.zoomToExtent(extent);
				// Zoom a bit inside as fitting bounds will be too large
				map.setCenter(map.getCenter(), map.getZoom() + 1);
			}
		});
	}

	@Override
	public void updateSize() {
		map.updateSize();
	}

	@Override
	public void showItineraryOnMap(ItineraryBean itinerary) {
		// Close popups (info and menu)
		hidePopupMenu();
		if (infoPopup != null) {
			map.removePopup(infoPopup);
		}
		// Remove existing itinerary
		if (selectedItinerary != null) {
			selectedItinerary.removeFromLayer();
			selectedItinerary = null;
		}
		if (itinerary == null) {
			// No itinerary: show departure/arrival flag
			departureWaypoint.show();
			arrivalWaypoint.show();
			itineraryLayer.redraw();
			// Reset stop/route layer visibility to what it was
			if (stopsLayer != null)
				stopsLayer.setOpacity(1.0f);
			if (routesLayer != null)
				routesLayer.setOpacity(1.0f);
			for (OpenLayersPOILayer poiLayer : poiLayers.values())
				poiLayer.setOpacity(1.0f);
		} else {
			if (stopsLayer != null)
				stopsLayer.setOpacity(0.2f);
			if (routesLayer != null)
				routesLayer.setOpacity(0.2f);
			for (OpenLayersPOILayer poiLayer : poiLayers.values())
				poiLayer.setOpacity(0.2f);
			// Show selected itinerary on map
			departureWaypoint.hide();
			arrivalWaypoint.hide();
			if (itinerary.getBounds() != null) {
				Bounds extent = convertBounds(itinerary.getBounds());
				map.zoomToExtent(extent);
			}
			selectedItinerary = new OpenLayersItinerary(itineraryLayer,
					mapProjection, itinerary, 1.0);
		}
	}

	@Override
	public void previewItineraryOnMap(ItineraryBean itinerary) {
		// Remove existing preview
		if (previewItinerary != null) {
			previewItinerary.removeFromLayer();
			previewItinerary = null;
		}
		if (itinerary != null) {
			previewItinerary = new OpenLayersItinerary(previewItineraryLayer,
					mapProjection, itinerary, 0.4);
		}
	}

	@Override
	public void previewLocation(LocationBean location) {
		if (location == null) {
			// TODO - popup marker seems to be broken here...
			// Only use info popup for now.
			// if (popupMarker.isSet()) {
			// popupMarker.hide();
			// }
			if (infoPopup != null) {
				map.removePopup(infoPopup);
				infoPopup = null;
			}
		} else {
			LonLat lonLat = convertLonLat(location.getLocation());
			if (infoPopup != null) {
				map.removePopup(infoPopup);
			}
			infoPopup = new FramedCloud("infoPopup", lonLat, new Size(150, 100),
					wrapPopupHtml("instructions-popup",
							"<h3>" + location.getAddress() + "</h3>"),
					null, true);
			infoPopup.setAutoSize(true);
			infoPopup.setPanMapIfOutOfView(true);
			map.addPopup(infoPopup);
			infoPopup.show();
			// popupMarker.moveTo(lonLat);
			// popupMarker.show();
			map.panTo(lonLat);
		}
	}

	@Override
	public void updateDeparturePosition(Wgs84LatLonBean departurePosition,
			boolean autoPan) {
		if (departurePosition == null) {
			departureWaypoint.hide();
		} else {
			LonLat lonLat = convertLonLat(departurePosition);
			departureWaypoint.moveTo(lonLat);
			if (autoPan)
				map.panTo(lonLat);
		}
		waypointLayer.redraw();
	}

	@Override
	public void updateArrivalPosition(Wgs84LatLonBean arrivalPosition,
			boolean autoPan) {
		if (arrivalPosition == null) {
			arrivalWaypoint.hide();
		} else {
			LonLat lonLat = convertLonLat(arrivalPosition);
			arrivalWaypoint.moveTo(lonLat);
			if (autoPan)
				map.panTo(lonLat);
		}
	}

	@Override
	public void highlightItineraryStep(String instructionsHtml,
			Wgs84LatLonBean location) {
		if (infoPopup != null) {
			map.removePopup(infoPopup);
		}
		infoPopup = new FramedCloud("infoPopup", convertLonLat(location),
				new Size(200, 200),
				wrapPopupHtml("instructions-popup", instructionsHtml), null,
				true);
		infoPopup.setAutoSize(true);
		infoPopup.setPanMapIfOutOfView(true);
		map.addPopup(infoPopup);
		infoPopup.show();
	}

	@Override
	public void focusDeparture() {
		if (departureWaypoint.isSet())
			map.panTo(departureWaypoint.getLonLat());
		waypointLayer.redraw();
	}

	@Override
	public void focusArrival() {
		if (arrivalWaypoint.isSet())
			map.panTo(arrivalWaypoint.getLonLat());
		waypointLayer.redraw();
	}

	@Override
	public void switchMapBackground(Set<TransportMode> modes) {
		if (modes.contains(TransportMode.BICYCLE)
				|| modes.contains(TransportMode.BICYCLE_RENTAL))
			map.setBaseLayer(cycleLayer);
	}

	@Override
	public void updatePOIs(POISource source,
			java.util.Map<String, POIBean> pois) {
		// Group POI by layers as a source can impact many layers
		java.util.Map<POILayerType, java.util.Map<String, POIBean>> poisPerLayer = new HashMap<POILayerType, java.util.Map<String, POIBean>>();
		for (POIBean poi : pois.values()) {
			POILayerType layerType = POIUtils
					.mapPoiTypeToLayerType(poi.getType());
			java.util.Map<String, POIBean> poisForLayer = poisPerLayer
					.get(layerType);
			if (poisForLayer == null) {
				poisForLayer = new HashMap<String, POIBean>();
				poisPerLayer.put(layerType, poisForLayer);
			}
			poisForLayer.put(poi.getId(), poi);
		}
		// For each POI layer group, update layer
		for (java.util.Map.Entry<POILayerType, java.util.Map<String, POIBean>> kv : poisPerLayer
				.entrySet()) {
			POILayerType layerType = kv.getKey();
			java.util.Map<String, POIBean> poisForLayer = kv.getValue();
			OpenLayersPOILayer poiLayer = poiLayers.get(layerType);
			poiLayer.updatePOIList(source.getUniqueId(), poisForLayer);
		}
	}

	private LonLat convertLonLat(Wgs84LatLonBean latLon) {
		LonLat lonLat = new LonLat(latLon.getLon(), latLon.getLat());
		lonLat.transform(WGS84_PROJECTION.getProjectionCode(),
				mapProjection.getProjectionCode());
		return lonLat;
	}

	private Wgs84LatLonBean convertLatLng(LonLat lonLat) {
		LonLat lonLat2 = new LonLat(lonLat.lon(), lonLat.lat());
		lonLat2.transform(mapProjection.getProjectionCode(),
				WGS84_PROJECTION.getProjectionCode());
		return new Wgs84LatLonBean(lonLat2.lat(), lonLat2.lon());
	}

	private Bounds convertBounds(Wgs84BoundsBean bounds) {
		Bounds extent = new Bounds(bounds.getMinLon(), bounds.getMinLat(),
				bounds.getMaxLon(), bounds.getMaxLat());
		extent.transform(WGS84_PROJECTION, mapProjection);
		return extent;
	}

	private void handleDrag(VectorFeature feature) {
		if (feature.getAttributes().getAttributeAsString(WAYPOINT_TYPE_KEY)
				.equals("departure")) {
			mapListener.onStartPointSelected(
					convertLatLng(feature.getCenterLonLat()));
		} else if (feature.getAttributes()
				.getAttributeAsString(WAYPOINT_TYPE_KEY).equals("arrival")) {
			mapListener.onEndPointSelected(
					convertLatLng(feature.getCenterLonLat()));
		}
	}

	private void hidePopupMenu() {
		if (popupMenu.isShowing())
			popupMenu.hide();
		if (popupMarker.isSet()) {
			popupMarker.hide();
		}
	}

	private void handleClickOnMap(LonLat lonLat, Pixel pixel) {
		if (popupMenu.isShowing()) {
			hidePopupMenu();
			return;
		}
		popupMarker.moveTo(lonLat);
		popupMenuPosition = lonLat;
		popupMenu.setPopupPosition(this.getAbsoluteLeft() + pixel.x() + 2,
				this.getAbsoluteTop() + pixel.y() + 2);
		popupMenu.show();
	}

	private void startHere() {
		hidePopupMenu();
		mapListener.onStartPointSelected(convertLatLng(popupMenuPosition));
	}

	private void endHere() {
		hidePopupMenu();
		mapListener.onEndPointSelected(convertLatLng(popupMenuPosition));
	}

	private void handleFeatureSelection(VectorFeature feature) {
		if (infoPopup != null) {
			map.removePopup(infoPopup);
			infoPopup = null;
		}
		String popupContent = feature.getAttributes()
				.getAttributeAsString(POPUP_CONTENT_KEY);
		String popupClass = feature.getAttributes()
				.getAttributeAsString(POPUP_CLASS_KEY);
		if (popupContent != null) {
			infoPopup = new FramedCloud("infoPopup", feature.getCenterLonLat(),
					new Size(200, 200), wrapPopupHtml(popupClass, popupContent),
					null, true);
			infoPopup.setAutoSize(true);
			infoPopup.setBorder("1px black solid");
			infoPopup.setPanMapIfOutOfView(true);
			map.addPopup(infoPopup);
			infoPopup.show();
		}
	}

	private String wrapPopupHtml(String popupClass, String html) {
		return "<div class='generic-popup"
				+ (popupClass == null ? "" : " " + popupClass) + "'>" + html
				+ "</div>";
	}
}

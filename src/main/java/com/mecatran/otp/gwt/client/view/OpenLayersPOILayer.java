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
import java.util.Comparator;
import java.util.List;

import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.Pixel;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.RendererOptions;
import org.gwtopenmaps.openlayers.client.layer.Vector;
import org.gwtopenmaps.openlayers.client.layer.VectorOptions;

import com.google.gwt.resources.client.ImageResource;
import com.mecatran.otp.gwt.client.i18n.I18nUtils;
import com.mecatran.otp.gwt.client.model.POIBean;
import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;
import com.mecatran.otp.gwt.client.view.POIUtils.POILayerType;

public class OpenLayersPOILayer {

	private static final Projection WGS84_PROJECTION = new Projection(
			"EPSG:4326");

	private Vector layer;
	private String mapProjectionCode;

	public OpenLayersPOILayer(Map map, POILayerType layerType) {
		VectorOptions opts = new VectorOptions();
		opts.setMaxResolution(20.0f);
		RendererOptions ropts = new RendererOptions();
		ropts.setZIndexing(true);
		opts.setRendererOptions(ropts);
		String layerName = I18nUtils.tr(false,
				"layer.poi." + layerType.toString());
		if (layerName == null)
			layerName = I18nUtils.tr("layer.poi.default");
		layer = new Vector(layerName, opts);
		layer.setDisplayInLayerSwitcher(false);
		map.addLayer(layer);
		mapProjectionCode = map.getProjection();
	}

	public void updatePOIList(String sourceId,
			java.util.Map<String, POIBean> pois) {
		// Remove only POI coming from the same sourceId
		if (layer.getFeatures() != null) {
			for (VectorFeature feature : layer.getFeatures()) {
				String poiSource = feature.getAttributes()
						.getAttributeAsString("sourceId");
				if (poiSource.equals(sourceId))
					layer.removeFeature(feature);
			}
		}
		// Order POI on z-index = latitude
		List<POIBean> poiList = new ArrayList<POIBean>(pois.values());
		Collections.sort(poiList, new Comparator<POIBean>() {
			@Override
			public int compare(POIBean o1, POIBean o2) {
				double delta = o2.getLocation().getLat()
						- o1.getLocation().getLat();
				return delta < 0.0 ? -1 : +1;
			}
		});
		for (POIBean poi : poiList) {
			ImageResource imageResource = POIUtils.getPoiIcon(poi.getType());
			String imageUrl = imageResource.getSafeUri().asString();

			Style pointStyle = new Style();
			pointStyle.setExternalGraphic(imageUrl);
			pointStyle.setGraphicSize(imageResource.getWidth(),
					imageResource.getHeight());
			// HACK ALERT! Assume all POI icons the same size.
			Pixel anchor = new Pixel(-12, -30);
			pointStyle.setGraphicOffset(anchor.x(), anchor.y());
			pointStyle.setFillOpacity(1.0);
			pointStyle.setStrokeOpacity(1.0);
			LonLat position = convertLonLat(poi.getLocation());
			Point point = new Point(position.lon(), position.lat());
			VectorFeature marker = new VectorFeature(point, pointStyle);
			// TODO Set POI name as <h3>title</h3>?
			marker.getAttributes().setAttribute(
					OpenLayersPlannerMapWidget.POPUP_CONTENT_KEY,
					poi.getHtmlDescription());
			marker.getAttributes().setAttribute(
					OpenLayersPlannerMapWidget.POPUP_CLASS_KEY, "poi-popup");
			layer.addFeature(marker);
		}
		layer.setDisplayInLayerSwitcher(!poiList.isEmpty());
	}

	private LonLat convertLonLat(Wgs84LatLonBean latLon) {
		LonLat lonLat = new LonLat(latLon.getLon(), latLon.getLat());
		lonLat.transform(WGS84_PROJECTION.getProjectionCode(),
				mapProjectionCode);
		return lonLat;
	}

	public void setOpacity(float opacity) {
		layer.setOpacity(opacity);
		// WORKAROUND: setOpacity() does not seem to work...
		// TODO Investigate this.
		layer.setIsVisible(opacity > 0.3f);
		layer.redraw();
	}

	public Vector getLayer() {
		return layer;
	}
}

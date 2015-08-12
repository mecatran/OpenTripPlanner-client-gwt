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

import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Pixel;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.resources.client.ImageResource;

public class OpenLayersWaypoint {

	private VectorFeature vectorFeature;
	private LonLat position;
	private Vector layer;
	private String name;
	private boolean draggable;
	private ImageResource imageResource;
	private double opacity;

	public OpenLayersWaypoint(Vector layer, String name, boolean draggable,
			ImageResource imageResource, double opacity) {
		position = null;
		vectorFeature = null;
		this.layer = layer;
		this.name = name;
		this.draggable = draggable;
		this.imageResource = imageResource;
		this.opacity = opacity;
	}

	public void moveTo(LonLat newPosition) {
		hide();
		position = newPosition;
		show();
	}

	public void show() {
		if (position == null)
			return;
		Point point = new Point(position.lon(), position.lat());
		vectorFeature = createFeatureFromImageResource(imageResource,
				new Pixel(-10, -39), point, opacity);
		vectorFeature.getAttributes().setAttribute(
				OpenLayersPlannerMapWidget.WAYPOINT_TYPE_KEY, name);
		vectorFeature.getAttributes().setAttribute(
				OpenLayersPlannerMapWidget.WAYPOINT_DRAGGABLE_KEY, draggable);
		layer.addFeature(vectorFeature);
		layer.redraw();
	}

	public void hide() {
		if (vectorFeature != null)
			layer.removeFeature(vectorFeature);
		vectorFeature = null;
	}

	public boolean isSet() {
		return (position != null);
	}

	private VectorFeature createFeatureFromImageResource(
			ImageResource imageResource, Pixel anchor, Point position,
			double opacity) {
		Style pointStyle = new Style();
		pointStyle.setExternalGraphic(imageResource.getSafeUri().asString());
		pointStyle.setGraphicSize(imageResource.getWidth(),
				imageResource.getHeight());
		pointStyle.setGraphicOffset(anchor.x(), anchor.y());
		pointStyle.setFillOpacity(1.0 * opacity);
		pointStyle.setStrokeOpacity(1.0 * opacity);
		VectorFeature retval = new VectorFeature(position, pointStyle);
		return retval;
	}

	public LonLat getLonLat() {
		if (position == null)
			return null;
		return position;
	}

	public void setAttribute(String name, String value) {
		if (vectorFeature != null)
			vectorFeature.getAttributes().setAttribute(name, value);
	}

}

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

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.LineCap;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mecatran.otp.gwt.client.PlannerResources;
import com.mecatran.otp.gwt.client.i18n.I18nUtils;
import com.mecatran.otp.gwt.client.model.AlertBean;
import com.mecatran.otp.gwt.client.model.ItineraryBean;
import com.mecatran.otp.gwt.client.model.ItineraryLegBean;
import com.mecatran.otp.gwt.client.model.ItineraryRoadLegBean;
import com.mecatran.otp.gwt.client.model.ItineraryRoadStepBean;
import com.mecatran.otp.gwt.client.model.ItineraryTransitLegBean;
import com.mecatran.otp.gwt.client.model.TransitRouteBean;
import com.mecatran.otp.gwt.client.model.TransportMode;
import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;
import com.mecatran.otp.gwt.client.utils.FormatUtils;
import com.mecatran.otp.gwt.client.view.ItineraryWidget.ItineraryListener;

public class ItineraryDetailsWidget extends Composite {

	private ItineraryListener listener;
	private boolean selected;

	public ItineraryDetailsWidget(ItineraryBean itinerary,
			ItineraryListener aListener) {
		this.listener = aListener;
		VerticalPanel rootPanel = new VerticalPanel();

		/* Alerts on top */
		for (AlertBean alert : itinerary.getAlerts()) {
			AlertWidget alertWidget = new AlertWidget(alert);
			alertWidget.setStyleName("itinerary-alert");
			rootPanel.add(alertWidget);
		}

		/* Departure */
		rootPanel.add(getEndPointDetails("departure",
				itinerary.getDepartureTime(), itinerary.getStartAddress(),
				itinerary.getStartLocation()));

		/* Details */
		for (ItineraryLegBean leg : itinerary.getLegs()) {
			switch (leg.getTravelType()) {
			case ROAD:
				rootPanel.add(getRoadDetails(leg.getAsRoadLeg()));
				break;
			case TRANSIT:
				rootPanel.add(getTransitDetails(leg.getAsTransitLeg()));
				break;
			default:
				throw new IllegalArgumentException(
						"Unsupported leg type: " + leg.getTravelType());
			}
		}

		/* Arrival */
		rootPanel.add(getEndPointDetails("arrival", itinerary.getArrivalTime(),
				itinerary.getEndAddress(), itinerary.getEndLocation()));

		/* Copyrights */
		Label copyrightLabel = new Label(itinerary.getCopyrights());
		copyrightLabel.addStyleName("itinerary-copyrights");
		rootPanel.add(copyrightLabel);

		initWidget(rootPanel);
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	private Widget getEndPointDetails(final String css, Date time,
			String address, final Wgs84LatLonBean position) {
		HTML line = new HTML();
		line.addStyleName("itinerary-details-line");
		line.addStyleName("itinerary-details-" + css);
		line.addStyleName(css + "-icon");
		String html = "<span class='time'>" + FormatUtils.formatTime(time)
				+ "</span> - " + FormatUtils.formatAddress(address);
		line.setHTML(html);
		final String infoHtml = "<div class='info-panel-" + css + " " + css
				+ "-icon'>" + html + "</div>";
		line.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				if (selected)
					listener.onItineraryStepClicked(infoHtml, position);
			}
		});
		return line;
	}

	private Widget getRoadDetails(ItineraryRoadLegBean leg) {
		VerticalPanel rootPanel = new VerticalPanel();

		rootPanel.addStyleName("itinerary-details-line");
		styleComponentWithMode(rootPanel, leg.getMode(), null);

		// Header: main instruction
		// TODO Use templating
		HTML instructionsLabel = new HTML(leg.getInstructions());
		instructionsLabel.addStyleName("itinerary-details-road-first-line");
		rootPanel.add(instructionsLabel);
		final String mainRoadHtml = "<div class='info-panel-road "
				+ FormatUtils.getCssClassNameFromTransportMode(leg.getMode())
				+ "-icon'>" + leg.getInstructions() + "</div>";
		leg.setCustomHtmlDetails(mainRoadHtml);
		final Wgs84LatLonBean mainRoadPosition = leg.getStartLocation();
		instructionsLabel.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if (selected)
					listener.onItineraryStepClicked(mainRoadHtml,
							mainRoadPosition);
			}
		});
		// Duration and distance + details link
		FlowPanel distanceAndLinkPanel = new FlowPanel();
		rootPanel.add(distanceAndLinkPanel);
		HTML distanceDurationLabel = new HTML(
				FormatUtils.formatDistance(leg.getDistanceMeters()) + " - "
						+ FormatUtils.formatDuration(leg.getDurationSeconds()));
		distanceDurationLabel
				.addStyleName("itinerary-details-road-second-line");
		distanceAndLinkPanel.add(distanceDurationLabel);

		// Open/close details link
		final Anchor showHideDetailsLink = new Anchor(
				I18nUtils.tr("show.road.details"));
		distanceAndLinkPanel.add(showHideDetailsLink);
		showHideDetailsLink.addStyleName("itinerary-details-show-hide");

		// Details
		final VerticalPanel detailsPanel = new VerticalPanel();
		rootPanel.add(detailsPanel);
		detailsPanel.setVisible(false);
		int nDetails = 0;
		for (ItineraryRoadStepBean step : leg.getRoadSteps()) {
			if (step.getInstructions() == null) {
				// Can happen in case of transfers between two transit legs
				continue;
			}
			// TODO Use templating
			String html = step.getInstructions()
					+ (step.getDistanceMeters() >= 5
							? " <span class='distance'>("
									+ FormatUtils.formatDistance(
											step.getDistanceMeters())
									+ " - "
									+ FormatUtils.formatDuration(
											step.getDurationSeconds())
									+ ")</span>"
							: "");
			final String infoHtml = "<div class='info-panel-road-step'>" + html
					+ "</div>";
			final Wgs84LatLonBean startLocation = step.getStartLocation();
			HTML stepLabel = new HTML(html);
			stepLabel.addStyleName("itinerary-details-road-step");
			stepLabel.addMouseOverHandler(new MouseOverHandler() {

				@Override
				public void onMouseOver(MouseOverEvent event) {
					if (selected)
						listener.onItineraryStepClicked(infoHtml,
								startLocation);
				}
			});
			detailsPanel.add(stepLabel);
			nDetails++;
		}

		// Open/close behavior
		showHideDetailsLink.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (detailsPanel.isVisible()) {
					showHideDetailsLink
							.setText(I18nUtils.tr("show.road.details"));
					detailsPanel.setVisible(false);
				} else {
					showHideDetailsLink
							.setText(I18nUtils.tr("hide.road.details"));
					detailsPanel.setVisible(true);
				}
				event.stopPropagation();
			}
		});
		// Can happen: no detail instructions.
		if (nDetails == 0)
			showHideDetailsLink.setVisible(false);

		return rootPanel;
	}

	private Widget getTransitDetails(ItineraryTransitLegBean leg) {
		VerticalPanel line = new VerticalPanel();
		TransitRouteBean route = leg.getRoute();
		line.addStyleName("itinerary-details-line");
		styleComponentWithMode(line, leg.getMode(), route.getBackgroundColor());
		// line.addStyleName(ItineraryWidget.getCssClassNameFromTransportMode(leg
		// .getMode()) + "-icon");
		// First line: time - departure stop name
		HTML departureLine = new HTML();
		departureLine.addStyleName("itinerary-details-transit-departure");
		departureLine.setHTML("<span class='time'>"
				+ FormatUtils.formatTime(leg.getDepartureTime()) + "</span> "
				+ leg.getDepartureStop().getName());
		String codeHtml = "<span class='route-code' style='color:"
				+ route.getForegroundColor() + "; background-color:"
				+ route.getBackgroundColor() + "'>" + route.getCode()
				+ "</span>";
		String transitHtml = "<div class='info-panel-transit "
				+ FormatUtils.getCssClassNameFromTransportMode(leg.getMode())
				+ "-icon'>";
		final String departureHtml = transitHtml + "<span class='time'>"
				+ FormatUtils.formatTime(leg.getDepartureTime())
				+ "</span> - <span class='stop'>"
				+ leg.getDepartureStop().getName() + "</span><br/>" + codeHtml
				+ " " + (route.getName() != null ? route.getName() : "")
				+ "<br/>" + I18nUtils.tr("heading.to")
				+ " <span class='headsign'>" + leg.getHeadsign()
				+ "</span></div>";
		final Wgs84LatLonBean departureLocation = leg.getDepartureStop()
				.getLocation();
		leg.setCustomHtmlDetails(departureHtml);
		departureLine.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				if (selected)
					listener.onItineraryStepClicked(departureHtml,
							departureLocation);
			}
		});
		line.add(departureLine);
		// Second line: Route code and headsign
		HTML headsignLine = new HTML();
		headsignLine.addStyleName("itinerary-details-transit-headsign");
		headsignLine.setHTML(codeHtml + " " + I18nUtils.tr("heading.to") + " "
				+ leg.getHeadsign());
		line.add(headsignLine);
		// Third line: time - arrival stop name
		HTML arrivalLine = new HTML();
		arrivalLine.addStyleName("itinerary-details-transit-arrival");
		arrivalLine.setHTML("<span class='time'>"
				+ FormatUtils.formatTime(leg.getArrivalTime()) + "</span> "
				+ leg.getArrivalStop().getName());
		final String arrivalHtml = transitHtml + "<span class='time'>"
				+ FormatUtils.formatTime(leg.getArrivalTime())
				+ "</span> - <small>" + I18nUtils.tr("hop.off.at")
				+ "</small> <span class='stop'>"
				+ leg.getArrivalStop().getName() + "</span></div>";
		final Wgs84LatLonBean arrivalLocation = leg.getArrivalStop()
				.getLocation();
		arrivalLine.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				if (selected)
					listener.onItineraryStepClicked(arrivalHtml,
							arrivalLocation);
			}
		});
		line.add(arrivalLine);
		return line;
	}

	/**
	 * Build the background image for the widget, according to the mode. Draw
	 * the mode image and a solid line below it with the route color (if in
	 * transit mode) or a dotted line (if in road mode). Set the
	 * background-image to the generated image for the given widget.
	 */
	public static void styleComponentWithMode(final Widget widget,
			TransportMode mode, String color) {
		PlannerResources resources = PlannerResources.INSTANCE;
		ImageResource baseImage = null;
		boolean road = false;
		switch (mode) {
		case WALK:
			road = true;
			color = "#666666";
			baseImage = resources.modeWalkPng();
			break;
		case BICYCLE:
			road = true;
			color = "#23C30B";
			baseImage = resources.modeBicyclePng();
			break;
		case BICYCLE_RENTAL:
			road = true;
			color = "#23C30B";
			baseImage = resources.modeBikeRentalPng();
			break;
		case CAR:
			road = true;
			color = "#333333";
			baseImage = resources.modeCarPng();
			break;
		default:
		case BUS:
			baseImage = resources.modeBusPng();
			break;
		case TRAM:
			baseImage = resources.modeTramPng();
			break;
		case FERRY:
			baseImage = resources.modeFerryPng();
			break;
		case GONDOLA:
			baseImage = resources.modeGondolaPng();
			break;
		case PLANE:
			baseImage = resources.modePlanePng();
			break;
		case RAIL:
			baseImage = resources.modeRailPng();
			break;
		case SUBWAY:
			baseImage = resources.modeSubwayPng();
			break;
		case TROLLEY:
			baseImage = resources.modeTrolleyPng();
			break;
		}
		final String url = baseImage.getSafeUri().asString();
		final Canvas canvas = Canvas.createIfSupported();
		if (canvas != null) {
			int width = baseImage.getWidth();
			int height = 1000;
			canvas.setCoordinateSpaceWidth(width);
			canvas.setCoordinateSpaceHeight(height);
			final Context2d context = canvas.getContext2d();
			context.setLineCap(LineCap.BUTT);
			if (road) {
				context.setStrokeStyle(CssColor.make(color));
				context.setLineWidth(4);
				for (int y = baseImage.getHeight(); y < 1000; y += 7) {
					context.moveTo(width / 2, y);
					context.lineTo(width / 2, y + 5);
				}
				context.stroke();
			} else {
				context.setStrokeStyle(CssColor.make("#000000"));
				context.setLineWidth(5);
				context.moveTo(width / 2, 0);
				context.lineTo(width / 2, height - 1);
				context.stroke();
				context.setStrokeStyle(CssColor.make(color));
				context.setLineWidth(4);
				context.moveTo(width / 2, 0);
				context.lineTo(width / 2, height - 1);
				context.stroke();
			}
			/*
			 * HACK ALERT! Image.onLoad event does not fire up when using
			 * internal resources (URL is internal data), but using the image
			 * immediately does not work (image does not seems to be ready). We
			 * defer the processing of the image rendering to a timer delayed a
			 * bit.
			 */
			Timer timer = new Timer() {
				@Override
				public void run() {
					Image image = new Image(url);
					ImageElement e = ImageElement.as(image.getElement());
					context.drawImage(e, 0, 0);
					String url2 = canvas.toDataUrl("image/png");
					widget.getElement().getStyle()
							.setBackgroundImage("url('" + url2 + "')");
				}
			};
			timer.schedule(500);
		} else {
			widget.getElement().getStyle()
					.setBackgroundImage("url('" + url + "')");
		}
	}
}

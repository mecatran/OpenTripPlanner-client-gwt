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

import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.mecatran.otp.gwt.client.i18n.I18nUtils;
import com.mecatran.otp.gwt.client.model.ItineraryBean;
import com.mecatran.otp.gwt.client.model.ItineraryLegBean;
import com.mecatran.otp.gwt.client.model.ItineraryRoadLegBean;
import com.mecatran.otp.gwt.client.model.ItineraryRoadStepBean;
import com.mecatran.otp.gwt.client.model.ItineraryTransitLegBean;
import com.mecatran.otp.gwt.client.model.TransitRouteBean;
import com.mecatran.otp.gwt.client.model.Wgs84LatLonBean;
import com.mecatran.otp.gwt.client.utils.FormatUtils;
import com.mecatran.otp.gwt.client.utils.PolylineEncoder;

/*
 * TODO Use html templating?
 */
public class PrintWidgetWindowImpl implements PrintWidget {

	private PrintWidgetListener printWidgetListener;

	private String printCss = "";

	public PrintWidgetWindowImpl(String customCssUrl) {
		if (customCssUrl != null)
			downloadPrintCss(customCssUrl);
	}

	@Override
	public Widget getAsWidget() {
		return null;
	}

	@Override
	public void setPrintWidgetListener(
			PrintWidgetListener printWidgetListener) {
		this.printWidgetListener = printWidgetListener;
	}

	@Override
	public void printItinerary(ItineraryBean itinerary) {
		FlowPanel rootPanel = new FlowPanel();

		/* Compute some values */
		int nTransfers = 0;
		long walkDistanceMeter = 0;
		long bikeDistanceMeter = 0;
		for (ItineraryLegBean leg : itinerary.getLegs()) {
			switch (leg.getTravelType()) {
			case ROAD:
				switch (leg.getAsRoadLeg().getMode()) {
				case BICYCLE:
				case BICYCLE_RENTAL:
					bikeDistanceMeter += leg.getAsRoadLeg().getDistanceMeters();
					break;
				case WALK:
					walkDistanceMeter += leg.getAsRoadLeg().getDistanceMeters();
					break;
				default:
					// pass
				}
				break;
			case TRANSIT:
				nTransfers++;
				break;
			}
		}

		/* Display summary */
		rootPanel.add(new HTML("<h1>"
				+ I18nUtils.tr("print.itinerary.header",
						itinerary.getStartAddress(), itinerary.getEndAddress())
				+ "</h1>"));
		rootPanel.add(new HTML(I18nUtils.tr("print.summary.departure",
				FormatUtils.formatDateTime(itinerary.getDepartureTime()),
				itinerary.getStartAddress())));
		rootPanel.add(new HTML(I18nUtils.tr("print.summary.arrival",
				FormatUtils.formatDateTime(itinerary.getArrivalTime()),
				itinerary.getEndAddress())));
		rootPanel.add(new HTML(I18nUtils.tr("print.summary.duration",
				FormatUtils.formatDuration(itinerary.getDurationSeconds()),
				FormatUtils.formatDistance(itinerary.getDistanceMeters()))));
		StringBuffer infoHtml = new StringBuffer();
		if (nTransfers > 0) {
			infoHtml.append(
					I18nUtils.tr("print.summary.transfers", nTransfers - 1))
					.append(", ");
		}
		if (walkDistanceMeter > 0) {
			infoHtml.append(I18nUtils.tr("print.summary.walk.distance",
					FormatUtils.formatDistance(walkDistanceMeter)))
					.append(", ");
		}
		if (bikeDistanceMeter > 0) {
			infoHtml.append(I18nUtils.tr("print.summary.bike.distance",
					FormatUtils.formatDistance(bikeDistanceMeter)))
					.append(", ");
		}
		// Remove last comma
		if (infoHtml.length() > 0)
			infoHtml.setLength(infoHtml.length() - 2);
		rootPanel.add(new HTML(infoHtml.toString()));

		rootPanel.add(new HTML("<h2>"
				+ I18nUtils.tr("print.itinerary.details.header") + "</h2>"));
		/* Departure */
		rootPanel.add(new HTML("<h3>"
				+ I18nUtils.tr("print.step.depart",
						FormatUtils.formatTime(itinerary.getDepartureTime()),
						FormatUtils.formatAddress(itinerary.getStartAddress()))
				+ "</h3>"));
		/* Details */
		for (ItineraryLegBean leg : itinerary.getLegs()) {
			switch (leg.getTravelType()) {
			case ROAD:
				rootPanel.add(getRoadDetails((ItineraryRoadLegBean) leg));
				break;
			case TRANSIT:
				rootPanel.add(getTransitDetails((ItineraryTransitLegBean) leg));
				break;
			default:
				throw new IllegalArgumentException(
						"Unsupported leg type: " + leg.getTravelType());
			}
		}
		/* Arrival */
		rootPanel.add(new HTML("<h3>" + I18nUtils.tr("print.step.arrival",
				FormatUtils.formatTime(itinerary.getArrivalTime()),
				FormatUtils.formatAddress(itinerary.getEndAddress()))
				+ "</h3>"));

		openWindow(rootPanel.getElement());
	}

	private void downloadPrintCss(String cssUrl) {
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET,
				cssUrl);
		try {
			requestBuilder.sendRequest(null, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request,
						Response response) {
					printCss = response.getText();
				}

				@Override
				public void onError(Request request, Throwable exception) {
				}
			});
		} catch (RequestException e) {
		}
	}

	private void openWindow(Element rootElement) {
		/* This is the hackish area ... */
		BodyElement printBody = openWindow(I18nUtils.tr("print.button"),
				printCss);
		printBody.getOwnerDocument().getElementById("printarea")
				.appendChild(rootElement);
	}

	/**
	 * Implementation note: Including a CSS as script with href in the HTML
	 * document below does not work. We need to include the CSS inline,
	 * pre-downloaded.
	 * 
	 * @param printLabel
	 * @param cssContent
	 *            CSS CONTENT, not URL.
	 * @return
	 */
	public static native BodyElement openWindow(String printLabel,
			String cssContent)
	/*-{
        var win = window.open("", "win", "width=940,height=400,status=1,resizeable=1,scrollbars=1");
        win.document.open("text/html", "replace");
        win.document.write("<html><head><style>\n" + cssContent
                + "\n</style></head><body><div><button onclick='window.print()'>" + printLabel
                + "</button></div><div id='printarea'/></body></html>");
        win.document.close();
        win.focus();
        return win.document.body;
	}-*/;

	private Widget getRoadDetails(ItineraryRoadLegBean leg) {
		FlowPanel line = new FlowPanel();
		// Main instruction
		line.add(new HTML("<h3>" + leg.getInstructions() + " ("
				+ FormatUtils.formatDistance(leg.getDistanceMeters()) + " - "
				+ FormatUtils.formatDuration(leg.getDurationSeconds())
				+ ")</h3>"));

		// Steps
		StringBuilder stepsHtml = new StringBuilder();
		stepsHtml.append("<ul>");
		for (ItineraryRoadStepBean step : leg.getRoadSteps()) {
			if (step.getInstructions() == null) {
				// Can happen in case of transfers between two transit legs
				continue;
			}
			stepsHtml.append("<li>").append(step.getInstructions())
					.append((step.getDistanceMeters() >= 5 ? " ("
							+ FormatUtils
									.formatDistance(step.getDistanceMeters())
							+ " - "
							+ FormatUtils.formatDuration(
									step.getDurationSeconds())
							+ ")" : ""))
					.append("</li>");
		}

		// Map
		StringBuilder mapsHtml = new StringBuilder();
		if (leg.getPath() != null && leg.getPath().length > 0) {
			for (String url : getMapUrl(leg.getPath())) {
				mapsHtml.append("<img style='width: 8cm' src='").append(url)
						.append("'><br/><hr/>");
			}
		}
		// Table
		StringBuilder tableHtml = new StringBuilder();
		tableHtml.append("<table><tr><td>").append(mapsHtml.toString())
				.append("</td><td>").append(stepsHtml.toString())
				.append("</td></tr></table>");
		line.add(new HTML(tableHtml.toString()));
		return line;
	}

	private Widget getTransitDetails(ItineraryTransitLegBean leg) {
		FlowPanel line = new FlowPanel();
		TransitRouteBean route = leg.getRoute();
		String codeHtml = "<b><span class='route-code' style='color:"
				+ route.getForegroundColor() + "; background-color:"
				+ route.getBackgroundColor() + "'>&nbsp;" + route.getCode()
				+ "&nbsp;</span></b>";
		line.add(new HTML("<h3>" + I18nUtils.tr("print.take.the.route",
				codeHtml, FormatUtils.formatTime(leg.getDepartureTime()),
				leg.getDepartureStop().getName(), leg.getHeadsign())
				+ "</h3>"));
		line.add(
				new HTML("<h3>"
						+ I18nUtils.tr("print.hop.off", "<span class='time'>"
								+ FormatUtils.formatTime(leg.getArrivalTime())
								+ "</span>", leg.getArrivalStop().getName())
						+ "\"</h3>"));
		return line;
	}

	private List<String> getMapUrl(Wgs84LatLonBean[] path) {
		final int MAP_PATH_SIZE = 100;
		NumberFormat dec5Format = NumberFormat.getFormat("#.00000");
		List<String> retval = new ArrayList<String>();
		int n = (path.length - 1) / MAP_PATH_SIZE + 1;
		int m = path.length / n + 1;
		int js = 0;
		for (int i = 0; i < n; i++) {
			StringBuilder urlBuilder = new StringBuilder();
			int je = js + m;
			if (je > path.length)
				je = path.length;
			boolean start = i == 0;
			boolean end = je == path.length;
			urlBuilder.append(
					"http://maps.googleapis.com/maps/api/staticmap?sensor=false&scale=2&size=800x800&maptype=roadmap");
			if (start)
				urlBuilder.append("&markers=")
						.append(dec5Format.format(path[0].getLat())).append(",")
						.append(dec5Format.format(path[0].getLon()));
			if (end && !start)
				urlBuilder.append("&markers=");
			else
				urlBuilder.append("|");
			if (end)
				urlBuilder
						.append(dec5Format
								.format(path[path.length - 1].getLat()))
						.append(",").append(dec5Format
								.format(path[path.length - 1].getLon()));
			urlBuilder.append("&path=color:blue|enc:");
			Wgs84LatLonBean[] subPath = new Wgs84LatLonBean[je - js];
			// GWT does not have Arrays.copyOfRange() ...
			for (int k = js; k < je; k++) {
				subPath[k - js] = path[k];
			}
			urlBuilder.append(PolylineEncoder.encode(subPath));
			retval.add(urlBuilder.toString());
			js = je;
		}
		assert (js == path.length);
		return retval;
	}
}

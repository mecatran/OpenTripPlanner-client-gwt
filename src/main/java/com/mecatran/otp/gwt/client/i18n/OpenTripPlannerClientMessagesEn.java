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
package com.mecatran.otp.gwt.client.i18n;

/**
 * 
 */
public class OpenTripPlannerClientMessagesEn extends
		OpenTripPlannerClientMessages {

	public OpenTripPlannerClientMessagesEn() {
		add("ok", "OK");
		add("cancel", "Cancel");
		add("start.here", "Start here");
		add("end.here", "End here");
		add("layer.mapquest.street", "Street map (MapQuest)");
		add("layer.osm.street", "Street map (OpenStreetMap)");
		add("layer.osm.cycle", "Cycle map (OpenCycleMap)");
		add("layer.poi.BIKE_RENTAL", "Bike share stations");
		add("layer.poi.TRANSPORT", "Transport");
		add("layer.poi.COMMERCIAL", "Commercial");
		add("layer.poi.CULTURE", "Culture");
		add("layer.poi.EMERGENCY", "Emergency services");
		add("layer.poi.INFRA", "Public infrastructure");
		add("layer.poi.PUBLIC", "Public services");
		add("layer.poi.SPORT", "Sport and leisure");
		add("layer.poi.TOURISM", "Tourism");
		add("layer.poi.default", "Other points of interest");
		add("layer.stops", "Stops");
		add("layer.routes", "Routes");
		add("welcome", "Welcome");
		add("hop.off.at", "Hop off at");
		add("depart.at", "Depart at");
		add("arrive.at", "Arrive at");
		add("at.time", "at");
		add("time.format.small", "K:mma");
		add("date.format.small", "MMM d");
		add("date.format.long", "MMM d yyyy");
		add("plan.itinerary", "Plan route");
		add("print.itinerary", "Print itinerary");
		add("please.wait", "Please wait");
		add("take.the.route", "Take route {0} ({1}) heading to '{2}'");
		add("walk.to", "Walk to {0}");
		add("bike.to", "Bike to {0}");
		add("drive.to", "Drive to {0}");
		add("unnamed.road", "(Unnamed road)");
		add("select.transport.mode", "Mode");
		add("walk.speed.handicap", "Disabled person");
		add("walk.speed.slow", "Slow walk");
		add("walk.speed.medium", "Medium walk");
		add("walk.speed.fast", "Fast walk");
		add("bike.speed.slow", "Slow rider");
		add("bike.speed.medium", "Medium rider");
		add("bike.speed.fast", "Fast rider");
		add("bike.factor.fastest", "Fastest trip");
		add("bike.factor.medium", "Best compromise");
		add("bike.factor.safest", "Safest trip");
		add("transit.least.walk", "Least walk");
		add("transit.best.trip", "Best trip");
		add("transit.least.transfers", "Least transfers");
		add("show.options", "More options...");
		add("hide.options", "Hide options");
		add("show.alerts", "Show alerts");
		add("more.info.alert", "More info...");
		add("hide.alerts", "Hide alerts");
		add("show.itinerary.details", "Details...");
		add("hide.itinerary.details", "Hide details");
		add("show.road.details", "(details...)");
		add("hide.road.details", "(hide details)");
		add("zoom.in", "Zoom in");
		add("zoom.out", "Zoom out");
		add("center.map", "Center map");
		add("geocode.stop", "Stop \"{0}\"");
		add("geocode.station", "Station \"{0}\"");
		add("geocode.bike-rental", "Bike share \"{0}\"");
		add("geocode.transport", "Station \"{0}\"");
		add("geocode.poi", "\"{0}\" (point of interest)");
		add("link.to.this.page", "Link...");
		add("copy.paste.link.hint", "Copy/paste this link to the itinerary:");
		add("mode.transit", "Transit");
		add("mode.walk", "Walk only");
		add("mode.bike", "Bicycle");
		add("mode.bike-rental", "Bike share");
		add("mode.transit.bike", "Bicycle and transit");
		add("mode.transit.bike-rental", "Bike share and transit");
		add("heading.to", "⇨");
		add("departure.address.not.found",
				"Departure address not found: '{0}'. ");
		add("arrival.address.not.found", "Arrival address not found: '{0}'");
		add("address.not.found",
				"Address not found. Please type a valid address or select a point on the map");
		add("several.address.matches",
				"Several addresses matches. Please select one in the list below:");
		add("stay.on", "Stay on <b>{0}</b>");
		add("reldir.Continue", "Continue on <b>{0}</b>");
		add("reldir.Elevator", "Take the elevator");
		add("reldir.CircleClockwise", "Take the round-about");
		add("reldir.CircleCounterClockwise", "Take the round-about");
		add("reldir.HardLeft", "Turn left on <b>{0}</b>");
		add("reldir.Left", "Turn left on <b>{0}</b>");
		add("reldir.SlightlyLeft", "Slightly left on <b>{0}</b>");
		add("reldir.HardRight", "Turn right on <b>{0}</b>");
		add("reldir.Right", "Turn right on <b>{0}</b>");
		add("reldir.SlightlyRight", "Slightly right on <b>{0}</b>");
		add("absdir.East", "Go East on <b>{0}</b>");
		add("absdir.NorthEast", "Go North-East on <b>{0}</b>");
		add("absdir.North", "Go North on <b>{0}</b>");
		add("absdir.NorthWest", "Go North-West on <b>{0}</b>");
		add("absdir.West", "Go West on <b>{0}</b>");
		add("absdir.SouthWest", "Go South-West on <b>{0}</b>");
		add("absdir.South", "Go South on <b>{0}");
		add("absdir.SouthEast", "Go South-East on <b>{0}</b>");

		add("print.button", "Print");
		add("print.itinerary.header", "Itinerary {0} → {1}");
		add("print.summary.departure", "Depart: <b>{0}</b>, {1}");
		add("print.summary.arrival", "Arrive: <b>{0}</b>, {1}");
		add("print.summary.duration",
				"Duration: <b>{0}</b>, Total distance: <b>{1}</b>");
		add("print.summary.transfers", "Transfers: <b>{0}</b>");
		add("print.summary.walk.distance", "Walk: <b>{0}</b>");
		add("print.summary.bike.distance", "Cycle: <b>{0}</b>");
		add("print.itinerary.details.header", "Itinerary details");
		add("print.step.depart", "Depart at: {0} - {1}");
		add("print.step.arrival", "Arrive at: {0} - {1}");
		add("print.take.the.route",
				"Take route {0} at {1} at stop \"{2}\" heading {3}");
		add("print.hop.off", "Hop-off at {0} at stop \"{1}\"");
	}
}

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
package com.mecatran.otp.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface PlannerResources extends ClientBundle {

	public static final PlannerResources INSTANCE = GWT
			.create(PlannerResources.class);

	@Source("js/GwtOpenLayersUtils.js")
	TextResource gwtOpenLayersUtilsJs();
	
	@Source("js/OpenStreetMap.js")
	TextResource gwtOpenStreetMapJs();

	@Source("css/OpenTripPlannerClient.css")
	@CssResource.NotStrict
	CssResource css();

	/* === General icons === */

	@Source("img/close_button.png")
	ImageResource closeButtonPng();

	@Source("img/loading.gif")
	ImageResource loadingGif();

	@Source("img/favorite.png")
	ImageResource favoritePng();

	@Source("img/nofavorite.png")
	ImageResource noFavoritePng();

	@Source("img/collapsed.png")
	ImageResource collapsedPng();

	@Source("img/expanded.png")
	ImageResource expandedPng();

	@Source("img/warn.png")
	ImageResource warnPng();

	@Source("img/info.png")
	ImageResource infoPng();

	/* === Plan options === */

	@Source("img/calendar.png")
	ImageResource calendarPng();

	@Source("img/clock.png")
	ImageResource clockPng();

	@Source("img/modes.png")
	ImageResource modesPng();

	@Source("img/walk_speed.png")
	ImageResource walkSpeedPng();

	@Source("img/bike_speed.png")
	ImageResource bikeSpeedPng();

	@Source("img/transfers_options.png")
	ImageResource transfersOptionsPng();

	/* === Flags (icons and map) === */

	@Source("img/flag_departure.png")
	ImageResource flagDeparturePng();

	@Source("img/flag_arrival.png")
	ImageResource flagArrivalPng();

	@Source("img/flag_waypoint.png")
	ImageResource flagWaypointPng();

	@Source("img/flagmap_departure.png")
	ImageResource flagmapDeparturePng();

	@Source("img/flagmap_departure_walk.png")
	ImageResource flagmapDepartureWalkPng();

	@Source("img/flagmap_departure_bike.png")
	ImageResource flagmapDepartureBikePng();

	@Source("img/flagmap_departure_car.png")
	ImageResource flagmapDepartureCarPng();

	@Source("img/flagmap_arrival.png")
	ImageResource flagmapArrivalPng();

	@Source("img/flagmap_waypoint.png")
	ImageResource flagmapWaypointPng();

	/* === Narrative modes === */

	@Source("img/mode_walk.png")
	ImageResource modeWalkPng();

	@Source("img/mode_car.png")
	ImageResource modeCarPng();

	@Source("img/mode_bicycle.png")
	ImageResource modeBicyclePng();

	@Source("img/mode_bus.png")
	ImageResource modeBusPng();

	@Source("img/mode_ferry.png")
	ImageResource modeFerryPng();

	@Source("img/mode_gondola.png")
	ImageResource modeGondolaPng();

	@Source("img/mode_plane.png")
	ImageResource modePlanePng();

	@Source("img/mode_rail.png")
	ImageResource modeRailPng();

	@Source("img/mode_subway.png")
	ImageResource modeSubwayPng();

	@Source("img/mode_tram.png")
	ImageResource modeTramPng();

	@Source("img/mode_trolley.png")
	ImageResource modeTrolleyPng();

	@Source("img/mode_bike_rental.png")
	ImageResource modeBikeRentalPng();

	/* === Timeline */

	@Source("img/timeline_clock.png")
	ImageResource timelineClockPng();

	@Source("img/timeline_walk.png")
	ImageResource timelineWalkPng();

	@Source("img/timeline_bicycle.png")
	ImageResource timelineBicyclePng();

	@Source("img/timeline_car.png")
	ImageResource timelineCarPng();

	@Source("img/timeline_wait.png")
	ImageResource timelineWaitPng();

	@Source("img/timeline_transit.png")
	ImageResource timelineTransitPng();

	/* === Mode switch (right/left) on map === */

	@Source("img/modemapr_bicycle.png")
	ImageResource modemaprBicyclePng();

	@Source("img/modemapl_bicycle.png")
	ImageResource modemaplBicyclePng();

	@Source("img/modemapr_bus.png")
	ImageResource modemaprBusPng();

	@Source("img/modemapl_bus.png")
	ImageResource modemaplBusPng();

	@Source("img/modemapr_car.png")
	ImageResource modemaprCarPng();

	@Source("img/modemapl_car.png")
	ImageResource modemaplCarPng();

	@Source("img/modemapr_ferry.png")
	ImageResource modemaprFerryPng();

	@Source("img/modemapl_ferry.png")
	ImageResource modemaplFerryPng();

	@Source("img/modemapr_gondola.png")
	ImageResource modemaprGondolaPng();

	@Source("img/modemapl_gondola.png")
	ImageResource modemaplGondolaPng();

	@Source("img/modemapr_plane.png")
	ImageResource modemaprPlanePng();

	@Source("img/modemapl_plane.png")
	ImageResource modemaplPlanePng();

	@Source("img/modemapr_rail.png")
	ImageResource modemaprRailPng();

	@Source("img/modemapl_rail.png")
	ImageResource modemaplRailPng();

	@Source("img/modemapr_subway.png")
	ImageResource modemaprSubwayPng();

	@Source("img/modemapl_subway.png")
	ImageResource modemaplSubwayPng();

	@Source("img/modemapr_tram.png")
	ImageResource modemaprTramPng();

	@Source("img/modemapl_tram.png")
	ImageResource modemaplTramPng();

	@Source("img/modemapr_trolley.png")
	ImageResource modemaprTrolleyPng();

	@Source("img/modemapl_trolley.png")
	ImageResource modemaplTrolleyPng();

	@Source("img/modemapr_walk.png")
	ImageResource modemaprWalkPng();

	@Source("img/modemapl_walk.png")
	ImageResource modemaplWalkPng();

	/* === Map popup icons === */

	@Source("img/zoom_in.png")
	ImageResource zoomInPng();

	@Source("img/zoom_out.png")
	ImageResource zoomOutPng();

	@Source("img/center_map.png")
	ImageResource centerMapPng();

	/* === POIs === */

	@Source("img/poimap_airport.png")
	ImageResource poimapAirportPng();

	@Source("img/poimap_atm.png")
	ImageResource poimapAtmPng();

	@Source("img/poimap_bank.png")
	ImageResource poimapBankPng();

	@Source("img/poimap_beach.png")
	ImageResource poimapBeachPng();

	@Source("img/poimap_bike_park.png")
	ImageResource poimapBikeParkPng();

	@Source("img/poimap_bike_rental.png")
	ImageResource poimapBikeRentalPng();

	@Source("img/poimap_bus_station.png")
	ImageResource poimapBusStationPng();

	@Source("img/poimap_cable_car.png")
	ImageResource poimapCableCarPng();

	@Source("img/poimap_cafe.png")
	ImageResource poimapCafePng();

	@Source("img/poimap_campsite.png")
	ImageResource poimapCampsitePng();

	@Source("img/poimap_car_park.png")
	ImageResource poimapCarParkPng();

	@Source("img/poimap_car_park_cover.png")
	ImageResource poimapCarParkCoverPng();

	@Source("img/poimap_fastfood.png")
	ImageResource poimapFastfoodPng();

	@Source("img/poimap_ferryport.png")
	ImageResource poimapFerryportPng();

	@Source("img/poimap_firestation.png")
	ImageResource poimapFirestationPng();

	@Source("img/poimap_generic.png")
	ImageResource poimapGenericPng();

	@Source("img/poimap_golf.png")
	ImageResource poimapGolfPng();

	@Source("img/poimap_hospital.png")
	ImageResource poimapHospitalPng();

	@Source("img/poimap_hotel.png")
	ImageResource poimapHotelPng();

	@Source("img/poimap_info_point.png")
	ImageResource poimapInfoPointPng();

	@Source("img/poimap_letterbox.png")
	ImageResource poimapLetterboxPng();

	@Source("img/poimap_museum.png")
	ImageResource poimapMuseumPng();

	@Source("img/poimap_park.png")
	ImageResource poimapParkPng();

	@Source("img/poimap_park_and_ride.png")
	ImageResource poimapParkAndRidePng();

	@Source("img/poimap_pharmacy.png")
	ImageResource poimapPharmacyPng();

	@Source("img/poimap_police.png")
	ImageResource poimapPolicePng();

	@Source("img/poimap_public_toilets.png")
	ImageResource poimapPublicToiletsPng();

	@Source("img/poimap_railway_station.png")
	ImageResource poimapRailwayStationPng();

	@Source("img/poimap_restaurant.png")
	ImageResource poimapRestaurantPng();

	@Source("img/poimap_shopping_center.png")
	ImageResource poimapShoppingCenterPng();

	@Source("img/poimap_stadium.png")
	ImageResource poimapStadiumPng();

	@Source("img/poimap_swimming_pool.png")
	ImageResource poimapSwimmingPoolPng();
}

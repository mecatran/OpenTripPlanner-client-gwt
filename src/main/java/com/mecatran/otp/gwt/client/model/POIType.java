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
package com.mecatran.otp.gwt.client.model;

public enum POIType {
	// Generic POI
	GENERIC,
	// Transport
	TRANSPORT_BIKE_RENTAL_STATION, TRANSPORT_STATION, TRANSPORT_BUS_STATION, TRANSPORT_RAIL_STATION, TRANSPORT_GONDOLA_STATION, TRANSPORT_AIRPORT, TRANSPORT_FERRYPORT, TRANSPORT_CAR_PARK, TRANSPORT_CAR_PARK_COVER, TRANSPORT_PARK_AND_RIDE, TRANSPORT_BIKE_PARK,
	// Tourism / leisure
	TOURISM_INFO_POINT, TOURISM_CAMPSITE, TOURISM_BEACH, TOURISM_PARK, TOURISM_HOTEL, TOURISM_BED_AND_BREAKFAST, TOURISM_YOUTH_HOSTEL, TOURISM_PICNIC,
	// Sport
	SPORT_SWIMMING_POOL, SPORT_STADIUM, SPORT_GOLF,
	// Commercial
	COMMERCIAL_SHOPPING_CENTER, COMMERCIAL_RESTAURANT, COMMERCIAL_FASTFOOD, COMMERCIAL_CAFE, COMMERCIAL_ATM, COMMERCIAL_BANK,
	// Culture
	CULTURE_MUSEUM, CULTURE_LIBRARY, // No icon
	CULTURE_MOVIE_THEATER, // No icon
	CULTURE_THEATER, // No icon
	CULTURE_NEWSPAPERS, // No icon
	CULTURE_RUINS, // No icon
	CULTURE_CASTLE, // No icon
	CULTURE_MONUMENT, // No icon
	// Public
	PUBLIC_TOWN_HALL, // No icon
	PUBLIC_CULT, // No icon
	PUBLIC_POST_OFFICE, // No icon
	// Emergency
	EMERGENCY_HOSPITAL, EMERGENCY_PHARMACY, EMERGENCY_POLICE_STATION, EMERGENCY_FIRE_STATION,
	// Infrastructure
	INFRA_RECYCLING, // No icon
	INFRA_PUBLIC_TOILET, INFRA_LETTERBOX,
}

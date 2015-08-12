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

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.resources.client.ImageResource;
import com.mecatran.otp.gwt.client.PlannerResources;
import com.mecatran.otp.gwt.client.model.POIType;

public class POIUtils {

	public enum POILayerType {
		GENERIC, TRANSPORT, BIKE_RENTAL, COMMERCIAL, CULTURE, INFRA, TOURISM, SPORT, PUBLIC, EMERGENCY
	}

	/**
	 * Map from POI type to layer ID. TODO Parametrize the mapping for a given
	 * client configuration (if needed).
	 */
	private static final Map<POIType, POILayerType> poiTypeToLayerTypeMap = new HashMap<POIType, POILayerType>();

	/**
	 * Map from POI type to ImageResource. TODO Parametrize (if needed).
	 */
	private static final Map<POIType, ImageResource> poiTypeToImgRsc = new HashMap<POIType, ImageResource>();

	static {
		// POI type to layer ID
		poiTypeToLayerTypeMap.put(POIType.GENERIC, POILayerType.GENERIC);
		poiTypeToLayerTypeMap.put(POIType.COMMERCIAL_ATM,
				POILayerType.COMMERCIAL);
		poiTypeToLayerTypeMap.put(POIType.COMMERCIAL_BANK,
				POILayerType.COMMERCIAL);
		poiTypeToLayerTypeMap.put(POIType.COMMERCIAL_CAFE,
				POILayerType.COMMERCIAL);
		poiTypeToLayerTypeMap.put(POIType.COMMERCIAL_FASTFOOD,
				POILayerType.COMMERCIAL);
		poiTypeToLayerTypeMap.put(POIType.COMMERCIAL_RESTAURANT,
				POILayerType.COMMERCIAL);
		poiTypeToLayerTypeMap.put(POIType.COMMERCIAL_SHOPPING_CENTER,
				POILayerType.COMMERCIAL);
		poiTypeToLayerTypeMap.put(POIType.CULTURE_CASTLE, POILayerType.CULTURE);
		poiTypeToLayerTypeMap
				.put(POIType.CULTURE_LIBRARY, POILayerType.CULTURE);
		poiTypeToLayerTypeMap.put(POIType.CULTURE_MONUMENT,
				POILayerType.CULTURE);
		poiTypeToLayerTypeMap.put(POIType.CULTURE_MOVIE_THEATER,
				POILayerType.CULTURE);
		poiTypeToLayerTypeMap.put(POIType.CULTURE_MUSEUM, POILayerType.CULTURE);
		poiTypeToLayerTypeMap.put(POIType.CULTURE_NEWSPAPERS,
				POILayerType.CULTURE);
		poiTypeToLayerTypeMap.put(POIType.CULTURE_RUINS, POILayerType.CULTURE);
		poiTypeToLayerTypeMap
				.put(POIType.CULTURE_THEATER, POILayerType.CULTURE);
		poiTypeToLayerTypeMap.put(POIType.EMERGENCY_FIRE_STATION,
				POILayerType.EMERGENCY);
		poiTypeToLayerTypeMap.put(POIType.EMERGENCY_HOSPITAL,
				POILayerType.EMERGENCY);
		poiTypeToLayerTypeMap.put(POIType.EMERGENCY_PHARMACY,
				POILayerType.EMERGENCY);
		poiTypeToLayerTypeMap.put(POIType.EMERGENCY_POLICE_STATION,
				POILayerType.EMERGENCY);
		poiTypeToLayerTypeMap.put(POIType.INFRA_LETTERBOX, POILayerType.INFRA);
		poiTypeToLayerTypeMap.put(POIType.INFRA_PUBLIC_TOILET,
				POILayerType.INFRA);
		poiTypeToLayerTypeMap.put(POIType.INFRA_RECYCLING, POILayerType.INFRA);
		poiTypeToLayerTypeMap.put(POIType.PUBLIC_CULT, POILayerType.PUBLIC);
		poiTypeToLayerTypeMap.put(POIType.PUBLIC_POST_OFFICE,
				POILayerType.PUBLIC);
		poiTypeToLayerTypeMap
				.put(POIType.PUBLIC_TOWN_HALL, POILayerType.PUBLIC);
		poiTypeToLayerTypeMap.put(POIType.SPORT_GOLF, POILayerType.SPORT);
		poiTypeToLayerTypeMap.put(POIType.SPORT_STADIUM, POILayerType.SPORT);
		poiTypeToLayerTypeMap.put(POIType.SPORT_SWIMMING_POOL,
				POILayerType.SPORT);
		poiTypeToLayerTypeMap.put(POIType.TOURISM_BEACH, POILayerType.TOURISM);
		poiTypeToLayerTypeMap.put(POIType.TOURISM_BED_AND_BREAKFAST,
				POILayerType.TOURISM);
		poiTypeToLayerTypeMap.put(POIType.TOURISM_CAMPSITE,
				POILayerType.TOURISM);
		poiTypeToLayerTypeMap.put(POIType.TOURISM_HOTEL, POILayerType.TOURISM);
		poiTypeToLayerTypeMap.put(POIType.TOURISM_INFO_POINT,
				POILayerType.TOURISM);
		poiTypeToLayerTypeMap.put(POIType.TOURISM_PARK, POILayerType.TOURISM);
		poiTypeToLayerTypeMap.put(POIType.TOURISM_PICNIC, POILayerType.TOURISM);
		poiTypeToLayerTypeMap.put(POIType.TOURISM_YOUTH_HOSTEL,
				POILayerType.TOURISM);
		poiTypeToLayerTypeMap.put(POIType.TRANSPORT_AIRPORT,
				POILayerType.TRANSPORT);
		poiTypeToLayerTypeMap.put(POIType.TRANSPORT_BIKE_PARK,
				POILayerType.BIKE_RENTAL);
		poiTypeToLayerTypeMap.put(POIType.TRANSPORT_BIKE_RENTAL_STATION,
				POILayerType.BIKE_RENTAL);
		poiTypeToLayerTypeMap.put(POIType.TRANSPORT_BUS_STATION,
				POILayerType.TRANSPORT);
		poiTypeToLayerTypeMap.put(POIType.TRANSPORT_CAR_PARK,
				POILayerType.TRANSPORT);
		poiTypeToLayerTypeMap.put(POIType.TRANSPORT_CAR_PARK_COVER,
				POILayerType.TRANSPORT);
		poiTypeToLayerTypeMap.put(POIType.TRANSPORT_FERRYPORT,
				POILayerType.TRANSPORT);
		poiTypeToLayerTypeMap.put(POIType.TRANSPORT_GONDOLA_STATION,
				POILayerType.TRANSPORT);
		poiTypeToLayerTypeMap.put(POIType.TRANSPORT_PARK_AND_RIDE,
				POILayerType.TRANSPORT);
		poiTypeToLayerTypeMap.put(POIType.TRANSPORT_RAIL_STATION,
				POILayerType.TRANSPORT);
		poiTypeToLayerTypeMap.put(POIType.TRANSPORT_STATION,
				POILayerType.TRANSPORT);

		// POI type to ImageResource
		PlannerResources pr = PlannerResources.INSTANCE;
		poiTypeToImgRsc.put(POIType.GENERIC, pr.poimapGenericPng());
		poiTypeToImgRsc.put(POIType.COMMERCIAL_ATM, pr.poimapAtmPng());
		poiTypeToImgRsc.put(POIType.COMMERCIAL_BANK, pr.poimapBankPng());
		poiTypeToImgRsc.put(POIType.COMMERCIAL_CAFE, pr.poimapCafePng());
		poiTypeToImgRsc
				.put(POIType.COMMERCIAL_FASTFOOD, pr.poimapFastfoodPng());
		poiTypeToImgRsc.put(POIType.COMMERCIAL_RESTAURANT,
				pr.poimapRestaurantPng());
		poiTypeToImgRsc.put(POIType.COMMERCIAL_SHOPPING_CENTER,
				pr.poimapShoppingCenterPng());
		poiTypeToImgRsc.put(POIType.CULTURE_CASTLE, pr.poimapMuseumPng());
		poiTypeToImgRsc.put(POIType.CULTURE_LIBRARY, pr.poimapMuseumPng());
		poiTypeToImgRsc.put(POIType.CULTURE_MONUMENT, pr.poimapMuseumPng());
		poiTypeToImgRsc
				.put(POIType.CULTURE_MOVIE_THEATER, pr.poimapMuseumPng());
		poiTypeToImgRsc.put(POIType.CULTURE_MUSEUM, pr.poimapMuseumPng());
		poiTypeToImgRsc.put(POIType.CULTURE_NEWSPAPERS, pr.poimapMuseumPng());
		poiTypeToImgRsc.put(POIType.CULTURE_RUINS, pr.poimapMuseumPng());
		poiTypeToImgRsc.put(POIType.CULTURE_THEATER, pr.poimapMuseumPng());
		poiTypeToImgRsc.put(POIType.EMERGENCY_FIRE_STATION,
				pr.poimapFirestationPng());
		poiTypeToImgRsc.put(POIType.EMERGENCY_HOSPITAL, pr.poimapHospitalPng());
		poiTypeToImgRsc.put(POIType.EMERGENCY_PHARMACY, pr.poimapPharmacyPng());
		poiTypeToImgRsc.put(POIType.EMERGENCY_POLICE_STATION,
				pr.poimapPolicePng());
		poiTypeToImgRsc.put(POIType.INFRA_LETTERBOX, pr.poimapLetterboxPng());
		poiTypeToImgRsc.put(POIType.INFRA_PUBLIC_TOILET,
				pr.poimapPublicToiletsPng());
		poiTypeToImgRsc.put(POIType.INFRA_RECYCLING, pr.poimapGenericPng());
		poiTypeToImgRsc.put(POIType.PUBLIC_CULT, pr.poimapGenericPng());
		poiTypeToImgRsc
				.put(POIType.PUBLIC_POST_OFFICE, pr.poimapLetterboxPng());
		poiTypeToImgRsc.put(POIType.PUBLIC_TOWN_HALL, pr.poimapMuseumPng());
		poiTypeToImgRsc.put(POIType.SPORT_GOLF, pr.poimapGolfPng());
		poiTypeToImgRsc.put(POIType.SPORT_STADIUM, pr.poimapStadiumPng());
		poiTypeToImgRsc.put(POIType.SPORT_SWIMMING_POOL,
				pr.poimapSwimmingPoolPng());
		poiTypeToImgRsc.put(POIType.TOURISM_BEACH, pr.poimapBeachPng());
		poiTypeToImgRsc.put(POIType.TOURISM_BED_AND_BREAKFAST,
				pr.poimapHotelPng());
		poiTypeToImgRsc.put(POIType.TOURISM_CAMPSITE, pr.poimapCampsitePng());
		poiTypeToImgRsc.put(POIType.TOURISM_HOTEL, pr.poimapHotelPng());
		poiTypeToImgRsc
				.put(POIType.TOURISM_INFO_POINT, pr.poimapInfoPointPng());
		poiTypeToImgRsc.put(POIType.TOURISM_PARK, pr.poimapParkPng());
		poiTypeToImgRsc.put(POIType.TOURISM_PICNIC, pr.poimapParkPng());
		poiTypeToImgRsc.put(POIType.TOURISM_YOUTH_HOSTEL, pr.poimapHotelPng());
		poiTypeToImgRsc.put(POIType.TRANSPORT_AIRPORT, pr.poimapAirportPng());
		poiTypeToImgRsc
				.put(POIType.TRANSPORT_BIKE_PARK, pr.poimapBikeParkPng());
		poiTypeToImgRsc.put(POIType.TRANSPORT_BIKE_RENTAL_STATION,
				pr.poimapBikeRentalPng());
		poiTypeToImgRsc.put(POIType.TRANSPORT_BUS_STATION,
				pr.poimapBusStationPng());
		poiTypeToImgRsc.put(POIType.TRANSPORT_CAR_PARK, pr.poimapCarParkPng());
		poiTypeToImgRsc.put(POIType.TRANSPORT_CAR_PARK_COVER,
				pr.poimapCarParkCoverPng());
		poiTypeToImgRsc.put(POIType.TRANSPORT_FERRYPORT,
				pr.poimapFerryportPng());
		poiTypeToImgRsc.put(POIType.TRANSPORT_GONDOLA_STATION,
				pr.poimapCableCarPng());
		poiTypeToImgRsc.put(POIType.TRANSPORT_PARK_AND_RIDE,
				pr.poimapParkAndRidePng());
		poiTypeToImgRsc.put(POIType.TRANSPORT_RAIL_STATION,
				pr.poimapRailwayStationPng());
		poiTypeToImgRsc
				.put(POIType.TRANSPORT_STATION, pr.poimapBusStationPng());
	}

	/** Return the layer ID for a specific POI type */
	public static POILayerType mapPoiTypeToLayerType(POIType poiType) {
		POILayerType layerType = poiTypeToLayerTypeMap.get(poiType);
		if (layerType == null)
			layerType = POILayerType.GENERIC; // Fall-through
		return layerType;
	}

	public static ImageResource getPoiIcon(POIType poiType) {
		ImageResource imgRsc = poiTypeToImgRsc.get(poiType);
		if (imgRsc == null)
			imgRsc = PlannerResources.INSTANCE.poimapGenericPng();
		return imgRsc;
	}

}
